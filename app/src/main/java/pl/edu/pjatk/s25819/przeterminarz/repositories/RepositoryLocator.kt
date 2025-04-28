package pl.edu.pjatk.s25819.przeterminarz.repositories

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.database.GoodsDatabase
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import java.time.LocalDate

object RepositoryLocator {

    lateinit var goodsRepository: GoodsRepository
        private set

    @Volatile
    var isInitialized = false
        private set

    suspend fun init(context: Context) {
        val goodsDb = GoodsDatabase.getDbInstance(context)
        goodsRepository = DBGoodsRepository(goodsDb.goodsDao())
        resetAndInitializeDatabase(context)
        isInitialized = true
    }

    private suspend fun resetAndInitializeDatabase(context: Context) {
        withContext(Dispatchers.IO) {
            val existingGoods = goodsRepository.getAllGoods()
            for (goods in existingGoods) {
                goodsRepository.removeGoods(goods)
            }

            val sampleGoods = listOf(
                Goods(
                    id = 0,
                    name = "Mleko 2%",
                    category = GoodsCategory.GROCERY,
                    quantity = 2,
                    expirationDate = LocalDate.now().minusDays(5),
                    thumbnail = null,
                    markedAsThrownAway = false
                ),
                Goods(
                    id = 0,
                    name = "Witaminy C",
                    category = GoodsCategory.MEDICINE,
                    quantity = 1,
                    expirationDate = LocalDate.now().plusMonths(2),
                    thumbnail = null,
                    markedAsThrownAway = false
                ),
                Goods(
                    id = 0,
                    name = "Szampon",
                    category = GoodsCategory.COSMETICS,
                    quantity = 1,
                    expirationDate = LocalDate.now().plusMonths(12),
                    thumbnail = null,
                    markedAsThrownAway = false
                )
            )

            sampleGoods.forEach { goods ->
                goodsRepository.saveGoods(goods)
                Log.d(TAG, "Dodano przykładowy produkt: ${goods.name}")
            }
        }
    }

    private const val TAG = "RepositoryLocator"
}
