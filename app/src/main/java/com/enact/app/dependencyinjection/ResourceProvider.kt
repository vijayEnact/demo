package com.enact.app.dependencyinjection

import android.content.Context

class ResourceProvider (private val context: Context){

    fun getStringResource(stringId: Int): String{
        return context.getString(stringId)
    }

}