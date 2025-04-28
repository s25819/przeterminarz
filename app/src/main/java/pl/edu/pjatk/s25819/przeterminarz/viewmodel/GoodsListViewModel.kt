package pl.edu.pjatk.s25819.przeterminarz.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        } else {
            Log.d(TAG, "Kategoria się nie zmieniła, pomijam loadGoods()")
        }
    }

    fun setFilter(newFilter: ExpirationFilter) {
        if (filter.value != newFilter) {
            filter.value = newFilter
            loadGoods()
        } else {
            Log.d(TAG, "Filtr expiration się nie zmienił, pomijam loadGoods()")
        }
    }

    fun loadGoods() {
        viewModelScope.launch {
            while (!RepositoryLocator.isInitialized) {
                Log.d(TAG, "Czekam na wgranie wszystkich przykładowych danych")
                delay(100)
            }
            val cat = category.value ?: GoodsCategory.ALL
            val filt = filter.value ?: ExpirationFilter.ALL
            val filtered = repository.getGoodsByCriteria(cat, filt)
            goodsList.value = filtered
        }
    }

    fun onRemoveGoods(goods: Goods) {
        viewModelScope.launch {
            repository.removeGoods(goods)
            loadGoods()
        }
    }

    fun onThrowAwayGoods(goods: Goods) {
        viewModelScope.launch {
            goods.markAsThrownAway()
            repository.saveGoods(goods)
            loadGoods()
        }
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

    companion object {
        private const val TAG = "GoodsListViewModel"
    }
}
