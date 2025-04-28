package pl.edu.pjatk.s25819.przeterminarz.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.navigation.Destination
import pl.edu.pjatk.s25819.przeterminarz.navigation.PopBackStack
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator
import java.io.IOException
import java.time.LocalDate

class ManageGoodsViewModel(application: Application) : AndroidViewModel(application) {

    private val goodsRepository = RepositoryLocator.goodsRepository

    private var currentGoods: Goods? = null

    val goodsName = MutableLiveData<String>()
    val goodsCategory = MutableLiveData<GoodsCategory>()
    val goodsQuantity = MutableLiveData<String>()
    val goodsExpirationDate = MutableLiveData<String>()
    val goodsImageThumbnail = MutableLiveData<String?>() // ścieżka pliku lub null
    val goodsManageButtonText = MutableLiveData<Int>()

    val navigation = MutableLiveData<Destination>()

    fun init(id: Int?) {
        viewModelScope.launch {
            currentGoods = id?.let { goodsRepository.getGoodsById(it) }?.also { goods ->
                goodsName.value = goods.name
                goodsCategory.value = goods.category
                goodsQuantity.value = goods.quantity?.toString()
                goodsExpirationDate.value = goods.expirationDate.toString()
                goodsImageThumbnail.value = goods.imageName
            }

            goodsManageButtonText.value = if (currentGoods == null) {
                R.string.manage_goods_button_add_goods_label
            } else {
                R.string.manage_goods_button_edit_goods_label
            }
        }
    }

    fun saveGoods(selectedCategory: GoodsCategory) {
        viewModelScope.launch {
            val id = currentGoods?.id ?: 0
            val quantity = goodsQuantity.value?.toIntOrNull()

            val finalBitmap = loadImageOrDefault(selectedCategory)

            val goods = Goods(
                id = id,
                name = goodsName.value.orEmpty(),
                category = selectedCategory,
                quantity = quantity,
                expirationDate = convertDate(),
                image = finalBitmap,
                imageName = goodsImageThumbnail.value ?: "",
                markedAsThrownAway = false
            )

            goodsRepository.saveGoods(goods)
            navigation.value = PopBackStack()
        }
    }

    private fun loadImageOrDefault(category: GoodsCategory): Bitmap {
        val imagePath = goodsImageThumbnail.value

        return if (!imagePath.isNullOrEmpty()) {
            try {
                BitmapFactory.decodeFile(imagePath) ?: GoodsCategory.getDefaultImage(getApplication(), category)
            } catch (e: IOException) {
                Log.e(TAG, "Błąd w ładowaniu obrazka: ${e.message}")
                GoodsCategory.getDefaultImage(getApplication(), category)
            }
        } else {
            GoodsCategory.getDefaultImage(getApplication(), category)
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
