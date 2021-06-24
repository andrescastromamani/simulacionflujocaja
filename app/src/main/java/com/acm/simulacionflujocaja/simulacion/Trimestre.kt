package com.acm.simulacionflujocaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm.simulacionflujocaja.databinding.FragmentTrimestreBinding





import com.google.firebase.firestore.FirebaseFirestore

class Trimestre : Fragment(R.layout.fragment_trimestre) {

    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentTrimestreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("SUELDOS Y SALARIOS")
        _binding = FragmentTrimestreBinding.inflate(inflater, container, false)
        val view = binding.root

        return view


    }
    companion object {


    }

}