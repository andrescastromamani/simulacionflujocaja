package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.reflect.Array.newInstance

enum class ProviderType{   // enum class declara palabra clave para definir un conjunto de constantes
    BASIC}
private const val TAG_ONE = "first"
private const val TAG_SECOND = "second"
private const val TAG_THIRD = "third"
private const val TAG_FOURTH = "fourth"
private const val TAG_FIVE = "cinco"
private const val TAG_SIX = "seis"
private const val TAG_SEVEN = "siete"
private const val MAX_HISTORIC = 20

class MainActivity : AppCompatActivity() {

    private lateinit var toogle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val headerView: View = nav_view.getHeaderView(0)
        val navUserEmail: TextView = headerView.findViewById(R.id.textView_email)
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
