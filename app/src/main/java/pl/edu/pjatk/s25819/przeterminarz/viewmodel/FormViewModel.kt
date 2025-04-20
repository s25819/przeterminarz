package pl.edu.pjatk.s25819.przeterminarz.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.repositories.GoodsRepository
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator

class FormViewModel : ViewModel() {

    val repository: GoodsRepository = RepositoryLocator.goodsRepository

    private var edited: Goods? = null

    val name = MutableLiveData<String>("")
    val category = MutableLiveData<GoodsCategory>()

    fun init(id: Int?) {
        edited = id?.let {
            repository.getGoodsById(it)
        }?.also {
            name.value = it.name
            category.value = it.category
        }
    }

    fun onSave() {}
}