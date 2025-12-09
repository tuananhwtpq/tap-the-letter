package com.example.baseproject.fragments

import android.content.Intent
import android.os.Bundle
import com.example.baseproject.activities.HomeActivity
import com.example.baseproject.activities.Testing_2_Activity
import com.example.baseproject.bases.BaseDialogFragment
import com.example.baseproject.databinding.FragmentResultBinding

class ResultFragment : BaseDialogFragment<FragmentResultBinding>(FragmentResultBinding::inflate) {

    private var isWin: Boolean? = null

    companion object {
        const val TAG = "ResultFragment"

        fun newInstance(isWin: Boolean): ResultFragment {

            val args = Bundle().apply {
                putBoolean("isWin", isWin)
            }

            return ResultFragment().apply {
                arguments = args
            }
        }
    }

    override fun initView() {

        isWin = arguments?.getBoolean("isWin")

        isWin?.let {
            binding.tvResult.text = if (it) "You Won!" else "You Lost!"
        }

    }

    override fun initActionView() {

        binding.layoutHome.setOnClickListener {
            navigateToHome()
        }

        binding.layoutReplay.setOnClickListener {

            if (activity is Testing_2_Activity) {
                (activity as Testing_2_Activity).resetGame()
                dismiss()
            }
        }


    }

    private fun navigateToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}