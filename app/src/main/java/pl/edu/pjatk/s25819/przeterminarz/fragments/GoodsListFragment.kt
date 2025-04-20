package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
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

    private fun setupRecyclerView() {
        goodsAdapter = GoodsAdapter { goods ->
            navigateToEditGoods(goods.id)
        }

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
        goodsAdapter.submitList(goods)
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

    companion object {
        private const val TAG = "GoodsListFragment"
    }
}