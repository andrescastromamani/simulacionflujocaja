package com.acm.simulacionflujocaja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.acm.simulacionflujocaja.databinding.ActivityMainBinding
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_inputs.*
import kotlinx.android.synthetic.main.fragment_inputs.view.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*

enum class ProviderType{   // enum class declara palabra clave para definir un conjunto de constantes
    BASIC}
class MainActivity : AppCompatActivity() {



    private lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        val email: String? = bundle?.getString("email")
        println(email)
        //textView_email.text=email

        /*val navUserEmail : TextView = nav_view.findViewById(R.id.textView_email)
        navUserEmail.text = email.toString()*/



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

