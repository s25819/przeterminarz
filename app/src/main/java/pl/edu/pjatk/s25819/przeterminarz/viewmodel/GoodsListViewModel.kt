package pl.edu.pjatk.s25819.przeterminarz.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.*
import pl.edu.pjatk.s25819.przeterminarz.navigation.*
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator

class GoodsListViewModel : ViewModel() {

    private val repository = RepositoryLocator.goodsRepository

    val category = MutableLiveData(GoodsCategory.ALL)
    val filter = MutableLiveData(ExpirationFilter.ALL)
    val goodsList = MutableLiveData<List<Goods>>(emptyList())
    val navigation = MutableLiveData<Destination>()

    fun setCategory(newCategory: GoodsCategory) {
        if (category.value != newCategory) {
            category.value = newCategory
            loadGoods()
        }
    }

    fun setFilter(newFilter: ExpirationFilter) {
        if (filter.value != newFilter) {
            filter.value = newFilter
            loadGoods()
        }
    }

    fun loadGoods() {
        val cat = category.value ?: GoodsCategory.ALL
        val filt = filter.value ?: ExpirationFilter.ALL
        val filtered = repository.getGoodsByCriteria(cat, filt).sortedBy { it.expirationDate }
        goodsList.value = filtered
    }

    fun onAddGoods(goods: Goods) {
        repository.saveGoods(goods)
        loadGoods()
    }

    fun onRemoveGoods(goods: Goods) {
        repository.removeGoods(goods)
        loadGoods()
    }

    fun onThrowAwayGoods(goods: Goods) {
        goods.markAsThrownAway()
        repository.saveGoods(goods)
        loadGoods()
    }

    fun navigateToAdd() {
        navigation.value = AddGoods()
    }

    fun navigateToEdit(goods: Goods) {
        navigation.value = EditGoods(goods)
    }

    fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destination.id == R.id.goodsListFragment) {
            loadGoods()
        }
    }
}
