package pl.edu.pjatk.s25819.przeterminarz.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.FormType

class PopBackStack : Destination() {
    override fun navigate(controller: NavController) {
        controller.popBackStack()
    }

    override fun toString(): String {
        return "Nawigacja powrotna"
    }
}