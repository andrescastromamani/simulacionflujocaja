package com.acm.simulacionflujocaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm.simulacionflujocaja.databinding.FragmentFlujoBinding
import com.acm.simulacionflujocaja.databinding.FragmentSueldosBinding
import com.google.firebase.firestore.FirebaseFirestore

class FlujoFragment : Fragment(R.layout.fragment_flujo) {
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentFlujoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("SUELDOS Y SALARIOS")
        _binding = FragmentFlujoBinding.inflate(inflater, container, false)
        val view = binding.root

        return view


    }
    companion object {

        @JvmStatic
        fun newInstance() = FlujoFragment()
    }
}