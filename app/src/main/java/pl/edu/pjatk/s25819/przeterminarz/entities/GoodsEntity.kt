package pl.edu.pjatk.s25819.przeterminarz.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goods")
data class GoodsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val quantity: Int?,
    val expirationDate: String,
    val thumbnail: ByteArray?,
    val thrownAway: Boolean
)
