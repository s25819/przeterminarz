package pl.edu.pjatk.s25819.przeterminarz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import pl.edu.pjatk.s25819.przeterminarz.adapters.GoodsAdapter
import pl.edu.pjatk.s25819.przeterminarz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        val adapter = GoodsAdapter()
        val recyclerView = binding.goodsRecyclerView
        recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
        }

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Snackbar.make(binding.main, "Hello!", Snackbar.LENGTH_SHORT).show()

    }
}