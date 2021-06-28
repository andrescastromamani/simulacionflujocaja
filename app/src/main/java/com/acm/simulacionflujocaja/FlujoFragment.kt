package com.acm.simulacionflujocaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm.simulacionflujocaja.databinding.FragmentFlujoBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import java.lang.Double.parseDouble

class FlujoFragment : Fragment(R.layout.fragment_flujo) {

    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentFlujoBinding? = null
    private val binding get() = _binding!!
    val r:RedondeoDecimal = RedondeoDecimal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("FLUJO EFECTIVO")
        val user = FirebaseAuth.getInstance().currentUser
        _binding = FragmentFlujoBinding.inflate(inflater, container, false)
        val view = binding.root
        recuperarDatosPresupuesto()
        recuperarDatos()
        validarCampos()
        saveInputsFlujo()
        return view
    }

    private fun saveInputsFlujo(){
        binding.btnSaveFlujo.setOnClickListener{
            validarCampos()
            saveTotalsFlujo()
        }
    }

    private fun saveTotalsFlujo() {
        val ingresosOperacion:Double= parseDouble(binding.etIngresosOperacion.text.toString())
        val gastosOperacion:Double= parseDouble(binding.etGastosOperacion.text.toString())

        val ingresoCapital:Double= parseDouble(binding.etIngresosCapital.text.toString())
        val gastosCapital:Double= parseDouble(binding.etGastosCapital.text.toString())

        val fuentes:Double= parseDouble(binding.etFuentes.text.toString())
        val usos:Double= parseDouble(binding.etUsos.text.toString())

        val efectivoInicioPeriodo:Double= parseDouble(binding.etEfectivoInicioPeriodo.text.toString())

        val flujoProyectadoActividadesOperacion:Double= parseDouble(binding.etFlujoActividadesOperacion.toString())


        //Calculo Flujo de efectivo por actividades de Operacion
        val flujoActividadesOperacion:Double=r.redondear(ingresoCapital - gastosCapital)
        binding.etFlujoActividadesOperacion.setText(flujoActividadesOperacion.toString())

        //Calculo Flujo de efectivo por actividades de financiamiento
        val flujoActividadesFinanciamiento:Double=r.redondear(fuentes - usos)
        binding.etFlujoActividadesOperacion.setText(flujoActividadesFinanciamiento.toString())

        //Calculo Incremento Proyectada del efectivo del periodo
        val incrementoProyectadoEfectivoPeriodo:Double = r.redondear(flujoProyectadoActividadesOperacion + flujoActividadesOperacion + flujoActividadesFinanciamiento)
        binding.etIncrementoEfectivoPeriodo.setText(incrementoProyectadoEfectivoPeriodo.toString())

        //Calculo Saldo de efecto final proyectado
        val saldoFinalProyectado:Double = r.redondear( incrementoProyectadoEfectivoPeriodo)
        binding.etSaldoFinalEfectivoProyectado.setText(saldoFinalProyectado.toString())

        //GUARDA EN BD TODAS LA ENTRADAS Y SUS CALCULOS
        db.collection("Users").document(user?.email.toString()).collection("FlujoEfectivoProyectado").document("DatosFlujoProyectado").set(
            hashMapOf(
                "flujoEfectivoActividadOperacion"    to binding.etFlujoActividadesOperacion.text.toString(),
                "ingresosOperacion"    to binding.etIngresosOperacion.text.toString(),
                "gastosOperacion"    to binding.etGastosOperacion.text.toString(),
                "flujoActividadesInversion"    to binding.etFlujoEfectivoInversion.text.toString(),
                "ingresosCapital"    to binding.etIngresosCapital.text.toString(),
                "gastosCapital"    to binding.etGastosCapital.text.toString(),
                "flujoActividadesFinanciamiento"    to binding.etFlujoActividadesFinanciamiento.text.toString(),
                "fuentes"    to binding.etFuentes.text.toString(),
                "usos"    to binding.etUsos.text.toString(),
                "incrementoEfectivoPeriodo"    to binding.etIncrementoEfectivoPeriodo.text.toString(),
                "efectivoInicioPeriodo"    to binding.etEfectivoInicioPeriodo.text.toString(),
                "saldoEfectivoFinalProyectado"    to binding.etSaldoFinalEfectivoProyectado.text.toString(),
            )
        )
    }
    private fun recuperarDatosPresupuesto() {
        db.collection("Users").document(email.toString()).collection("Entradas").document("DatosPresupuesto").get().addOnSuccessListener {
            val totalEntradas:Double= parseDouble(it.get("totalEntradas") as String?)
            val totalSalidas:Double= parseDouble(it.get("totalTotalSalidas") as String?)
            val saldoAnterior:Double= parseDouble(it.get("saldoAnt1") as String?)

            binding.etIngresosOperacion.setText(totalEntradas.toString())
            binding.etGastosOperacion.setText(totalSalidas.toString())
            binding.etEfectivoInicioPeriodo.setText(saldoAnterior.toString())
            val flujoProyectadoActividadesOperacion=totalEntradas-totalSalidas
            binding.etFlujoActividadesOperacion.setText(flujoProyectadoActividadesOperacion.toString())
        }
    }

    private fun recuperarDatos() {
        db.collection("Users").document(email.toString()).collection("FlujoEfectivoProyectado").document("DatosFlujoProyectado").get().addOnSuccessListener{
            binding.etFlujoActividadesOperacion.setText(it.get("flujoEfectivoActividadOperacion") as String?)
            binding.etIngresosOperacion.setText(it.get("ingresosOperacion") as String?)
            binding.etGastosOperacion.setText(it.get("gastosOperacion") as String?)
            binding.etFlujoEfectivoInversion.setText(it.get("flujoActividadesInversion") as String?)
            binding.etIngresosCapital.setText(it.get("ingresosCapital") as String?)
            binding.etGastosCapital.setText(it.get("gastosCapital") as String?)
            binding.etFlujoActividadesFinanciamiento.setText(it.get("flujoActividadesFinanciamiento") as String?)
            binding.etFuentes.setText(it.get("fuentes") as String?)
            binding.etUsos.setText(it.get("usos") as String?)
            binding.etIncrementoEfectivoPeriodo.setText(it.get("incrementoEfectivoPeriodo") as String?)
            binding.etEfectivoInicioPeriodo.setText(it.get("efectivoInicioPeriodo") as String?)
            binding.etSaldoFinalEfectivoProyectado.setText(it.get("saldoEfectivoFinalProyectado") as String?)
        }
    }
    private fun validarCampos() {
        if (binding.etFlujoActividadesOperacion.text.toString().length == 0 ) {
            binding.etFlujoActividadesOperacion.setText("0.0")
        }
        if (binding.etIngresosOperacion.text.toString().length == 0) {
            binding.etIngresosOperacion.setText("0.0")
        }
        if (binding.etGastosOperacion.text.toString().length == 0) {
            binding.etGastosOperacion.setText("0.0")
        }
        if (binding.etFlujoEfectivoInversion.text.toString().length == 0) {
            binding.etFlujoEfectivoInversion.setText("0.0")
        }
        if (binding.etIngresosCapital.text.toString().length == 0) {
            binding.etIngresosCapital.setText("0.0")
        }
        if (binding.etGastosCapital.text.toString().length == 0) {
            binding.etGastosCapital.setText("0.0")
        }
        if (binding.etFlujoActividadesFinanciamiento.text.toString().length == 0) {
            binding.etFlujoActividadesFinanciamiento.setText("0.0")
        }
        if (binding.etFuentes.text.toString().length == 0) {
            binding.etFuentes.setText("0.0")
        }
        if (binding.etUsos.text.toString().length == 0) {
            binding.etUsos.setText("0.0")
        }
        if (binding.etIncrementoEfectivoPeriodo.text.toString().length == 0) {
            binding.etIncrementoEfectivoPeriodo.setText("0.0")
        }
        if (binding.etEfectivoInicioPeriodo.text.toString().length == 0) {
            binding.etEfectivoInicioPeriodo.setText("0.0")
        }
        if (binding.etSaldoFinalEfectivoProyectado.text.toString().length == 0) {
            binding.etSaldoFinalEfectivoProyectado.setText("0.0")
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = FlujoFragment()
    }
}