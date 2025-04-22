package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.adapters.GoodsAdapter
import pl.edu.pjatk.s25819.przeterminarz.databinding.FragmentGoodsListBinding
import pl.edu.pjatk.s25819.przeterminarz.exceptions.CategoryNotFoundException
import pl.edu.pjatk.s25819.przeterminarz.exceptions.StatusNotFoundException
import pl.edu.pjatk.s25819.przeterminarz.model.ExpirationFilter
import pl.edu.pjatk.s25819.przeterminarz.model.FormType
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.repositories.GoodsRepository
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator

class GoodsListFragment : Fragment() {

    private var _binding: FragmentGoodsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var goodsAdapter: GoodsAdapter
    private lateinit var goodsRepository: GoodsRepository

    // pola z kryteriami do filtrowania
    private var selectedCategory: GoodsCategory = GoodsCategory.ALL
    private var selectedExpirationFilter: ExpirationFilter = ExpirationFilter.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goodsRepository = RepositoryLocator.goodsRepository
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoodsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        loadGoods()
    }

    /**
     * dłuższe przytrzymanie spowoduje pokazanie alertu z zapytaniem o usunięcie elementu z listy (jeśli produkt jest dalej ważny). Jeśli użytkownik zatwierdzi usunięcie wpis powinien zniknąć z listy, a podsumowanie się aktualizować. Jeśli produkt jest nie ważny w ten sposób można go oznaczyć jako wyrzucony
     */

    private fun setupRecyclerView() {
        goodsAdapter = GoodsAdapter(
            onCardClick = { ::handleCardClick },
            onCardLongClick = { goods -> handleCardLongClick(goods) }
        )

        binding.goodsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = goodsAdapter
        }
    }

    private fun setupListeners() {
        binding.addNewItem.setOnClickListener {
            navigateToAddNewGoods()
        }

        binding.categoryFilterGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull() ?: R.id.category_all
            selectedCategory = chipIdToCategoryMap[selectedChipId]
                ?: throw CategoryNotFoundException("Nie znaleziono kategorii dla id: $selectedChipId")

            Log.d(TAG, "Wybrana kategoria: $selectedCategory")
            loadGoods()
        }

        binding.statusFilterGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull() ?: R.id.status_all
            selectedExpirationFilter = chipIdToStatusMap[selectedChipId]
                ?: throw StatusNotFoundException("Nie znaleziono statusu dla id: $selectedChipId")

            Log.d(TAG, "Wybrany status: $selectedExpirationFilter")
            loadGoods()
        }
    }

    private fun navigateToEditGoods(id: Int) {
        findNavController().navigate(
            R.id.action_goodsListFragment_to_manageGoodsFragment,
            bundleOf("type" to FormType.Edit(id))
        )
    }

    private fun navigateToAddNewGoods() {
        findNavController().navigate(R.id.action_goodsListFragment_to_manageGoodsFragment)
    }

    private fun loadGoods() {
        val goods = goodsRepository.getGoodsByCriteria(selectedCategory, selectedExpirationFilter)
        goodsAdapter.submitList(goods.sortedBy {
            it.expirationDate
        })
        binding.goodsCounterLabel.text = getString(R.string.goods_counter_label, goods.size)
    }

    private fun updateGoods(goods: Goods) {
        goodsRepository.saveGoods(goods)
        loadGoods()
    }

    private fun removeGoods(goods: Goods) {
        goodsRepository.removeGoods(goods)
        loadGoods()
    }


    private val chipIdToCategoryMap = mapOf(
        R.id.category_all to GoodsCategory.ALL,
        R.id.category_food to GoodsCategory.GROCERY,
        R.id.category_medicine to GoodsCategory.MEDICINE,
        R.id.category_cosmetics to GoodsCategory.COSMETICS
    )

    private val chipIdToStatusMap = mapOf(
        R.id.status_all to ExpirationFilter.ALL,
        R.id.status_valid to ExpirationFilter.VALID,
        R.id.status_expired to ExpirationFilter.EXPIRED
    )

    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(::onDestinationChanged)
    }

    override fun onStop() {
        findNavController().removeOnDestinationChangedListener(::onDestinationChanged)
        super.onStop()
    }

    private fun onDestinationChanged(
        controller: NavController, destination: NavDestination, arguments: Bundle?
    ) {
        if (destination.id == R.id.goodsListFragment) {
            loadGoods()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun informAboutExpiredGoods(goods: Goods) {
        Toast.makeText(
            requireContext(),
            getString(R.string.goods_expired_toast, goods.name),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleCardClick(goods: Goods): Unit {
        if (goods.isExpired()) {
            informAboutExpiredGoods(goods)
        } else {
            navigateToEditGoods(goods.id)
        }
    }

    private fun handleCardLongClick(goods: Goods): Boolean {

        // jeśli towar jest już wyrzucony nie można go usunąć
        if (goods.isThrownAway()) {
            Toast.makeText(
                context,
                getString(R.string.goods_already_thrown_away, goods.name),
                Toast.LENGTH_SHORT
            ).show()
            return true
        }

        // jeśli towar jest przeterminowany oznacz go jako wyrzucony
        if (goods.isExpired()) {
            goods.markAsThrownAway()
            updateGoods(goods)
            loadGoods()
            Toast.makeText(
                context,
                getString(R.string.goods_marked_as_thrown_away, goods.name),
                Toast.LENGTH_SHORT
            ).show()
            return true
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder
                .setTitle(getString(R.string.goods_dialog_confirm_delete_title, goods.name))
                .setMessage(
                    getString(
                        R.string.goods_dialog_confirm_delete_message,
                        goods.name
                    )
                )
                .setPositiveButton(getString(R.string.goods_dialog_confirm_delete_positive)) { _, _ ->
                    removeGoods(goods)
                    Toast.makeText(
                        context,
                        getString(R.string.goods_deleted, goods.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton(getString(R.string.goods_dialog_confirm_delete_negative)) { dialog, _ ->
                    dialog.cancel()
                    Toast.makeText(
                        context,
                        getString(R.string.goods_not_deleted, goods.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            builder.create().show()
            return true
        }
    }

    companion object {
        private const val TAG = "GoodsListFragment"
    }
}