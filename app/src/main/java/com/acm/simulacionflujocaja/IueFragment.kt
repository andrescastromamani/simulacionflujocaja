package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentIueBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Double.parseDouble

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
        recuperarDatos()
        validarCampos()
        saveInputsIue()
        return view
    }

    private fun saveInputsIue(){
        binding.btnSaveIue.setOnClickListener{
            validarCampos()
            saveTotalsIue()

        }
    }

    private fun saveTotalsIue() {
        val iue = 0.25
        val utilidanAntesImpuestos:Double= parseDouble(binding.etAntesImpuestos1.text.toString())
        val gastosDeducibles:Double= parseDouble(binding.etGastosDeducibles1.text.toString())
        val ingresosNoImponibles:Double= parseDouble(binding.etIngresoImponible1.text.toString())

        //Calculo utilidad Ipositiva
        val utilidadImpositiva:Double=r.redondear(utilidanAntesImpuestos+gastosDeducibles+ingresosNoImponibles)
        binding.etUtilidadImpositiva1.setText(utilidadImpositiva.toString())

        //Iue por Pagar
        val iuePorPagar:Double=r.redondear(utilidadImpositiva * iue)
        binding.etPorPagar1.setText(iuePorPagar.toString())

        //Utilidad despues Impuestos
        val utilidadDespuesImpuestos:Double=r.redondear(utilidadImpositiva - iuePorPagar)
        binding.etDespuesImpuestos1.setText(utilidadDespuesImpuestos.toString())

        //GUARDA EN BD TODAS LA ENTRADAS Y SUS CALCULOS
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
            )
        )
    }

    private fun recuperarDatos() {
        db.collection("Users").document(email.toString()).collection("Iue").document("DatosIUE").get().addOnSuccessListener{
            binding.etAntesImpuestos1.setText(it.get("utilidadAntesImpuestos") as String?)
            binding.etGastosDeducibles1.setText(it.get("gastosDeducibles") as String?)
            binding.etIngresoImponible1.setText(it.get("ingresosImponibles") as String?)
            binding.etUtilidadImpositiva1.setText(it.get("gastosDeducibles") as String?)
            binding.etPorPagar1.setText(it.get("utilidadImpositiva") as String?)
            binding.etDespuesImpuestos1.setText(it.get("utilidadDespuesImpuestos") as String?)
            binding.etDividendosPagar1.setText(it.get("dividendosPorPagar") as String?)
            binding.etPrimasPagar1.setText(it.get("primasPorPagar") as String?)
        }
    }

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
