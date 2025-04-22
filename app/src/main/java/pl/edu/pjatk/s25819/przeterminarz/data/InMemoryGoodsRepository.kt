package pl.edu.pjatk.s25819.przeterminarz.data

import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.exceptions.GoodsNotFoundException
import pl.edu.pjatk.s25819.przeterminarz.model.ExpirationFilter
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.repositories.GoodsRepository
import java.time.LocalDate

object InMemoryGoodsRepository : GoodsRepository {

    private var goodsData = mutableListOf<Goods>(
        Goods(0, "Mleko", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(6), R.mipmap.mleko),
        Goods(
            1, "Chleb", GoodsCategory.COSMETICS, 3, LocalDate.now().minusDays(10), R.mipmap.mleko
        ),
        Goods(2, "Masło", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(4), R.mipmap.mleko),
        Goods(3, "Jajka", GoodsCategory.MEDICINE, 1, LocalDate.now().minusDays(3), R.mipmap.mleko),
        Goods(4, "Ser", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(10), R.mipmap.mleko),
    )

    override fun getAllGoods(): List<Goods> = goodsData

    override fun getGoodsByCategory(category: GoodsCategory): List<Goods> {
        return when (category) {
            GoodsCategory.ALL -> goodsData
            else -> goodsData.filter { it.category == category }
        }
    }

    override fun getGoodsByCriteria(
        category: GoodsCategory,
        expired: ExpirationFilter
    ): List<Goods> {
        return goodsData
            .filter { goods ->
                val categoryMatch = category == GoodsCategory.ALL || goods.category == category
                val statusMatch = when (expired) {
                    ExpirationFilter.VALID -> !goods.isExpired()
                    ExpirationFilter.EXPIRED -> goods.isExpired()
                    ExpirationFilter.ALL -> true
                }
                (categoryMatch && statusMatch)
            }
            .map { it.copy()
            }
    }

    override fun getGoodsById(id: Int): Goods {
        val goods = goodsData.find { it.id == id }
        return goods ?: throw GoodsNotFoundException("Produkt o ID $id nie istnieje")
    }

    override fun saveGoods(goods: Goods) {
        if (goods.id == -1) {
            val newId = this.goodsData.size + 1
            this.goodsData.add(goods.copy(id = newId))
        } else {
            val index = this.goodsData.indexOfFirst { it.id == goods.id }
            this.goodsData[index] = goods
        }
    }

    override fun removeGoods(goods: Goods) {
        goodsData.remove(goods)
    }
}