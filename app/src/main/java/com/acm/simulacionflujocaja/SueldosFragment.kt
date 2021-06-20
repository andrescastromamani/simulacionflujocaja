package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding
import com.acm.simulacionflujocaja.databinding.FragmentIvaBinding
import com.acm.simulacionflujocaja.databinding.FragmentSueldosBinding
import com.google.firebase.firestore.FirebaseFirestore

class SueldosFragment : Fragment(R.layout.fragment_sueldos) {

    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentSueldosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("SUELDOS Y SALARIOS")
        _binding = FragmentSueldosBinding.inflate(inflater, container, false)
        val view = binding.root

        return view


    }
    companion object {

        @JvmStatic
        fun newInstance() = SueldosFragment()
    }

}