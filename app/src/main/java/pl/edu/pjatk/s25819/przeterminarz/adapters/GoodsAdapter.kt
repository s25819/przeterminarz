package pl.edu.pjatk.s25819.przeterminarz.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.databinding.GoodsItemLayoutBinding
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory

class GoodsAdapter(
    private val onCardClick: (Goods) -> Unit,
    private val onCardLongClick: (Goods) -> Boolean
) : ListAdapter<Goods, GoodsViewHolder>(GoodsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GoodsItemLayoutBinding.inflate(layoutInflater, parent, false)
        return GoodsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        holder.bind(getItem(position), onCardClick, onCardLongClick)
    }
}

class GoodsViewHolder(private val binding: GoodsItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        goods: Goods,
        onCardClick: (Goods) -> Unit,
        onCardLongClick: (Goods) -> Boolean
    ): Unit = with(binding) {
        goodsName.text = goods.name
        expirationDateValue.text = goods.expirationDate.toString()
        goodsCategoryValue.text = goods.category.getDisplayName(root.context)

        goods.thumbnail?.let { thumbnail ->
            val bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.size)
            goodsImage.setImageBitmap(bitmap)
        } ?: run {
            goodsImage.setImageBitmap(GoodsCategory.getDefaultImage(root.context, goods.category))
        }

        setValidLabel(goods)

        quantityGroup.visibility = if (goods.hasQuantity()) View.VISIBLE else View.GONE
        quantityValue.text = goods.quantity?.toString() ?: ""

        root.setCardBackgroundColor(
            root.context.getColor(
                if (goods.isExpired()) R.color.red_light else R.color.white
            )
        )

        root.setOnClickListener { onCardClick(goods) }
        root.setOnLongClickListener { onCardLongClick(goods) }
    }

    private fun GoodsItemLayoutBinding.setValidLabel(goods: Goods) {
        if (goods.isThrownAway()) {
            isValidLabel.text =
                root.context.getString(R.string.goods_is_thrown_away, goods.name)
            isValidValue.visibility = View.GONE
        } else {
            isValidValue.text = root.context.getString(
                if (goods.isExpired()) R.string.expired else R.string.valid
            )
        }
    }
}

class GoodsDiffCallback : DiffUtil.ItemCallback<Goods>() {
    override fun areItemsTheSame(oldItem: Goods, newItem: Goods): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Goods, newItem: Goods): Boolean =
        oldItem == newItem
}
