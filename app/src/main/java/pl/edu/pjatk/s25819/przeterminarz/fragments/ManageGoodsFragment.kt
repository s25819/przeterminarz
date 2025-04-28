package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import pl.edu.pjatk.s25819.przeterminarz.R
import pl.edu.pjatk.s25819.przeterminarz.adapters.GoodsCategoryAdapter
import pl.edu.pjatk.s25819.przeterminarz.databinding.FragmentManageGoodsBinding
import pl.edu.pjatk.s25819.przeterminarz.model.FormType
import pl.edu.pjatk.s25819.przeterminarz.model.GoodsCategory
import pl.edu.pjatk.s25819.przeterminarz.viewmodel.ManageGoodsViewModel
import java.time.LocalDate

private const val TYPE_KEY = "type"

@SuppressLint("LogNotTimber")
class ManageGoodsFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentManageGoodsBinding
    private val viewModel: ManageGoodsViewModel by viewModels()

    private lateinit var formType: FormType

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageGoodsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFormType()
        setupUI()
        observeViewModel()
    }

    private fun initFormType() {
        formType = arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(TYPE_KEY, FormType::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getSerializable(TYPE_KEY) as? FormType
            }
        } ?: FormType.New

        if (formType is FormType.Edit) {
            viewModel.init((formType as FormType.Edit).id)
        }
    }

    private fun setupUI() {
        setupCategorySpinner()
        setupDatePicker()
        setupImagePicker()
        setupSwitchQuantity()
        setupSaveButton()
    }

    private fun observeViewModel() {
        viewModel.navigation.observe(viewLifecycleOwner) {
            it.resolve(findNavController())
        }
    }

    private fun setupCategorySpinner() {
        binding.manageGoodsCategorySpinner.apply {
            adapter = GoodsCategoryAdapter(
                requireContext(),
                GoodsCategory.entries.filter { it != GoodsCategory.ALL }
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
    }

    private fun setupDatePicker() {
        binding.manageGoodsExpirationDateValue.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)

        if (pickedDate.isBefore(LocalDate.now())) {
            Toast.makeText(requireContext(), R.string.invalid_date_toast, Toast.LENGTH_SHORT).show()
            return
        }
        binding.manageGoodsExpirationDateValue.setText(pickedDate.toString())
        viewModel.goodsExpirationDate.value = pickedDate.toString()
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.manageGoodsImageView.setImageURI(it)
            // TODO: Zapisać URI lub Bitmapę
        }
    }

    private fun setupImagePicker() {
        binding.manageGoodsPickImageButton.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun setupSwitchQuantity() {
        binding.switchQuantity.setOnCheckedChangeListener { _, isChecked ->
            binding.manageGoodsQuantityValue.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun setupSaveButton() {
        binding.manageGoodsSaveButton.apply {
            text = getString(
                if (formType is FormType.Edit)
                    R.string.manage_goods_button_edit_goods_label
                else
                    R.string.manage_goods_button_add_goods_label
            )

            setOnClickListener {
                if (!isFieldsValid()) {
                    Toast.makeText(
                        requireContext(),
                        R.string.invalid_input_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.saveGoods(
                    binding.manageGoodsCategorySpinner.selectedItem as GoodsCategory
                )

                Toast.makeText(requireContext(), R.string.success_toast, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isFieldsValid(): Boolean {
        val name = binding.manageGoodsGoodsNameValue.text.toString().trim()
        val quantityText = binding.manageGoodsQuantityValue.text.toString().trim()

        return name.isNotEmpty() &&
                (!binding.switchQuantity.isChecked || quantityText.toIntOrNull() != null) &&
                binding.manageGoodsExpirationDateValue.text.toString().isNotBlank()
    }

    companion object {
        private const val TAG = "ManageGoodsFragment"
    }
}
