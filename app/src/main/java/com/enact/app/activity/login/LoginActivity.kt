package com.enact.app.activity.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil


import com.enact.app.R
import com.enact.app.activity.base.BaseActivity
import com.enact.app.activity.forgot.ForgotPassword
import com.enact.app.activity.home.HomeActivity
import com.enact.app.activity.register.RegisterActivity
import com.enact.app.databinding.ActivityLoginBinding
import com.enact.app.dependencyinjection.SealedClass
import com.enact.app.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel=viewModel
        binding.apply {
            lifecycleOwner= this@LoginActivity
            executePendingBindings()
        }
        intiView()
    }

    private fun intiView() {
        makeSpannableText()
        observeLiveData()
        viewModel.checkUserAlreadyLogin()
    }

    private fun makeSpannableText() {
        val firstPointStr = getString(R.string.dont_have_an_account_text)
        //val typefaceSpan = TypefaceSpan("font/brandon_bld.otf")
        val firstPointString = SpannableString(getString(R.string.dont_have_an_account_text))
        firstPointString.setSpan(object : ClickableSpan() {
            override fun onClick(v: View) {
                startNextActivity(RegisterActivity::class.java)
                viewModel.errorViewModel()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun updateDrawState(ds: TextPaint) {
                ds.color = getColor(R.color.redColor)
                ds.isUnderlineText = true
            }
        }, 24, firstPointStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.txtNotAccount.text = firstPointString
        binding.txtNotAccount.setMovementMethod(LinkMovementMethod.getInstance())
    }

    /*observeLiveData=>Observe All observer here
   * */
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

                }
                is SealedClass.StartNextScreen->{
                    hideProgressDialog()
                    when(it.targetScreen){
                        Constants.forgotScreen->{
                            viewModel.errorViewModel()
                            startNextActivity(ForgotPassword::class.java)
                        }
                        Constants.homeScreen->{
                            viewModel.errorViewModel()
                            startNextActivity(HomeActivity::class.java)
                        }
                    }
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