package pl.edu.pjatk.s25819.przeterminarz.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.navigation.Destination
import pl.edu.pjatk.s25819.przeterminarz.navigation.PopBackStack
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator
import java.time.LocalDate

class ManageGoodsViewModel : ViewModel() {

    private val goodsRepository = RepositoryLocator.goodsRepository

    private var currentGoods: Goods? = null

    val goodsName = MutableLiveData<String>()
    val goodsCategory = MutableLiveData<GoodsCategory>()
    val goodsQuantity = MutableLiveData<String>()
    val goodsExpirationDate = MutableLiveData<String>()
    val goodsImage = MutableLiveData<Int>(R.mipmap.mleko) // Domyślne zdjęcie
    val goodsManageButtonText = MutableLiveData<Int>()

    val navigation = MutableLiveData<Destination>()

    fun init(id: Int?) {
        currentGoods = id?.let { goodsRepository.getGoodsById(it) }?.also { goods ->
            goodsName.value = goods.name
            goodsCategory.value = goods.category
            goodsQuantity.value = goods.quantity?.toString()
            goodsExpirationDate.value = goods.expirationDate.toString()
            goodsImage.value = goods.image
        }

        goodsManageButtonText.value = if (currentGoods == null) {
            R.string.manage_goods_button_add_goods_label
        } else {
            R.string.manage_goods_button_edit_goods_label
        }
    }

    fun saveGoods(selectedCategory: GoodsCategory) {
        val id = (currentGoods)?.id ?: -1
        val quantity = goodsQuantity.value?.toIntOrNull()

        val goods = Goods(
            id = id,
            name = goodsName.value.orEmpty(),
            category = selectedCategory,
            quantity = quantity,
            expirationDate = convertDate(),
            image = goodsImage.value ?: selectedCategory.getDefaultImage()
        )

        navigation.value = PopBackStack()

        goodsRepository.saveGoods(goods)
    }

    private fun convertDate(): LocalDate {
        return goodsExpirationDate.value?.let {
            try {
                LocalDate.parse(it)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting date: ${e.message}")
                null
            }
        } ?: LocalDate.now()
    }

    companion object {
        private const val TAG = "ManageGoodsViewModel"
    }
}