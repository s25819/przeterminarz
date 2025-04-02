package pl.edu.pjatk.s25819.przeterminarz.model

import android.content.Context
import pl.edu.pjatk.s25819.przeterminarz.R

enum class GoodsCategory(private val displayNameCode: Int) {

    GROCERY(R.string.grocery),
    MEDICINE(R.string.medicine),
    COSMETICS(R.string.cosmetics);

    public fun getDisplayName(context: Context): String {
        return context.getString(displayNameCode)
    }
}