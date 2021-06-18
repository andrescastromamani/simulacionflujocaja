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
        //_binding!!.etDividendo1.setText("100%")
        recuperarDatos()
        _binding!!.btnSyncIue.setOnClickListener{
            guardarDatos(user?.email.toString())
            recuperarDatos()
        }
        return view
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

    private fun recuperarDatos() {
        db.collection("Users").document(email.toString()).collection("Entradas").document("Inputs").get().addOnSuccessListener{
            binding.etAntesImpuestos1.setText(it.get("Ventas contado mes 1") as String?)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = IueFragment()
    }
}
