package pl.edu.pjatk.s25819.przeterminarz.viewmodel.bindingatapters

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory

@BindingAdapter("selectedCategory")
fun bindSpinnerSelectedCategory(spinner: Spinner, category: GoodsCategory?) {
    if (category == null) return
    val adapter = spinner.adapter
    for (i in 0 until adapter.count) {
        val item = adapter.getItem(i)
        if (item == category) {
            spinner.setSelection(i)
            break
        }
    }
}