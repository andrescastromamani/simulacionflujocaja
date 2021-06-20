package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentIueBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class IueFragment : Fragment(R.layout.fragment_iue) {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentIueBinding? = null
    private val binding get() = _binding!!
    val r:RedondeoDecimal = RedondeoDecimal()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("IUE")
        val user = FirebaseAuth.getInstance().currentUser
        _binding = FragmentIueBinding.inflate(inflater, container, false)
        val view = binding.root
        //recuperarDatos()
        /*_binding!!.btnSyncIue.setOnClickListener{
            guardarDatos(user?.email.toString())
            //recuperarDatos()
        }*/
        guardarDatos(user?.email.toString())
        return view
    }

    private fun saveInputsIue(){
        binding.btnSaveIue.setOnClickListener{
            validarCampos()

        }
    }
    private fun guardarDatos(email: String) {
        binding.btnSaveIue.setOnClickListener{
            db.collection("Users").document(user?.email.toString()).collection("Iue").document("DatosIUE").set(
                hashMapOf(
                    "utilidadAntesImpuestos"    to binding.etAntesImpuestos1.text.toString(),
                    "gastosDeducibles"          to binding.etGastosDeducibles1.text.toString(),
                    "ingresosImponibles"        to binding.etIngresoImponible1.text.toString(),
                    "utilidadImpositiva"        to binding.etUtilidadImpositiva1.text.toString(),
                    "iuePorPagar"               to binding.etPorPagar1.text.toString(),
                    "utilidadDespuesImpuestos"  to binding.etDespuesImpuestos1.text.toString(),
                    "dividendosPorPagar"        to binding.etDividendosPagar1.text.toString(),
                    "primasPorPagar"            to binding.etPrimasPagar1.text.toString(),
                ))
            val utilidadAntesImpuestos:Double = binding.etAntesImpuestos1.text.toString().toDouble()
            val gastosDeducibles:Double       = binding.etGastosDeducibles1.text.toString().toDouble()
            val ingresosImpobibles:Double     = binding.etIngresoImponible1.text.toString().toDouble()
            val utilidadImpositiva:Double     = binding.etUtilidadImpositiva1.text.toString().toDouble()
            val iuePorPagar:Double     = binding.etPorPagar1.text.toString().toDouble()
            val utilidadDespuesImpuestos:Double     = binding.etDespuesImpuestos1.text.toString().toDouble()
            val dividendosPorPagar:Double     = binding.etDividendosPagar1.text.toString().toDouble()
            val primasPorPagar:Double     = binding.etPrimasPagar1.text.toString().toDouble()

            //val utilidadImpositiva:Double=utilidadAntesImpuestos + gastosDeducibles + ingresosImpobibles
        }
    }

    /*
    private fun recuperarDatos() {
        db.collection("Users").document(email.toString()).collection("Entradas").document("Inputs").get().addOnSuccessListener{
            binding.etAntesImpuestos1.setText(it.get("Ventas contado mes 1") as String?)
        }
    }*/

    private fun validarCampos() {

        if (binding.etDividendo1.text.toString().length == 0 ) {
            binding.etDividendo1.setText("0.0")
        }
        if (binding.etPrimas1.text.toString().length == 0) {
            binding.etPrimas1.setText("0.0")
        }
        if (binding.etAntesImpuestos1.text.toString().length == 0) {
            binding.etAntesImpuestos1.setText("0.0")
        }
        if (binding.etGastosDeducibles1.text.toString().length == 0) {
            binding.etGastosDeducibles1.setText("0.0")
        }
        if (binding.etIngresoImponible1.text.toString().length == 0) {
            binding.etIngresoImponible1.setText("0.0")
        }
        if (binding.etUtilidadImpositiva1.text.toString().length == 0) {
            binding.etUtilidadImpositiva1.setText("0.0")
        }
        if (binding.etPorPagar1.text.toString().length == 0) {
            binding.etPorPagar1.setText("0.0")
        }
        if (binding.etDespuesImpuestos1.text.toString().length == 0) {
            binding.etPrimas1.setText("0.0")
        }
        if (binding.etDividendosPagar1.text.toString().length == 0) {
            binding.etDividendosPagar1.setText("0.0")
        }
        if (binding.etPrimasPagar1.text.toString().length == 0) {
            binding.etPrimasPagar1.setText("0.0")
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = IueFragment()
    }
}
