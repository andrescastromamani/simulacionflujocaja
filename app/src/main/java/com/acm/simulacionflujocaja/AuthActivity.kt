package com.acm.simulacionflujocaja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.content.Intent
import com.acm.simulacionflujocaja.databinding.ActivityAuthBinding

import com.google.firebase.auth.FirebaseAuth



class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }
    private fun setup(){
        val binding1 = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding1.root)
        title="Login"

        binding1.btnReg.setOnClickListener {
            if (binding1.etEmail.text.isNotEmpty() && binding1.etPassword.text.isNotEmpty() ){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding1.etEmail.
                text.toString(),binding1.etPassword.text.toString()).addOnCompleteListener {//addOncompletListener nos notificara si la autenticacion a sido satisfactoria
                    if(it.isSuccessful){
                        mostrarMainActivity(it.result?.user?.email ?: "", ProviderType.BASIC)

                    }else{
                        showAlert()

                    }
                }
            }

        }
        binding1.btnLogIn.setOnClickListener {
            if (binding1.etEmail.text.isNotEmpty() && binding1.etPassword.text.isNotEmpty() ){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding1.etEmail.
                text.toString(),binding1.etPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        mostrarMainActivity(it.result?.user?.email ?: "", ProviderType.BASIC)


                    }else{
                        showAlert()

                    }
                }
            }
        }

    }
    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autenticacción del usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }

    private fun mostrarMainActivity(email:String, provider: ProviderType) {

        val MainIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)

        }
        startActivity(MainIntent)
       /* class AuthActivity : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_auth)
            }
        }*/
    }
}