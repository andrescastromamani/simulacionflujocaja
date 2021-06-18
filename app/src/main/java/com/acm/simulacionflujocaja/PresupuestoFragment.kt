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
        recDatosInputs()
        calculoTotalesEntrada()


        /*val presuMes1:Double= parseDouble(arguments?.getString("c1"))
        val presuMes2:Double= parseDouble(arguments?.getString("c2"))
        val presuMes3:Double= parseDouble(arguments?.getString("c3"))
        val r30d1:Double= parseDouble(arguments?.getString("r30d1"))
        val r30d2:Double= parseDouble(arguments?.getString("r30d2"))
        val r30d3:Double= parseDouble(arguments?.getString("r30d3"))
        val r60d1:Double= parseDouble(arguments?.getString("r60d1"))
        val r60d2:Double= parseDouble(arguments?.getString("r60d2"))
        val r60d3:Double= parseDouble(arguments?.getString("r60d3"))
        val totalmes1:Double=r.redondear(presuMes1+r30d1+r60d1)
        _binding!!.textView.setText(totalmes1.toString())*/
        //entradasEfectivo()







        return view
    }
    private fun recDatosMeses(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").get().addOnSuccessListener {
            binding.mes1.setText(it.get("Mes 3") as String?)
            binding.mes2.setText(it.get("Mes 4") as String?)
            binding.mes3.setText(it.get("Mes 5") as String?)

        } }


    private fun recDatosInputs(){
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

        }


    }






    private fun calculoTotalesEntrada(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Calculo").get().addOnSuccessListener {


            val ventas1:Double?= parseDouble((it.get("VentasMes1")).toString())
            val ventas2:Double?= parseDouble((it.get("VentasMes2")).toString())
            val ventas3:Double?= parseDouble((it.get("VentasMes3")).toString())
            val r30d1:Double?= parseDouble((it.get("rec30dias1")).toString())
            val r30d2:Double?= parseDouble((it.get("rec30dias2")).toString())
            val r30d3:Double?= parseDouble((it.get("rec30dias3")).toString())
            val r60d1:Double?= parseDouble((it.get("rec60dias1")).toString())
            val r60d2:Double?= parseDouble((it.get("rec60dias2")).toString())
            val r60d3:Double?= parseDouble((it.get("rec60dias3")).toString())
            val totalMes1:Double= ventas1!! + r30d1!! + r60d1!!
            val totalMes2:Double= ventas2!! + r30d2!! + r60d2!!
            val totalMes3:Double= ventas3!! + r30d3!! + r60d3!!
            val total:Double=totalMes1+totalMes2+totalMes3
            binding.textView.text = r.redondear(totalMes1).toString()
            binding.textView2.text = r.redondear(totalMes2).toString()
            binding.textView3.text = r.redondear(totalMes3).toString()
            binding.totalv4.text = r.redondear(total).toString()



    }}

    private fun salidas(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("OtrosDatos").get().addOnSuccessListener {


            val ventas1:Double?= parseDouble((it.get("VentasMes1")).toString())
            val ventas2:Double?= parseDouble((it.get("VentasMes2")).toString())
            val ventas3:Double?= parseDouble((it.get("VentasMes3")).toString())
            val r30d1:Double?= parseDouble((it.get("rec30dias1")).toString())
            val r30d2:Double?= parseDouble((it.get("rec30dias2")).toString())
            val r30d3:Double?= parseDouble((it.get("rec30dias3")).toString())
            val r60d1:Double?= parseDouble((it.get("rec60dias1")).toString())
            val r60d2:Double?= parseDouble((it.get("rec60dias2")).toString())
            val r60d3:Double?= parseDouble((it.get("rec60dias3")).toString())
            val totalMes1:Double= ventas1!! + r30d1!! + r60d1!!
            val totalMes2:Double= ventas2!! + r30d2!! + r60d2!!
            val totalMes3:Double= ventas3!! + r30d3!! + r60d3!!
            val total:Double=totalMes1+totalMes2+totalMes3
            binding.textView.text = r.redondear(totalMes1).toString()
            binding.textView2.text = r.redondear(totalMes2).toString()
            binding.textView3.text = r.redondear(totalMes3).toString()
            binding.totalv4.text = r.redondear(total).toString()



        }}




    companion object {

        @JvmStatic
        fun newInstance() = PresupuestoFragment()
    }
}