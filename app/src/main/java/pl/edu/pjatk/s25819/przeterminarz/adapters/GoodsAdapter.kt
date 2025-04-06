package pl.edu.pjatk.s25819.przeterminarz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.s25819.przeterminarz.databinding.GoodsItemLayoutBinding
import pl.edu.pjatk.s25819.przeterminarz.model.Goods

class GoodsViewHolder(itemViewBinding: GoodsItemLayoutBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {

    val imageView = itemViewBinding.goodsImage
    val nameTextView = itemViewBinding.goodsName
}

class GoodsAdapter : RecyclerView.Adapter<GoodsViewHolder>() {

    private var goodsList = mutableListOf<Goods>()

    fun updateList(updatedGoodsList: List<Goods>) {
        goodsList.clear()
        goodsList.addAll(updatedGoodsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {

        val itemViewBinding = GoodsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
            .also {

                it.root.setOnClickListener {
                    println("Kliknięcie " + it)
                }
                it.root.setOnLongClickListener {
                    true
                }
            }

        return GoodsViewHolder(itemViewBinding)

    }

    override fun getItemCount(): Int = goodsList.size

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        holder.let {
            it.nameTextView.text = goodsList[position].name
            it.imageView.setImageResource(goodsList[position].image)
        }
    }
}