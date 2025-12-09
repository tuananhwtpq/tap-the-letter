package com.example.baseproject.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.adapters.SetTimeAdapter
import com.example.baseproject.bases.BaseDialogFragment
import com.example.baseproject.databinding.FragmentSetCategoryBinding
import com.example.baseproject.utils.SharedPrefManager
import com.example.baseproject.utils.setOnUnDoubleClick

class SetCategoryFragment : BaseDialogFragment<FragmentSetCategoryBinding>(
    FragmentSetCategoryBinding::inflate
) {

    private var isExpanded = false
    private var popupWindow: PopupWindow? = null
    private var currentTimeIndex: Int = 0

    val listData = listOf(
        "Sport",
        "Animals",
        "Flowers",
        "Clothing Items",
        "Type of Dance",
        "Vegetables",
        "Holidays"
    )

    companion object {
        const val TAG = "SetTimeFragment"

        fun newInstance(): SetCategoryFragment {
            return SetCategoryFragment()
        }
    }

    override fun initView() {

        currentTimeIndex = SharedPrefManager.getInt("currentCategory", 0)
        binding.tvSetTime.text = listData[currentTimeIndex]
    }

    override fun initActionView() {

        binding.btnDelete.setOnClickListener {
            dismiss()
        }

        binding.icDownButton.setOnUnDoubleClick {
            toggleDropdown()
        }

        binding.imgLayoutTop.setOnUnDoubleClick {
            toggleDropdown()
        }

        binding.btnOK.setOnClickListener {
            dismiss()
        }
    }

    fun toggleDropdown() {
        if (isExpanded) {
            popupWindow?.dismiss()
        } else {
            showDropdown()
        }
    }

    private fun showDropdown() {
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.layout_popup_dropdown, null)
        val rcvPopup = popupView.findViewById<RecyclerView>(R.id.rcvPopupData)

        val adapter = SetTimeAdapter { selectedItem ->
            binding.tvSetTime.text = selectedItem

            val index = listData.indexOf(selectedItem)
            SharedPrefManager.putInt("currentCategory", index)

            popupWindow?.dismiss()
        }

        rcvPopup.layoutManager = LinearLayoutManager(requireContext())
        rcvPopup.adapter = adapter
        adapter.submitList(listData)

        val anchorWidth = binding.imgLayoutTop.width
        val desiredWidth = binding.imgLayoutTop.width - 50

        popupWindow = PopupWindow(
            popupView,
            desiredWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            elevation = 10f

            setOnDismissListener {
                isExpanded = false
                binding.icDownButton.animate().rotation(0f).setDuration(200).start()
            }
        }

        val yOffset = dpToPx(20)
        val xOffset = (anchorWidth - desiredWidth) / 2

        popupWindow?.showAsDropDown(binding.imgLayoutTop, xOffset, yOffset)

        isExpanded = true
        binding.icDownButton.animate().rotation(180f).setDuration(200).start()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }


}