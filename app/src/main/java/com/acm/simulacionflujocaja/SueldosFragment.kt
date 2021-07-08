package com.acm.simulacionflujocaja

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding
import com.acm.simulacionflujocaja.databinding.FragmentIvaBinding
import com.acm.simulacionflujocaja.databinding.FragmentSueldosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.fragment_sueldos.*
import java.io.FileOutputStream
import java.lang.Double.parseDouble
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SueldosFragment : Fragment(R.layout.fragment_sueldos) {
    private lateinit var etTotalGanadoAntes : EditText
    private lateinit var btnGeneratePdf : EditText
    private val STORAGE_CODE = 1001

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
        generatePdf()
        validarCampos()
        resuperarDatosInput()
        recuperarDatos()
        saveInputsSueldos()
        return view
    }

    private fun generatePdf() {
        val titleSueldosSalarios = binding.titleSueldosSalarios.text.toString()
        val totalGanadoAntes = binding.etTotalGanadoMensualAntes.text.toString()
        binding.btnGeneratePdf.setOnClickListener{
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, STORAGE_CODE)
                }else{
                    savePdf()
                }
            }else{
                savePdf()
            }
        }

    }

    private fun savePdf() {
        val myDoc = Document()
        val myFileName = SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val filePath = Environment.getExternalStorageDirectory().toString() + "/" + myFileName + ".pdf"

        try {
            PdfWriter.getInstance(myDoc,FileOutputStream(filePath))
            myDoc.open()
            val data = titleSueldosSalarios.text.toString().trim()
            myDoc.addAuthor("ACM")
            myDoc.add(Paragraph(data))
            myDoc.close()
            Toast.makeText(this, "$myFileName.pdf\n is create to \n$filePath", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveInputsSueldos(){
        binding.btnSaveSueldos.setOnClickListener{
            if(binding.etNumeroMesesSueldos.text.isEmpty())
            {binding.etNumeroMesesSueldos.setText("0.0")}
            if(binding.etNumeroMesesAportes.text.isEmpty()){
                binding.etNumeroMesesAportes.setText("0.0")
            }
            saveTotalsSueldos()

        }
    }

    private fun saveTotalsSueldos() {
        val totGanMenAntIS:Double= parseDouble(binding.etTotalGanadoMensualAntes.text.toString())
        println(totGanMenAntIS)
        val retroactivoSueldosPorMes:Double  = parseDouble(binding.etRetroactivoSueldosPorMes1.text.toString())
        val numeroMesesSueldos:Double  = parseDouble(binding.etNumeroMesesSueldos.text.toString())
        val retSueldoMesTot=r.redondear(retroactivoSueldosPorMes*numeroMesesSueldos)
        binding.etRetroactivoSueldosPorMes2.setText(retSueldoMesTot.toString())

        val retroactivoAportesPorMes:Double  = parseDouble(binding.etRetroactivoAportesPorMes1.text.toString())
        val numeroMesesAportes:Double  = parseDouble(binding.etNumeroMesesAportes.text.toString())
        val retAporteMesTot=r.redondear(retroactivoAportesPorMes*numeroMesesAportes)
        binding.etRetroactivoAportesPorMes2.setText(retAporteMesTot.toString())





        //Calculo retroactivo sueldos por mes

        val retSuelXmes1:Double=0.0
        val retSuelXmes2:Double=0.0
        val retSuelXmes3:Double=retSueldoMesTot
        //Calculo retroactivo aportes por mes

        val retApXmes1:Double=0.0
        val retApXmes2:Double=0.0
        val retApXmes3:Double=retAporteMesTot

        //GUARDA EN BD TODAS LA ENTRADAS Y SUS CALCULOS
        db.collection("Users").document(user?.email.toString()).collection("SueldosSalarios").document("DatosSueldosSalarios").set(
            hashMapOf(
              "aportePat1"    to binding.etAportesPatronalesMensualesAntes.text.toString(),
                "aportePat2"          to binding.etAportesPatronalesMensualesAntes.text.toString(),
                "aportePat3"        to binding.etAportesPatronalesMensualesDespues.text.toString(),
                 "retSuelXmes1"    to retSuelXmes1.toString(),
                 "retSuelXmes2"          to retSuelXmes2.toString(),
                 "retSuelXmes3"        to retSuelXmes3.toString(),
                 "retApXmes1"    to retApXmes1.toString(),
                 "retApXmes2"          to retApXmes2.toString(),
                 "retApXmes3"        to retApXmes3.toString(),
                "totalGanadoMensualAntes"    to binding.etTotalGanadoMensualAntes.text.toString(),
               // "aportesPatronalesMensualesAntes"          to binding.etAportesPatronalesMensualesAntes.text.toString(),
                //"totalGanadoMensualDespues"        to binding.etTotalGanadoMensualDespues.text.toString(),
               // "aportesPatronalesMensualesDespues"        to binding.etAportesPatronalesMensualesDespues.text.toString(),
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


if(it.get("sueldo1")==null&&it.get("sueldo2")==null&&it.get("sueldo3")==null)

{
    binding.etTotalGanadoMensualAntes.setText("0.0")
    binding.etTotalGanadoMensualDespues.setText("0.0")

    binding.etRetroactivoSueldosPorMes1.setText("0.0")
    binding.etRetroactivoAportesPorMes1.setText("0.0")
}
            else{

            val sueldoSinIncSal:Double= parseDouble(it.get("sueldo1") as String?)
            binding.etTotalGanadoMensualAntes.setText(sueldoSinIncSal.toString())
            val sueldoConIncrementoSalarial:Double= parseDouble(it.get("sueldo3") as String?)
            binding.etTotalGanadoMensualDespues.setText(sueldoConIncrementoSalarial.toString())
            val aportesPatronales = 0.1671
            val aportesPatMensAntes=r.redondear(sueldoSinIncSal*aportesPatronales)
            val aportesPatMensDesp=r.redondear(sueldoConIncrementoSalarial*aportesPatronales)


            //Calculo Total Ganado Mensual Antes del incremento Salarial

            binding.etAportesPatronalesMensualesAntes.setText(aportesPatMensAntes.toString())
            //Calculo Total Ganado Mensual Despues del incremento Salarial

            binding.etAportesPatronalesMensualesDespues.setText(aportesPatMensDesp.toString())
           //Retroactivo sueldo por mes
            val retSueldoMes=r.redondear(sueldoConIncrementoSalarial-sueldoSinIncSal)
            binding.etRetroactivoSueldosPorMes1.setText(retSueldoMes.toString())
            //Retroactivo aportes por mes
            val retAportesMes=r.redondear(retSueldoMes*aportesPatronales)
            binding.etRetroactivoAportesPorMes1.setText(retAportesMes.toString())}



        }
    }

    private fun validarCampos() {
        if (binding.etTotalGanadoMensualAntes.text.toString().isEmpty()) {
            binding.etTotalGanadoMensualAntes.setText("0.0")
        }
        if (binding.etAportesPatronalesMensualesAntes.text.toString().isEmpty()) {
            binding.etAportesPatronalesMensualesAntes.setText("0.0")
        }
        if (binding.etTotalGanadoMensualDespues.text.toString().isEmpty()) {
            binding.etTotalGanadoMensualDespues.setText("0.0")
        }
        if (binding.etAportesPatronalesMensualesDespues.text.toString().isEmpty()) {
            binding.etAportesPatronalesMensualesDespues.setText("0.0")
        }
        if (binding.etRetroactivoSueldosPorMes1.text.toString().isEmpty()) {
            binding.etRetroactivoSueldosPorMes1.setText("0.0")
        }
        if (binding.etNumeroMesesSueldos.text.toString().isEmpty()) {
            binding.etNumeroMesesSueldos.setText("0.0")
        }
        if (binding.etRetroactivoSueldosPorMes2.text.toString().isEmpty()) {
            binding.etRetroactivoSueldosPorMes2.setText("0.0")
        }
        if (binding.etRetroactivoAportesPorMes1.text.toString().isEmpty()) {
            binding.etRetroactivoAportesPorMes1.setText("0.0")
        }
        if (binding.etNumeroMesesAportes.text.toString().isEmpty()) {
            binding.etNumeroMesesAportes.setText("0.0")
        }
        if (binding.etRetroactivoAportesPorMes2.text.toString().isEmpty()) {
            binding.etRetroactivoAportesPorMes2.setText("0.0")
        }
    }

    private fun recuperarDatos() {
        db.collection("Users").document(email.toString()).collection("SueldosSalarios").document("DatosSueldosSalarios").get().addOnSuccessListener{
          //  binding.etTotalGanadoMensualAntes.setText(it.get("totalGanadoMensualAntes") as String?)
           // binding.etAportesPatronalesMensualesAntes.setText(it.get("aportesPatronalesMensualesAntes") as String?)
            //binding.etTotalGanadoMensualDespues.setText(it.get("totalGanadoMensualDespues") as String?)
            //binding.etAportesPatronalesMensualesDespues.setText(it.get("aportesPatronalesMensualesDespues") as String?)
            //binding.etRetroactivoSueldosPorMes1.setText(it.get("retroactivoSueldosPorMes1") as String?)
            binding.etNumeroMesesSueldos.setText(it.get("numeroMesesSueldos") as String?)
            binding.etRetroactivoSueldosPorMes2.setText(it.get("retroactivoSueldosPorMes2") as String?)
            //binding.etRetroactivoAportesPorMes1.setText(it.get("retroactivoAportesPorMes1") as String?)
            binding.etNumeroMesesAportes.setText(it.get("numeroMesesAportes") as String?)
            binding.etRetroactivoAportesPorMes2.setText(it.get("retroactivoAportesPorMes2") as String?)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SueldosFragment()
    }

}