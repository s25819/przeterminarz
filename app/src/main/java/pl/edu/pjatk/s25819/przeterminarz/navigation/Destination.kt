package pl.edu.pjatk.s25819.przeterminarz.navigation

import android.util.Log
import androidx.navigation.NavController
import java.util.concurrent.atomic.AtomicBoolean

abstract class Destination {

    private val consumed : AtomicBoolean = AtomicBoolean(false)

    abstract fun navigate(controller: NavController)

    fun resolve(controller: NavController) {
        Log.d(TAG, "Navigating to $this")
        if (consumed.compareAndSet(false, true)) {
            navigate(controller)
        }
    }

    companion object {
        val TAG = this::class.java.name.toString()
    }
}