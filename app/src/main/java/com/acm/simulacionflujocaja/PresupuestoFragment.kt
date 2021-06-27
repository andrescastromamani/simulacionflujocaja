package com.acm.simulacionflujocaja
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm.simulacionflujocaja.databinding.FragmentPresupuestoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Double.parseDouble

class PresupuestoFragment : Fragment(R.layout.fragment_presupuesto) {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    val r :RedondeoDecimal= RedondeoDecimal()
    private var _binding: FragmentPresupuestoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("Presupuesto de caja")
        _binding = FragmentPresupuestoBinding.inflate(inflater, container, false)

        val view = binding.root
        val nr:aleatorioUniforme=aleatorioUniforme()
        for(num in 1..10){
            println(nr.getRandom())}

        recuperarTodoPresupuesto()
        recDatosMeses()
        calculoTotalEntradas()
        recDatosSueldosInputs()
        recDatosIVA()
        recDatosIT()
        recDatosIUE()
        

        binding.btnSavePreCaja.setOnClickListener {
            //validarCampos()
            saveTotalesPresupuesto()
        }

        return view
    }

    private fun recDatosMeses(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").get().addOnSuccessListener {
            binding.mes1.setText(it.get("Mes 3") as String?)
            binding.mes2.setText(it.get("Mes 4") as String?)
            binding.mes3.setText(it.get("Mes 5") as String?)

        } }
    private fun recDatosSueldosInputs(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("SueldosMes").get().addOnSuccessListener {
            binding.editText19.setText(it.get("sueldo1") as String?)
            binding.editText20.setText(it.get("sueldo2") as String?)
            binding.editText21.setText(it.get("sueldo3") as String?)
            val sueldo_1:Double= parseDouble(it.get("sueldo1") as String?)
            val sueldo_2:Double= parseDouble(it.get("sueldo2") as String?)
            val sueldo_3:Double= parseDouble(it.get("sueldo3") as String?)
            val totalSueldos:Double=r.redondear(sueldo_1+sueldo_2+sueldo_3)
            binding.totalv7.setText(totalSueldos.toString())

        } }

    private fun recDatosIVA(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("DatosIva").get().addOnSuccessListener {
            binding.textView6.setText(it.get("Tot1") as String?)
            binding.textView7.setText(it.get("Tot2") as String?)

            val iva2:Double= r.redondear(parseDouble(it.get("Tot1") as String?))
            val iva3:Double= r.redondear(parseDouble(it.get("Tot2") as String?))


        } }



    private fun recDatosIT(){
        db.collection("Users").document(email.toString()).collection("IT").document("DatosIt").get().addOnSuccessListener {
            binding.textView9.setText(it.get("totFavFoC1") as String?)

            if(parseDouble(it.get("totFavFoC2") as String?)<=0.0)
            {
                binding.textView10.setText("0.0")
            }
            else{
                binding.textView10.setText(it.get("totFavFoC2") as String?)
            }
        } }

    private fun recDatosIUE(){
        db.collection("Users").document(email.toString()).collection("Iue").document("DatosIUE").get().addOnSuccessListener {
            binding.textView11.setText(it.get("iuePorPagar") as String?)
            binding.textView13.setText("0.0")
            binding.textView14.setText("0.0")
            val iue1:Double= parseDouble(it.get("iuePorPagar") as String?)
            val iue2:Double=0.0
            val iue3:Double=0.0
            val totalIue:Double=iue1+iue2+iue3
            binding.totalv14.setText(totalIue.toString())
            //val totalIUE:Double=parseDouble(it.get("1") as String?)

        }

    }
    private fun calculoTotalEntradas(){ //RECUPERA DATOS DE INPUTS Y HACE CALCULOS DE LOS TOTALES DE LAS ENTRADAS
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Inputs").get().addOnSuccessListener {


            binding.ventas1.setText(it.get("Ventas contado mes 3") as String?)
            binding.ventas2.setText(it.get("Ventas contado mes 4") as String?)
            binding.ventas3.setText(it.get("Ventas contado mes 5") as String?)


            binding.editText4.setText(it.get("Recuperacion 30 dias mes 3") as String?)
            binding.editText5.setText(it.get("Recuperacion 30 dias mes 4") as String?)
            binding.editText6.setText(it.get("Recuperacion 30 dias mes 5") as String?)

            binding.editText7.setText(it.get("Recuperacion 60 dias mes 3") as String?)
            binding.editText8.setText(it.get("Recuperacion 60 dias mes 4") as String?)
            binding.editText9.setText(it.get("Recuperacion 60 dias mes 5") as String?)

            val ventas1:Double?= parseDouble((it.get("Ventas contado mes 3")).toString())
            val ventas2:Double?= parseDouble((it.get("Ventas contado mes 4")).toString())
            val ventas3:Double?= parseDouble((it.get("Ventas contado mes 5")).toString())
            val totalVentas:Double= ventas1!! + ventas2!! + ventas3!!
            binding.totalven.text = r.redondear(totalVentas).toString()

            val ventas30d1:Double?= parseDouble((it.get("Recuperacion 30 dias mes 3")).toString())
            val ventas30d2:Double?= parseDouble((it.get("Recuperacion 30 dias mes 4")).toString())
            val ventas30d3:Double?= parseDouble((it.get("Recuperacion 30 dias mes 5")).toString())
            val totalRec30d:Double= ventas30d1!! + ventas30d2!! + ventas30d3!!
            binding.totalv2.text = r.redondear(totalRec30d).toString()
            val ventas60d1:Double= parseDouble((it.get("Recuperacion 60 dias mes 3")).toString())
            val ventas60d2:Double= parseDouble((it.get("Recuperacion 60 dias mes 4")).toString())
            val ventas60d3:Double= parseDouble((it.get("Recuperacion 60 dias mes 5")).toString())
            val totalRec60d:Double= ventas60d1 + ventas60d2 + ventas60d3
            binding.totalv3.text = r.redondear(totalRec60d).toString()

            val totalMes1:Double= ventas1!! + ventas30d1!! + ventas60d1!!
            val totalMes2:Double= ventas2!! + ventas30d2!! + ventas60d2!!
            val totalMes3:Double= ventas3!! + ventas30d3!! + totalRec60d!!
            val total:Double=totalMes1+totalMes2+totalMes3
            binding.textView.text = r.redondear(totalMes1).toString()
            binding.textView2.text = r.redondear(totalMes2).toString()
            binding.textView3.text = r.redondear(totalMes3).toString()
            binding.totalv4.text = r.redondear(total).toString()

        }

    }
    fun saveTotalesPresupuesto(){//calcula y guarda todo de presupuesto de caja

        // CALCULO TOTAL SALIDAS

        val totEntrada1:Double= parseDouble(binding.textView.text.toString())
        val totEntrada2:Double= parseDouble(binding.textView2.text.toString())
        val totEntrada3:Double= parseDouble(binding.textView3.text.toString())
        val totTotalEntradas:Double=parseDouble(binding.totalv4.text.toString())

        val compras1:Double= parseDouble(binding.editText16.text.toString())
        val compras2:Double= parseDouble(binding.editText17.text.toString())
        val compras3:Double= parseDouble(binding.editText18.text.toString())
        val totCompras:Double= compras1+compras2+compras3//total compras
        val toComprasAux=totCompras
        binding.totalv6.setText(totCompras.toString())
        val sueldos1:Double= parseDouble(binding.editText19.text.toString())
        val sueldos2:Double= parseDouble(binding.editText20.text.toString())
        val sueldos3:Double= parseDouble(binding.editText21.text.toString())
        val totalSueldosySal:Double= parseDouble(binding.totalv7.text.toString())//total sueldos y salarios
        val totalSueldosySalAux=totalSueldosySal
        //sueldos y salarios

        //Totals costos y gastos
        val costosyGast1:Double=parseDouble(binding.editText31.text.toString())
        val costosyGast2:Double=parseDouble(binding.editText32.text.toString())
        val costosyGast3:Double=parseDouble(binding.editText33.text.toString())
        val totalCostoyGasto:Double=r.redondear(costosyGast1+costosyGast2+costosyGast3)
        val totalCostoyGastoAux=totalCostoyGasto
        binding.totalv11.setText(totalCostoyGasto.toString())
        //total iva
        val iva1:Double=parseDouble(binding.textView4.text.toString())
        val iva2:Double=parseDouble(binding.textView6.text.toString())
        val iva3:Double=parseDouble(binding.textView7.text.toString())
        val totalIVA:Double=r.redondear(iva1+iva2+iva3)
        val totalIVAAux=totalIVA
        binding.totalv12.setText(totalIVA.toString())
        //total it
        val it1:Double=parseDouble(binding.textView8.text.toString())
        val it2:Double=parseDouble(binding.textView9.text.toString())
        val it3:Double=parseDouble(binding.textView10.text.toString())
        val totalIT:Double=r.redondear(it1+it2+it3)
        val totalITAux=totalIT
        binding.totalv13.setText(totalIT.toString())
        //total iue
        val iue1:Double=parseDouble(binding.textView11.text.toString())
        val iue2:Double=parseDouble(binding.textView13.text.toString())
        val iue3:Double=parseDouble(binding.textView14.text.toString())
        val totalIUE:Double=r.redondear(iue1+iue2+iue3)
        val totalIUEAux=totalIUE
        binding.totalv14.setText(totalIUE.toString())
        //total otros impuestos
        val otIm1:Double=parseDouble(binding.editText43.text.toString())
        val otIm2:Double=parseDouble(binding.editText44.text.toString())
        val otIm3:Double=parseDouble(binding.editText45.text.toString())
        val totalOtrosImp:Double=r.redondear(otIm1+otIm2+otIm3)
        val totalOtrosImpAux=totalOtrosImp
        binding.totalv15.setText(totalOtrosImp.toString())

        //calculo total salidas
        val totalSalidas1:Double=r.redondear(compras1+sueldos1+costosyGast1+iva1+it1+iue1+otIm1)
        val totalSalidas2:Double=r.redondear(compras2+sueldos2+costosyGast2+iva2+it2+iue2+otIm2)
        val totalSalidas3:Double=r.redondear(compras3+sueldos3+costosyGast3+iva3+it3+iue3+otIm3)
        val totalTotalSalidas:Double=r.redondear(toComprasAux+totalSueldosySalAux+totalCostoyGastoAux+totalIVAAux+totalITAux+totalIUEAux+totalOtrosImpAux)
        binding.textView15.setText(totalSalidas1.toString())
        binding.textView16.setText(totalSalidas2.toString())
        binding.textView42.setText(totalSalidas3.toString())
        binding.totalv16.setText(totalTotalSalidas.toString())

        //calculo total flujo neto sin financiamiento
        val flujoNeto1:Double=r.redondear(totEntrada1-totalSalidas1)
        binding.textView43.setText(flujoNeto1.toString())
        val flujoNeto2:Double=r.redondear(totEntrada2-totalSalidas2)
        binding.textView44.setText(flujoNeto2.toString())
        val flujoNeto3:Double=r.redondear(totEntrada3-totalSalidas3)
        binding.textView45.setText(flujoNeto3.toString())
        val totalFlujoNeto:Double=r.redondear(totTotalEntradas-totalTotalSalidas)
        binding.totalv17.setText(totalFlujoNeto.toString())

        //calculo total saldo final mes anterior
        val saldoAnt1:Double= parseDouble(binding.textView46.text.toString())
        val saldoAnt2:Double=r.redondear(saldoAnt1+flujoNeto1)
        binding.textView47.setText(saldoAnt2.toString())
        val saldoAnt3:Double=r.redondear(saldoAnt2+flujoNeto2)
        binding.textView48.setText(saldoAnt3.toString())
        val totSaldoMesAnterior:Double=r.redondear(saldoAnt1+saldoAnt2+saldoAnt3)
        binding.totalv18.setText(totSaldoMesAnterior.toString())
        //calculo total final efectivo proyectado sin financiamiento
        val saldoFinal1=r.redondear(flujoNeto1+saldoAnt1)
        binding.textView49.setText(saldoFinal1.toString())
        val saldoFinal2=r.redondear(flujoNeto2+saldoAnt2)
        binding.textView50.setText(saldoFinal2.toString())
        val saldoFinal3=r.redondear(flujoNeto3+saldoAnt3)
        binding.textView51.setText(saldoFinal3.toString())

        val saldoTotalFinalSF=r.redondear(totalFlujoNeto+totSaldoMesAnterior)
        binding.totalv19.setText(saldoTotalFinalSF.toString())


        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("DatosPresupuesto").set(
            hashMapOf(
                "Compras1" to binding.editText16.text.toString(),
                "Compras2" to binding.editText17.text.toString(),
                "Compras3" to binding.editText18.text.toString(),
                "totalCompra" to binding.totalv6.text.toString(),
                "costosyGast1" to binding.editText31.text.toString(),
                "costosyGast2" to binding.editText32.text.toString(),
                "costosyGast3" to binding.editText33.text.toString(),
                "totalCostoyGasto" to binding.totalv11.text.toString(),
                "SueldoConIncSal" to  binding.editText21.text.toString(),
                "totalEntradas" to binding.totalv4.text.toString(),  //<---total entradas
                "iva1" to binding.textView4.text.toString(),
                "totalIVA" to binding.totalv12.text.toString(),
                "it1" to binding.textView8.text.toString(),
                "totalIT" to binding.totalv13.text.toString(),
                "otIm1" to binding.editText43.text.toString(),
                "otIm2" to binding.editText44.text.toString(),
                "otIm3" to binding.editText45.text.toString(),
                "totalOtrosImp" to binding.totalv15.text.toString(),
                "totalSalidas1" to binding.textView15.text.toString(),
                "totalSalidas2" to binding.textView16.text.toString(),
                "totalSalidas3" to binding.textView42.text.toString(),
                "totalTotalSalidas" to binding.totalv16.text.toString(),
                "flujoNeto1" to binding.textView43.text.toString(),
                "flujoNeto2" to binding.textView44.text.toString(),
                "flujoNeto3" to binding.textView45.text.toString(),
                "totalFlujoNeto" to binding.totalv17.text.toString(),
                "saldoAnt1" to binding.textView46.text.toString(),
                "saldoAnt2" to binding.textView47.text.toString(),
                "saldoAnt3" to binding.textView48.text.toString(),
                "totSaldoMesAnterior" to binding.totalv18.text.toString(),
                "saldoFinal1" to binding.textView49.text.toString(),
                "saldoFinal2" to binding.textView50.text.toString(),
                "saldoFinal3" to binding.textView51.text.toString(),
                "saldoTotalFinalSF" to binding.totalv19.text.toString(),


            ))

    }
    private fun recuperarTodoPresupuesto(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("DatosPresupuesto").get().addOnSuccessListener {

            binding.editText16.setText(it.get("Compras1") as String?)
            binding.editText17.setText(it.get("Compras2") as String?)
            binding.editText18.setText(it.get("Compras3") as String?)
            binding.totalv6.setText(it.get("totalCompra") as String?)
            binding.totalv4.setText(it.get("totalEntradas") as String?)//<----
            binding.editText31.setText(it.get("costosyGast1") as String?)
            binding.editText32.setText(it.get("costosyGast2") as String?)
            binding.editText33.setText(it.get("costosyGast3") as String?)
            binding.totalv11.setText(it.get("totalCostoyGasto") as String?)
            binding.textView4.setText(it.get("iva1") as String?)
            binding.totalv12.setText(it.get("totalIVA") as String?)
            binding.textView8.setText(it.get("it1") as String?)
            binding.totalv13.setText(it.get("totalIT") as String?)
            binding.editText43.setText(it.get("otIm1") as String?)
            binding.editText44.setText(it.get("otIm2") as String?)
            binding.editText45.setText(it.get("otIm3") as String?)
            binding.totalv15.setText(it.get("totalOtrosImp") as String?)
            binding.textView15.setText(it.get("totalSalidas1") as String?)
            binding.textView16.setText(it.get("totalSalidas2") as String?)
            binding.textView42.setText(it.get("totalSalidas3") as String?)
            binding.totalv16.setText(it.get("totalTotalSalidas") as String?)
            binding.textView43.setText(it.get("flujoNeto1") as String?)
            binding.textView44.setText(it.get("flujoNeto2") as String?)
            binding.textView45.setText(it.get("flujoNeto3") as String?)
            binding.totalv17.setText(it.get("totalFlujoNeto") as String?)
            binding.textView46.setText(it.get("saldoAnt1") as String?)
            binding.textView47.setText(it.get("saldoAnt2") as String?)
            binding.textView48.setText(it.get("saldoAnt3") as String?)
            binding.totalv18.setText(it.get("totSaldoMesAnterior") as String?)
            binding.textView49.setText(it.get("saldoFinal1") as String?)
            binding.textView50.setText(it.get("saldoFinal2") as String?)
            binding.textView51.setText(it.get("saldoFinal3") as String?)
            binding.totalv19.setText(it.get("saldoTotalFinalSF") as String?)
        }}
    private fun validarCampos(){

        if(binding.editText16.text.toString().length==0){
            binding.editText16.setText("0.0")
        }else{}
        if(binding.editText17.text.toString().length==0){
            binding.editText17.setText("0.0")
        }else{}
        if(binding.editText18.text.toString().length==0){
            binding.editText18.setText("0.0")
        }else{}
        if(binding.editText19.text.toString().length==0){
            binding.editText19.setText("0.0")
        }else{}
        if(binding.editText20.text.toString().length==0){
            binding.editText20.setText("0.0")
        }else{}
        if(binding.editText21.text.toString().length==0){
            binding.editText21.setText("0.0")
        }else{}
        if(binding.editText22.text.toString().length==0){
            binding.editText22.setText("0.0")
        }else{}
        if(binding.editText23.text.toString().length==0){
            binding.editText23.setText("0.0")
        }else{}
        if(binding.editText24.text.toString().length==0){
            binding.editText24.setText("0.0")
        }else{}
        if(binding.editText25.text.toString().length==0){
            binding.editText25.setText("0.0")
        }else{}
        if(binding.editText26.text.toString().length==0){
            binding.editText26.setText("0.0")
        }else{}
        if(binding.editText27.text.toString().length==0){
            binding.editText27.setText("0.0")
        }else{}
        if(binding.editText28.text.toString().length==0){
            binding.editText28.setText("0.0")
        }else{}
        if(binding.editText29.text.toString().length==0){
            binding.editText29.setText("0.0")
        }else{}
        if(binding.editText30.text.toString().length==0){
            binding.editText30.setText("0.0")
        }else{}
        if(binding.editText31.text.toString().length==0){
            binding.editText31.setText("0.0")
        }else{}
        if(binding.editText32.text.toString().length==0){
            binding.editText32.setText("0.0")
        }else{}
        if(binding.editText33.text.toString().length==0){
            binding.editText33.setText("0.0")
        }else{}
        if(binding.textView4.text.toString().length==0){
            binding.textView4.setText("0.0")
        }else{}
        if(binding.textView8.text.toString().length==0){
            binding.textView8.setText("0.0")
        }else{}
        if(binding.editText43.text.toString().length==0){
            binding.editText43.setText("0.0")
        }else{}

        if(binding.editText44.text.toString().length==0){
            binding.editText44.setText("0.0")
        }else{}
        if(binding.editText45.text.toString().length==0){
            binding.editText45.setText("0.0")
        }else{}
        if(binding.editText58.text.toString().length==0){
            binding.editText58.setText("0.0")
        }else

            if(binding.editText59.text.toString().length==0){
                binding.editText59.setText("0.0")
            }else{}
        if(binding.editText60.text.toString().length==0){
            binding.editText60.setText("0.0")
        }else{}
        if(binding.editText61.text.toString().length==0){
            binding.editText61.setText("0.0")
        }else{}
        if(binding.editText62.text.toString().length==0){
            binding.editText62.setText("0.0")
        }else{}
        if(binding.editText63.text.toString().length==0){
            binding.editText63.setText("0.0")
        }else{}
        if(binding.editText64.text.toString().length==0){
            binding.editText64.setText("0.0")
        }else{}
        if(binding.editText65.text.toString().length==0){
            binding.editText65.setText("0.0")
        }else{}
        if(binding.editText66.text.toString().length==0){
            binding.editText66.setText("0.0")
        }else{}

    }

    companion object {

        @JvmStatic
        fun newInstance() = PresupuestoFragment()
    }
}