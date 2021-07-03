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
        //validarCampos()
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
        val destinadoDividendos:Double= parseDouble(binding.etDestinadoDividendos.text.toString())
        val destinadoPagoPrima:Double= parseDouble(binding.etDestinadoPagosPrimas.text.toString())

        val utilidanAntesImpuestos:Double= parseDouble(binding.etUtilidadAntesImpuestos.text.toString())
        val gastosDeducibles:Double= parseDouble(binding.etGastosDeducibles.text.toString())
        val ingresosNoImponibles:Double= parseDouble(binding.etIngresosNoImponibles.text.toString())

        //Calculo utilidad Ipositiva
        val utilidadImpositiva:Double=r.redondear(utilidanAntesImpuestos + gastosDeducibles - ingresosNoImponibles)
        binding.etUtilidadImpositiva.setText(utilidadImpositiva.toString())

        //Iue por Pagar
        val iuePorPagar:Double=r.redondear(utilidadImpositiva * iue)
        binding.etIuePorPagar.setText(iuePorPagar.toString())

        //Utilidad despues Impuestos
        val utilidadDespuesImpuestos:Double=r.redondear(utilidadImpositiva - iuePorPagar)
        binding.etUtilidadDespuesImpuestos.setText(utilidadDespuesImpuestos.toString())

        //Dividendos Por Pagar
        val dividendosPorPagar:Double=r.redondear(utilidadDespuesImpuestos*(destinadoDividendos/100))
        binding.etDividendosPorPagar.setText(dividendosPorPagar.toString())

        //Dividendos Por Pagar
        val primasPorPagar:Double=r.redondear(utilidadDespuesImpuestos*(destinadoPagoPrima/100))
        binding.etPrimasPorPagar.setText(primasPorPagar.toString())

        //GUARDA EN BD TODAS LA ENTRADAS Y SUS CALCULOS
        db.collection("Users").document(user?.email.toString()).collection("Iue").document("DatosIUE").set(
            hashMapOf(
                "destinadoDividendos"    to binding.etDestinadoDividendos.text.toString(),
                "destinadoPagosPrimas"    to binding.etDestinadoPagosPrimas.text.toString(),
                "utilidadAntesImpuestos"    to binding.etUtilidadAntesImpuestos.text.toString(),
                "gastosDeducibles"          to binding.etGastosDeducibles.text.toString(),
                "ingresosNoImponibles"        to binding.etIngresosNoImponibles.text.toString(),
                "utilidadImpositiva"        to binding.etUtilidadImpositiva.text.toString(),
                "iuePorPagar"               to binding.etIuePorPagar.text.toString(),
                "utilidadDespuesImpuestos"  to binding.etUtilidadDespuesImpuestos.text.toString(),
                "dividendosPorPagar"        to binding.etDividendosPorPagar.text.toString(),
                "primasPorPagar"            to binding.etPrimasPorPagar.text.toString(),
            )
        )
    }

    private fun recuperarDatos() {
        db.collection("Users").document(email.toString()).collection("Iue").document("DatosIUE").get().addOnSuccessListener{
            binding.etDestinadoDividendos.setText(it.get("destinadoDividendos") as String?)
            binding.etDestinadoPagosPrimas.setText(it.get("destinadoPagosPrimas") as String?)
            binding.etUtilidadAntesImpuestos.setText(it.get("utilidadAntesImpuestos") as String?)
            binding.etGastosDeducibles.setText(it.get("gastosDeducibles") as String?)
            binding.etIngresosNoImponibles.setText(it.get("ingresosNoImponibles") as String?)
            binding.etUtilidadImpositiva.setText(it.get("utilidadImpositiva") as String?)
            binding.etIuePorPagar.setText(it.get("iuePorPagar") as String?)
            binding.etUtilidadDespuesImpuestos.setText(it.get("utilidadDespuesImpuestos") as String?)
            binding.etDividendosPorPagar.setText(it.get("dividendosPorPagar") as String?)
            binding.etPrimasPorPagar.setText(it.get("primasPorPagar") as String?)
        }
    }

    private fun validarCampos() {

        if (binding.etDestinadoDividendos.text.toString().length == 0 ) {
            binding.etDestinadoDividendos.setText("0.0")
        }
        if (binding.etDestinadoPagosPrimas.text.toString().length == 0) {
            binding.etDestinadoPagosPrimas.setText("0.0")
        }
        if (binding.etUtilidadAntesImpuestos.text.toString().length == 0) {
            binding.etUtilidadAntesImpuestos.setText("0.0")
        }
        if (binding.etGastosDeducibles.text.toString().length == 0) {
            binding.etGastosDeducibles.setText("0.0")
        }
        if (binding.etIngresosNoImponibles.text.toString().length == 0) {
            binding.etIngresosNoImponibles.setText("0.0")
        }
        if (binding.etUtilidadImpositiva.text.toString().length == 0) {
            binding.etUtilidadImpositiva.setText("0.0")
        }
        if (binding.etIuePorPagar.text.toString().length == 0) {
            binding.etIuePorPagar.setText("0.0")
        }
        if (binding.etUtilidadDespuesImpuestos.text.toString().length == 0) {
            binding.etUtilidadDespuesImpuestos.setText("0.0")
        }
        if (binding.etDividendosPorPagar.text.toString().length == 0) {
            binding.etDividendosPorPagar.setText("0.0")
        }
        if (binding.etPrimasPorPagar.text.toString().length == 0) {
            binding.etPrimasPorPagar.setText("0.0")
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = IueFragment()
    }
}
