package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.adapters.GoodsAdapter
import pl.edu.pjatk.s25819.przeterminarz.databinding.FragmentGoodsListBinding
import pl.edu.pjatk.s25819.przeterminarz.repositories.GoodsRepository
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator

class GoodsListFragment : Fragment() {

    private lateinit var binding : FragmentGoodsListBinding
    private lateinit var adapter : GoodsAdapter
    private lateinit var goodsRepository : GoodsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentGoodsListBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_goods_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        goodsRepository = RepositoryLocator().goodsRepository

        adapter = GoodsAdapter()
        binding.goodsRecyclerView.apply {
            adapter = this@GoodsListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        loadGoods()
    }

    private fun loadGoods() {
        goodsRepository.fetchAllGoods().apply {
            adapter.updateList(this)
        }
    }
}