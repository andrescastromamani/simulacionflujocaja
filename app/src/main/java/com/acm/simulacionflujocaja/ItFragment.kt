package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentItBinding

class ItFragment : Fragment(R.layout.fragment_it) {
    var displayMessage: String?= ""
    private var _binding: FragmentItBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItBinding.inflate(inflater, container, false)
        val view = binding.root



        displayMessage= arguments?.getString("message")
       // val a:Int= Integer.parseInt(displayMessage)
        _binding!!.mes1.text =displayMessage
        displayMessage= arguments?.getString("a")
        //val b:Int= Integer.parseInt(displayMessage)
        _binding!!.mes2.text =displayMessage
       // val res:Int=a*b
        displayMessage= arguments?.getString("b")
        _binding!!.mes3.text =displayMessage

        return view
    }

}