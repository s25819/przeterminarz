package pl.edu.pjatk.s25819.przeterminarz.repositories

import pl.edu.pjatk.s25819.przeterminarz.model.ExpirationFilter
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory

interface GoodsRepository {

    fun getAllGoods() : List<Goods>

    fun getGoodsByCategory(category: GoodsCategory) : List<Goods>

    fun getGoodsByCriteria(category: GoodsCategory, expired: ExpirationFilter ): List<Goods>

    fun getGoodsById(id: Int): Goods

    fun saveGoods(goods: Goods)

    fun removeGoods(goods: Goods)

}