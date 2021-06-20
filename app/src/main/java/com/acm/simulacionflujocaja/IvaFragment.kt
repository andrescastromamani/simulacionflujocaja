package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding

import com.acm.simulacionflujocaja.databinding.FragmentIvaBinding
import com.acm.simulacionflujocaja.databinding.FragmentPresupuestoBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_iva.*
import java.lang.Double.parseDouble

class IvaFragment : Fragment(R.layout.fragment_iva) {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentIvaBinding? = null
    private val binding get() = _binding!!
    val r :RedondeoDecimal= RedondeoDecimal()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("IVA")
        _binding = FragmentIvaBinding.inflate(inflater, container, false)
        val view = binding.root
        recuperarTodoIVA()
        recDatosMeses()//recupera los meses que esta en bd y establece como texto en los textView
        recDataVentCont()//recupera de la bd y manda la informacion a las ventas contado
        saveInputsIVA() //guarda todas las entradas que existen en IVA





        return view
    }

    private fun saveInputsIVA(){


        binding.btnSaveIva.setOnClickListener {
            validarCampos()
            saveTotalDebFisc()



       /* db.collection("Users").document(user?.email.toString()).collection("Entradas").document("DatosIva").set(
            hashMapOf(
                "VentCon1" to binding.etVentas1.text.toString(),
                "VentCon2" to binding.etVentas2.text.toString(),
                "VentCon3" to binding.etVentas3.text.toString(),
                "InCoDF1" to binding.Inco1.text.toString(),
                "InCoDF2" to binding.Inco2.text.toString(),
                "InCoDF3" to binding.Inco3.text.toString(),
                "VeAcFi1" to binding.vaf1.text.toString(),
                "VeAcFi2" to binding.vaf2.text.toString(),
                "VeAcFi3" to binding.vaf3.text.toString(),
                "Alq1" to binding.alq1.text.toString(),
                "Alq2" to binding.alq2.text.toString(),
                "Alq3" to binding.alq3.text.toString(),
                "otros1" to binding.ot1.text.toString(),
                "otros2" to binding.ot2.text.toString(),
                "otros3" to binding.ot3.text.toString(),
                "tot1" to binding.tot1.text.toString(),
                "tot2" to binding.tot2.text.toString(),
                "tot3" to binding.tot3.text.toString(),
                "Defis1" to binding.defis1.text.toString(),
                "Defis2" to binding.defis2.text.toString(),
                "Defis3" to binding.defis3.text.toString(),
                "CompMerc1" to binding.compMerc1.text.toString(),
                "CompMerc2" to binding.compMerc2.text.toString(),
                "CompMerc3" to binding.compMerc3.text.toString(),
                "InCoCF1" to binding.inCoCF1.text.toString(),
                "InCoCF2" to binding.inCoCF2.text.toString(),
                "InCoCF3" to binding.inCoCF3.text.toString(),
                "CoAF1" to binding.coAF1.text.toString(),
                "CoAF2" to binding.coAF2.text.toString(),
                "CoAF3" to binding.coAF3.text.toString(),
                "Sub1" to binding.sub1.text.toString(),
                "Sub2" to binding.sub2.text.toString(),
                "Sub3" to binding.sub3.text.toString(),
                "Cgo1" to binding.cgo1.text.toString(),
                "Cgo2" to binding.cgo2.text.toString(),
                "Cgo3" to binding.cgo3.text.toString(),
                "OtCF1" to binding.otCF1.text.toString(),
                "OtCF2" to binding.otCF2.text.toString(),
                "OtCF3" to binding.otCF3.text.toString(),
                "TotComp1" to binding.totComp1.text.toString(),
                "TotComp2" to binding.totComp2.text.toString(),
                "TotComp3" to binding.totComp3.text.toString(),
                "TotCF1" to binding.totCF1.text.toString(),
                "TotCF2" to binding.totCF2.text.toString(),
                "TotCF3" to binding.totCF3.text.toString(),
                "SaldFF1" to binding.saldFF1.text.toString(),
                "SaldFF2" to binding.saldFF2.text.toString(),
                "SaldFF3" to binding.saldFF3.text.toString(),
                "SaldMA1" to binding.saldMA1.text.toString(),
                "SaldMA2" to binding.saldMA2.text.toString(),
                "SaldMA3" to binding.saldMA3.text.toString(),
                "Tot1" to binding.totFinal1.text.toString(),
                "Tot2" to binding.totFinal2.text.toString(),
                "Tot3" to binding.totFinal3.text.toString(),




            )) */}



    }
    private fun saveTotalDebFisc(){//calcula y guarda total debito fiscal
        val porcIva:Double=0.13


        // CALCULO PARA CREDITOS
        val ventasC1:Double= parseDouble(binding.etVentas1.text.toString())
        val ventasC2:Double= parseDouble(binding.etVentas2.text.toString())
        val ventasC3:Double= parseDouble(binding.etVentas3.text.toString())
        val inCoDF1:Double= parseDouble(binding.Inco1.text.toString())
        val inCoDF2:Double= parseDouble(binding.Inco2.text.toString())
        val inCoDF3:Double= parseDouble(binding.Inco3.text.toString())
        val veAcFi1:Double= parseDouble(binding.vaf1.text.toString())
        val veAcFi2:Double= parseDouble(binding.vaf2.text.toString())
        val veAcFi3:Double= parseDouble(binding.vaf3.text.toString())
        val Alq1:Double= parseDouble(binding.alq1.text.toString())
        val Alq2:Double= parseDouble(binding.alq2.text.toString())
        val Alq3:Double= parseDouble(binding.alq3.text.toString())
        val otros1:Double= parseDouble(binding.ot1.text.toString())
        val otros2:Double= parseDouble(binding.ot2.text.toString())
        val otros3:Double= parseDouble(binding.ot3.text.toString())

        //calculo de TOTAL VENTAS DE BIENES Y SERVICIOS FACTURADAS Mes1
        val totalVByS1:Double=r.redondear(ventasC1+inCoDF1+veAcFi1+Alq1+otros1)
        binding.tot1.setText(totalVByS1.toString())
        val totDebFiscal1:Double=r.redondear(totalVByS1*porcIva)
        binding.defis1.setText(totDebFiscal1.toString())
        //calculo de TOTAL VENTAS DE BIENES Y SERVICIOS FACTURADAS Mes2
        val totalVByS2:Double=r.redondear(ventasC2+inCoDF2+veAcFi2+Alq2+otros2)
        binding.tot2.setText(totalVByS2.toString())
        val totDebFiscal2:Double=r.redondear(totalVByS2*porcIva)
        binding.defis2.setText(totDebFiscal2.toString())
        //calculo de TOTAL VENTAS DE BIENES Y SERVICIOS FACTURADAS Mes3
        val totalVByS3:Double=r.redondear(ventasC3+inCoDF3+veAcFi3+Alq3+otros3)
        binding.tot3.setText(totalVByS3.toString())
        val totDebFiscal3:Double=r.redondear(totalVByS3*porcIva)
        binding.defis3.setText(totDebFiscal3.toString())

        //CALCULO PARA CREDITOS
        val compraM1:Double= parseDouble(binding.compMerc1.text.toString())
        val compraM2:Double= parseDouble(binding.compMerc2.text.toString())
        val compraM3:Double= parseDouble(binding.compMerc3.text.toString())
        val interesCred1:Double= parseDouble(binding.inCoCF1.text.toString())
        val interesCred2:Double= parseDouble(binding.inCoCF2.text.toString())
        val interesCred3:Double= parseDouble(binding.inCoCF3.text.toString())
        val compraAF1:Double= parseDouble(binding.coAF1.text.toString())
        val compraAF2:Double= parseDouble(binding.coAF2.text.toString())
        val compraAF3:Double= parseDouble(binding.coAF3.text.toString())
        val subsid1:Double= parseDouble(binding.sub1.text.toString())
        val subsid2:Double= parseDouble(binding.sub2.text.toString())
        val subsid3:Double= parseDouble(binding.sub3.text.toString())
        val costYGasOp1:Double= parseDouble(binding.cgo1.text.toString())
        val costYGasOp2:Double= parseDouble(binding.cgo2.text.toString())
        val costYGasOp3:Double= parseDouble(binding.cgo3.text.toString())
        val otrosCred1:Double= parseDouble(binding.otCF1.text.toString())
        val otrosCred2:Double= parseDouble(binding.otCF2.text.toString())
        val otrosCred3:Double= parseDouble(binding.otCF3.text.toString())

        val totalCompCred1:Double=r.redondear(compraM1+interesCred1+compraAF1+subsid1+costYGasOp1+otrosCred1)
        val totalCompCred2:Double=r.redondear(compraM2+interesCred2+compraAF2+subsid2+costYGasOp2+otrosCred2)
        val totalCompCred3:Double=r.redondear(compraM3+interesCred3+compraAF3+subsid3+costYGasOp3+otrosCred3)
        binding.totComp1.setText(totalCompCred1.toString())
        binding.totComp2.setText(totalCompCred2.toString())
        binding.totComp3.setText(totalCompCred3.toString())
        val totalCredFis1:Double=r.redondear(totalCompCred1*porcIva)
        val totalCredFis2:Double=r.redondear(totalCompCred2*porcIva)
        val totalCredFis3:Double=r.redondear(totalCompCred3*porcIva)
        binding.totCF1.setText(totalCredFis1.toString())
        binding.totCF2.setText(totalCredFis2.toString())
        binding.totCF3.setText(totalCredFis3.toString())

        //CALCULO PARA SALDO A FAVOR DEL FISCO

        val saldoFavCon1:Double= r.redondear(totDebFiscal1-totalCredFis1)
        val saldoFavCon2:Double= r.redondear(totDebFiscal2-totalCredFis2)
        val saldoFavCon3:Double= r.redondear(totDebFiscal3-totalCredFis3)
        binding.saldFF1.setText(saldoFavCon1.toString())
        binding.saldFF2.setText(saldoFavCon2.toString())
        binding.saldFF3.setText(saldoFavCon3.toString())


        val saldoCFMesAnt1:Double= parseDouble(binding.saldMA1.text.toString())
        val saldoCFMesAnt2:Double= parseDouble(binding.saldMA2.text.toString())
        val saldoCFMesAnt3:Double= parseDouble(binding.saldMA3.text.toString())

        //CALCULO SALDO DEFINITIVO A FAVOR DEL FISCO
        val saldoFinalFisco1:Double=r.redondear(saldoFavCon1+saldoCFMesAnt1)
        val saldoFinalFisco2:Double=r.redondear(saldoFavCon2+saldoCFMesAnt2)
        val saldoFinalFisco3:Double=r.redondear(saldoFavCon3+saldoCFMesAnt3)
        binding.totFinal1.setText(saldoFinalFisco1.toString())
        binding.totFinal2.setText(saldoFinalFisco2.toString())
        binding.totFinal3.setText(saldoFinalFisco3.toString())






        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("DatosIva").set(
            hashMapOf(
                "VentCon1" to binding.etVentas1.text.toString(),
                "VentCon2" to binding.etVentas2.text.toString(),
                "VentCon3" to binding.etVentas3.text.toString(),
                "InCoDF1" to binding.Inco1.text.toString(),
                "InCoDF2" to binding.Inco2.text.toString(),
                "InCoDF3" to binding.Inco3.text.toString(),
                "VeAcFi1" to binding.vaf1.text.toString(),
                "VeAcFi2" to binding.vaf2.text.toString(),
                "VeAcFi3" to binding.vaf3.text.toString(),
                "Alq1" to binding.alq1.text.toString(),
                "Alq2" to binding.alq2.text.toString(),
                "Alq3" to binding.alq3.text.toString(),
                "otros1" to binding.ot1.text.toString(),
                "otros2" to binding.ot2.text.toString(),
                "otros3" to binding.ot3.text.toString(),
                "tot1" to binding.tot1.text.toString(),
                "tot2" to binding.tot2.text.toString(),
                "tot3" to binding.tot3.text.toString(),
                "Defis1" to binding.defis1.text.toString(),
                "Defis2" to binding.defis2.text.toString(),
                "Defis3" to binding.defis3.text.toString(),
                "CompMerc1" to binding.compMerc1.text.toString(),
                "CompMerc2" to binding.compMerc2.text.toString(),
                "CompMerc3" to binding.compMerc3.text.toString(),
                "InCoCF1" to binding.inCoCF1.text.toString(),
                "InCoCF2" to binding.inCoCF2.text.toString(),
                "InCoCF3" to binding.inCoCF3.text.toString(),
                "CoAF1" to binding.coAF1.text.toString(),
                "CoAF2" to binding.coAF2.text.toString(),
                "CoAF3" to binding.coAF3.text.toString(),
                "Sub1" to binding.sub1.text.toString(),
                "Sub2" to binding.sub2.text.toString(),
                "Sub3" to binding.sub3.text.toString(),
                "Cgo1" to binding.cgo1.text.toString(),
                "Cgo2" to binding.cgo2.text.toString(),
                "Cgo3" to binding.cgo3.text.toString(),
                "OtCF1" to binding.otCF1.text.toString(),
                "OtCF2" to binding.otCF2.text.toString(),
                "OtCF3" to binding.otCF3.text.toString(),
                "TotComp1" to binding.totComp1.text.toString(),
                "TotComp2" to binding.totComp2.text.toString(),
                "TotComp3" to binding.totComp3.text.toString(),
                "TotCF1" to binding.totCF1.text.toString(),
                "TotCF2" to binding.totCF2.text.toString(),
                "TotCF3" to binding.totCF3.text.toString(),
                "SaldFF1" to binding.saldFF1.text.toString(),
                "SaldFF2" to binding.saldFF2.text.toString(),
                "SaldFF3" to binding.saldFF3.text.toString(),
                "SaldMA1" to binding.saldMA1.text.toString(),
                "SaldMA2" to binding.saldMA2.text.toString(),
                "SaldMA3" to binding.saldMA3.text.toString(),
                "Tot1" to binding.totFinal1.text.toString(),
                "Tot2" to binding.totFinal2.text.toString(),
                "Tot3" to binding.totFinal3.text.toString(),
                ))

    }

    private fun recDatosMeses(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").get().addOnSuccessListener {
            binding.etmes1.setText(it.get("Mes 3") as String?)
            binding.etmes2.setText(it.get("Mes 4") as String?)
            binding.etmes3.setText(it.get("Mes 5") as String?)

        } }
     //Recupera de inputs ventas al contado y los intereses si es que los hubiera
    private fun recDataVentCont(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Inputs").get().addOnSuccessListener {
            binding.etVentas1.setText(it.get("Ventas contado mes 3") as String?)
            binding.etVentas2.setText(it.get("Ventas contado mes 4") as String?)
            binding.etVentas3.setText(it.get("Ventas contado mes 5") as String?)
        } }
    private fun recuperarTodoIVA(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("DatosIva").get().addOnSuccessListener {
       /* binding.etVentas1.setText(it.get("VentCont1") as String?)
        binding.etVentas2.setText(it.get("VentCont2") as String?)
        binding.etVentas3.setText(it.get("VentCont3") as String?)*/
        binding.Inco1.setText(it.get("InCoDF1") as String?)
        binding.Inco2.setText(it.get("InCoDF2") as String?)
        binding.Inco3.setText(it.get("InCoDF3") as String?)
        binding.vaf1.setText(it.get("VeAcFi1") as String?)
        binding.vaf2.setText(it.get("VeAcFi2") as String?)
        binding.vaf3.setText(it.get("VeAcFi3") as String?)
        binding.alq1.setText(it.get("Alq1") as String?)
        binding.alq2.setText(it.get("Alq2") as String?)
        binding.alq3.setText(it.get("Alq3") as String?)
        binding.ot1.setText(it.get("otros1") as String?)
        binding.ot2.setText(it.get("otros2") as String?)
        binding.ot3.setText(it.get("otros3") as String?)
        binding.tot1.setText(it.get("tot1") as String?)
           binding.tot2.setText(it.get("tot2") as String?)
           binding.tot3.setText(it.get("tot3") as String?)
            binding.defis1.setText(it.get("Defis1") as String?)
            binding.defis2.setText(it.get("Defis2") as String?)
            binding.defis3.setText(it.get("Defis3") as String?)

            binding.compMerc1.setText(it.get("CompMerc1") as String?)
            binding.compMerc2.setText(it.get("CompMerc2") as String?)
            binding.compMerc3.setText(it.get("CompMerc3") as String?)
            binding.inCoCF1.setText(it.get("InCoCF1") as String?)
            binding.inCoCF2.setText(it.get("InCoCF2") as String?)
            binding.inCoCF3.setText(it.get("InCoCF3") as String?)

            binding.coAF1.setText(it.get("CoAF1") as String?)
            binding.coAF2.setText(it.get("CoAF2") as String?)
            binding.coAF3.setText(it.get("CoAF3") as String?)
            binding.sub1.setText(it.get("Sub1") as String?)
            binding.sub2.setText(it.get("Sub2") as String?)
            binding.sub3.setText(it.get("Sub3") as String?)

            binding.cgo1.setText(it.get("Cgo1") as String?)
            binding.cgo2.setText(it.get("Cgo2") as String?)
            binding.cgo3.setText(it.get("Cgo3") as String?)
            binding.otCF1.setText(it.get("OtCF1") as String?)
            binding.otCF2.setText(it.get("OtCF2") as String?)
            binding.otCF3.setText(it.get("OtCF3") as String?)
            binding.totComp1.setText(it.get("TotComp1") as String?)
            binding.totComp2.setText(it.get("TotComp2") as String?)
            binding.totComp3.setText(it.get("TotComp3") as String?)
            binding.totCF1.setText(it.get("TotCF1") as String?)
            binding.totCF2.setText(it.get("TotCF2") as String?)
            binding.totCF3.setText(it.get("TotCF3") as String?)
            binding.saldFF1.setText(it.get("SaldFF1") as String?)
            binding.saldFF2.setText(it.get("SaldFF2") as String?)
            binding.saldFF3.setText(it.get("SaldFF3") as String?)
            binding.saldMA1.setText(it.get("SaldMA1") as String?)
            binding.saldMA2.setText(it.get("SaldMA2") as String?)
            binding.saldMA3.setText(it.get("SaldMA3") as String?)
            binding.totFinal1.setText(it.get("Tot1") as String?)
            binding.totFinal2.setText(it.get("Tot2") as String?)
            binding.totFinal3.setText(it.get("Tot3") as String?)



        }
    }
    //metodo para evitar errores si se deja campo vacio
    private fun validarCampos(){

        if(binding.etVentas1.text.toString().length==0){
            binding.etVentas1.setText("0.0")
        }else{}
        if(binding.etVentas2.text.toString().length==0){
            binding.etVentas2.setText("0.0")
        }else{}
        if(binding.etVentas3.text.toString().length==0){
            binding.etVentas3.setText("0.0")
        }else{}
        if(binding.Inco1.text.toString().length==0){
            binding.Inco1.setText("0.0")
        }else{}
        if(binding.Inco2.text.toString().length==0){
            binding.Inco2.setText("0.0")
        }else{}
        if(binding.Inco3.text.toString().length==0){
            binding.Inco3.setText("0.0")
        }else{}
        if(binding.vaf1.text.toString().length==0){
            binding.vaf1.setText("0.0")
        }else{}
        if(binding.vaf2.text.toString().length==0){
            binding.vaf2.setText("0.0")
        }else{}
        if(binding.vaf3.text.toString().length==0){
            binding.vaf3.setText("0.0")
        }else{}
        if(binding.alq1.text.toString().length==0){
            binding.alq1.setText("0.0")
        }else{}
        if(binding.alq2.text.toString().length==0){
            binding.alq2.setText("0.0")
        }else{}
        if(binding.alq3.text.toString().length==0){
            binding.alq3.setText("0.0")
        }else{}
        if(binding.ot1.text.toString().length==0){
            binding.ot1.setText("0.0")
        }else{}
        if(binding.ot2.text.toString().length==0){
            binding.ot2.setText("0.0")
        }else{}
        if(binding.ot3.text.toString().length==0){
            binding.ot3.setText("0.0")
        }else{}
        if(binding.compMerc1.text.toString().length==0){
            binding.compMerc1.setText("0.0")
        }else{}
        if(binding.compMerc2.text.toString().length==0){
            binding.compMerc2.setText("0.0")
        }else{}
        if(binding.compMerc3.text.toString().length==0){
            binding.compMerc3.setText("0.0")
        }else{}
        if(binding.inCoCF1.text.toString().length==0){
            binding.inCoCF1.setText("0.0")
        }else{}
        if(binding.inCoCF2.text.toString().length==0){
            binding.inCoCF2.setText("0.0")
        }else{}
        if(binding.inCoCF3.text.toString().length==0){
            binding.inCoCF3.setText("0.0")
        }else{}

        if(binding.coAF1.text.toString().length==0){
            binding.coAF1.setText("0.0")
        }else{}
        if(binding.coAF2.text.toString().length==0){
            binding.coAF2.setText("0.0")
        }else{}
        if(binding.coAF3.text.toString().length==0){
            binding.coAF3.setText("0.0")
        }else

        if(binding.sub1.text.toString().length==0){
                binding.sub1.setText("0.0")
        }else{}
        if(binding.sub2.text.toString().length==0){
            binding.sub2.setText("0.0")
        }else{}
        if(binding.sub3.text.toString().length==0){
            binding.sub3.setText("0.0")
        }else{}
        if(binding.cgo1.text.toString().length==0){
            binding.cgo1.setText("0.0")
        }else{}
        if(binding.cgo2.text.toString().length==0){
            binding.cgo2.setText("0.0")
        }else{}
        if(binding.cgo3.text.toString().length==0){
            binding.cgo3.setText("0.0")
        }else{}
        if(binding.otCF1.text.toString().length==0){
            binding.otCF1.setText("0.0")
        }else{}
        if(binding.otCF2.text.toString().length==0){
            binding.otCF2.setText("0.0")
        }else{}
        if(binding.otCF3.text.toString().length==0){
            binding.otCF3.setText("0.0")
        }else{}
        if(binding.totComp1.text.toString().length==0){
            binding.totComp1.setText("0.0")
        }else{}
        if(binding.totComp2.text.toString().length==0){
            binding.totComp2.setText("0.0")
        }else{}
        if(binding.totComp3.text.toString().length==0){
            binding.totComp3.setText("0.0")
        }else{}
        if(binding.totCF1.text.toString().length==0){
            binding.totCF1.setText("0.0")
        }else{}
        if(binding.totCF2.text.toString().length==0){
            binding.totCF2.setText("0.0")
        }else{}
        if(binding.totCF3.text.toString().length==0){
            binding.totCF3.setText("0.0")
        }else{}
        if(binding.saldFF1.text.toString().length==0){
            binding.saldFF1.setText("0.0")
        }else{}
        if(binding.saldFF2.text.toString().length==0){
            binding.saldFF2.setText("0.0")
        }else{}
        if(binding.saldFF3.text.toString().length==0){
            binding.saldFF3.setText("0.0")
        }else{}
        if(binding.saldMA1.text.toString().length==0){
            binding.saldMA1.setText("0.0")
        }else{}
        if(binding.saldMA2.text.toString().length==0){
            binding.saldMA2.setText("0.0")
        }else{}
        if(binding.saldMA3.text.toString().length==0){
            binding.saldMA3.setText("0.0")
        }else{}
        if(binding.totFinal1.text.toString().length==0){
            binding.totFinal1.setText("0.0")
        }else{}
        if(binding.totFinal2.text.toString().length==0){
            binding.totFinal2.setText("0.0")
        }else{}
        if(binding.totFinal3.text.toString().length==0){
            binding.totFinal3.setText("0.0")
        }else{}
    }
    companion object {

        @JvmStatic
        fun newInstance() = IvaFragment()
    }

    }



