package pl.edu.pjatk.s25819.przeterminarz.repositories.mappers

import pl.edu.pjatk.s25819.przeterminarz.entities.GoodsEntity
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import java.time.LocalDate

fun GoodsEntity.toGoods(): Goods {
    return Goods(
        id = this.id,
        name = this.name,
        category = GoodsCategory.valueOf(this.category),
        quantity = this.quantity,
        expirationDate = LocalDate.parse(this.expirationDate),
        thumbnail = this.thumbnail,
        markedAsThrownAway = this.thrownAway
    )
}

fun Goods.toEntity(): GoodsEntity {
    return GoodsEntity(
        id = this.id,
        name = this.name,
        category = this.category.name,
        quantity = this.quantity,
        expirationDate = this.expirationDate.toString(),
        thumbnail = this.thumbnail,
        thrownAway = this.markedAsThrownAway
    )
}
