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


        recDatosMeses()
        calculoTotalEntradas()
        recDatosIVA()
        recDatosSueldosInputs()
        //recupDatosInputYcalculo()


        return view
    }
    private fun saveInputsPresCaja(){

        binding.btnSavePreCaja.setOnClickListener {
            validarCampos()
        }
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
            binding.textView6.setText(it.get("SaldFF1") as String?)
            binding.textView7.setText(it.get("SaldFF2") as String?)

            val iva2:Double= r.redondear(parseDouble(it.get("SaldFF1") as String?))
            val iva3:Double= r.redondear(parseDouble(it.get("SaldFF2") as String?))


        } }



    private fun recDatosIT(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("IT").get().addOnSuccessListener {
            binding.textView9.setText(it.get("1") as String?)

            if(parseDouble(it.get("2") as String?)<=0.0)
            {
                binding.textView10.setText("0.0")
            }
            else{
                binding.textView10.setText(it.get("2") as String?)
            }
        } }

    private fun recDatosIUE(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("IUE").get().addOnSuccessListener {
            binding.textView11.setText(it.get("1") as String?)
            binding.textView13.setText("0.0")
            binding.textView14.setText("0.0")
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
            binding.totalv.text = r.redondear(totalVentas).toString()

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
        fun saveTotalesIVA(){//calcula y guarda total debito fiscal
            val porcIva:Double=0.13
            // CALCULO TOTAL SALIDAS
            val totEntrada1:Double= parseDouble(binding.textView.text.toString())
            val totEntrada2:Double= parseDouble(binding.textView2.text.toString())
            val totEntrada3:Double= parseDouble(binding.textView3.text.toString())

            val compras1:Double= parseDouble(binding.editText16.text.toString())
            val compras2:Double= parseDouble(binding.editText17.text.toString())
            val compras3:Double= parseDouble(binding.editText18.text.toString())
            val totCompras:Double= parseDouble(binding.totalv6.text.toString())

            /*val veAcFi1:Double= parseDouble(binding.vaf1.text.toString())
            val veAcFi2:Double= parseDouble(binding.vaf2.text.toString())
            val veAcFi3:Double= parseDouble(binding.vaf3.text.toString())
            val Alq1:Double= parseDouble(binding.alq1.text.toString())
            val Alq2:Double= parseDouble(binding.alq2.text.toString())
            val Alq3:Double= parseDouble(binding.alq3.text.toString())
            val otros1:Double= parseDouble(binding.ot1.text.toString())
            val otros2:Double= parseDouble(binding.ot2.text.toString())
            val otros3:Double= parseDouble(binding.ot3.text.toString())*/
        }


    }
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