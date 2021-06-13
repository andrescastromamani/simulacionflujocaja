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

class MainActivity : AppCompatActivity(), Communicator {


    private val listState = mutableListOf<StateFragment>() //
    private var currentTag: String = TAG_ONE
    private var oldTag: String = TAG_ONE
    private var currentMenuItemId: Int = R.id.nav_inputs

    private lateinit var toogle: ActionBarDrawerToggle
    private val bundle = Bundle()
    private var email = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val headerView: View = nav_view.getHeaderView(0)
        val navUserEmail: TextView = headerView.findViewById(R.id.textView_email)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            navUserEmail.text = user.email.toString()
        }

        /*val fragmentInputs = InputsFragment()*/


        /*supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragmentInputs).commit()*/

        toogle =ActionBarDrawerToggle(this, drawer_layout, R.string.open_drawer, R.string.close_drawer)
        drawer_layout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) loadFirstFragment()
        //else if(savedInstanceState !=null) loadSecondFragment()
        iniciar()


    }

    private fun iniciar() {

        nav_view.setNavigationItemSelectedListener {

           /* if (currentMenuItemId != menuItem.itemId) {
                currentMenuItemId = menuItem.itemId*/

                when (it.itemId) {
                    R.id.nav_inputs -> changeFragment(TAG_ONE, InputsFragment.newInstance())
                    R.id.nav_sueldos -> changeFragment(TAG_SECOND, SueldosFragment.newInstance())
                    R.id.nav_iva -> changeFragment(TAG_THIRD, IvaFragment.newInstance())
                    R.id.nav_it -> changeFragment(TAG_FOURTH, ItFragment.newInstance())
                    R.id.nav_iue -> changeFragment(TAG_FIVE, IueFragment.newInstance())
                    R.id.nav_flujo -> changeFragment(TAG_SIX, FlujoFragment.newInstance())
                    R.id.nav_presupuesto -> changeFragment(TAG_SEVEN, PresupuestoFragment.newInstance())
                }

                return@setNavigationItemSelectedListener true
            }

            false
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (toogle.onOptionsItemSelected(item)) {
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        override fun passDataMeses(
            editTextMes3: String,
            editTextMes4: String,
            editTextMes5: String
        ) {
            val bundle = Bundle()

            bundle.putString("mesp1", editTextMes3)
            bundle.putString("mesp2", editTextMes4)
            bundle.putString("mesp3", editTextMes5)

            val transaction = this.supportFragmentManager.beginTransaction()
            val fragmentoIt = ItFragment()
            fragmentoIt.arguments = bundle

            transaction.replace(R.id.fragmentContainerView, fragmentoIt)
            transaction.commit()
        }

        private fun changeFragment(tagToChange: String, fragment: Fragment) {
            if (currentTag != tagToChange) {
                val ft = supportFragmentManager.beginTransaction()
                val currentFragment = supportFragmentManager.findFragmentByTag(currentTag)
                val fragmentToReplaceByTag = supportFragmentManager.findFragmentByTag(tagToChange)

                oldTag = currentTag
                currentTag = tagToChange

                if (fragmentToReplaceByTag != null) {
                    currentFragment?.let { ft.hide(it).show(fragmentToReplaceByTag) }
                } else {
                    currentFragment?.let {
                        ft.hide(it).add(R.id.fragmentContainerView, fragment, tagToChange)
                    }
                }

                ft.commit()

                addBackStack()
            }
        }

        override fun onBackPressed() {
            if (listState.size >= 1) {
                recoverFragment()
            } else {
                super.onBackPressed()
            }
        }

        private fun recoverFragment() {

            val lastState = listState.last()

            val ft = supportFragmentManager.beginTransaction()

            val currentFragmentByTag =
                supportFragmentManager.findFragmentByTag(lastState.currentFragmentTag)
            val oldFragmentByTag =
                supportFragmentManager.findFragmentByTag(lastState.oldFragmentTag)

            if ((currentFragmentByTag != null && currentFragmentByTag.isVisible) &&
                (oldFragmentByTag != null && oldFragmentByTag.isHidden)
            ) {
                ft.hide(currentFragmentByTag).show(oldFragmentByTag)
            }

            ft.commit()

            val menu = nav_view.menu

            when (lastState.oldFragmentTag) {
                TAG_ONE -> setMenuItem(menu.getItem(0))
                TAG_SECOND -> setMenuItem(menu.getItem(1))
                TAG_THIRD -> setMenuItem(menu.getItem(2))
                TAG_FOURTH -> setMenuItem(menu.getItem(3))
                TAG_FIVE -> setMenuItem(menu.getItem(4))
                TAG_SIX -> setMenuItem(menu.getItem(5))
                TAG_SEVEN -> setMenuItem(menu.getItem(6))
            }

            //Remove from Stack
            listState.removeLast()

            if (listState.isEmpty()) {
                currentTag = TAG_ONE
                oldTag = TAG_ONE
            } else {
                currentTag = listState.last().currentFragmentTag
                oldTag = listState.last().oldFragmentTag
            }

        }

        /*private fun updateLog() {
        tvCurrentTag.text = currentTag
        tvOldTag.text = oldTag
    }*/

        private fun setMenuItem(menuItem: MenuItem) {
            menuItem.isChecked = true
            currentMenuItemId = menuItem.itemId
        }

        private fun loadFirstFragment() {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(
                R.id.fragmentContainerView,
                InputsFragment.newInstance(),
                TAG_ONE
            )
            transaction.commit()
        }
    private fun loadSecondFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(
            R.id.fragmentContainerView,
            ItFragment.newInstance(),
            TAG_THIRD
        )
        transaction.commit()
    }

        //Like YouTube
        private fun addBackStack() {


            when (listState.size) {
                MAX_HISTORIC -> {

                    listState[1].oldFragmentTag = TAG_ONE
                    val firstState = listState[1]

                    for (i in listState.indices) {
                        if (listState.indices.contains((i + 1))) {
                            listState[i] = listState[i + 1]
                        }
                    }

                    listState[0] = firstState
                    listState[listState.lastIndex] = StateFragment(currentTag, oldTag)
                }
                else -> {
                    listState.add(StateFragment(currentTag, oldTag))
                }
            }
        }
    }






