package pl.edu.pjatk.s25819.przeterminarz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.databinding.GoodsItemLayoutBinding
import pl.edu.pjatk.s25819.przeterminarz.model.Goods

class GoodsAdapter(
    private val onCardClick: (Goods) -> Unit
) : ListAdapter<Goods, GoodsViewHolder>(GoodsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GoodsItemLayoutBinding.inflate(layoutInflater, parent, false)
        return GoodsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        holder.bind(getItem(position), onCardClick)
    }
}

class GoodsViewHolder(private val binding: GoodsItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(goods: Goods, onCardClick: (Goods) -> Unit) = with(binding) {
        goodsName.text = goods.name
        goodsImage.setImageResource(goods.image)
        expirationDateValue.text = goods.expirationDate.toString()
        goodsCategoryValue.text = goods.category.getDisplayName(root.context)
        isValidValue.text = root.context.getString(
            if (goods.isExpired()) R.string.expired else R.string.valid
        )
        root.setCardBackgroundColor(
            root.context.getColor(
                if (goods.isExpired()) R.color.red_light else R.color.white
            )
        )
        root.setOnClickListener { onCardClick(goods) }
    }
}

class GoodsDiffCallback : DiffUtil.ItemCallback<Goods>() {
    override fun areItemsTheSame(oldItem: Goods, newItem: Goods): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Goods, newItem: Goods): Boolean = oldItem == newItem
}
