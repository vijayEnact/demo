package com.enact.app.activity.base

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.enact.app.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker


open class BaseActivity :AppCompatActivity() {
    lateinit var context: Context
     var dialog:Dialog?=null
    lateinit var builder:AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        supportActionBar?.hide()

    }




    fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
    fun showAlertDialog(message: String, title:String?="Alert!") {
        val dialogBuilder = AlertDialog.Builder(context)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(title)
        // show alert dialog
        if (!(context as Activity).isFinishing()) {
            alert.show()
        }

    }

    fun showAlertDialogWithCallBack(message: String, alertCallBack: AlertCallBack) {
        val dialogBuilder = AlertDialog.Builder(context)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                    alertCallBack.onClickOk()
                })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Alert!")
        // show alert dialog
        if (!(context as Activity).isFinishing()) {
            alert.show()
        }

    }


    fun showProgressDialog() {

        if (dialog?.isShowing?:false){
            dialog?.dismiss()
        }
        dialog = Dialog(context)
        val inflate = LayoutInflater.from(context).inflate(R.layout.custom_progress, null)
        dialog?.setContentView(inflate)
        dialog?.setCancelable(false)
        dialog?.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
       // CallProgressWheel.showLoadingDialog(context)
    }



    fun hideProgressDialog() {
        try {
            if (dialog != null && dialog?.isShowing?:false) {
                dialog?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /*if (CallProgressWheel.isDialogShowing) {
            CallProgressWheel.dismissLoadingDialog()
        }*/
    }


    fun hideKeyboard(applicationContext: Context, mView: View?) {
        if (mView != null) {
            val imm =
                applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mView.windowToken, 0)
        }
    }

    override fun onDestroy() {
        hideProgressDialog()
        super.onDestroy()
    }

    open fun startSpecificActivity(otherActivityClass: Class<*>?) {
        val intent = Intent(applicationContext, otherActivityClass)
        Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    open fun startNextActivity(otherActivityClass: Class<*>?) {
        val intent = Intent(applicationContext, otherActivityClass)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }
    open fun startNextActivityAnimation(otherActivityClass: Class<*>?) {
        val intent = Intent(applicationContext, otherActivityClass)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }

    open fun startPreviousActivity(otherActivityClass: Class<*>?) {
        val intent = Intent(applicationContext, otherActivityClass)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


    open fun finishAll(otherActivityClass: Class<*>?) {
        val intent = Intent(applicationContext, otherActivityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    open fun addOrReplaceFragmentName(fragmentName: Fragment, container: Int) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,R.anim.slide_in_left, R.anim.slide_out_right)
            .replace(container, fragmentName)
            .addToBackStack(null)
            .commit()
    }
    open fun replaceFrag(fragmentName: Fragment, container: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(container, fragmentName)
            .commit()


    }

    open fun addOFirstFragment(fragmentName: Fragment, container: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(container, fragmentName)
            .addToBackStack(null)
            .commit()


    }

    open fun showImageCapturing(imageCallBack: ImageCallBack) {
        TedBottomPicker.with(this)
            .show {
                imageCallBack.returnImageUri(it)
            }
    }

    open fun permissionForLocation(commonInterface: CommonInterface) {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                commonInterface.approvePermission()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                commonInterface.deniedPermission("Please give the permission for accessing the images")
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check()
    }

        open fun permissionForImage(imageCallBack: ImageCallBack) {
            val permissionlistener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    showImageCapturing(imageCallBack)
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    imageCallBack.deniedPermission("Please give the permission for accessing the images")
                }
            }
            TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .check()
        }


/*open fun logOutUser(){
    val intent = Intent(applicationContext, LoginActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
}*/

    }


    interface ImageCallBack {
        fun returnImageUri(it: Uri)
        fun deniedPermission(message: String)
    }

    interface CommonInterface {

        fun deniedPermission(message: String)
        fun approvePermission()
    }

    interface AlertCallBack{
        fun onClickOk()
    }
