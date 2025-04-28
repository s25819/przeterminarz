package pl.edu.pjatk.s25819.przeterminarz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.edu.pjatk.s25819.przeterminarz.dao.GoodsDao
import pl.edu.pjatk.s25819.przeterminarz.entities.GoodsEntity

@Database(entities = [GoodsEntity::class], version = 3)
abstract class GoodsDatabase : RoomDatabase() {

    abstract fun goodsDao(): GoodsDao

    companion object {
        const val DATABASE_NAME: String = "goods_db"
        private var instance: GoodsDatabase? = null

        fun getDbInstance(context: Context): GoodsDatabase {
            return instance ?: createInstance(context)
        }

        private fun createInstance(context: Context): GoodsDatabase {
            instance = Room.databaseBuilder(
                context.applicationContext,
                GoodsDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()

            return instance!!
        }
    }
}