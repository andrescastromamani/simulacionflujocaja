package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding
import com.google.firebase.firestore.FirebaseFirestore

class SueldosFragment : Fragment(R.layout.fragment_sueldos) {

    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentInputsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("SUELDOS Y SALARIOS")


        return view


    }
    companion object {

        @JvmStatic
        fun newInstance() = SueldosFragment()
    }

}