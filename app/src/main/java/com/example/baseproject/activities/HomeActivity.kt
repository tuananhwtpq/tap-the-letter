package com.example.baseproject.activities

import android.content.Intent
import com.example.baseproject.bases.BaseActivity
import com.example.baseproject.databinding.ActivityHomeBinding
import com.example.baseproject.fragments.SetCategoryFragment
import com.example.baseproject.fragments.SetTimeFragment
import com.example.baseproject.utils.setOnUnDoubleClick

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun initData() {

    }

    override fun initView() {
    }

    override fun initActionView() {
        binding.btnPlay.setOnUnDoubleClick {
            navigateToMain()
        }

        binding.layoutTime.setOnUnDoubleClick {
            val setTimeDialog = SetTimeFragment.newInstance()
            setTimeDialog.show(supportFragmentManager, SetTimeFragment.TAG)
        }

        binding.layoutCategory.setOnUnDoubleClick {
            val setCategoryDialog = SetCategoryFragment.newInstance()
            setCategoryDialog.show(supportFragmentManager, SetCategoryFragment.TAG)
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, Testing_2_Activity::class.java))
    }

}