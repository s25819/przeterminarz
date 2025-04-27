package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.adapters.GoodsAdapter
import pl.edu.pjatk.s25819.przeterminarz.databinding.FragmentGoodsListBinding
import pl.edu.pjatk.s25819.przeterminarz.exceptions.CategoryNotFoundException
import pl.edu.pjatk.s25819.przeterminarz.exceptions.StatusNotFoundException
import pl.edu.pjatk.s25819.przeterminarz.model.*
import pl.edu.pjatk.s25819.przeterminarz.viewmodel.GoodsListViewModel

class GoodsListFragment : Fragment() {

    private var _binding: FragmentGoodsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var goodsAdapter: GoodsAdapter
    private val viewModel: GoodsListViewModel by viewModels()

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
        observeViewModel()
    }

    private fun setupRecyclerView() {
        goodsAdapter = GoodsAdapter(
            onCardClick = { handleCardClick(it) },
            onCardLongClick = { handleCardLongClick(it) }
        )

        binding.goodsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = goodsAdapter
        }
    }

    private fun setupListeners() {
        binding.addNewItem.setOnClickListener {
            viewModel.navigateToAdd()
        }

        binding.categoryFilterGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull() ?: R.id.category_all
            val category = chipIdToCategoryMap[selectedChipId]
                ?: throw CategoryNotFoundException("Nie znaleziono kategorii dla id: $selectedChipId")
            viewModel.setCategory(category)
        }

        binding.statusFilterGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull() ?: R.id.status_all
            val filter = chipIdToStatusMap[selectedChipId]
                ?: throw StatusNotFoundException("Nie znaleziono statusu dla id: $selectedChipId")
            viewModel.setFilter(filter)
        }
    }

    private fun observeViewModel() {
        viewModel.goods.observe(viewLifecycleOwner) { goods ->
            goodsAdapter.submitList(goods)
            binding.goodsCounterLabel.text = getString(R.string.goods_counter_label, goods.size)
        }

        viewModel.navigation.observe(viewLifecycleOwner) {
            it.resolve(findNavController())
        }
    }

    private fun handleCardClick(goods: Goods) {
        if (goods.isExpired()) {
            showToast(getString(R.string.goods_expired_toast, goods.name))
        } else {
            viewModel.navigateToEdit(goods)
        }
    }

    @Suppress()
    private fun handleCardLongClick(goods: Goods): Boolean {
        if (goods.isThrownAway()) {
            showToast(getString(R.string.goods_already_thrown_away, goods.name))
            return true
        }

        if (goods.isExpired()) {
            viewModel.onThrowAwayGoods(goods)
            showToast(getString(R.string.goods_marked_as_thrown_away, goods.name))
            return true
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.goods_dialog_confirm_delete_title, goods.name))
            .setMessage(getString(R.string.goods_dialog_confirm_delete_message, goods.name))
            .setPositiveButton(getString(R.string.goods_dialog_confirm_delete_positive)) { _, _ ->
                viewModel.onRemoveGoods(goods)
                showToast(getString(R.string.goods_deleted, goods.name))
            }
            .setNegativeButton(getString(R.string.goods_dialog_confirm_delete_negative)) { dialog, _ ->
                dialog.cancel()
                showToast(getString(R.string.goods_not_deleted, goods.name))
            }
            .create()
            .show()

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(viewModel::onDestinationChanged)
    }

    override fun onStop() {
        findNavController().removeOnDestinationChangedListener(viewModel::onDestinationChanged)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    companion object {
        private const val TAG = "GoodsListFragment"
    }
}
