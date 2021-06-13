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
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round


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

        _binding = FragmentInputsBinding.inflate(inflater, container, false)
        val view = binding.root
        communicator=activity as Communicator


        GuardarVariables(user?.email.toString())
        return view


    }
    private fun GuardarVariables(email:String){
        binding.btnSave.setOnClickListener {
            datosMeses()
            datosVentasYPrecio()
            ingresoBruto()
            otrosDatos()
            ventasContado()
        }
        //recuperar variables
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
                         db.collection("Users").document(email.toString()).collection("Entradas").document("IngresoBruto").get().addOnSuccessListener {
                             binding.tvIngresoM1.setText(it.get("IngresoBrutoMes1") as String?)
                             binding.tvIngresoM2.setText(it.get("IngresoBrutoMes2") as String?)
                             binding.tvIngresoM3.setText(it.get("IngresoBrutoMes3") as String?)
                             binding.tvIngresoM4.setText(it.get("IngresoBrutoMes4") as String?)
                             binding.tvIngresoM5.setText(it.get("IngresoBrutoMes5") as String?)

                         }

                        db.collection("Users").document(email.toString()).collection("Entradas").document("OtrosDatos").get().addOnSuccessListener {
                            binding.etNrEmpl.setText(it.get("Nro Empleados") as String?)
                            binding.etSueldoEmp.setText(it.get("Sueldo Empleados") as String?)
                            binding.etIncrementoSalarial.setText(it.get("Incremento Salarial") as String?)
                            binding.etPorcCont.setText(it.get("Porcentaje Contado") as String?)
                            binding.etPorc30d.setText(it.get("Porcenta credito a 30 dias") as String?)
                            binding.etPorc60d.setText(it.get("Porcentaje credito a 60 dias") as String?)
                            binding.etPorcIncobrabilidad.setText(it.get("Porcentaje Incobrabilidad") as String?)
                            binding.etInteresCredito.setText(it.get("Interes Credito") as String?)

                        }
                    }
        //eliminar entradas
        binding.btnEliminarInputs.setOnClickListener {
                        db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").delete()
                        db.collection("Users").document(email.toString()).collection("Entradas").document("Ventas").delete()
                        db.collection("Users").document(email.toString()).collection("Entradas").document("PrecioUd").delete()
                        db.collection("Users").document(email.toString()).collection("Entradas").document("IngresoBruto").delete()
                        db.collection("Users").document(email.toString()).collection("Entradas").document("OtrosDatos").delete()
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
    private fun otrosDatos(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("OtrosDatos").set(
            hashMapOf(
                "Nro Empleados" to  binding.etNrEmpl.text.toString(),
                "Sueldo Empleados" to  binding.etSueldoEmp.text.toString(),
                "Incremento Salarial" to  binding.etIncrementoSalarial.text.toString(),
                "Porcentaje Contado" to  binding.etPorcCont.text.toString(),
                "Porcenta credito a 30 dias" to  binding.etPorc30d.text.toString(),
                "Porcentaje credito a 60 dias" to  binding.etPorc60d.text.toString(),
                "Porcentaje Incobrabilidad" to  binding.etPorcIncobrabilidad.text.toString(),
                "Interes Credito" to  binding.etInteresCredito.text.toString()

                )
        )

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

        val totPorc:Double= redondearDecimal(100.0)
        //para porcentaje contado
        val porcjContado=_binding?.etPorcCont?.text.toString()
        val porcCont= redondearDecimal(parseDouble(porcjContado))
        //para porcentaje credito30dias
        val porcjCr30d=_binding?.etPorc30d?.text.toString()
        val porc30dias= redondearDecimal(parseDouble(porcjCr30d))
        //para porcentaje credito60dias
        val porcjCr60d=_binding?.etPorc60d?.text.toString()
        val porc60dias=redondearDecimal(parseDouble(porcjCr60d))


       if((porcCont<0||porcCont>100) || (porc30dias<0||porc30dias>100)||(porc60dias<0||porc60dias>100))

           if((porcCont<0||porcCont>100) || (porc30dias<0||porc30dias>100)||(porc60dias<0||porc60dias>100))

           {
               _binding?.etPorcCont?.setError("Error")
               _binding?.etPorc30d?.setError("Error")
               _binding?.etPorc60d?.setError("Error")

           }
           else if(porcCont<totPorc-porc30dias-porc60dias)
           {
               _binding?.etPorcCont?.setError("Error")
               _binding?.etPorc30d?.setText(porc30dias.toString())
               _binding?.etPorc30d?.setError(null)
               _binding?.etPorc60d?.setText(porc60dias.toString())
               _binding?.etPorc60d?.setError(null)
               //Toast.makeText(getContext(),"Ingrese un numero valido entre 1 a 100",Toast.LENGTH_LONG).show()
           }
           else if(porc30dias<totPorc-porcCont-porc60dias){
               _binding?.etPorcCont?.setText(porcCont.toString())
               _binding?.etPorcCont?.setError(null)
               _binding?.etPorc30d?.setError("Error")
               _binding?.etPorc60d?.setText(porc60dias.toString())
               _binding?.etPorc60d?.setError(null)

           }
           else if(porc60dias<totPorc-porcCont-porc30dias){
               _binding?.etPorcCont?.setText(porcCont.toString())
               _binding?.etPorcCont?.setError(null)
               _binding?.etPorc30d?.setText(porc30dias.toString())
               _binding?.etPorc30d?.setError(null)
               _binding?.etPorc60d?.setError("Error")
           }
           if(porcCont+porc30dias+porc60dias==totPorc)
           {
               _binding?.etPorcCont?.setText(porcCont.toString())
               _binding?.etPorc30d?.setText(porc30dias.toString())
               _binding?.etPorc60d?.setText(porc60dias.toString())

               _binding?.etPorcCont?.setError(null)
               _binding?.etPorc30d?.setError(null)
               _binding?.etPorc60d?.setError(null)
           }

           else{
               _binding?.etPorcCont?.setError("Error")
               _binding?.etPorc30d?.setError("Error")
               _binding?.etPorc60d?.setError("Error")

           }
        if(porcCont+porc30dias+porc60dias!=totPorc){
            _binding?.etPorcCont?.setError("Error")
            _binding?.etPorc30d?.setError("Error")
            _binding?.etPorc60d?.setError("Error")}

        if(porcCont<=100 && porc30dias<=100 && porc60dias<=100 && porcCont+porc30dias+porc60dias==totPorc)
       {
           //calculo ventas contado
           val ingBruto1:Double=parseDouble(_binding?.tvIngresoM1?.text.toString())
           val ventasContado1:Double= (porcCont/100)*ingBruto1
           _binding?.etVenCont1?.setText(ventasContado1.toString())

           val ingBruto2:Double=parseDouble(_binding?.tvIngresoM2?.text.toString())
           val ventasContado2:Double=(porcCont/100)*ingBruto2
           _binding?.etVenCont2?.setText(ventasContado2.toString())

           val ingBruto3:Double=parseDouble(_binding?.tvIngresoM3?.text.toString())
           val ventasContado3:Double=(porcCont/100)*ingBruto3
           _binding?.etVenCont3?.setText(ventasContado3.toString())

           val ingBruto4:Double=parseDouble(_binding?.tvIngresoM4?.text.toString())
           val ventasContado4:Double=(porcCont/100)*ingBruto4
           _binding?.etVenCont4?.setText(ventasContado4.toString())

           val ingBruto5:Double=parseDouble(_binding?.tvIngresoM5?.text.toString())
           val ventasContado5:Double=(porcCont/100)*ingBruto5
           _binding?.etVenCont5?.setText(ventasContado5.toString())
           //calculo recuperacion 30 dias
           val rec30dias2:Double=(porc30dias/100)*ingBruto1
           val rec30dias3:Double=(porc30dias/100)*ingBruto2
           val rec30dias4:Double=(porc30dias/100)*ingBruto3
           val rec30dias5:Double=(porc30dias/100)*ingBruto4
           _binding?.etRec30d1?.setText("0.00")
           _binding?.etRec30d2?.setText(rec30dias2.toString())
           _binding?.etRec30d3?.setText(rec30dias3.toString())
           _binding?.etRec30d4?.setText(rec30dias4.toString())
           _binding?.etRec30d5?.setText(rec30dias5.toString())
      //calculo recuperacion 60 dias
           val rec60dias3:Double=(porc60dias/100)*ingBruto1
           val rec60dias4:Double=(porc60dias/100)*ingBruto2
           val rec60dias5:Double=(porc60dias/100)*ingBruto3
           _binding?.etRec60d1?.setText("0.00")
           _binding?.etRec60d2?.setText("0.00")
           _binding?.etRec60d3?.setText(rec60dias3.toString())
           _binding?.etRec60d4?.setText(rec60dias4.toString())
           _binding?.etRec60d5?.setText(rec60dias5.toString())

           if(porcCont==totPorc && porc30dias ==0.0 && porc60dias==0.0 && porcCont+porc30dias+porc60dias==totPorc)
           {
               _binding?.etRec30d1?.setText("0.00")
               _binding?.etRec30d2?.setText("0.00")
               _binding?.etRec30d3?.setText("0.00")
               _binding?.etRec30d4?.setText("0.00")
               _binding?.etRec30d5?.setText("0.00")

               _binding?.etRec60d1?.setText("0.00")
               _binding?.etRec60d2?.setText("0.00")
               _binding?.etRec60d3?.setText("0.00")
               _binding?.etRec60d4?.setText("0.00")
               _binding?.etRec60d5?.setText("0.00")


           }
           if(porcCont==0.0 && porc30dias ==100.0 && porc60dias==0.0 && porcCont+porc30dias+porc60dias==totPorc)
           {
               _binding?.etVenCont1?.setText("0.00")
               _binding?.etVenCont2?.setText("0.00")
               _binding?.etVenCont3?.setText("0.00")
               _binding?.etVenCont4?.setText("0.00")
               _binding?.etVenCont5?.setText("0.00")
               _binding?.etRec60d1?.setText("0.00")
               _binding?.etRec60d2?.setText("0.00")
               _binding?.etRec60d3?.setText("0.00")
               _binding?.etRec60d4?.setText("0.00")
               _binding?.etRec60d5?.setText("0.00")


           }
           if(porcCont==0.0 && porc30dias ==0.0 && porc60dias==100.0 && porcCont+porc30dias+porc60dias==totPorc)
           {
               _binding?.etVenCont1?.setText("0.00")
               _binding?.etVenCont2?.setText("0.00")
               _binding?.etVenCont3?.setText("0.00")
               _binding?.etVenCont4?.setText("0.00")
               _binding?.etVenCont5?.setText("0.00")
               _binding?.etRec30d1?.setText("0.00")
               _binding?.etRec30d2?.setText("0.00")
               _binding?.etRec30d3?.setText("0.00")
               _binding?.etRec30d4?.setText("0.00")
               _binding?.etRec30d5?.setText("0.00")


           }


       }


        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Ventas Contado").set(
            hashMapOf(
                "Ventas contado mes 1" to  binding.etVenCont1.text.toString(),
                "Ventas contado mes 2" to  binding.etVenCont2.text.toString(),
                "Ventas contado mes 3" to  binding.etVenCont3.text.toString(),
                "Ventas contado mes 4" to  binding.etVenCont4.text.toString(),
                "Ventas contado mes 5" to  binding.etVenCont5.text.toString()

            ))
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Recuperacion 30 dias").set(
            hashMapOf(
                "Recuperacion 30 dias mes 1" to  binding.etRec30d1.text.toString(),
                "Recuperacion 30 dias mes 2" to  binding.etRec30d2.text.toString(),
                "Recuperacion 30 dias mes 3" to  binding.etRec30d3.text.toString(),
                "Recuperacion 30 dias mes 4" to  binding.etRec30d4.text.toString(),
                "Recuperacion 30 dias mes 5" to  binding.etRec30d5.text.toString()

            ))
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Recuperacion 60 dias").set(
            hashMapOf(
                "Recuperacion 60 dias mes 1" to  binding.etRec60d1.text.toString(),
                "Recuperacion 60 dias mes 2" to  binding.etRec60d2.text.toString(),
                "Recuperacion 60 dias mes 3" to  binding.etRec60d3.text.toString(),
                "Recuperacion 60 dias mes 4" to  binding.etRec60d4.text.toString(),
                "Recuperacion 60 dias mes 5" to  binding.etRec60d5.text.toString()

            ))


    }
    fun redondearDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }
    companion object {

        @JvmStatic
        fun newInstance() = InputsFragment()//para instanciar con los TAG designados
    }


}