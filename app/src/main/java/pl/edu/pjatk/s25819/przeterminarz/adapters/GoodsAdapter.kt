package pl.edu.pjatk.s25819.przeterminarz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.databinding.GoodsItemLayoutBinding
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory

class GoodsViewHolder(itemViewBinding: GoodsItemLayoutBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {

    val imageView = itemViewBinding.goodsImage
    val nameTextView = itemViewBinding.goodsName
    val expirationTextView = itemViewBinding.expirationDateValue
    val isExpiredFlag = itemViewBinding.isValidValue
    val categoryTextView = itemViewBinding.goodsCategoryValue
    val cardView = itemViewBinding.root

    fun bind(goods: Goods) {
        nameTextView.text = goods.name
        imageView.setImageResource(goods.image)
        expirationTextView.text = goods.expirationDate.toString()
        categoryTextView.text = getCategoryName(goods.category)
        isExpiredFlag.text =
            isExpiredFlag.context.getString(if (goods.isExpired()) R.string.expired else R.string.valid)
        cardView.setCardBackgroundColor(cardView.context.getColor(if (goods.isExpired()) R.color.red_light else R.color.white))
    }

    private fun getCategoryName(category: GoodsCategory): String {
        val context = categoryTextView.context
        return when (category) {
            GoodsCategory.GROCERY -> context.getString(R.string.grocery)
            GoodsCategory.MEDICINE -> context.getString(R.string.medicine)
            GoodsCategory.COSMETICS -> context.getString(R.string.cosmetics)
            GoodsCategory.ALL -> context.getString(R.string.all)
        }
    }
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
            LayoutInflater.from(parent.context), parent, false
        ).also {
            it.root.setOnClickListener {
                println("Kliknięcie " + it)
            }
        }
        return GoodsViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int = goodsList.size

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        holder.bind(goodsList[position])
    }
}