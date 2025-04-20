package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.edu.pjatk.s25819.przeterminarz.databinding.FragmentManageGoodsBinding
import pl.edu.pjatk.s25819.przeterminarz.model.FormType

private const val TYPE_KEY = "type"

class ManageGoodsFragment : Fragment() {
    private lateinit var type: FormType
    private lateinit var binding: FragmentManageGoodsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(TYPE_KEY, FormType::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getSerializable(TYPE_KEY) as? FormType
            } ?: FormType.New
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentManageGoodsBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

}