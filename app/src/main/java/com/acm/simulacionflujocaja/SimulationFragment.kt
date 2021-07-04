package com.acm.simulacionflujocaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.acm.simulacionflujocaja.databinding.FragmentSimulationBinding
import com.acm.simulacionflujocaja.databinding.FragmentPresupuestoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_iva.*
import java.lang.Double.parseDouble
import kotlin.math.abs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SimulationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SimulationFragment : Fragment(R.layout.fragment_simulation) {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentSimulationBinding? = null
    private val binding get() = _binding!!
    val r :RedondeoDecimal= RedondeoDecimal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("Simulacion Semestral")
        _binding = FragmentSimulationBinding.inflate(inflater, container, false)
        val view = binding.root
        recuperarSimSemestral()
        recDatosEntradas()

        binding.btnSaveIT.setOnClickListener {
            saveTotalesSimSem()
        }
        //recupera los meses que esta en bd y establece como texto en los textView
        //recupera de la bd y manda la informacion a las ventas contado
        return view
    }

    private fun saveTotalesSimSem() {//calcula total 
        val input1:Double= parseDouble(binding.totalMes1.text.toString())
        val input2:Double= parseDouble(binding.totalMes2.text.toString())
        val input3:Double= parseDouble(binding.totalMes3.text.toString())

        val salid1:Double=parseDouble(binding.detalle24.text.toString())
        val salid2:Double=parseDouble(binding.detalle25.text.toString())
        val salid3:Double=parseDouble(binding.detalle25.text.toString())
        val desvSalida: Double = r.redondear(itPeriod1 - iue1)
        binding.SaDff1.setText(totFavFoC1.toString())
        val totFavFoC2: Double = r.redondear(itPeriod2 - iue2)
        binding.SaDff2.setText(totFavFoC2.toString())
        val iue3: Double = abs(totFavFoC2)
        binding.detalle26.setText(iue3.toString())
        val totFavFoC3: Double = r.redondear(itPeriod3 - iue3)
        binding.SaDff3.setText(totFavFoC3.toString())
        //GUARDA EN BD TODAS LA ENTRADAS Y SUS CALCULOS
        db.collection("Users").document(user?.email.toString()).collection("IT").document("DatosIt")
            .set(
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
                )
            )
        }

    //Recupera de inputs ventas al contado y los intereses si es que los hubiera
    private fun recDatosEntradas(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Inputs").get().addOnSuccessListener {
            binding.itventas1.setText(it.get("Ventas contado mes 3") as String?)
            binding.itventas2.setText(it.get("Ventas contado mes 4") as String?)
            binding.itventas3.setText(it.get("Ventas contado mes 5") as String?)
        }
    }

    private fun recuperarSimSemestral(){
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