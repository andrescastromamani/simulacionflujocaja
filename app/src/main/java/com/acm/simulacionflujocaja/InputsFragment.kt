package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentInputsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Integer.parseInt


class InputsFragment : Fragment(R.layout.fragment_inputs) {

    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentInputsBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: Communicator
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)




    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("ENTRADAS")

        val user = FirebaseAuth.getInstance().currentUser
      /*  if (user != null) {
            val userEmail = user.email
        } else {
            // No user is signed in
        }*/


        _binding = FragmentInputsBinding.inflate(inflater, container, false)
        val view = binding.root
        communicator=activity as Communicator



        //val email: String? = arguments?.getString("email1")
        //println(email)
      // _binding!!.tvIngresoM2.setText()

        binding.btnSend.setOnClickListener {
            communicator.passDataMeses(binding.ptMesP1.text.toString(),binding.ptMesP2.text.toString(),binding.ptMesP3.text.toString())

        }
        GuardarVariables(user?.email.toString())
        return view


    }
    private fun GuardarVariables(email:String){
        binding.btnSave.setOnClickListener {
            datosMeses()
            datosVentasYPrecio()
            ingresoBruto()
            ventasContado()






            /*
                        /* db.collection("Users").document(email.toString()).collection("Entradas").document("IngresoBruto").set(
                             hashMapOf(
                                 "IngresoMes1" to binding.tvIngresoM1.setText(stringDouble).toString(),
                                 "IngresoMes2" to binding.tvIngresoM2.text.toString(),
                                 "IngresoMes3" to binding.tvIngresoM3.text.toString(),
                                 "IngresoMes4" to binding.tvIngresoM4.text.toString(),
                                 "IngresoMes5" to binding.tvIngresoM5.text.toString(),

                                 )
                         )*/


                    }
                    binding.btnRecuperar.setOnClickListener {
                        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").get().addOnSuccessListener {
                            binding.ptMesH1.setText(it.get("Mes 1") as String?)
                            binding.ptMesH2.setText(it.get("Mes 2") as String?)
                            binding.ptMesP1.setText(it.get("Mes 3") as String?)
                            binding.ptMesP2.setText(it.get("Mes 4") as String?)
                            binding.ptMesP3.setText(it.get("Mes 5") as String?)

                        }
                        db.collection("Users").document(email.toString()).collection("Entradas").document("Ventas").get().addOnSuccessListener {
                            binding.ptVentasH1.setText(it.get("VentasMes1") as String?)
                            binding.ptVentasH2.setText(it.get("VentasMes2") as String?)
                            binding.ptVentasP1.setText(it.get("VentasMes3") as String?)
                            binding.ptVentasP2.setText(it.get("VentasMes4") as String?)
                            binding.ptVentasP3.setText(it.get("VentasMes5") as String?)

                        }
                        db.collection("Users").document(email.toString()).collection("Entradas").document("PrecioUd").get().addOnSuccessListener {
                            binding.ptPrecioUdh1.setText(it.get("PrecioMes1") as String?)
                            binding.ptPrecioUdh2.setText(it.get("PrecioMes2") as String?)
                            binding.ptPrecioUdp1.setText(it.get("PrecioMes3") as String?)
                            binding.ptPrecioUdp2.setText(it.get("PrecioMes4") as String?)
                            binding.ptPrecioUdp3.setText(it.get("PrecioMes5") as String?)

                        }
                        /* db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").get().addOnSuccessListener {
                             binding.ptMesH1.setText(it.get("Mes 1") as String?)
                             binding.ptMesH2.setText(it.get("Mes 2") as String?)
                             binding.ptMesP1.setText(it.get("Mes 3") as String?)
                             binding.ptMesP2.setText(it.get("Mes 4") as String?)
                             binding.ptMesP3.setText(it.get("Mes 5") as String?)

                         }*/

                    }
                    binding.btnEliminarInputs.setOnClickListener {
                        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").delete()
                        db.collection("Users").document(email.toString()).collection("Entradas").document("Ventas").delete()
                        db.collection("Users").document(email.toString()).collection("Entradas").document("PrecioUd").delete()

                    }

                }

            /*companion object{
                fun getNewInstance(args: Bundle?):InputsFragment{
                    val inputsFragment =InputsFragment()
                    val bundle = Bundle()
                    inputsFragment.arguments = args
                    return inputsFragment
                }*/*/
}
    }
    private fun datosMeses(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Meses").set(
            hashMapOf(
                "Mes 1" to binding.ptMesH1.text.toString(),
                "Mes 2" to binding.ptMesH2.text.toString(),
                "Mes 3" to binding.ptMesP1.text.toString(),
                "Mes 4" to binding.ptMesP2.text.toString(),
                "Mes 5" to binding.ptMesP3.text.toString(),
                )) }
    private fun datosVentasYPrecio(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Ventas").set(
            hashMapOf(
                "VentasMes1" to binding.ptVentasH1.text.toString(),
                "VentasMes2" to  binding.ptVentasH2.text.toString(),
                "VentasMes3" to  binding.ptVentasP1.text.toString(),
                "VentasMes4" to  binding.ptVentasP2.text.toString(),
                "VentasMes5" to  binding.ptVentasP3.text.toString(),

                )
        )
        val ventasMes1:Double=binding.ptVentasH1.text.toString().toDouble()
        val ventasMes2:Double=binding.ptVentasH2.text.toString().toDouble()
        val ventasMes3:Double=binding.ptVentasP1.text.toString().toDouble()
        val ventasMes4:Double=binding.ptVentasP2.text.toString().toDouble()
        val ventasMes5:Double=binding.ptVentasP3.text.toString().toDouble()
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("PrecioUd").set(
            hashMapOf(
                "PrecioMes1" to  binding.ptPrecioUdh1.text.toString(),
                "PrecioMes2" to  binding.ptPrecioUdh2.text.toString(),
                "PrecioMes3" to  binding.ptPrecioUdp1.text.toString(),
                "PrecioMes4" to  binding.ptPrecioUdp2.text.toString(),
                "PrecioMes5" to  binding.ptPrecioUdp3.text.toString(),

                )
        )
        val precioMes1:Double=_binding!!.ptPrecioUdh1.text.toString().toDouble()
        val precioMes2:Double=_binding!!.ptPrecioUdh2.text.toString().toDouble()
        val precioMes3:Double=_binding!!.ptPrecioUdp1.text.toString().toDouble()
        val precioMes4:Double=_binding!!.ptPrecioUdp2.text.toString().toDouble()
        val precioMes5:Double=_binding!!.ptPrecioUdp3.text.toString().toDouble()

        val resIngreso1:Double=precioMes1*ventasMes1
        val resIngreso2:Double=precioMes2*ventasMes2
        val resIngreso3:Double=precioMes3*ventasMes3
        val resIngreso4:Double=precioMes4*ventasMes4
        val resIngreso5:Double=precioMes5*ventasMes5

        val ingresoBruto1:String = resIngreso1.toString()
        val ingresoBruto2:String = resIngreso2.toString()
        val ingresoBruto3:String = resIngreso3.toString()
        val ingresoBruto4:String = resIngreso4.toString()
        val ingresoBruto5:String = resIngreso5.toString()

        _binding!!.tvIngresoM1.setText(ingresoBruto1)
        _binding!!.tvIngresoM2.setText(ingresoBruto2)
        _binding!!.tvIngresoM3.setText(ingresoBruto3)
        _binding!!.tvIngresoM4.setText(ingresoBruto4)
        _binding!!.tvIngresoM5.setText(ingresoBruto5)

    }
    private fun ingresoBruto(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("IngresoBruto").set(
            hashMapOf(
                "IngresoBrutoMes1" to  binding.tvIngresoM1.text.toString(),
                "IngresoBrutoMes2" to  binding.tvIngresoM2.text.toString(),
                "IngresoBrutoMes3" to  binding.tvIngresoM3.text.toString(),
                "IngresoBrutoMes4" to  binding.tvIngresoM4.text.toString(),
                "IngresoBrutoMes5" to  binding.tvIngresoM5.text.toString(),

                )) }
    private fun ventasContado(){

        val TotPorc=100
        var PorcCont=parseInt(_binding?.etPorcCont?.text as String)
        //val PorcV30d=parseInt(_binding?.etPorc30d?.text as String)
        //val PorcV60d=parseInt(_binding?.etPorc60d?.text as String)
        //val cont=TotPorc-PorcCont-PorcV30d-PorcV60d
        if(PorcCont==100)
        { _binding?.etPorc30d?.setText("0")
            _binding?.etPorc60d?.setText("0")}


        /*else if(PorcV30d==100){
            _binding?.etPorcCont?.setText("0")
            _binding?.etPorc60d?.setText("0")

        }
        else if(PorcV60d==60){
            _binding?.etPorcCont?.setText("0")
            _binding?.etPorc30d?.setText("0")
        }
        else
        { _binding?.etPorc30d?.setText("0")
            //val toast=Toast.makeText(context,"error",Toast.LENGTH_LONG)}*/






    }


    companion object {

        @JvmStatic
        fun newInstance() = InputsFragment()
    }


}