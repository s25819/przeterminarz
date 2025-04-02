package pl.edu.pjatk.s25819.przeterminarz.repositories

import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory

interface GoodsRepository {

    fun fetchAllGoods() : List<Goods>

    fun fetchGoodsByCategory(category: GoodsCategory) : List<Goods>

}