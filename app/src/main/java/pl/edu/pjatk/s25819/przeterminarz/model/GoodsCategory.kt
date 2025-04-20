package pl.edu.pjatk.s25819.przeterminarz.model

import android.content.Context
import android.util.Log
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
            // Trzeba przekazać kontekst gdzieś indziej — tymczasowo null-safe
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

