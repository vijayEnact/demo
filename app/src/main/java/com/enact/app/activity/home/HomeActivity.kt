package com.enact.app.activity.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.enact.app.R
import com.enact.app.activity.base.BaseActivity
import com.enact.app.activity.login.LoginActivity
import com.enact.app.databinding.ActivityHomeBinding
import com.enact.app.fragment.home.HomeFragment
import com.enact.app.utils.LocalDb
import kotlinx.android.synthetic.main.header.view.*

class HomeActivity : BaseActivity() {
    lateinit var binding:ActivityHomeBinding
    lateinit var drawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initView()
        logOutUser()
    }

    private fun initView() {
        setToolBar()
        addOrReplaceFragmentName((HomeFragment()), R.id.container)
    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
      //  actionBar?.title = "CHHAPPAN BHOG"

        drawerToggle = object : ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                Log.e("closed","ssssss")
                binding.rootLayout.isClickable= true
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                Log.e("open","sssss")
                binding.rootLayout.isClickable= false
                //toast("Drawer opened")
            }
        }



        drawerToggle.setHomeAsUpIndicator(R.drawable.manu_icon)
        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = false
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.setToolbarNavigationClickListener {

            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
          /*  if (AppConstant.backButtonClick){
                onBackPressed()
            }
            else {
                if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }*/
        }
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }


    fun logOutUser()
    {
        binding.drawerHeader.txtLogout.setOnClickListener {
            LocalDb.clearData()
            startPreviousActivity(LoginActivity::class.java)
        }
    }
}