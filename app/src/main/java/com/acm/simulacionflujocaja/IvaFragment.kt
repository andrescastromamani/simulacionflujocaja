package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding

import com.acm.simulacionflujocaja.databinding.FragmentIvaBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_iva.*

class IvaFragment : Fragment(R.layout.fragment_iva) {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentIvaBinding? = null
    private val binding get() = _binding!!


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

        recDatosMeses()
        recDatosIngresosBrutos()
        _binding!!.btnSaveIva.setOnClickListener {
            recDatosMeses()
            recDatosIngresosBrutos()}







        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() = IvaFragment()
    }
    private fun recDatosMeses(){
        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").get().addOnSuccessListener {
            binding.etmes1.setText(it.get("Mes 3") as String?)
            binding.etmes2.setText(it.get("Mes 4") as String?)
            binding.etmes3.setText(it.get("Mes 5") as String?)

        } }

    private fun recDatosIngresosBrutos(){
            db.collection("Users").document(email.toString()).collection("Entradas").document("IngresoBruto").get().addOnSuccessListener {
                binding.etVentas1.setText(it.get("IngresoBrutoMes3") as String?)
                binding.etVentas2.setText(it.get("IngresoBrutoMes4") as String?)
                binding.etVentas3.setText(it.get("IngresoBrutoMes5") as String?)
            } }

    }

