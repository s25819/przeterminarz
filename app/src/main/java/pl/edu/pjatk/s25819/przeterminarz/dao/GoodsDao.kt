package pl.edu.pjatk.s25819.przeterminarz.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.edu.pjatk.s25819.przeterminarz.entities.GoodsEntity

@Dao
interface GoodsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(goods: GoodsEntity)

    @Query("SELECT * FROM goods ORDER BY expirationDate ASC")
    suspend fun getAllGoods() : List<GoodsEntity>

    @Query("SELECT * FROM goods WHERE id = :id")
    suspend fun getGoodsById(id: Int): GoodsEntity?

    @Query("SELECT * FROM goods WHERE category = :category ORDER BY expirationDate ASC")
    suspend fun getGoodsByCriteria(category: String) : List<GoodsEntity>

    @Delete
    suspend fun deleteGoods(goods: GoodsEntity)
}