package pl.edu.pjatk.s25819.przeterminarz.entities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goods")
data class GoodsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val quantity: Int?,
    val expirationDate: String,
    val imageThumbnail: String,
    val imageName: String,
    val thrownAway: Boolean
) {}

private fun GoodsEntity.convertToBitmap(string: String): Bitmap {
    return BitmapFactory.decodeFile(string)
}
