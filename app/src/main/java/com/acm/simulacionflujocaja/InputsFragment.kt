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
import kotlinx.android.synthetic.main.fragment_inputs.*
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round
import java.math.BigDecimal



class InputsFragment : Fragment(R.layout.fragment_inputs) {

    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentInputsBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: Communicator
    private val user = FirebaseAuth.getInstance().currentUser
    private val r :RedondeoDecimal= RedondeoDecimal()
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




        GuardarVariables(user?.email.toString())
        recuperarTodo()

        return view


    }
    private fun GuardarVariables(email:String){
        communicator=activity as Communicator
        binding.btnSave.setOnClickListener {
            saveDatosMeses()//guarda meses
            datosVentasYPrecio()//guarda ventas y precio unitario
            ingresoBruto()
            otrosDatos()
            ventasContado()
            //para pasar datos de inputs a presupuesto de caja
            val ventasMes1= parseDouble(_binding?.etVenCont3?.text.toString())
            val ventasMes2= parseDouble(_binding?.etVenCont4?.text.toString())
            val ventasMes3= parseDouble(_binding?.etVenCont5?.text.toString())
            val rec30dias1=parseDouble(_binding?.etRec30d3?.text.toString())
            val rec30dias2=parseDouble(_binding?.etRec30d4?.text.toString())
            val rec30dias3=parseDouble(_binding?.etRec30d5?.text.toString())
            val rec60dias1=parseDouble(_binding?.etRec60d3?.text.toString())
            val rec60dias2=parseDouble(_binding?.etRec60d4?.text.toString())
            val rec60dias3=parseDouble(_binding?.etRec60d5?.text.toString())
            db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Calculo").set(
                hashMapOf(
                    "VentasMes1" to ventasMes1.toString(),
                    "VentasMes2" to  ventasMes2.toString(),
                    "VentasMes3" to  ventasMes3.toString(),

                    "rec30dias1" to  rec30dias1.toString(),
                    "rec30dias2" to  rec30dias2.toString(),
                    "rec30dias3" to  rec30dias3.toString(),

                    "rec60dias1" to  rec60dias1.toString(),
                    "rec60dias2" to rec60dias2.toString(),
                    "rec60dias3" to  rec60dias3.toString()

                    )
            )
           // communicator.passDataMeses(ventasMes1,ventasMes2,ventasMes3,rec30dias1,rec30dias2,rec30dias3,rec60dias1,rec60dias2,rec60dias3)


        }
        //recuperar variables con boton
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


            db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Inputs").get().addOnSuccessListener {

                binding.etVenCont1.setText(it.get("Ventas contado mes 1") as String?)
                binding.etVenCont2.setText(it.get("Ventas contado mes 2") as String?)
                binding.etVenCont3.setText(it.get("Ventas contado mes 3") as String?)
                binding.etVenCont4.setText(it.get("Ventas contado mes 4") as String?)
                binding.etVenCont5.setText(it.get("Ventas contado mes 5") as String?)

                binding.etRec30d1.setText(it.get("Recuperacion 30 dias mes 1") as String?)
                binding.etRec30d2.setText(it.get("Recuperacion 30 dias mes 2") as String?)
                binding.etRec30d3.setText(it.get("Recuperacion 30 dias mes 3") as String?)
                binding.etRec30d4.setText(it.get("Recuperacion 30 dias mes 4") as String?)
                binding.etRec30d5.setText(it.get("Recuperacion 30 dias mes 5") as String?)

                binding.etRec60d1.setText(it.get("Recuperacion 60 dias mes 1") as String?)
                binding.etRec60d2.setText(it.get("Recuperacion 60 dias mes 2") as String?)
                binding.etRec60d3.setText(it.get("Recuperacion 60 dias mes 3") as String?)
                binding.etRec60d4.setText(it.get("Recuperacion 60 dias mes 4") as String?)
                binding.etRec60d5.setText(it.get("Recuperacion 60 dias mes 5") as String?)

                binding.interes60d1.setText(it.get("Interes 60 mes 1") as String?)
                binding.interes60d2.setText(it.get("Interes 60 mes 2") as String?)
                binding.interes60d3.setText(it.get("Interes 60 mes 3") as String?)
                binding.interes60d4.setText(it.get("Interes 60 mes 4") as String?)
                binding.interes60d5.setText(it.get("Interes 60 mes 5") as String?)

                binding.incob30d1.setText(it.get("Incobrabilidad 30 mes 1") as String?)
                binding.incob30d2.setText(it.get("Incobrabilidad 30 mes 2") as String?)
                binding.incob30d3.setText(it.get("Incobrabilidad 30 mes 3") as String?)
                binding.incob30d4.setText(it.get("Incobrabilidad 30 mes 4") as String?)
                binding.incob30d5.setText(it.get("Incobrabilidad 30 mes 5") as String?)

                binding.incob60d1.setText(it.get("Incobrabilidad 60 mes 1") as String?)
                binding.incob60d2.setText(it.get("Incobrabilidad 60 mes 2") as String?)
                binding.incob60d3.setText(it.get("Incobrabilidad 60 mes 3") as String?)
                binding.incob60d4.setText(it.get("Incobrabilidad 60 mes 4") as String?)
                binding.incob60d5.setText(it.get("Incobrabilidad 60 mes 5") as String?)

            }

                    }

        //eliminar entradas
        binding.btnEliminarInputs.setOnClickListener {
            db.collection("Users").document(email.toString()).collection("Entradas").document("Meses").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("Ventas").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("PrecioUd").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("IngresoBruto").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("OtrosDatos").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("Ventas Contado").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("Recuperacion 30 dias").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("Recuperacion 60 dias").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("Intereses a cobrar 30d dias").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("Intereses a cobrar 60d dias").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("Incobrabilidad 30 dias").delete()
            db.collection("Users").document(email.toString()).collection("Entradas").document("Incobrabilidad 60 dias").delete()
                    }

                }

    private fun saveDatosMeses(){
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
    private fun ventasContado(){//guarda ventas contado, recuperacion 30d y rec. 60d

        val totPorc:Double= r.redondear(100.0)
        //para porcentaje contado
        val porcjContado=_binding?.etPorcCont?.text.toString()
        val porcCont= r.redondear(parseDouble(porcjContado))
        //para porcentaje credito30dias
        val porcjCr30d=_binding?.etPorc30d?.text.toString()
        val porc30dias= r.redondear(parseDouble(porcjCr30d))
        //para porcentaje credito60dias
        val porcjCr60d=_binding?.etPorc60d?.text.toString()
        val porc60dias=r.redondear(parseDouble(porcjCr60d))
       //para porcentaje interes credito
        val interes= _binding?.etInteresCredito?.text.toString()
        val interesCredito= r.redondear(parseDouble(interes))/100
        //para porcentaje incobrabilidad
        val incobrabilidad= _binding?.etPorcIncobrabilidad?.text.toString()
        val porcIncobrabilidad=r.redondear(parseDouble(incobrabilidad))/100

        if(interesCredito<0||interesCredito>0.075)
        {
            _binding?.etInteresCredito?.setError("Error")
        }
        else{
            _binding?.etInteresCredito?.setError(null)
        }

        if(porcIncobrabilidad<0||porcIncobrabilidad>0.03)
        {
            _binding?.etPorcIncobrabilidad?.setError("Error")
        }
        else{
            _binding?.etPorcIncobrabilidad?.setError(null)
        }

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
           //calculo ventas contado e intereses 30d y 60d
           val ingBruto1:Double=parseDouble(_binding?.tvIngresoM1?.text.toString())
           val ventasContado1:Double= (porcCont/100)*ingBruto1
           _binding?.etVenCont1?.setText(r.redondear(ventasContado1).toString())

           val ingBruto2:Double=parseDouble(_binding?.tvIngresoM2?.text.toString())
           val ventasContado2:Double=(porcCont/100)*ingBruto2
           _binding?.etVenCont2?.setText(r.redondear(ventasContado2).toString())

           val ingBruto3:Double=parseDouble(_binding?.tvIngresoM3?.text.toString())
           val ventasContado3:Double=(porcCont/100)*ingBruto3
           _binding?.etVenCont3?.setText(r.redondear(ventasContado3).toString())

           val ingBruto4:Double=parseDouble(_binding?.tvIngresoM4?.text.toString())
           val ventasContado4:Double=(porcCont/100)*ingBruto4
           _binding?.etVenCont4?.setText(r.redondear(ventasContado4).toString())

           val ingBruto5:Double=parseDouble(_binding?.tvIngresoM5?.text.toString())
           val ventasContado5:Double=(porcCont/100)*ingBruto5
           _binding?.etVenCont5?.setText(r.redondear(ventasContado5).toString())

           //calculo recuperacion 30 dias
           val rec30dias2:Double=(porc30dias/100)*ingBruto1
           val rec30dias3:Double=(porc30dias/100)*ingBruto2
           val rec30dias4:Double=(porc30dias/100)*ingBruto3
           val rec30dias5:Double=(porc30dias/100)*ingBruto4

           _binding?.etRec30d1?.setText("0.0")
           _binding?.etRec30d2?.setText(r.redondear(rec30dias2).toString())
           _binding?.etRec30d3?.setText(r.redondear(rec30dias3).toString())
           _binding?.etRec30d4?.setText(r.redondear(rec30dias4).toString())
           _binding?.etRec30d5?.setText(r.redondear(rec30dias5).toString())
           //calculo interes para 30 dias
           val totInteres30d2:Double= rec30dias2*interesCredito
           val totInteres30d3:Double= rec30dias3*interesCredito
           val totInteres30d4:Double= rec30dias4*interesCredito
           val totInteres30d5:Double= rec30dias5*interesCredito
           _binding?.interes30d1?.setText("0.0")
           _binding?.interes30d2?.setText(r.redondear(totInteres30d2).toString())
           _binding?.interes30d3?.setText(r.redondear(totInteres30d3).toString())
           _binding?.interes30d4?.setText(r.redondear(totInteres30d4).toString())
           _binding?.interes30d5?.setText(r.redondear(totInteres30d5).toString())
       //calculo incobrabilidad para 30 dias
           val totIncob30d2:Double= (rec30dias2+totInteres30d2)*porcIncobrabilidad
           val totIncob30d3:Double= (rec30dias3+totInteres30d3)*porcIncobrabilidad
           val totIncob30d4:Double= (rec30dias4+totInteres30d4)*porcIncobrabilidad
           val totIncob30d5:Double= (rec30dias5+totInteres30d5)*porcIncobrabilidad
           _binding?.incob30d1?.setText("0.0")
           _binding?.incob30d2?.setText(r.redondear(totIncob30d2).toString())
           _binding?.incob30d3?.setText(r.redondear(totIncob30d3).toString())
           _binding?.incob30d4?.setText(r.redondear(totIncob30d4).toString())
           _binding?.incob30d5?.setText(r.redondear(totIncob30d5).toString())
      //calculo recuperacion 60 dias
           val rec60dias3:Double=(porc60dias/100)*ingBruto1
           val rec60dias4:Double=(porc60dias/100)*ingBruto2
           val rec60dias5:Double=(porc60dias/100)*ingBruto3


           _binding?.etRec60d1?.setText("0.0")
           _binding?.etRec60d2?.setText("0.0")
           _binding?.etRec60d3?.setText(r.redondear(rec60dias3).toString())
           _binding?.etRec60d4?.setText(r.redondear(rec60dias4).toString())
           _binding?.etRec60d5?.setText(r.redondear(rec60dias5).toString())

           //calculo intereses para 60 dias
           val totInteres60d3:Double= r.redondear(rec60dias3*interesCredito)
           val totInteres60d4:Double= r.redondear(rec60dias4*interesCredito)
           val totInteres60d5:Double= r.redondear(rec60dias5*interesCredito)
           _binding?.interes60d1?.setText("0.0")
           _binding?.interes60d2?.setText("0.0")
           _binding?.interes60d3?.setText(r.redondear(totInteres60d3).toString())
           _binding?.interes60d4?.setText(r.redondear(totInteres60d4).toString())
           _binding?.interes60d5?.setText(r.redondear(totInteres60d5).toString())
           //calculo incobrabilidad para 60 dias
           val totIncob60d3:Double= (rec60dias3+totInteres60d3)*porcIncobrabilidad
           val totIncob60d4:Double= (rec60dias4+totInteres60d4)*porcIncobrabilidad
           val totIncob60d5:Double= (rec60dias5+totInteres60d5)*porcIncobrabilidad
           _binding?.incob60d1?.setText("0.0")
           _binding?.incob60d2?.setText("0.0")
           _binding?.incob60d3?.setText(r.redondear(totIncob60d3).toString())
           _binding?.incob60d4?.setText(r.redondear(totIncob60d4).toString())
           _binding?.incob60d5?.setText(r.redondear(totIncob60d5).toString())
           val incrementoSal:Double= parseDouble(binding.etIncrementoSalarial.text.toString())/100.0
           val sueldoEmp:Double= parseDouble(binding.etSueldoEmp.text.toString())
           if(incrementoSal==0.0){
               val sueldo1:Double=sueldoEmp
               val sueldo2:Double=sueldoEmp
               val sueldo3:Double=sueldoEmp
               db.collection("Users").document(user?.email.toString()).collection("Entradas").document("SueldosMes").set(
                   hashMapOf(

                       "sueldo1" to  sueldo1.toString(),
                       "sueldo2" to  sueldo2.toString(),
                       "sueldo3" to  sueldo3.toString()
                   ))
           }
           else{
               val sueldo1:Double=sueldoEmp
               val sueldo2:Double=sueldoEmp
               val sueldo3:Double=r.redondear(sueldoEmp+(sueldoEmp*incrementoSal))

               db.collection("Users").document(user?.email.toString()).collection("Entradas").document("SueldosMes").set(
                   hashMapOf(

                       "sueldo1" to  sueldo1.toString(),
                       "sueldo2" to  sueldo2.toString(),
                       "sueldo3" to  sueldo3.toString()
                   ))
           }


           if(porcCont==totPorc && porc30dias ==0.0 && porc60dias==0.0 && porcCont+porc30dias+porc60dias==totPorc)
           {
               _binding?.etRec30d1?.setText("0.0")
               _binding?.etRec30d2?.setText("0.0")
               _binding?.etRec30d3?.setText("0.0")
               _binding?.etRec30d4?.setText("0.0")
               _binding?.etRec30d5?.setText("0.0")

               _binding?.etRec60d1?.setText("0.0")
               _binding?.etRec60d2?.setText("0.0")
               _binding?.etRec60d3?.setText("0.0")
               _binding?.etRec60d4?.setText("0.0")
               _binding?.etRec60d5?.setText("0.0")

               _binding?.etInteresCredito?.setText("0.0")
               _binding?.etPorcIncobrabilidad?.setText("0.0")



           }
           if(porcCont==0.0 && porc30dias ==100.0 && porc60dias==0.0 && porcCont+porc30dias+porc60dias==totPorc)
           {
               _binding?.etVenCont1?.setText("0.0")
               _binding?.etVenCont2?.setText("0.0")
               _binding?.etVenCont3?.setText("0.0")
               _binding?.etVenCont4?.setText("0.0")
               _binding?.etVenCont5?.setText("0.0")
               _binding?.etRec60d1?.setText("0.0")
               _binding?.etRec60d2?.setText("0.0")
               _binding?.etRec60d3?.setText("0.0")
               _binding?.etRec60d4?.setText("0.0")
               _binding?.etRec60d5?.setText("0.0")


           }
           if(porcCont==0.0 && porc30dias ==0.0 && porc60dias==100.0 && porcCont+porc30dias+porc60dias==totPorc)
           {
               _binding?.etVenCont1?.setText("0.0")
               _binding?.etVenCont2?.setText("0.0")
               _binding?.etVenCont3?.setText("0.0")
               _binding?.etVenCont4?.setText("0.0")
               _binding?.etVenCont5?.setText("0.0")
               _binding?.etRec30d1?.setText("0.0")
               _binding?.etRec30d2?.setText("0.0")
               _binding?.etRec30d3?.setText("0.0")
               _binding?.etRec30d4?.setText("0.0")
               _binding?.etRec30d5?.setText("0.0")


           }



       }


        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Inputs").set(
            hashMapOf(

                "Ventas contado mes 1" to  binding.etVenCont1.text.toString(),
                "Ventas contado mes 2" to  binding.etVenCont2.text.toString(),
                "Ventas contado mes 3" to  binding.etVenCont3.text.toString(),
                "Ventas contado mes 4" to  binding.etVenCont4.text.toString(),
                "Ventas contado mes 5" to  binding.etVenCont5.text.toString(),

                "Recuperacion 30 dias mes 1" to  binding.etRec30d1.text.toString(),
                "Recuperacion 30 dias mes 2" to  binding.etRec30d2.text.toString(),
                "Recuperacion 30 dias mes 3" to  binding.etRec30d3.text.toString(),
                "Recuperacion 30 dias mes 4" to  binding.etRec30d4.text.toString(),
                "Recuperacion 30 dias mes 5" to  binding.etRec30d5.text.toString(),

                "Recuperacion 60 dias mes 1" to  binding.etRec60d1.text.toString(),
                "Recuperacion 60 dias mes 2" to  binding.etRec60d2.text.toString(),
                "Recuperacion 60 dias mes 3" to  binding.etRec60d3.text.toString(),
                "Recuperacion 60 dias mes 4" to  binding.etRec60d4.text.toString(),
                "Recuperacion 60 dias mes 5" to  binding.etRec60d5.text.toString(),

                "Interes 30 mes 1" to  binding.interes30d1.text.toString(),
                "Interes 30 mes 2" to  binding.interes30d2.text.toString(),
                "Interes 30 mes 3" to  binding.interes30d3.text.toString(),
                "Interes 30 mes 4" to  binding.interes30d4.text.toString(),
                "Interes 30 mes 5" to  binding.interes30d5.text.toString(),

                "Interes 60 mes 1" to  binding.interes60d1.text.toString(),
                "Interes 60 mes 2" to  binding.interes60d2.text.toString(),
                "Interes 60 mes 3" to  binding.interes60d3.text.toString(),
                "Interes 60 mes 4" to  binding.interes60d4.text.toString(),
                "Interes 60 mes 5" to  binding.interes60d5.text.toString(),

                "Incobrabilidad 30 mes 1" to  binding.interes30d1.text.toString(),
                "Incobrabilidad 30 mes 2" to  binding.interes30d2.text.toString(),
                "Incobrabilidad 30 mes 3" to  binding.interes30d3.text.toString(),
                "Incobrabilidad 30 mes 4" to  binding.interes30d4.text.toString(),
                "Incobrabilidad 30 mes 5" to  binding.interes30d5.text.toString(),

                "Incobrabilidad 60 mes 1" to  binding.interes60d1.text.toString(),
                "Incobrabilidad 60 mes 2" to  binding.interes60d2.text.toString(),
                "Incobrabilidad 60 mes 3" to  binding.interes60d3.text.toString(),
                "Incobrabilidad 60 mes 4" to  binding.interes60d4.text.toString(),
                "Incobrabilidad 60 mes 5" to  binding.interes60d5.text.toString()

            ))


    }

    fun recuperarTodo(){//recupera todas las entradas
        val user= FirebaseAuth.getInstance().currentUser
        val email=user?.email
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

        //recupera de base de datos
        //recuperacion ventascontado, 30d, 60d, intereses generados para 30d,60d, incobrabilidad 30d,60d
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("Inputs").get().addOnSuccessListener {
            binding.etVenCont1.setText(it.get("Ventas contado mes 1") as String?)
            binding.etVenCont2.setText(it.get("Ventas contado mes 2") as String?)
            binding.etVenCont3.setText(it.get("Ventas contado mes 3") as String?)
            binding.etVenCont4.setText(it.get("Ventas contado mes 4") as String?)
            binding.etVenCont5.setText(it.get("Ventas contado mes 5") as String?)

            binding.etRec30d1.setText(it.get("Recuperacion 30 dias mes 1") as String?)
            binding.etRec30d2.setText(it.get("Recuperacion 30 dias mes 2") as String?)
            binding.etRec30d3.setText(it.get("Recuperacion 30 dias mes 3") as String?)
            binding.etRec30d4.setText(it.get("Recuperacion 30 dias mes 4") as String?)
            binding.etRec30d5.setText(it.get("Recuperacion 30 dias mes 5") as String?)

            binding.etRec60d1.setText(it.get("Recuperacion 60 dias mes 1") as String?)
            binding.etRec60d2.setText(it.get("Recuperacion 60 dias mes 2") as String?)
            binding.etRec60d3.setText(it.get("Recuperacion 60 dias mes 3") as String?)
            binding.etRec60d4.setText(it.get("Recuperacion 60 dias mes 4") as String?)
            binding.etRec60d5.setText(it.get("Recuperacion 60 dias mes 5") as String?)


            binding.interes30d1.setText(it.get("Interes 30 mes 1") as String?)
            binding.interes30d2.setText(it.get("Interes 30 mes 2") as String?)
            binding.interes30d3.setText(it.get("Interes 30 mes 3") as String?)
            binding.interes30d4.setText(it.get("Interes 30 mes 4") as String?)
            binding.interes30d5.setText(it.get("Interes 30 mes 5") as String?)

            binding.interes60d1.setText(it.get("Interes 60 mes 1") as String?)
            binding.interes60d2.setText(it.get("Interes 60 mes 2") as String?)
            binding.interes60d3.setText(it.get("Interes 60 mes 3") as String?)
            binding.interes60d4.setText(it.get("Interes 60 mes 4") as String?)
            binding.interes60d5.setText(it.get("Interes 60 mes 5") as String?)

            binding.incob30d1.setText(it.get("Incobrabilidad 30 mes 1") as String?)
            binding.incob30d2.setText(it.get("Incobrabilidad 30 mes 2") as String?)
            binding.incob30d3.setText(it.get("Incobrabilidad 30 mes 3") as String?)
            binding.incob30d4.setText(it.get("Incobrabilidad 30 mes 4") as String?)
            binding.incob30d5.setText(it.get("Incobrabilidad 30 mes 5") as String?)

            binding.incob60d1.setText(it.get("Incobrabilidad 60 mes 1") as String?)
            binding.incob60d2.setText(it.get("Incobrabilidad 60 mes 2") as String?)
            binding.incob60d3.setText(it.get("Incobrabilidad 60 mes 3") as String?)
            binding.incob60d4.setText(it.get("Incobrabilidad 60 mes 4") as String?)
            binding.incob60d5.setText(it.get("Incobrabilidad 60 mes 5") as String?)
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = InputsFragment()//para instanciar con los TAG designados
    }


}