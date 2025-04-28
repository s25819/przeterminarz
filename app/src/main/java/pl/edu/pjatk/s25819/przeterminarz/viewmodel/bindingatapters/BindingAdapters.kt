package pl.edu.pjatk.s25819.przeterminarz.viewmodel.bindingatapters

import android.graphics.BitmapFactory
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import pl.edu.pjatk.s25819.przeterminarz.R
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

@InverseBindingAdapter(attribute = "selectedCategory", event = "selectedCategoryAttrChanged")
fun getSelectedCategory(spinner: Spinner): GoodsCategory? {
    return spinner.selectedItem as? GoodsCategory
}

@BindingAdapter("selectedCategoryAttrChanged")
fun setSpinnerCategoryListener(spinner: Spinner, listener: InverseBindingListener?) {
    if (listener == null) return
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: android.view.View?,
            position: Int,
            id: Long
        ) {
            listener.onChange()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            listener.onChange()
        }
    }
}

@BindingAdapter("imageThumbnail")
fun loadImageThumbnail(view: ImageView, imageBytes: ByteArray?) {
    if (imageBytes == null) {
        view.setImageResource(R.mipmap.groceries_default)
        return
    }

    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    view.setImageBitmap(bitmap)
}