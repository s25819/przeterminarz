package pl.edu.pjatk.s25819.przeterminarz.repositories.sampledata

import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import java.time.LocalDate

object SampleData {
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
            name = "Ziemniaki",
            category = GoodsCategory.GROCERY,
            quantity = 15,
            expirationDate = LocalDate.now().minusDays(15),
            thumbnail = null,
            markedAsThrownAway = true
        ),
        Goods(
            id = 0,
            name = "Apap",
            category = GoodsCategory.MEDICINE,
            quantity = 0,
            expirationDate = LocalDate.now().plusDays(10),
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
}