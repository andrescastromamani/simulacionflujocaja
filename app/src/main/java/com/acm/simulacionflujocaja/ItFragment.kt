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

        saveTotalesIT()
        recuperarTodoIT()
        recDatosMeses()//recupera los meses que esta en bd y establece como texto en los textView
        recDataVentCont()//recupera de la bd y manda la informacion a las ventas contado

        return view
    }

    private fun saveTotalesIT(){//calcula y guarda total debito fiscal
        val porcIt:Double=0.03
        // CALCULO PARA IT
        val ventasB1:Double= parseDouble(binding.Tventas1.text.toString())
        val ventasB2:Double= parseDouble(binding.Tventas2.text.toString())
        val ventasB3:Double= parseDouble(binding.Tventas3.text.toString())
        //total it
        val totalit1:Double=r.redondear(ventasB1*porcIt)
        val totalit2:Double=r.redondear(ventasB2*porcIt)
        val totalit3:Double=r.redondear(ventasB3*porcIt)
        binding.Itper1.setText(totalit1.toString())
        binding.Itper2.setText(totalit2.toString())
        binding.Itper3.setText(totalit3.toString())

        //GUARDA EN BD TODAS LA ENTRADAS Y SUS CALCULOS
        db.collection("Users").document(user?.email.toString()).collection("IT").document("DatosIt").set(
            hashMapOf(

                "Itper1" to binding.Itper1.text.toString(),
                "Itper2" to binding.Itper2.text.toString(),
                "Itper3" to binding.Itper3.text.toString(),
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

    private fun recuperarTodoIT(){
        db.collection("Users").document(user?.email.toString()).collection("IT").document("DatosIt").get().addOnSuccessListener {
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
            binding.Itper1.setText(it.get("Itper4") as String?)
            binding.Itper2.setText(it.get("Defis2") as String?)
            binding.Itper3.setText(it.get("Defis3") as String?)

            //binding.compMerc1.setText(it.get("CompMerc1") as String?)
            //binding.compMerc2.setText(it.get("CompMerc2") as String?)
            //binding.compMerc3.setText(it.get("CompMerc3") as String?)

            binding.SaDff1.setText(it.get("SaldFF1") as String?)
            binding.SaDff2.setText(it.get("SaldFF2") as String?)
            binding.SaDff3.setText(it.get("SaldFF3") as String?)
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() = ItFragment()
    }

}