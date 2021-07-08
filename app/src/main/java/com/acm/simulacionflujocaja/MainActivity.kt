package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{   // enum class declara palabra clave para definir un conjunto de constantes
    BASIC}
private const val TAG_ONE = "first"
private const val TAG_SECOND = "second"
private const val TAG_THIRD = "third"
private const val TAG_FOURTH = "fourth"
private const val TAG_FIVE = "cinco"
private const val TAG_SIX = "seis"
private const val TAG_SEVEN = "siete"
private const val TAG_OCHO = "ocho"
private const val MAX_HISTORIC = 20

class MainActivity : AppCompatActivity() ,Communicator{

    private lateinit var toogle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


         val headerView: View = nav_view.getHeaderView(0)
        val navUserEmail: TextView = headerView.findViewById(R.id.textView_email)
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

    override fun passDataMeses(
        contado1: Double,
        contado2: Double,
        contado3: Double,
        rec30d1: Double,
        rec30d2: Double,
        rec30d3: Double,
        rec60d1: Double,
        rec60d2: Double,
        rec60d3: Double
    ) {
        val bundle =Bundle()
        bundle.putString("c1", contado1.toString())
        bundle.putString("c2", contado2.toString())
        bundle.putString("c3", contado3.toString())
        bundle.putString("r30d1", rec30d1.toString())
        bundle.putString("r30d2", rec30d2.toString())
        bundle.putString("r30d3", rec30d3.toString())
        bundle.putString("r60d1", rec60d1.toString())
        bundle.putString("r60d2", rec60d2.toString())
        bundle.putString("r60d3", rec60d3.toString())

        val transaction=this.supportFragmentManager.beginTransaction()
        val presFrag=PresupuestoFragment()
        presFrag.arguments = bundle
        transaction.replace(R.id.fragmentContainerView, presFrag)
        transaction.commit()



    }

}
