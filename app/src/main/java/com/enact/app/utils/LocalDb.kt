package com.enact.app.utils

import com.enact.app.model.register.RegisterResponse
import io.paperdb.Paper

object LocalDb {
     const val user="user"

    var userLogined:RegisterResponse?
       get() = Paper.book().read(user,null)
       set(value) { Paper.book().write(user,value)}


    fun clearData(){
        Paper.book().destroy()
    }

}
