package pl.edu.pjatk.s25819.przeterminarz.repositories.mappers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import pl.edu.pjatk.s25819.przeterminarz.entities.GoodsEntity
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import java.time.LocalDate
import androidx.core.graphics.createBitmap

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

private fun createEmptyBitmap(): Bitmap {
    return createBitmap(100, 100)
}
