package pl.edu.pjatk.s25819.przeterminarz.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.model.FormType
import pl.edu.pjatk.s25819.przeterminarz.model.Goods

class EditGoods(private val goods: Goods) : Destination() {
    override fun navigate(controller: NavController) {
        controller.navigate(
            R.id.action_goodsListFragment_to_manageGoodsFragment, bundleOf(
                "type" to FormType.Edit(goods.id)
            )
        )
    }
}