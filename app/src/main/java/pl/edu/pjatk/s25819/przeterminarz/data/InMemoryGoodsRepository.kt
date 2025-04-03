package pl.edu.pjatk.s25819.przeterminarz.data

import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.repositories.GoodsRepository
import java.time.LocalDate

object InMemoryGoodsRepository : GoodsRepository {

    private var goods = mutableListOf<Goods>(
        Goods(1, "Mleko", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(10), R.mipmap.mleko),
        Goods(2, "Chleb", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(10), R.mipmap.mleko),
        Goods(3, "Masło", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(10), R.mipmap.mleko),
        Goods(4, "Jajka", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(10), R.mipmap.mleko),
        Goods(5, "Ser", GoodsCategory.GROCERY, 0, LocalDate.now().plusDays(10), R.mipmap.mleko),
    )

    override fun fetchAllGoods(): List<Goods> = goods

    override fun fetchGoodsByCategory(category: GoodsCategory): List<Goods> {
        return goods.filter { it.category == category }
    }
}