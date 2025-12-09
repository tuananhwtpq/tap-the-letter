package com.example.baseproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemDropdownOptionBinding

class SetTimeAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SetTimeAdapter.SetTimeViewHolder>() {

    private val listItem = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SetTimeViewHolder {
        val binding = ItemDropdownOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SetTimeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SetTimeViewHolder,
        position: Int
    ) {
        val isLastItem = position == listItem.size - 1
        holder.bind(listItem[position], isLastItem)
    }

    override fun getItemCount(): Int = listItem.size

    inner class SetTimeViewHolder(private val binding: ItemDropdownOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvOption = binding.tvOption
        val divider = binding.divider

        fun bind(text: String, isLastItem: Boolean) {
            tvOption.text = text

            divider.visibility = if (isLastItem) View.GONE else View.VISIBLE

            binding.root.setOnClickListener {
                onItemClick(text)
            }
        }
    }

    fun submitList(data: List<String>) {
        listItem.clear()
        listItem.addAll(data)
        notifyDataSetChanged()
    }


}