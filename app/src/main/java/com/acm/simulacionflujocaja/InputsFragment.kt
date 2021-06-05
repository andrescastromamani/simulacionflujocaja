package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding

class InputsFragment : Fragment(R.layout.fragment_inputs) {

    private var _binding: FragmentInputsBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: Communicator


    override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

        //_binding?.ptMesH1?.setText("holaprueba")


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputsBinding.inflate(inflater, container, false)
        val view = binding.root
        communicator=activity as Communicator
        _binding!!.btnSend.setOnClickListener {
            communicator.passDataCom(_binding!!.ptMesH1.text.toString(),_binding!!.ptMesH2.text.toString(),_binding!!.ptMesP1.text.toString())

        }
        return view





    }

}