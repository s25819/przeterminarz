package pl.edu.pjatk.s25819.przeterminarz.repositories

import pl.edu.pjatk.s25819.przeterminarz.exceptions.GoodsNotFoundException
import pl.edu.pjatk.s25819.przeterminarz.model.ExpirationFilter
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import java.time.LocalDate

object InMemoryGoodsRepository : GoodsRepository {

    private var goodsData = mutableListOf<Goods>(
        Goods(
            0,
            "Mleko",
            GoodsCategory.GROCERY,
            0,
            LocalDate.now().plusDays(6),
            null,
            markedAsThrownAway = false
        )
    )

    override suspend fun getAllGoods(): List<Goods> = goodsData

    override suspend fun getGoodsByCategory(category: GoodsCategory): List<Goods> {
        return when (category) {
            GoodsCategory.ALL -> goodsData
            else -> goodsData.filter { it.category == category }
        }
    }

    override suspend fun getGoodsByCriteria(
        category: GoodsCategory, expired: ExpirationFilter
    ): List<Goods> {
        return goodsData.filter { goods ->
            val categoryMatch = category == GoodsCategory.ALL || goods.category == category
            val statusMatch = when (expired) {
                ExpirationFilter.VALID -> !goods.isExpired()
                ExpirationFilter.EXPIRED -> goods.isExpired()
                ExpirationFilter.ALL -> true
            }
            (categoryMatch && statusMatch)
        }.map {
            it.copy()
        }
    }

    override suspend fun getGoodsById(id: Int): Goods {
        val goods = goodsData.find { it.id == id }
        return goods ?: throw GoodsNotFoundException("Produkt o ID $id nie istnieje")
    }

    override suspend fun saveGoods(goods: Goods) {
        if (goods.id == -1) {
            val newId = this.goodsData.size + 1
            this.goodsData.add(goods.copy(id = newId))
        } else {
            val index = this.goodsData.indexOfFirst { it.id == goods.id }
            this.goodsData[index] = goods
        }
    }

    override suspend fun removeGoods(goods: Goods) {
        goodsData.remove(goods)
    }
}