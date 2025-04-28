package pl.edu.pjatk.s25819.przeterminarz.model

import java.time.LocalDate

data class Goods(
    val id: Int,
    val name: String,
    val category: GoodsCategory,
    val quantity: Int? = 0,
    val expirationDate: LocalDate,
    val thumbnail: ByteArray?,
    var markedAsThrownAway: Boolean = false
) {

    fun isExpired(): Boolean {
        return expirationDate.isBefore(LocalDate.now())
    }

    fun hasQuantity(): Boolean {
        return quantity != null && quantity > 0
    }

    fun markAsThrownAway() {
        markedAsThrownAway = true
    }

    fun isThrownAway(): Boolean {
        return markedAsThrownAway
    }
}
