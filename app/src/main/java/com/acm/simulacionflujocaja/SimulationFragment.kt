package com.acm.simulacionflujocaja

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm.simulacionflujocaja.databinding.FragmentFlujoBinding

import com.acm.simulacionflujocaja.databinding.FragmentSimulationBinding



class SimulationFragment : Fragment(R.layout.fragment_simulation) {
    private var _binding: FragmentSimulationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("Simulacion")
        binding.btnTri.setOnClickListener {

        }
        return view
    }


}