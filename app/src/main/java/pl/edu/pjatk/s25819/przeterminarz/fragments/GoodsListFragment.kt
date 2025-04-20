package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.adapters.GoodsAdapter
import pl.edu.pjatk.s25819.przeterminarz.databinding.FragmentGoodsListBinding
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.repositories.GoodsRepository
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator

class GoodsListFragment : Fragment() {

    private lateinit var adapter: GoodsAdapter
    private lateinit var goodsRepository: GoodsRepository

    private var _binding: FragmentGoodsListBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory: GoodsCategory = GoodsCategory.ALL
    private var selectedStatus: String = "all"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoodsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        goodsRepository = RepositoryLocator().goodsRepository

        adapter = GoodsAdapter()
        binding.goodsRecyclerView.apply {
            adapter = this@GoodsListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        setupListeners()
        loadGoods()
    }

    private fun loadGoods(selectedCategory: GoodsCategory = GoodsCategory.ALL) {
        goodsRepository.getGoodsByCategory(selectedCategory).let { goods ->
            adapter.updateList(goods)
        }
    }

    private fun setupListeners() {

        /**
         * Listener do dodawania nowego produktu
         */
        binding.addNewItem.setOnClickListener {
            findNavController().navigate(R.id.action_goodsListFragment_to_manageGoodsFragment)
        }

        /**
         * Listener do filtrowania produktów po kategorii i ważności
         */
        binding.categoryFilterGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            val selectedCategoryId = checkedIds.firstOrNull() ?: R.id.category_all

            val categoryMap = mapOf(
                R.id.category_all to GoodsCategory.ALL,
                R.id.category_food to GoodsCategory.GROCERY,
                R.id.category_medicine to GoodsCategory.MEDICINE,
                R.id.category_cosmetics to GoodsCategory.COSMETICS,
            )

            selectedCategory = categoryMap[selectedCategoryId] ?: GoodsCategory.ALL

            Log.d("CategoryFilter", "selectedCategory: $selectedCategory")

            loadGoods(selectedCategory)
        }
    }
}