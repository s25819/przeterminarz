package pl.edu.pjatk.s25819.przeterminarz.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
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

        when (formType) {
            is FormType.Edit -> {
                viewModel.init((formType as FormType.Edit).id)
            }
            is FormType.New -> {
                viewModel.init(null)
            }
        }
    }

    private fun setupUI() {
        setupCategorySpinner()
        setupCategoryChangeListener()
        setupDatePicker()
        setupImagePicker()
        setupSwitchQuantity()
        setupSaveButton()
    }

    private fun observeViewModel() {
        viewModel.navigation.observe(viewLifecycleOwner) {
            Log.d(TAG, "Nawiguję do $it")
            it.resolve(findNavController())
        }

        viewModel.goodsManageButtonText.observe(viewLifecycleOwner) {
            Log.d(TAG, "Manage goods button text changed to $it")
            binding.manageGoodsSaveButton.text = getString(it)
        }
    }

    private fun setupCategorySpinner() {
        binding.manageGoodsCategorySpinner.apply {
            adapter = GoodsCategoryAdapter(
                requireContext(),
                GoodsCategory.entries.filter { it != GoodsCategory.ALL }
            )
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
            viewModel.goodsImageThumbnail.value = it.toString()
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

    private fun setupCategoryChangeListener() {
        binding.manageGoodsCategorySpinner.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCategory = parent.getItemAtPosition(position) as GoodsCategory

                    if (viewModel.goodsImageThumbnail.value.isNullOrEmpty()) {
                        val bitmap =
                            GoodsCategory.getDefaultImage(requireContext(), selectedCategory)
                        binding.manageGoodsImageView.setImageBitmap(bitmap)
                    }
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
            }
    }

    companion object {
        private const val TAG = "ManageGoodsFragment"
    }
}
