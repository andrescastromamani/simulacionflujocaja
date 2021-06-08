package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentIueBinding
import com.google.firebase.firestore.FirebaseFirestore

class IueFragment : Fragment(R.layout.fragment_iue) {
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentIueBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("IUE")
        _binding = FragmentIueBinding.inflate(inflater, container, false)
        val view = binding.root
     _binding!!.ptPorcentajeDiv.setText("0%")

        return view


    }



    companion object {

        @JvmStatic
        fun newInstance() = IueFragment()
    }

}
