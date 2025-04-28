package pl.edu.pjatk.s25819.przeterminarz.model

import java.time.LocalDate

enum class ExpirationFilter {
    VALID, EXPIRED, ALL;


    fun matches(expirationDate: LocalDate): Boolean {
        return when (this) {
            VALID -> expirationDate.isAfter(LocalDate.now()) || expirationDate.isEqual(LocalDate.now())
            EXPIRED -> expirationDate.isBefore(LocalDate.now())
            ALL -> true
        }
    }
}