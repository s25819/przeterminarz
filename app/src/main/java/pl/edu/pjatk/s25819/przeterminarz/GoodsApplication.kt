package pl.edu.pjatk.s25819.przeterminarz

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator

class GoodsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            RepositoryLocator.init(applicationContext)
        }
    }
}