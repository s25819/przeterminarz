package pl.edu.pjatk.s25819.przeterminarz.data

import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.repositories.GoodsRepository
import java.time.LocalDate

object InMemoryGoodsRepository : GoodsRepository {

    private var goods = mutableListOf<Goods>(
        Goods(1, "Mleko", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(6), R.mipmap.mleko),
        Goods(2, "Chleb", GoodsCategory.COSMETICS, 0, LocalDate.now().minusDays(10), R.mipmap.mleko),
        Goods(3, "Masło", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(4), R.mipmap.mleko),
        Goods(4, "Jajka", GoodsCategory.MEDICINE, 0, LocalDate.now().minusDays(3), R.mipmap.mleko),
        Goods(5, "Ser", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(10), R.mipmap.mleko),
    )

    override fun getAllGoods(): List<Goods> = goods

    override fun getGoodsByCategory(category: GoodsCategory): List<Goods> {
        return when (category) {
            GoodsCategory.ALL -> goods
            else -> goods.filter { it.category == category }
        }
    }

    override fun getGoodsByCriteria(category: GoodsCategory, expired: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getGoodsById(id: Int): Goods? {
        return goods.find { it.id == id }
    }
}