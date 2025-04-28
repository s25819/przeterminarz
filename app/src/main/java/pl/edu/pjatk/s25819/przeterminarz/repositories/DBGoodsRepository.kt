package pl.edu.pjatk.s25819.przeterminarz.repositories

import pl.edu.pjatk.s25819.przeterminarz.dao.GoodsDao
import pl.edu.pjatk.s25819.przeterminarz.model.ExpirationFilter
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.repositories.mappers.toEntity
import pl.edu.pjatk.s25819.przeterminarz.repositories.mappers.toGoods

class DBGoodsRepository(private val goodsDao: GoodsDao) : GoodsRepository {

    override suspend fun getAllGoods(): List<Goods> {
        return goodsDao.getAllGoods().map { it.toGoods() }
    }

    override suspend fun getGoodsByCategory(category: GoodsCategory): List<Goods> {
        val allGoods = goodsDao.getAllGoods()
        return allGoods.map { it.toGoods() }.filter { it.category == category }
    }

    override suspend fun getGoodsByCriteria(
        category: GoodsCategory,
        expired: ExpirationFilter
    ): List<Goods> {
        val goodsEntities = if (category == GoodsCategory.ALL) {
            goodsDao.getAllGoods()
        } else {
            goodsDao.getGoodsByCriteria(category.name)
        }

        return goodsEntities.map { it.toGoods() }
            .filter { goods -> expired.matches(goods.expirationDate) }
    }

    override suspend fun getGoodsById(id: Int): Goods {
        return goodsDao.getGoodsById(id)?.toGoods()
            ?: throw IllegalArgumentException("Goods with ID $id not found")
    }

    override suspend fun saveGoods(goods: Goods) {
        goodsDao.createOrUpdate(goods.toEntity())
    }

    override suspend fun removeGoods(goods: Goods) {
        goodsDao.deleteGoods(goods.toEntity())
    }
}
