package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.acm.simulacionflujocaja.databinding.ActivityMainBinding
import com.acm.simulacionflujocaja.databinding.NavHeaderBinding
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC}


class MainActivity : AppCompatActivity() {

    private lateinit var toogle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
val  binding= ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)
        
         val headerView: View = nav_view.getHeaderView(0)
        val navUserEmail: TextView = headerView.findViewById(R.id.textView_email)
        nav_view.getMenu().findItem(R.id.btnLogout).setOnMenuItemClickListener { menuItem ->
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
            true
        }

        val inputsFragmet=InputsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, inputsFragmet).commit()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            navUserEmail.text = user.email.toString()}


            toogle = ActionBarDrawerToggle(this, drawer_layout,R.string.open_drawer,R.string.close_drawer)
            drawer_layout.addDrawerListener(toogle)
            toogle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            nav_view.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.nav_inputs->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,InputsFragment())
                            commit()
                        }
                    }
                    R.id.nav_sueldos->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,SueldosFragment())
                            commit()
                        }
                    }
                    R.id.nav_iva->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,IvaFragment())
                            commit()
                        }
                    }
                    R.id.nav_it->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,ItFragment())
                            commit()
                        }
                    }
                    R.id.nav_iue->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,IueFragment())
                            commit()
                        }
                    }
                    R.id.nav_flujo->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,FlujoFragment())
                            commit()
                        }
                    }
                    R.id.nav_presupuesto->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,PresupuestoFragment())
                            commit()
                        }
                    }
                    R.id.nav_trimestral->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,TrimestralFragment())
                            commit()
                        }
                    }
                    R.id.nav_semestral->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,SemestralFragment())
                            commit()
                        }
                    }
                    R.id.nav_anual->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,AnualFragment())
                            commit()
                        }
                    }
                    R.id.nav_quinquenal->{
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,QuinquenalFragment())
                            commit()
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
