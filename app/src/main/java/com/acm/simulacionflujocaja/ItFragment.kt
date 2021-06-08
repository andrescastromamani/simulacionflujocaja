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



        displayMessage= arguments?.getString("mesp1")
       // val a:Int= Integer.parseInt(displayMessage)
        _binding!!.mes1.text =displayMessage
        displayMessage= arguments?.getString("mesp2")
        //val b:Int= Integer.parseInt(displayMessage)
        _binding!!.mes2.text =displayMessage
       // val res:Int=a*b
        displayMessage= arguments?.getString("mesp3")
        _binding!!.mes3.text =displayMessage


        return view
    }
    companion object {

        @JvmStatic
        fun newInstance() = ItFragment()
    }

}