package pl.edu.pjatk.s25819.przeterminarz.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.navigation.Destination
import pl.edu.pjatk.s25819.przeterminarz.navigation.PopBackStack
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator
import java.time.LocalDate

class ManageGoodsViewModel(application: Application) : AndroidViewModel(application) {

    private val goodsRepository = RepositoryLocator.goodsRepository

    private var currentGoods: Goods? = null

    val goodsName = MutableLiveData<String>()
    val goodsCategory = MutableLiveData<GoodsCategory>()
    val goodsQuantity = MutableLiveData<String>()
    val goodsExpirationDate = MutableLiveData<String>()
    val goodsImageByteArray = MutableLiveData<ByteArray?>()

    val switchQuantityEnabled = MutableLiveData<Boolean>(true)
    val goodsManageButtonText = MutableLiveData<Int>()
    val navigation = MutableLiveData<Destination>()

    fun init(id: Int?) {
        Log.i(TAG, "Wywołano init z id: $id")

        viewModelScope.launch {
            currentGoods = id?.let { goodsRepository.getGoodsById(it) }?.also { goods ->
                goodsName.value = goods.name
                goodsCategory.value = goods.category
                goodsQuantity.value = goods.quantity?.toString()
                goodsExpirationDate.value = goods.expirationDate.toString()
                goodsImageByteArray.value = goods.thumbnail
            }

            goodsManageButtonText.value = currentGoods?.run {
                R.string.manage_goods_button_edit_goods_label
            } ?: run {
                R.string.manage_goods_button_add_goods_label
            }

            Log.i(TAG, "Wartość currentGoods?quantity != null: ${currentGoods?.quantity != null}")
            switchQuantityEnabled.value = currentGoods?.hasQuantity() ?: true
        }
    }

    fun saveGoods(selectedCategory: GoodsCategory) {
        viewModelScope.launch {
            val id = currentGoods?.id ?: 0
            val quantity = calculateQuantity()

            val goods = Goods(
                id = id,
                name = goodsName.value.orEmpty(),
                category = selectedCategory,
                quantity = quantity,
                expirationDate = convertDate(),
                thumbnail = goodsImageByteArray.value,
                markedAsThrownAway = false
            )

            goodsRepository.saveGoods(goods)
            navigation.value = PopBackStack()
        }
    }

    private fun calculateQuantity(): Int? {
        Log.i("ManageGoodsViewModel", "Stan switchQuantityEnabled: ${switchQuantityEnabled.value}")
        if (switchQuantityEnabled.value == false) {
            return null
        }
        return goodsQuantity.value?.toIntOrNull()
    }

    fun onImageSelected(uri: Uri) {
        viewModelScope.launch {
            val byteArray = withContext(Dispatchers.IO) {
                createThumbnailByteArray(uri.toString())
            }
            goodsImageByteArray.value = byteArray
        }
    }

    private fun createThumbnailByteArray(uriString: String): ByteArray? {
        val context = getApplication<Application>().applicationContext
        return try {
            val uri = uriString.toUri()
            val inputStream = context.contentResolver.openInputStream(uri)
            val originalBitmap = inputStream?.use { BitmapFactory.decodeStream(it) }
            if (originalBitmap != null) {
                val thumbnail = originalBitmap.scale(150, 150)

                val outputStream = java.io.ByteArrayOutputStream()
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.toByteArray()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Błąd tworzenia miniaturki ByteArray: ${e.message}")
            null
        }
    }

    private fun convertDate(): LocalDate {
        return goodsExpirationDate.value?.let {
            try {
                LocalDate.parse(it)
            } catch (e: Exception) {
                Log.e(TAG, "Błąd w konwersji daty: ${e.message}")
                null
            }
        } ?: LocalDate.now()
    }

    companion object {
        private const val TAG = "ManageGoodsViewModel"
    }
}
