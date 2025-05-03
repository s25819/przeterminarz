package pl.edu.pjatk.s25819.przeterminarz.repositories

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.edu.pjatk.s25819.przeterminarz.database.GoodsDatabase
import pl.edu.pjatk.s25819.przeterminarz.repositories.impl.DBGoodsRepository
import pl.edu.pjatk.s25819.przeterminarz.repositories.sampledata.SampleData.sampleGoods

object RepositoryLocator {

    lateinit var goodsRepository: GoodsRepository
        private set

    @Volatile
    var isInitialized = false
        private set

    suspend fun init(context: Context) {
        val goodsDb = GoodsDatabase.getDbInstance(context)
        goodsRepository = DBGoodsRepository(goodsDb.goodsDao())
        resetAndInitializeDatabase()
        isInitialized = true
    }

    private suspend fun resetAndInitializeDatabase() {
        withContext(Dispatchers.IO) {
            val existingGoods = goodsRepository.getAllGoods()
            for (goods in existingGoods) {
                goodsRepository.removeGoods(goods)
            }

            sampleGoods.forEach { goods ->
                goodsRepository.saveGoods(goods)
                Log.d(TAG, "Dodano przykładowy produkt: ${goods.name}")
            }
        }
    }

    private const val TAG = "RepositoryLocator"
}
