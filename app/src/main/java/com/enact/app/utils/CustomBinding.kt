package com.enact.app.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.enact.app.R

import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Enact.
 */

@BindingAdapter("error")
fun EditText.setError(errorMessage: String?) {
    if (errorMessage != "") {
        error = errorMessage
    } else {
        error = null
    }
}

@BindingAdapter("setVisibility")
fun View.setVisibility(visibility: Boolean?){
    if(visibility==true)this.visibility=View.VISIBLE
    else View.GONE
}

@BindingAdapter("bind:imageUrl",)
    fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view)
            .load(imageUrl)
            .placeholder(R.drawable.product_placeholder)
            .circleCrop()
            .into(view)


}
