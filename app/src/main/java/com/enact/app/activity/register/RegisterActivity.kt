package com.enact.app.activity.register


import android.os.Build
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
import com.enact.app.activity.login.LoginActivity
import com.enact.app.databinding.ActivityRegisterBinding
import com.enact.app.dependencyinjection.SealedClass
import org.koin.androidx.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class RegisterActivity : BaseActivity() {
    lateinit var binding: ActivityRegisterBinding
    val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register)
        binding.viewModel=viewModel
        binding.apply {
            lifecycleOwner= this@RegisterActivity
            executePendingBindings()
        }
        initView()
    }

    private fun initView() {
        makeSpannableText()
        observeLiveData()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startPreviousActivity(LoginActivity::class.java)
    }
    private fun makeSpannableText() {
        val firstPointStr = getString(R.string.already_have_account)
        //val typefaceSpan = TypefaceSpan("font/brandon_bld.otf")
        val firstPointString = SpannableString(getString(R.string.already_have_account))
        firstPointString.setSpan(object : ClickableSpan() {
            override fun onClick(v: View) {
                startPreviousActivity(LoginActivity::class.java)
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun updateDrawState(ds: TextPaint) {
                ds.color = getColor(R.color.redColor)
                ds.isUnderlineText = true
            }
        }, 25, firstPointStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.txtAlreadyAccount.text = firstPointString
        binding.txtAlreadyAccount.setMovementMethod(LinkMovementMethod.getInstance())
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

                is SealedClass.StartNextScreen -> {
                    hideProgressDialog()
                    startPreviousActivity(LoginActivity::class.java)

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