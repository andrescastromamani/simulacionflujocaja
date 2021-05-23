package com.acm.simulacionflujocaja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var toogle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toogle = ActionBarDrawerToggle(this, drawer_layout,R.string.open_drawer,R.string.close_drawer)
        drawer_layout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_inputs->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView,InputsFragment())
                    }
                }
                R.id.nav_sueldos->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView,SueldosFragment())
                    }
                }
                R.id.nav_iva->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView,IvaFragment())
                    }
                }
                R.id.nav_it->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView,ItFragment())
                    }
                }
                R.id.nav_iue->{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView,IueFragment())
                    }
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}