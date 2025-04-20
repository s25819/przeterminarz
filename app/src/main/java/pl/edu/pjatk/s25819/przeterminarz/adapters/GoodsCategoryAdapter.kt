package pl.edu.pjatk.s25819.przeterminarz.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory

class GoodsCategoryAdapter(context: Context, items: List<GoodsCategory>) :
    ArrayAdapter<GoodsCategory>(context, android.R.layout.simple_spinner_item, items) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        (view as? TextView)?.text = getItem(position)?.getDisplayName(context)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        (view as? TextView)?.text = getItem(position)?.getDisplayName(context)
        return view
    }

    override fun setDropDownViewResource(resource: Int) {
        super.setDropDownViewResource(resource)
    }
}
