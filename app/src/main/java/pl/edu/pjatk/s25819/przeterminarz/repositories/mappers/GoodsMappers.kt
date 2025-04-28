package pl.edu.pjatk.s25819.przeterminarz.repositories.mappers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import pl.edu.pjatk.s25819.przeterminarz.entities.GoodsEntity
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

fun GoodsEntity.toGoods(): Goods {
    return Goods(
        id = this.id,
        name = this.name,
        category = GoodsCategory.valueOf(this.category),
        quantity = this.quantity,
        expirationDate = LocalDate.parse(this.expirationDate),
        image = convertToBitmap(this.imageName),
        imageName = this.imageName,
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
        imageThumbnail = convertToByteArray(this.image),
        imageName = this.imageName,
        thrownAway = this.markedAsThrownAway
    )
}

private fun convertToBitmap(path: String): Bitmap {
    val file = File(path)
    return if (file.exists()) {
        BitmapFactory.decodeFile(file.absolutePath) ?: createEmptyBitmap()
    } else {
        createEmptyBitmap()
    }
}

private fun createEmptyBitmap(): Bitmap {
    return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
}

private fun convertToByteArray(bitmap: Bitmap): String {
    val file = File.createTempFile("goods_image_", ".jpg") // tworzymy tymczasowy plik
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return file.absolutePath
}