package pl.edu.pjatk.s25819.przeterminarz.model

import androidx.annotation.DrawableRes
import java.time.LocalDate

data class Goods(
    val id: Int,
    val name: String,
    val category: GoodsCategory,
    val quantity: Int? = 0,
    val expirationDate: LocalDate,
    @DrawableRes
    val image: Int
) {

    /**
     * Funkcja zwracająca informację czy produkt jest przeterminowany
     *
     * @return true jeśli produkt jest przeterminowany, false w przeciwnym wypadku
     */
    fun isExpired(): Boolean {
        val currentDate = LocalDate.now()
        return expirationDate.isBefore(currentDate)
    }
}
