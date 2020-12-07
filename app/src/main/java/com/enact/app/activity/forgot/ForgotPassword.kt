package com.enact.app.activity.forgot

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.enact.app.R
import com.enact.app.activity.base.AlertCallBack
import com.enact.app.activity.base.BaseActivity
import com.enact.app.activity.home.HomeActivity
import com.enact.app.activity.login.LoginActivity
import com.enact.app.databinding.ActivityForgotPasswordBinding
import com.enact.app.dependencyinjection.SealedClass
import com.enact.app.model.CommonObject
import com.enact.app.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class ForgotPassword : BaseActivity() {
    val viewModel: ForgotViewModel by viewModel()
    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
        binding.viewModel=viewModel
        binding.apply {
            lifecycleOwner= this@ForgotPassword
            executePendingBindings()
        }
        initView()
    }

    private fun initView() {

        observeLiveData()
    }

    /*observeLiveData=>Observe All observer here
 * */

    override fun onBackPressed() {
        super.onBackPressed()
        startPreviousActivity(LoginActivity::class.java)
    }

    private fun observeLiveData() {
        observeNonNull(viewModel.singleEventUiState) {
            when (it) {
                is SealedClass.Loading -> {
                    showProgressDialog()
                }
                is SealedClass.DismissLoading -> {
                    hideProgressDialog()
                }

                is SealedClass.HasData<*> -> {
                    hideProgressDialog()
                    val data = it.content as CommonObject<*>
                    showAlertDialogWithCallBack(data.message,object :AlertCallBack{
                        override fun onClickOk() {
                         startNextActivity(LoginActivity::class.java)
                        }
                    })

                }

                is SealedClass.ErrorClass -> {
                    hideProgressDialog()
                    showAlertDialog(it.errorMessage)
                    // Showing Error
                }
            }
        }
    }
}