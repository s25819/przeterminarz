package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import com.google.android.material.materialswitch.MaterialSwitch
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.adapters.GoodsCategoryAdapter
import pl.edu.pjatk.s25819.przeterminarz.databinding.FragmentManageGoodsBinding
import pl.edu.pjatk.s25819.przeterminarz.model.FormType
import pl.edu.pjatk.s25819.przeterminarz.model.Goods
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.repositories.GoodsRepository
import pl.edu.pjatk.s25819.przeterminarz.repositories.RepositoryLocator
import java.time.LocalDate

private const val TYPE_KEY = "type"

@SuppressLint("LogNotTimber")
class ManageGoodsFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var type: FormType
    private lateinit var binding: FragmentManageGoodsBinding
    private lateinit var goodsRepository: GoodsRepository

    private lateinit var nameEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var quantityEditText: EditText
    private lateinit var expirationDateEditText: EditText
    private lateinit var imageViewGoods: ImageView
    private lateinit var buttonPickImage: Button
    private lateinit var buttonSaveGoods: Button
    private lateinit var switchQuantity: MaterialSwitch
    private lateinit var categoryAdapter: GoodsCategoryAdapter

    private var selectedDate: LocalDate? = null
    private var selectedImage: Int = R.mipmap.mleko

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goodsRepository = RepositoryLocator.goodsRepository

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentManageGoodsBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupCategorySpinner()
        setupDatePicker()
        setupImagePicker()
        setupSwitchQuantity()
        setupSaveButton()

        if (type is FormType.Edit) {
            fillFormForEdit((type as FormType.Edit).id)
        }
    }

    private fun setupViews() {
        with(binding) {
            nameEditText = editTextName
            this@ManageGoodsFragment.categorySpinner = categorySpinner
            this@ManageGoodsFragment.switchQuantity = switchQuantity
            quantityEditText = editTextQuantity
            expirationDateEditText = editTextExpirationDate
            this@ManageGoodsFragment.imageViewGoods = imageViewGoods
            this@ManageGoodsFragment.buttonPickImage = buttonPickImage
            this@ManageGoodsFragment.buttonSaveGoods = buttonSaveGoods
        }
    }

    private fun setupCategorySpinner() {
        categoryAdapter = GoodsCategoryAdapter(
            requireContext(),
            GoodsCategory.entries.filter { it != GoodsCategory.ALL }
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = it
        }
    }

    private fun fillFormForEdit(id: Int) {
        val goods = goodsRepository.getGoodsById(id)
        Log.d(TAG, "Wczytuję produkt o ID: $id")

        nameEditText.setText(goods.name)
        quantityEditText.setText(goods.quantity?.toString() ?: "")
        expirationDateEditText.setText(goods.expirationDate.toString())
        imageViewGoods.setImageResource(goods.image)
        switchQuantity.isChecked = goods.hasQuantity()

        val position = categoryAdapter.getPosition(goods.category).takeIf { it >= 0 } ?: 0
        categorySpinner.setSelection(position)
    }

    private fun setupSaveButton() {
        buttonSaveGoods.apply {
            text = if (type is FormType.Edit) "Edytuj" else "Zapisz"
            setOnClickListener {
                if (!isFieldsValid()) {
                    Toast.makeText(requireContext(), "Niepoprawne dane", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                saveGoods()
                Toast.makeText(requireContext(), "Sukces", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun saveGoods() {
        val goodsId = (type as? FormType.Edit)?.id ?: -1
        val selectedCategory = categorySpinner.selectedItem as GoodsCategory

        val goods = Goods(
            id = goodsId,
            name = nameEditText.text.toString(),
            category = selectedCategory,
            quantity = if (switchQuantity.isChecked) quantityEditText.text.toString()
                .toInt() else null,
            expirationDate = selectedDate ?: LocalDate.now(),
            image = selectedImage
        )

        goodsRepository.saveGoods(goods)
    }

    private fun isFieldsValid(): Boolean {
        val name = nameEditText.text.toString().trim()
        val quantityText = quantityEditText.text.toString().trim()

        return name.isNotEmpty() &&
                (!switchQuantity.isChecked || quantityText.toIntOrNull() != null) &&
                expirationDateEditText.text.toString().isNotBlank()
    }

    private fun setupDatePicker() {
        expirationDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        expirationDateEditText.setText(selectedDate.toString())
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageViewGoods.setImageURI(it)
            // TODO: obsłuż zapis bitmapy lub ścieżki do bazy
        }
    }

    private fun setupImagePicker() {
        buttonPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun setupSwitchQuantity() {
        switchQuantity.setOnCheckedChangeListener { _, isChecked ->
            quantityEditText.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    companion object {
        private const val TAG = "ManageGoodsFragment"
    }
}
