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
    val image: Int,
    private var markedAsThrownAway: Boolean = false
) {

    /**
     * Funkcja zwracająca informację czy produkt jest przeterminowany
     *
     * @return true jeśli produkt jest przeterminowany, false w przeciwnym wypadku
     */
    fun isExpired(): Boolean {
        return expirationDate.isBefore(LocalDate.now())
    }

    /**
     * Funkcja zwracająca informację czy produkt ma określona ilość
     *
     * @return true jeśli produkt ma określona ilość, false w przeciwnym wypadku
     */
    fun hasQuantity(): Boolean {
        return quantity != null && quantity > 0
    }

    /**
     * Funkcja ustawiająca, że produkt został odrzucony
     *
     * @return void
     */
    fun markAsThrownAway() {
        markedAsThrownAway = true
    }

    /**
     * Funkcja zwracająca informację czy produkt został odrzucony
     *
     * @return true jeśli produkt został odrzucony, false w przeciwnym wypadku
     */
    fun isThrownAway(): Boolean {
        return markedAsThrownAway
    }
}
