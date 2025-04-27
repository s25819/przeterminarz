package pl.edu.pjatk.s25819.przeterminarz.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.FormType

class AddGoods : Destination() {
    override fun navigate(controller: NavController) {
        controller.navigate(
            R.id.action_goodsListFragment_to_manageGoodsFragment, bundleOf(
                "type" to FormType.New
            )
        )
    }

    override fun toString(): String {
        return "Nawigacja do dodania nowego goods"
    }
}