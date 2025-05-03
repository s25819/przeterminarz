package pl.edu.pjatk.s25819.przeterminarz.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.exceptions.CategoryNotFoundException

enum class GoodsCategory(private val displayNameCode: Int) {

    ALL(R.string.all),
    GROCERY(R.string.grocery),
    MEDICINE(R.string.medicine),
    COSMETICS(R.string.cosmetics);

    fun getDisplayName(context: Context): String {
        return context.getString(displayNameCode)
    }

    override fun toString(): String {
        return displayNameCode.let {
            "[GoodsCategory]"
        }
    }

    companion object {
        private val categoryNameToCategoryMap = mapOf(
            R.string.grocery to GROCERY,
            R.string.medicine to MEDICINE,
            R.string.cosmetics to COSMETICS,
            R.string.all to ALL
        )

        fun getDefaultImage(context: Context, category: GoodsCategory): Bitmap {
            val assetsManager = context.assets
            val defaultImage =  when (category) {
                GROCERY -> "images/groceries_default.jpg"
                MEDICINE -> "images/medicine_default.jpg"
                COSMETICS -> "images/cosmetics_default.jpg"
                else -> "images/groceries_default.jpg"
            }

            assetsManager.open(defaultImage).use { inputStream ->
                return BitmapFactory.decodeStream(inputStream)
            }
        }

        fun getCategory(resourceId: Int): GoodsCategory = categoryNameToCategoryMap[resourceId]
            ?: throw CategoryNotFoundException("Nie znaleziono kategorii o nazwie $resourceId")

        fun getCategory(context: Context, categoryDisplayName: String): GoodsCategory {
            return categoryNameToCategoryMap.entries.firstOrNull {
                context.getString(it.key) == categoryDisplayName
            }?.value
                ?: throw CategoryNotFoundException("Nie znaleziono kategorii o nazwie $categoryDisplayName")
        }
    }
}

