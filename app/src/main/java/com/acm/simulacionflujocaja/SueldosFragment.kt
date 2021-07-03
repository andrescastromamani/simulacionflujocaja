package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding
import com.acm.simulacionflujocaja.databinding.FragmentIvaBinding
import com.acm.simulacionflujocaja.databinding.FragmentSueldosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Double.parseDouble

class SueldosFragment : Fragment(R.layout.fragment_sueldos) {

    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentSueldosBinding? = null
    private val binding get() = _binding!!
    val r:RedondeoDecimal = RedondeoDecimal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("SUELDOS Y SALARIOS")
        val user = FirebaseAuth.getInstance().currentUser
        _binding = FragmentSueldosBinding.inflate(inflater, container, false)
        val view = binding.root
        resuperarDatosInput()

        //validarCampos()

        recuperarDatos()
        validarCampos()

        saveInputsSueldos()
        return view
    }
    private fun saveInputsSueldos(){
        binding.btnSaveSueldos.setOnClickListener{
            //validarCampos()
            saveTotalsSueldos()

        }
    }

    private fun saveTotalsSueldos() {
        val aportesPatronales = 0.1671
        val caja = 0.1
        val riesgo = 0.0171
        val sol = 0.03
        val prov = 0.02

        val totalGanadoMensualAntes:Double= parseDouble(binding.etTotalGanadoMensualAntes.text.toString())
        val totalGanadoMensualDespues:Double= parseDouble(binding.etTotalGanadoMensualDespues.text.toString())

        val retroactivoSueldosPorMes:Double  = parseDouble(binding.etRetroactivoSueldosPorMes1.text.toString())
        val numeroMesesSueldos:Double  = parseDouble(binding.etNumeroMesesSueldos.text.toString())

        val retroactivoAportesPorMes:Double  = parseDouble(binding.etRetroactivoAportesPorMes1.text.toString())
        val numeroMesesAportes:Double  = parseDouble(binding.etNumeroMesesAportes.text.toString())

        //Calculo aporte patronal mensual Antes del Incremento salarial
        val aportePatronalMensualAntes:Double=r.redondear(totalGanadoMensualAntes * aportesPatronales)
        binding.etAportesPatronalesMensualesAntes.setText(aportePatronalMensualAntes.toString())
        val aportePat1=aportePatronalMensualAntes
        val aportePat2=aportePatronalMensualAntes


        //Calculo aporte patronal mensual Despues del Incremento salarial
        val aportePatronalMensualDespues:Double=r.redondear(totalGanadoMensualDespues * aportesPatronales)
        binding.etAportesPatronalesMensualesDespues.setText(aportePatronalMensualDespues.toString())
        val aportePat3=aportePatronalMensualDespues
        //Calculo retroactivo sueldos por mes
        val retroactivoSueldoPorMesTotal:Double=r.redondear(retroactivoSueldosPorMes * numeroMesesSueldos)
        binding.etRetroactivoSueldosPorMes2.setText(retroactivoSueldoPorMesTotal.toString())
        val retSuelXmes1:Double=0.0
        val retSuelXmes2:Double=0.0
        val retSuelXmes3:Double=retroactivoSueldoPorMesTotal
        //Calculo retroactivo aportes por mes
        val retroactivoAportesPorMesTotal:Double=r.redondear(retroactivoAportesPorMes * numeroMesesAportes)
        binding.etRetroactivoAportesPorMes2.setText(retroactivoAportesPorMesTotal.toString())
        val retApXmes1:Double=0.0
        val retApXmes2:Double=0.0
        val retApXmes3:Double=retroactivoAportesPorMesTotal

        //GUARDA EN BD TODAS LA ENTRADAS Y SUS CALCULOS
        db.collection("Users").document(user?.email.toString()).collection("SueldosSalarios").document("DatosSueldosSalarios").set(
            hashMapOf(
                "aportePat1"    to aportePat1.toString(),
                "aportePat2"          to aportePat2.toString(),
                "aportePat3"        to aportePat3.toString(),
                "retSuelXmes1"    to retSuelXmes1.toString(),
                "retSuelXmes2"          to retSuelXmes2.toString(),
                "retSuelXmes3"        to retSuelXmes3.toString(),
                "retApXmes1"    to retApXmes1.toString(),
                "retApXmes2"          to retApXmes2.toString(),
                "retApXmes3"        to retApXmes3.toString(),
                "totalGanadoMensualAntes"    to binding.etTotalGanadoMensualAntes.text.toString(),
                "aportesPatronalesMensualesAntes"          to binding.etAportesPatronalesMensualesAntes.text.toString(),
                "totalGanadoMensualDespues"        to binding.etTotalGanadoMensualDespues.text.toString(),
                "aportesPatronalesMensualesDespues"        to binding.etAportesPatronalesMensualesDespues.text.toString(),
                "retroactivoSueldosPorMes1"               to binding.etRetroactivoSueldosPorMes1.text.toString(),
                "numeroMesesSueldos"  to binding.etNumeroMesesSueldos.text.toString(),
                "retroactivoSueldosPorMes2"        to binding.etRetroactivoSueldosPorMes2.text.toString(),
                "retroactivoAportesPorMes1"            to binding.etRetroactivoAportesPorMes1.text.toString(),
                "numeroMesesAportes"            to binding.etNumeroMesesAportes.text.toString(),
                "retroactivoAportesPorMes2"            to binding.etRetroactivoAportesPorMes2.text.toString(),
            )
        )
    }

    private fun resuperarDatosInput(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("SueldosMes").get().addOnSuccessListener {
            val sueldoSinIncSal1:Double= parseDouble(it.get("sueldo1") as String?)
            val sueldoSinIncSal:Double= parseDouble(it.get("sueldo2") as String?)
            val sueldoConIncrementoSalarial:Double= parseDouble(it.get("sueldo3") as String?)


            //Calculo Total Ganado Mensual Antes del incremento Salarial
            binding.etTotalGanadoMensualAntes.setText(sueldoSinIncSal.toString())
            binding.etTotalGanadoMensualDespues.setText(sueldoConIncrementoSalarial.toString())
        }
    }
    private fun recuperarDatosInputs(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("OtrosDatos").get().addOnSuccessListener {
            val incrementoSalarial:Double= parseDouble(it.get("Incremento Salarial") as String?)
            val sueldoEmpleado:Double= parseDouble(it.get("Sueldo Empleados") as String?)
            val numeroEmpleados:Double= parseDouble(it.get("Nro Empleados") as String?)

            //Calculo Retroactivos Sueldos Por Mes
            val retroactivoSueldosMes:Double=r.redondear((incrementoSalarial/100) * sueldoEmpleado * numeroEmpleados)
            binding.etRetroactivoSueldosPorMes1.setText(retroactivoSueldosMes.toString())
        }
    }
    private fun validarCampos() {
        if (binding.etTotalGanadoMensualAntes.text.toString().length == 0 ) {
            binding.etTotalGanadoMensualAntes.setText("0.0")
        }
        if (binding.etAportesPatronalesMensualesAntes.text.toString().length == 0) {
            binding.etAportesPatronalesMensualesAntes.setText("0.0")
        }
        if (binding.etTotalGanadoMensualDespues.text.toString().length == 0) {
            binding.etTotalGanadoMensualDespues.setText("0.0")
        }
        if (binding.etAportesPatronalesMensualesDespues.text.toString().length == 0) {
            binding.etAportesPatronalesMensualesDespues.setText("0.0")
        }
        if (binding.etRetroactivoSueldosPorMes1.text.toString().length == 0) {
            binding.etRetroactivoSueldosPorMes1.setText("0.0")
        }
        if (binding.etNumeroMesesSueldos.text.toString().length == 0) {
            binding.etNumeroMesesSueldos.setText("0.0")
        }
        if (binding.etRetroactivoSueldosPorMes2.text.toString().length == 0) {
            binding.etRetroactivoSueldosPorMes2.setText("0.0")
        }
        if (binding.etRetroactivoAportesPorMes1.text.toString().length == 0) {
            binding.etRetroactivoAportesPorMes1.setText("0.0")
        }
        if (binding.etNumeroMesesAportes.text.toString().length == 0) {
            binding.etNumeroMesesAportes.setText("0.0")
        }
        if (binding.etRetroactivoAportesPorMes2.text.toString().length == 0) {
            binding.etRetroactivoAportesPorMes2.setText("0.0")
        }
    }

    private fun recuperarDatos() {
        db.collection("Users").document(email.toString()).collection("SueldosSalarios").document("DatosSueldosSalarios").get().addOnSuccessListener{
            binding.etTotalGanadoMensualAntes.setText(it.get("totalGanadoMensualAntes") as String?)
            binding.etAportesPatronalesMensualesAntes.setText(it.get("aportesPatronalesMensualesAntes") as String?)
            //binding.etTotalGanadoMensualDespues.setText(it.get("totalGanadoMensualDespues") as String?)
            binding.etAportesPatronalesMensualesDespues.setText(it.get("aportesPatronalesMensualesDespues") as String?)
            binding.etRetroactivoSueldosPorMes1.setText(it.get("retroactivoSueldosPorMes1") as String?)
            binding.etNumeroMesesSueldos.setText(it.get("numeroMesesSueldos") as String?)
            binding.etRetroactivoSueldosPorMes2.setText(it.get("retroactivoSueldosPorMes2") as String?)
            binding.etRetroactivoAportesPorMes1.setText(it.get("retroactivoAportesPorMes1") as String?)
            binding.etNumeroMesesAportes.setText(it.get("numeroMesesAportes") as String?)
            binding.etRetroactivoAportesPorMes2.setText(it.get("retroactivoAportesPorMes2") as String?)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SueldosFragment()
    }

}