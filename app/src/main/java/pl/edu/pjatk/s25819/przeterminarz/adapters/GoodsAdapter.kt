package pl.edu.pjatk.s25819.przeterminarz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.databinding.GoodsItemLayoutBinding
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import java.time.LocalDate

class GoodsViewHolder(itemViewBinding: GoodsItemLayoutBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {

    val imageView = itemViewBinding.goodsImage
    val nameTextView = itemViewBinding.goodsName
}

class GoodsAdapter : RecyclerView.Adapter<GoodsViewHolder>() {

    private val goodsList: List<Goods> = listOf(
        Goods(
            1,
            "Produkt 1",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            2,
            "Produkt 2",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            3,
            "Produkt 3",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            1,
            "Produkt 4",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            1,
            "Produkt 5",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            1,
            "Produkt 6",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            1,
            "Produkt 7",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            1,
            "Produkt 8",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            1,
            "Produkt 9",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        ),
        Goods(
            1,
            "Produkt 10",
            GoodsCategory.GROCERY,
            1,
            LocalDate.now(),
            R.drawable.ic_launcher_foreground
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {

        val itemViewBinding = GoodsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
            .also {

                it.root.setOnClickListener {
                    println("Kliknięcie")
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