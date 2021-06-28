package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.acm.simulacionflujocaja.databinding.FragmentItBinding
import com.acm.simulacionflujocaja.databinding.FragmentPresupuestoBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_iva.*
import java.lang.Double.parseDouble
import kotlin.math.abs


class ItFragment : Fragment(R.layout.fragment_it) {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentItBinding? = null
    private val binding get() = _binding!!
    val r :RedondeoDecimal= RedondeoDecimal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("IT")
        _binding = FragmentItBinding.inflate(inflater, container, false)
        val view = binding.root
        recuperarIT()
        recDatosMeses()
        recuperarDatosIVA()
        recuperarDatosIUE()
        binding.btnSaveIT.setOnClickListener {
            saveTotalesIT()
        }


       //recupera los meses que esta en bd y establece como texto en los textView
        recDataVentCont()//recupera de la bd y manda la informacion a las ventas contado

        return view
    }

    private fun saveTotalesIT(){//calcula total saldo definitivo a favor de contribuyente o fisco


        val itPeriod1:Double= parseDouble(binding.Itper1.text.toString())
        val itPeriod2:Double= parseDouble(binding.Itper2.text.toString())
        val itPeriod3:Double= parseDouble(binding.Itper3.text.toString())

        val iue1:Double=parseDouble(binding.detalle24.text.toString())
        val iue2:Double=parseDouble(binding.detalle25.text.toString())
        val totFavFoC1:Double=r.redondear(itPeriod1-iue1)
        binding.SaDff1.setText(totFavFoC1.toString())
        val totFavFoC2:Double=r.redondear(itPeriod2-iue2)
        binding.SaDff2.setText(totFavFoC2.toString())
        val iue3:Double=abs(totFavFoC2)
        binding.detalle26.setText(iue3.toString())
        val totFavFoC3:Double=r.redondear(itPeriod3-iue3)
        binding.SaDff3.setText(totFavFoC3.toString())



        //GUARDA EN BD TODAS LA ENTRADAS Y SUS CALCULOS
        db.collection("Users").document(user?.email.toString()).collection("IT").document("DatosIt").set(
            hashMapOf(

                "Itper1" to binding.Itper1.text.toString(),
                "Itper2" to binding.Itper2.text.toString(),
                "Itper3" to binding.Itper3.text.toString(),
                "iue1" to binding.detalle24.text.toString(),
                "iue2" to binding.detalle25.text.toString(),
                "iue3" to binding.detalle26.text.toString(),
                "totFavFoC1" to binding.SaDff1.text.toString(),
                "totFavFoC2" to binding.SaDff2.text.toString(),
                "totFavFoC3" to binding.SaDff3.text.toString()
            ))

    }
    private fun recDatosMeses(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").get().addOnSuccessListener {
            binding.mes1.setText(it.get("Mes 3") as String?)
            binding.mes2.setText(it.get("Mes 4") as String?)
            binding.mes3.setText(it.get("Mes 5") as String?)
        }
    }
    //Recupera de inputs ventas al contado y los intereses si es que los hubiera
    private fun recDataVentCont(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Inputs").get().addOnSuccessListener {
            binding.itventas1.setText(it.get("Ventas contado mes 3") as String?)
            binding.itventas2.setText(it.get("Ventas contado mes 4") as String?)
            binding.itventas3.setText(it.get("Ventas contado mes 5") as String?)
        }
    }

    private fun recuperarDatosIVA(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("DatosIva").get().addOnSuccessListener {
            binding.Incom1.setText(it.get("InCoDF1") as String?)
            binding.Incom2.setText(it.get("InCoDF2") as String?)
            binding.Incom3.setText(it.get("InCoDF3") as String?)
            binding.Vafi1.setText(it.get("VeAcFi1") as String?)
            binding.Vafi2.setText(it.get("VeAcFi2") as String?)
            binding.Vafi3.setText(it.get("VeAcFi3") as String?)
            binding.Alqu1.setText(it.get("Alq1") as String?)
            binding.Alqu2.setText(it.get("Alq2") as String?)
            binding.Alqu3.setText(it.get("Alq3") as String?)
            binding.Otros1.setText(it.get("otros1") as String?)
            binding.Otros2.setText(it.get("otros2") as String?)
            binding.Otros3.setText(it.get("otros3") as String?)
            binding.Tventas1.setText(it.get("tot1") as String?)
            binding.Tventas2.setText(it.get("tot2") as String?)
            binding.Tventas3.setText(it.get("tot3") as String?)


            val porcIT:Double=0.03
            val totVentasBYS1:Double= parseDouble(it.get("tot1") as String?)
            val totVentasBYS2:Double= parseDouble(it.get("tot2") as String?)
            val totVentasBYS3:Double= parseDouble(it.get("tot3") as String?)

            val TotItPer1:Double=r.redondear(porcIT*totVentasBYS1)
            val TotItPer2:Double=r.redondear(porcIT*totVentasBYS2)
            val TotItPer3:Double=r.redondear(porcIT*totVentasBYS3)

            binding.Itper1.setText(TotItPer1.toString())
            binding.Itper2.setText(TotItPer2.toString())
            binding.Itper3.setText(TotItPer3.toString())
        }
    }
    private fun recuperarDatosIUE(){
        db.collection("Users").document(user?.email.toString()).collection("Iue").document("DatosIUE").get().addOnSuccessListener {
            binding.detalle25.setText(it.get("iuePorPagar") as String?)
            binding.detalle24.setText("0.0")
        }

    }
    private fun recuperarIT(){
        db.collection("Users").document(user?.email.toString()).collection("IT").document("DatosIt").get().addOnSuccessListener {
            binding.detalle26.setText(it.get("iue3") as String?)
            binding.SaDff1.setText(it.get("totFavFoC1") as String?)
            binding.SaDff2.setText(it.get("totFavFoC2") as String?)
            binding.SaDff3.setText(it.get("totFavFoC3") as String?)

    }}
    companion object {

        @JvmStatic
        fun newInstance() = ItFragment()
    }

}