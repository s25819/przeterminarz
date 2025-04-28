package pl.edu.pjatk.s25819.przeterminarz.repositories

import pl.edu.pjatk.s25819.przeterminarz.model.ExpirationFilter
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory

interface GoodsRepository {

    suspend fun getAllGoods() : List<Goods>

    suspend fun getGoodsByCategory(category: GoodsCategory) : List<Goods>

    suspend fun getGoodsByCriteria(category: GoodsCategory, expired: ExpirationFilter ): List<Goods>

    suspend fun getGoodsById(id: Int): Goods

    suspend fun saveGoods(goods: Goods)

    suspend fun removeGoods(goods: Goods)

}