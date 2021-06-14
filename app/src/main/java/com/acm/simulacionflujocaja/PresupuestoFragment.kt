package com.acm.simulacionflujocaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm.simulacionflujocaja.databinding.FragmentIvaBinding

import com.acm.simulacionflujocaja.databinding.FragmentPresupuestoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Double.parseDouble

class PresupuestoFragment : Fragment(R.layout.fragment_presupuesto) {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email

    var displayMessage: String?= ""
    private var _binding: FragmentPresupuestoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("IT")
        _binding = FragmentPresupuestoBinding.inflate(inflater, container, false)
        val view = binding.root
        recDatosMeses()
        recDatosVentas()
        recDatos30dias()
        recDatos60dias()






        return view
    }
    private fun recDatosMeses(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").get().addOnSuccessListener {
            binding.mes1.setText(it.get("Mes 3") as String?)
            binding.mes2.setText(it.get("Mes 4") as String?)
            binding.mes3.setText(it.get("Mes 5") as String?)

        } }

    private fun recDatosVentas(){
        //recupera ventas y hace calculo de total de ventas
        db.collection("Users").document(email.toString()).collection("Entradas").document("IngresoBruto").get().addOnSuccessListener {
            binding.ventas1.setText(it.get("IngresoBrutoMes3") as String?)
            binding.ventas2.setText(it.get("IngresoBrutoMes4") as String?)
            binding.ventas3.setText(it.get("IngresoBrutoMes5") as String?)
            val ventas1:Double?= parseDouble((it.get("IngresoBrutoMes3")).toString())
            val ventas2:Double?= parseDouble((it.get("IngresoBrutoMes4")).toString())
            val ventas3:Double?= parseDouble((it.get("IngresoBrutoMes5")).toString())
            val totalVentas:Double= ventas1!! + ventas2!! + ventas3!!
            binding.totalv.text = totalVentas.toString()

        }

    }
    private fun recDatos30dias(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Recuperacion 30 dias").get().addOnSuccessListener {

            binding.editText4.setText(it.get("Recuperacion 30 dias mes 3") as String?)
            binding.editText5.setText(it.get("Recuperacion 30 dias mes 4") as String?)
            binding.editText6.setText(it.get("Recuperacion 30 dias mes 5") as String?)
            val ventas30d1:Double?= parseDouble((it.get("Recuperacion 30 dias mes 3")).toString())
            val ventas30d2:Double?= parseDouble((it.get("Recuperacion 30 dias mes 4")).toString())
            val ventas30d3:Double?= parseDouble((it.get("Recuperacion 30 dias mes 5")).toString())
            val totalRec30d:Double= ventas30d1!! + ventas30d2!! + ventas30d3!!
            binding.totalv2.text = totalRec30d.toString()

        }


    }
    private fun recDatos60dias(){
    db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Recuperacion 60 dias").get().addOnSuccessListener {


        binding.editText7.setText(it.get("Recuperacion 60 dias mes 3") as String?)
        binding.editText8.setText(it.get("Recuperacion 60 dias mes 4") as String?)
        binding.editText9.setText(it.get("Recuperacion 60 dias mes 5") as String?)
        val ventas60d1:Double?= parseDouble((it.get("Recuperacion 60 dias mes 3")).toString())
        val ventas60d2:Double?= parseDouble((it.get("Recuperacion 60 dias mes 4")).toString())
        val ventas60d3:Double?= parseDouble((it.get("Recuperacion 60 dias mes 5")).toString())
        val totalRec60d:Double= ventas60d1!! + ventas60d2!! + ventas60d3!!
        binding.totalv3.text = totalRec60d.toString()

    }}


    companion object {

        @JvmStatic
        fun newInstance() = PresupuestoFragment()
    }
}