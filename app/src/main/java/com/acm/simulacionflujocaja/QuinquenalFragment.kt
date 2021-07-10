package com.acm.simulacionflujocaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm.simulacionflujocaja.databinding.FragmentQuinquenalBinding
import com.acm.simulacionflujocaja.databinding.FragmentSemestralBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.pow
import kotlin.math.sqrt


class QuinquenalFragment : Fragment(R.layout.fragment_quinquenal) {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentQuinquenalBinding? = null
    private val binding get() = _binding!!
    val r:RedondeoDecimal = RedondeoDecimal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = FirebaseAuth.getInstance().currentUser
        _binding = FragmentQuinquenalBinding.inflate(inflater, container, false)
        val view = binding.root
        CalculoCorridasSimulacion()
        binding.btnSimularQuinque.setOnClickListener{
            CalculoCorridasSimulacion() }


        return view
    }


    private fun CalculoCorridasSimulacion(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("DatosPresupuesto").get().addOnSuccessListener {



            //Calculo de las medias
            if ((it.get("totalEntradas1") as String?)!=null)  { //valida si recupera de base de datos caso contrario establece variables a 0
                val totalSueldoefectivoFinalProy: Double =
                    r.redondear(java.lang.Double.parseDouble((it.get("totalSueldoefectivoFinalProy") as String?)))
                binding.etSaldoInicial.setText(totalSueldoefectivoFinalProy.toString())
                val entrada1: Double =
                    java.lang.Double.parseDouble((it.get("totalEntradas1") as String?))
                val entrada2: Double =
                    java.lang.Double.parseDouble((it.get("totalEntradas2") as String?))
                val entrada3: Double =
                    java.lang.Double.parseDouble((it.get("totalEntradas3") as String?))

                val salida1: Double =
                    java.lang.Double.parseDouble((it.get("totalSalidas1") as String?))
                val salida2: Double =
                    java.lang.Double.parseDouble((it.get("totalSalidas2") as String?))
                val salida3: Double =
                    java.lang.Double.parseDouble((it.get("totalSalidas3") as String?))
                val listaEntradas = mutableListOf<Double>(entrada1, entrada2, entrada3)
                val tamEnt: Double = listaEntradas.size.toDouble()
                var sumaEnt = 0.0
                var sumaSal = 0.0
                var varianzaEnt = 0.0
                var varianzaSal = 0.0


                val listaSalidas = mutableListOf<Double>(salida1, salida2, salida3)
                val tamSalida: Double = listaSalidas.size.toDouble()

                listaEntradas.forEach {
                    sumaEnt = it + sumaEnt
                }
                //calculo medias
                val mediaEntradas = r.redondear(sumaEnt / tamEnt)
                listaSalidas.forEach {
                    sumaSal = sumaSal + it
                }
                val mediaSalidas = r.redondear(sumaSal / tamSalida)

                listaEntradas.forEach {
                    varianzaEnt = ((mediaEntradas - it).pow(2.0) / tamEnt) + varianzaEnt
                }
                //calculo de las desv tipicas
                var desvTipicaEnt = r.redondear(sqrt(varianzaEnt))
                binding.etDesvTipEnt.setText(desvTipicaEnt.toString())

                listaSalidas.forEach {
                    varianzaSal = ((mediaSalidas - it).pow(2.0) / tamSalida) + varianzaSal
                }
                var desvTipicaSal = r.redondear(sqrt(varianzaSal))
                binding.etDesvTipSal.setText(desvTipicaSal.toString())

                binding.etMediaEnt.setText(mediaEntradas.toString())
                binding.etMediaSal.setText(mediaSalidas.toString())


                val listaNetoMes1 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    totalSueldoefectivoFinalProy
                )
                val varianzaNetoMes1 = calculoVarianzaNetoMes(listaNetoMes1)
                var desvTipicaNetoM1 = r.redondear(sqrt(varianzaNetoMes1))
                binding.etDesvTipNetoM1.setText(desvTipicaNetoM1.toString())
                val promedioNetoMes1 = promedioNetoMes(listaNetoMes1)
                binding.etPromNetoM1.setText(promedioNetoMes1.toString())
                var valoresIntervaloConfianzaM1 = calculoIntervaloConfianza95porc(listaNetoMes1)
                binding.etMinM1.setText(valoresIntervaloConfianzaM1[0].toString())
                binding.etMaxM1.setText(valoresIntervaloConfianzaM1[1].toString())
                binding.etDesvTipNetoM1.setText(desvTipicaNetoM1.toString())


                val listaNetoMes2 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes1
                )
                val varianzaNetoMes2 = calculoVarianzaNetoMes(listaNetoMes2)
                var desvTipicaNetoM2 = r.redondear(sqrt(varianzaNetoMes2))
                binding.etDesvTipNetoM2.setText(desvTipicaNetoM2.toString())
                val promedioNetoMes2 = promedioNetoMes(listaNetoMes2)
                binding.etPromNetoM2.setText(promedioNetoMes2.toString())
                var valoresIntervaloConfianzaM2 = calculoIntervaloConfianza95porc(listaNetoMes2)
                binding.etMinM2.setText(valoresIntervaloConfianzaM2[0].toString())
                binding.etMaxM2.setText(valoresIntervaloConfianzaM2[1].toString())
                binding.etDesvTipNetoM2.setText(desvTipicaNetoM2.toString())

                val listaNetoMes3 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes2
                )
                val varianzaNetoMes3 = calculoVarianzaNetoMes(listaNetoMes3)
                var desvTipicaNetoM3 = r.redondear(sqrt(varianzaNetoMes3))
                binding.etDesvTipNetoM3.setText(desvTipicaNetoM3.toString())
                val promedioNetoMes3 = promedioNetoMes(listaNetoMes3)
                binding.etPromNetoM3.setText(promedioNetoMes3.toString())
                var valoresIntervaloConfianzaM3 = calculoIntervaloConfianza95porc(listaNetoMes3)
                binding.etMinM3.setText(valoresIntervaloConfianzaM3[0].toString())
                binding.etMaxM3.setText(valoresIntervaloConfianzaM3[1].toString())
                binding.etDesvTipNetoM3.setText(desvTipicaNetoM3.toString())

                val listaNetoMes4 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes3
                )
                val varianzaNetoMes4 = calculoVarianzaNetoMes(listaNetoMes4)
                var desvTipicaNetoM4 = r.redondear(sqrt(varianzaNetoMes4))
                binding.etDesvTipNetoM4.setText(desvTipicaNetoM4.toString())
                val promedioNetoMes4 = promedioNetoMes(listaNetoMes4)
                binding.etPromNetoM4.setText(promedioNetoMes4.toString())
                var valoresIntervaloConfianzaM4 = calculoIntervaloConfianza95porc(listaNetoMes4)
                binding.etMinM4.setText(valoresIntervaloConfianzaM4[0].toString())
                binding.etMaxM4.setText(valoresIntervaloConfianzaM4[1].toString())
                binding.etDesvTipNetoM4.setText(desvTipicaNetoM4.toString())

                val listaNetoMes5 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes4
                )
                val varianzaNetoMes5 = calculoVarianzaNetoMes(listaNetoMes5)
                var desvTipicaNetoM5 = r.redondear(sqrt(varianzaNetoMes5))
                binding.etDesvTipNetoM5.setText(desvTipicaNetoM5.toString())
                val promedioNetoMes5 = promedioNetoMes(listaNetoMes5)
                binding.etPromNetoM5.setText(promedioNetoMes5.toString())
                var valoresIntervaloConfianzaM5 = calculoIntervaloConfianza95porc(listaNetoMes5)
                binding.etMinM5.setText(valoresIntervaloConfianzaM5[0].toString())
                binding.etMaxM5.setText(valoresIntervaloConfianzaM5[1].toString())
                binding.etDesvTipNetoM5.setText(desvTipicaNetoM5.toString())

                val listaNetoMes6 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes5
                )
                val varianzaNetoMes6 = calculoVarianzaNetoMes(listaNetoMes6)
                var desvTipicaNetoM6 = r.redondear(sqrt(varianzaNetoMes6))
                binding.etDesvTipNetoM6.setText(desvTipicaNetoM6.toString())
                val promedioNetoMes6= promedioNetoMes(listaNetoMes6)
                binding.etPromNetoM6.setText(promedioNetoMes6.toString())
                var valoresIntervaloConfianzaM6 = calculoIntervaloConfianza95porc(listaNetoMes6)
                binding.etMinM6.setText(valoresIntervaloConfianzaM6[0].toString())
                binding.etMaxM6.setText(valoresIntervaloConfianzaM6[1].toString())
                binding.etDesvTipNetoM6.setText(desvTipicaNetoM6.toString())

                val listaNetoMes7 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes6
                )
                val varianzaNetoMes7 = calculoVarianzaNetoMes(listaNetoMes7)
                var desvTipicaNetoM7 = r.redondear(sqrt(varianzaNetoMes7))
                binding.etDesvTipNetoM7.setText(desvTipicaNetoM7.toString())
                val promedioNetoMes7 = promedioNetoMes(listaNetoMes7)
                binding.etPromNetoM7.setText(promedioNetoMes7.toString())
                var valoresIntervaloConfianzaM7 = calculoIntervaloConfianza95porc(listaNetoMes7)
                binding.etMinM7.setText(valoresIntervaloConfianzaM7[0].toString())
                binding.etMaxM7.setText(valoresIntervaloConfianzaM7[1].toString())
                binding.etDesvTipNetoM7.setText(desvTipicaNetoM7.toString())


                val listaNetoMes8 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes7
                )
                val varianzaNetoMes8 = calculoVarianzaNetoMes(listaNetoMes8)
                var desvTipicaNetoM8 = r.redondear(sqrt(varianzaNetoMes8))
                binding.etDesvTipNetoM8.setText(desvTipicaNetoM8.toString())
                val promedioNetoMes8 = promedioNetoMes(listaNetoMes8)
                binding.etPromNetoM8.setText(promedioNetoMes8.toString())
                var valoresIntervaloConfianzaM8 = calculoIntervaloConfianza95porc(listaNetoMes8)
                binding.etMinM8.setText(valoresIntervaloConfianzaM8[0].toString())
                binding.etMaxM8.setText(valoresIntervaloConfianzaM8[1].toString())
                binding.etDesvTipNetoM8.setText(desvTipicaNetoM8.toString())

                val listaNetoMes9 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes8
                )
                val varianzaNetoMes9 = calculoVarianzaNetoMes(listaNetoMes9)
                var desvTipicaNetoM9 = r.redondear(sqrt(varianzaNetoMes9))
                binding.etDesvTipNetoM9.setText(desvTipicaNetoM9.toString())
                val promedioNetoMes9 = promedioNetoMes(listaNetoMes9)
                binding.etPromNetoM9.setText(promedioNetoMes9.toString())
                var valoresIntervaloConfianzaM9 = calculoIntervaloConfianza95porc(listaNetoMes9)
                binding.etMinM9.setText(valoresIntervaloConfianzaM9[0].toString())
                binding.etMaxM9.setText(valoresIntervaloConfianzaM9[1].toString())
                binding.etDesvTipNetoM9.setText(desvTipicaNetoM9.toString())

                val listaNetoMes10 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes9
                )
                val varianzaNetoMes10 = calculoVarianzaNetoMes(listaNetoMes10)
                var desvTipicaNetoM10 = r.redondear(sqrt(varianzaNetoMes10))
                binding.etDesvTipNetoM10.setText(desvTipicaNetoM10.toString())
                val promedioNetoMes10 = promedioNetoMes(listaNetoMes10)
                binding.etPromNetoM10.setText(promedioNetoMes10.toString())
                var valoresIntervaloConfianzaM10 = calculoIntervaloConfianza95porc(listaNetoMes10)
                binding.etMinM10.setText(valoresIntervaloConfianzaM10[0].toString())
                binding.etMaxM10.setText(valoresIntervaloConfianzaM10[1].toString())
                binding.etDesvTipNetoM10.setText(desvTipicaNetoM10.toString())

                val listaNetoMes11 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes10
                )
                val varianzaNetoMes11 = calculoVarianzaNetoMes(listaNetoMes11)
                var desvTipicaNetoM11 = r.redondear(sqrt(varianzaNetoMes11))
                binding.etDesvTipNetoM11.setText(desvTipicaNetoM11.toString())
                val promedioNetoMes11 = promedioNetoMes(listaNetoMes11)
                binding.etPromNetoM11.setText(promedioNetoMes11.toString())
                var valoresIntervaloConfianzaM11 = calculoIntervaloConfianza95porc(listaNetoMes11)
                binding.etMinM11.setText(valoresIntervaloConfianzaM11[0].toString())
                binding.etMaxM11.setText(valoresIntervaloConfianzaM11[1].toString())
                binding.etDesvTipNetoM11.setText(desvTipicaNetoM11.toString())

                val listaNetoMes12 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes11
                )
                val varianzaNetoMes12 = calculoVarianzaNetoMes(listaNetoMes12)
                var desvTipicaNetoM12 = r.redondear(sqrt(varianzaNetoMes12))
                binding.etDesvTipNetoM12.setText(desvTipicaNetoM12.toString())
                val promedioNetoMes12= promedioNetoMes(listaNetoMes12)
                binding.etPromNetoM12.setText(promedioNetoMes12.toString())
                var valoresIntervaloConfianzaM12 = calculoIntervaloConfianza95porc(listaNetoMes12)
                binding.etMinM12.setText(valoresIntervaloConfianzaM12[0].toString())
                binding.etMaxM12.setText(valoresIntervaloConfianzaM12[1].toString())
                binding.etDesvTipNetoM12.setText(desvTipicaNetoM12.toString())

                val listaNetoMes13 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes12
                )
                val varianzaNetoMes13 = calculoVarianzaNetoMes(listaNetoMes13)
                var desvTipicaNetoM13 = r.redondear(sqrt(varianzaNetoMes13))
                binding.etDesvTipNetoM13.setText(desvTipicaNetoM13.toString())
                val promedioNetoMes13 = promedioNetoMes(listaNetoMes13)
                binding.etPromNetoM13.setText(promedioNetoMes13.toString())
                var valoresIntervaloConfianzaM13 = calculoIntervaloConfianza95porc(listaNetoMes13)
                binding.etMinM13.setText(valoresIntervaloConfianzaM13[0].toString())
                binding.etMaxM13.setText(valoresIntervaloConfianzaM13[1].toString())
                binding.etDesvTipNetoM13.setText(desvTipicaNetoM13.toString())

                val listaNetoMes14 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes13
                )
                val varianzaNetoMes14 = calculoVarianzaNetoMes(listaNetoMes14)
                var desvTipicaNetoM14 = r.redondear(sqrt(varianzaNetoMes14))
                binding.etDesvTipNetoM14.setText(desvTipicaNetoM14.toString())
                val promedioNetoMes14 = promedioNetoMes(listaNetoMes14)
                binding.etPromNetoM14.setText(promedioNetoMes14.toString())
                var valoresIntervaloConfianzaM14 = calculoIntervaloConfianza95porc(listaNetoMes14)
                binding.etMinM14.setText(valoresIntervaloConfianzaM14[0].toString())
                binding.etMaxM14.setText(valoresIntervaloConfianzaM14[1].toString())
                binding.etDesvTipNetoM14.setText(desvTipicaNetoM14.toString())

                val listaNetoMes15 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes14
                )
                val varianzaNetoMes15 = calculoVarianzaNetoMes(listaNetoMes15)
                var desvTipicaNetoM15 = r.redondear(sqrt(varianzaNetoMes15))
                binding.etDesvTipNetoM15.setText(desvTipicaNetoM15.toString())
                val promedioNetoMes15 = promedioNetoMes(listaNetoMes15)
                binding.etPromNetoM15.setText(promedioNetoMes15.toString())
                var valoresIntervaloConfianzaM15 = calculoIntervaloConfianza95porc(listaNetoMes15)
                binding.etMinM15.setText(valoresIntervaloConfianzaM15[0].toString())
                binding.etMaxM15.setText(valoresIntervaloConfianzaM15[1].toString())
                binding.etDesvTipNetoM15.setText(desvTipicaNetoM15.toString())

                val listaNetoMes16 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes15
                )
                val varianzaNetoMes16 = calculoVarianzaNetoMes(listaNetoMes16)
                var desvTipicaNetoM16 = r.redondear(sqrt(varianzaNetoMes16))
                binding.etDesvTipNetoM16.setText(desvTipicaNetoM16.toString())
                val promedioNetoMes16= promedioNetoMes(listaNetoMes16)
                binding.etPromNetoM16.setText(promedioNetoMes16.toString())
                var valoresIntervaloConfianzaM16 = calculoIntervaloConfianza95porc(listaNetoMes16)
                binding.etMinM16.setText(valoresIntervaloConfianzaM16[0].toString())
                binding.etMaxM16.setText(valoresIntervaloConfianzaM16[1].toString())
                binding.etDesvTipNetoM16.setText(desvTipicaNetoM16.toString())

                val listaNetoMes17 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes16
                )
                val varianzaNetoMes17 = calculoVarianzaNetoMes(listaNetoMes17)
                var desvTipicaNetoM17 = r.redondear(sqrt(varianzaNetoMes17))
                binding.etDesvTipNetoM17.setText(desvTipicaNetoM17.toString())
                val promedioNetoMes17 = promedioNetoMes(listaNetoMes17)
                binding.etPromNetoM17.setText(promedioNetoMes17.toString())
                var valoresIntervaloConfianzaM17 = calculoIntervaloConfianza95porc(listaNetoMes17)
                binding.etMinM17.setText(valoresIntervaloConfianzaM17[0].toString())
                binding.etMaxM17.setText(valoresIntervaloConfianzaM17[1].toString())
                binding.etDesvTipNetoM17.setText(desvTipicaNetoM17.toString())


                val listaNetoMes18 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes17
                )
                val varianzaNetoMes18 = calculoVarianzaNetoMes(listaNetoMes18)
                var desvTipicaNetoM18 = r.redondear(sqrt(varianzaNetoMes18))
                binding.etDesvTipNetoM18.setText(desvTipicaNetoM18.toString())
                val promedioNetoMes18 = promedioNetoMes(listaNetoMes18)
                binding.etPromNetoM18.setText(promedioNetoMes18.toString())
                var valoresIntervaloConfianzaM18 = calculoIntervaloConfianza95porc(listaNetoMes18)
                binding.etMinM18.setText(valoresIntervaloConfianzaM18[0].toString())
                binding.etMaxM18.setText(valoresIntervaloConfianzaM18[1].toString())
                binding.etDesvTipNetoM18.setText(desvTipicaNetoM18.toString())

                val listaNetoMes19 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes18
                )
                val varianzaNetoMes19 = calculoVarianzaNetoMes(listaNetoMes19)
                var desvTipicaNetoM19 = r.redondear(sqrt(varianzaNetoMes19))
                binding.etDesvTipNetoM19.setText(desvTipicaNetoM19.toString())
                val promedioNetoMes19 = promedioNetoMes(listaNetoMes19)
                binding.etPromNetoM19.setText(promedioNetoMes19.toString())
                var valoresIntervaloConfianzaM19 = calculoIntervaloConfianza95porc(listaNetoMes19)
                binding.etMinM19.setText(valoresIntervaloConfianzaM19[0].toString())
                binding.etMaxM19.setText(valoresIntervaloConfianzaM19[1].toString())
                binding.etDesvTipNetoM19.setText(desvTipicaNetoM19.toString())

                val listaNetoMes20 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes19
                )
                val varianzaNetoMes20 = calculoVarianzaNetoMes(listaNetoMes20)
                var desvTipicaNetoM20 = r.redondear(sqrt(varianzaNetoMes20))
                binding.etDesvTipNetoM20.setText(desvTipicaNetoM20.toString())
                val promedioNetoMes20 = promedioNetoMes(listaNetoMes20)
                binding.etPromNetoM20.setText(promedioNetoMes20.toString())
                var valoresIntervaloConfianzaM20 = calculoIntervaloConfianza95porc(listaNetoMes20)
                binding.etMinM20.setText(valoresIntervaloConfianzaM20[0].toString())
                binding.etMaxM20.setText(valoresIntervaloConfianzaM20[1].toString())
                binding.etDesvTipNetoM20.setText(desvTipicaNetoM20.toString())

                val listaNetoMes21 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes20
                )
                val varianzaNetoMes21 = calculoVarianzaNetoMes(listaNetoMes21)
                var desvTipicaNetoM21 = r.redondear(sqrt(varianzaNetoMes21))
                binding.etDesvTipNetoM21.setText(desvTipicaNetoM21.toString())
                val promedioNetoMes21 = promedioNetoMes(listaNetoMes21)
                binding.etPromNetoM21.setText(promedioNetoMes21.toString())
                var valoresIntervaloConfianzaM21 = calculoIntervaloConfianza95porc(listaNetoMes21)
                binding.etMinM21.setText(valoresIntervaloConfianzaM21[0].toString())
                binding.etMaxM21.setText(valoresIntervaloConfianzaM21[1].toString())
                binding.etDesvTipNetoM21.setText(desvTipicaNetoM21.toString())

                val listaNetoMes22 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes21
                )
                val varianzaNetoMes22 = calculoVarianzaNetoMes(listaNetoMes22)
                var desvTipicaNetoM22 = r.redondear(sqrt(varianzaNetoMes22))
                binding.etDesvTipNetoM22.setText(desvTipicaNetoM22.toString())
                val promedioNetoMes22 = promedioNetoMes(listaNetoMes22)
                binding.etPromNetoM22.setText(promedioNetoMes22.toString())
                var valoresIntervaloConfianzaM22 = calculoIntervaloConfianza95porc(listaNetoMes22)
                binding.etMinM22.setText(valoresIntervaloConfianzaM22[0].toString())
                binding.etMaxM22.setText(valoresIntervaloConfianzaM22[1].toString())
                binding.etDesvTipNetoM22.setText(desvTipicaNetoM22.toString())

                val listaNetoMes23 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes22
                )
                val varianzaNetoMes23 = calculoVarianzaNetoMes(listaNetoMes23)
                var desvTipicaNetoM23 = r.redondear(sqrt(varianzaNetoMes23))
                binding.etDesvTipNetoM23.setText(desvTipicaNetoM23.toString())
                val promedioNetoMes23 = promedioNetoMes(listaNetoMes23)
                binding.etPromNetoM23.setText(promedioNetoMes23.toString())
                var valoresIntervaloConfianzaM23 = calculoIntervaloConfianza95porc(listaNetoMes23)
                binding.etMinM23.setText(valoresIntervaloConfianzaM23[0].toString())
                binding.etMaxM23.setText(valoresIntervaloConfianzaM23[1].toString())
                binding.etDesvTipNetoM23.setText(desvTipicaNetoM23.toString())

                val listaNetoMes24 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes23
                )
                val varianzaNetoMes24 = calculoVarianzaNetoMes(listaNetoMes24)
                var desvTipicaNetoM24 = r.redondear(sqrt(varianzaNetoMes24))
                binding.etDesvTipNetoM24.setText(desvTipicaNetoM24.toString())
                val promedioNetoMes24 = promedioNetoMes(listaNetoMes24)
                binding.etPromNetoM24.setText(promedioNetoMes24.toString())
                var valoresIntervaloConfianzaM24 = calculoIntervaloConfianza95porc(listaNetoMes24)
                binding.etMinM24.setText(valoresIntervaloConfianzaM24[0].toString())
                binding.etMaxM24.setText(valoresIntervaloConfianzaM24[1].toString())
                binding.etDesvTipNetoM24.setText(desvTipicaNetoM24.toString())

                val listaNetoMes25 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes24
                )
                val varianzaNetoMes25 = calculoVarianzaNetoMes(listaNetoMes25)
                var desvTipicaNetoM25 = r.redondear(sqrt(varianzaNetoMes25))
                binding.etDesvTipNetoM25.setText(desvTipicaNetoM25.toString())
                val promedioNetoMes25 = promedioNetoMes(listaNetoMes25)
                binding.etPromNetoM25.setText(promedioNetoMes25.toString())
                var valoresIntervaloConfianzaM25 = calculoIntervaloConfianza95porc(listaNetoMes25)
                binding.etMinM25.setText(valoresIntervaloConfianzaM25[0].toString())
                binding.etMaxM25.setText(valoresIntervaloConfianzaM25[1].toString())
                binding.etDesvTipNetoM25.setText(desvTipicaNetoM25.toString())

                val listaNetoMes26 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes25
                )
                val varianzaNetoMes26 = calculoVarianzaNetoMes(listaNetoMes26)
                var desvTipicaNetoM26 = r.redondear(sqrt(varianzaNetoMes6))
                binding.etDesvTipNetoM26.setText(desvTipicaNetoM26.toString())
                val promedioNetoMes26= promedioNetoMes(listaNetoMes26)
                binding.etPromNetoM26.setText(promedioNetoMes26.toString())
                var valoresIntervaloConfianzaM26 = calculoIntervaloConfianza95porc(listaNetoMes26)
                binding.etMinM26.setText(valoresIntervaloConfianzaM26[0].toString())
                binding.etMaxM26.setText(valoresIntervaloConfianzaM26[1].toString())
                binding.etDesvTipNetoM26.setText(desvTipicaNetoM26.toString())


                val listaNetoMes27 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes26
                )
                val varianzaNetoMes27 = calculoVarianzaNetoMes(listaNetoMes27)
                var desvTipicaNetoM27 = r.redondear(sqrt(varianzaNetoMes27))
                binding.etDesvTipNetoM27.setText(desvTipicaNetoM27.toString())
                val promedioNetoMes27 = promedioNetoMes(listaNetoMes27)
                binding.etPromNetoM27.setText(promedioNetoMes27.toString())
                var valoresIntervaloConfianzaM27 = calculoIntervaloConfianza95porc(listaNetoMes27)
                binding.etMinM27.setText(valoresIntervaloConfianzaM27[0].toString())
                binding.etMaxM27.setText(valoresIntervaloConfianzaM27[1].toString())
                binding.etDesvTipNetoM27.setText(desvTipicaNetoM27.toString())


                val listaNetoMes28 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes27
                )
                val varianzaNetoMes28 = calculoVarianzaNetoMes(listaNetoMes28)
                var desvTipicaNetoM28 = r.redondear(sqrt(varianzaNetoMes28))
                binding.etDesvTipNetoM28.setText(desvTipicaNetoM28.toString())
                val promedioNetoMes28 = promedioNetoMes(listaNetoMes28)
                binding.etPromNetoM28.setText(promedioNetoMes28.toString())
                var valoresIntervaloConfianzaM28 = calculoIntervaloConfianza95porc(listaNetoMes28)
                binding.etMinM28.setText(valoresIntervaloConfianzaM28[0].toString())
                binding.etMaxM28.setText(valoresIntervaloConfianzaM28[1].toString())
                binding.etDesvTipNetoM28.setText(desvTipicaNetoM28.toString())

                val listaNetoMes29 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes28
                )
                val varianzaNetoMes29 = calculoVarianzaNetoMes(listaNetoMes29)
                var desvTipicaNetoM29 = r.redondear(sqrt(varianzaNetoMes29))
                binding.etDesvTipNetoM29.setText(desvTipicaNetoM29.toString())
                val promedioNetoMes29 = promedioNetoMes(listaNetoMes29)
                binding.etPromNetoM29.setText(promedioNetoMes29.toString())
                var valoresIntervaloConfianzaM29 = calculoIntervaloConfianza95porc(listaNetoMes29)
                binding.etMinM29.setText(valoresIntervaloConfianzaM29[0].toString())
                binding.etMaxM29.setText(valoresIntervaloConfianzaM29[1].toString())
                binding.etDesvTipNetoM29.setText(desvTipicaNetoM29.toString())

                val listaNetoMes30 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes29
                )
                val varianzaNetoMes30 = calculoVarianzaNetoMes(listaNetoMes30)
                var desvTipicaNetoM30 = r.redondear(sqrt(varianzaNetoMes30))
                binding.etDesvTipNetoM30.setText(desvTipicaNetoM30.toString())
                val promedioNetoMes30 = promedioNetoMes(listaNetoMes30)
                binding.etPromNetoM30.setText(promedioNetoMes30.toString())
                var valoresIntervaloConfianzaM30 = calculoIntervaloConfianza95porc(listaNetoMes30)
                binding.etMinM30.setText(valoresIntervaloConfianzaM30[0].toString())
                binding.etMaxM30.setText(valoresIntervaloConfianzaM30[1].toString())
                binding.etDesvTipNetoM30.setText(desvTipicaNetoM30.toString())

                val listaNetoMes31 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes30
                )
                val varianzaNetoMes31 = calculoVarianzaNetoMes(listaNetoMes31)
                var desvTipicaNetoM31 = r.redondear(sqrt(varianzaNetoMes31))
                binding.etDesvTipNetoM31.setText(desvTipicaNetoM31.toString())
                val promedioNetoMes31 = promedioNetoMes(listaNetoMes31)
                binding.etPromNetoM31.setText(promedioNetoMes31.toString())
                var valoresIntervaloConfianzaM31 = calculoIntervaloConfianza95porc(listaNetoMes31)
                binding.etMinM31.setText(valoresIntervaloConfianzaM31[0].toString())
                binding.etMaxM31.setText(valoresIntervaloConfianzaM31[1].toString())
                binding.etDesvTipNetoM31.setText(desvTipicaNetoM31.toString())


                val listaNetoMes32 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes31
                )
                val varianzaNetoMes32 = calculoVarianzaNetoMes(listaNetoMes32)
                var desvTipicaNetoM32 = r.redondear(sqrt(varianzaNetoMes32))
                binding.etDesvTipNetoM32.setText(desvTipicaNetoM32.toString())
                val promedioNetoMes32 = promedioNetoMes(listaNetoMes32)
                binding.etPromNetoM32.setText(promedioNetoMes32.toString())
                var valoresIntervaloConfianzaM32 = calculoIntervaloConfianza95porc(listaNetoMes32)
                binding.etMinM32.setText(valoresIntervaloConfianzaM32[0].toString())
                binding.etMaxM32.setText(valoresIntervaloConfianzaM32[1].toString())
                binding.etDesvTipNetoM32.setText(desvTipicaNetoM32.toString())

                val listaNetoMes33 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes32
                )
                val varianzaNetoMes33 = calculoVarianzaNetoMes(listaNetoMes33)
                var desvTipicaNetoM33 = r.redondear(sqrt(varianzaNetoMes33))
                binding.etDesvTipNetoM33.setText(desvTipicaNetoM33.toString())
                val promedioNetoMes33 = promedioNetoMes(listaNetoMes33)
                binding.etPromNetoM33.setText(promedioNetoMes33.toString())
                var valoresIntervaloConfianzaM33 = calculoIntervaloConfianza95porc(listaNetoMes33)
                binding.etMinM33.setText(valoresIntervaloConfianzaM33[0].toString())
                binding.etMaxM33.setText(valoresIntervaloConfianzaM33[1].toString())
                binding.etDesvTipNetoM33.setText(desvTipicaNetoM33.toString())

                val listaNetoMes34 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes33
                )
                val varianzaNetoMes34 = calculoVarianzaNetoMes(listaNetoMes34)
                var desvTipicaNetoM34 = r.redondear(sqrt(varianzaNetoMes34))
                binding.etDesvTipNetoM34.setText(desvTipicaNetoM34.toString())
                val promedioNetoMes34 = promedioNetoMes(listaNetoMes34)
                binding.etPromNetoM34.setText(promedioNetoMes4.toString())
                var valoresIntervaloConfianzaM34 = calculoIntervaloConfianza95porc(listaNetoMes34)
                binding.etMinM34.setText(valoresIntervaloConfianzaM34[0].toString())
                binding.etMaxM34.setText(valoresIntervaloConfianzaM34[1].toString())
                binding.etDesvTipNetoM34.setText(desvTipicaNetoM34.toString())

                val listaNetoMes35 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes34
                )
                val varianzaNetoMes35 = calculoVarianzaNetoMes(listaNetoMes35)
                var desvTipicaNetoM35 = r.redondear(sqrt(varianzaNetoMes35))
                binding.etDesvTipNetoM35.setText(desvTipicaNetoM35.toString())
                val promedioNetoMes35 = promedioNetoMes(listaNetoMes35)
                binding.etPromNetoM35.setText(promedioNetoMes35.toString())
                var valoresIntervaloConfianzaM35 = calculoIntervaloConfianza95porc(listaNetoMes35)
                binding.etMinM35.setText(valoresIntervaloConfianzaM35[0].toString())
                binding.etMaxM35.setText(valoresIntervaloConfianzaM35[1].toString())
                binding.etDesvTipNetoM35.setText(desvTipicaNetoM35.toString())

                val listaNetoMes36 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes35
                )
                val varianzaNetoMes36 = calculoVarianzaNetoMes(listaNetoMes36)
                var desvTipicaNetoM36 = r.redondear(sqrt(varianzaNetoMes36))
                binding.etDesvTipNetoM36.setText(desvTipicaNetoM36.toString())
                val promedioNetoMes36= promedioNetoMes(listaNetoMes36)
                binding.etPromNetoM36.setText(promedioNetoMes36.toString())
                var valoresIntervaloConfianzaM36 = calculoIntervaloConfianza95porc(listaNetoMes36)
                binding.etMinM36.setText(valoresIntervaloConfianzaM36[0].toString())
                binding.etMaxM36.setText(valoresIntervaloConfianzaM36[1].toString())
                binding.etDesvTipNetoM36.setText(desvTipicaNetoM36.toString())

                val listaNetoMes37 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes36
                )
                val varianzaNetoMes37 = calculoVarianzaNetoMes(listaNetoMes37)
                var desvTipicaNetoM37 = r.redondear(sqrt(varianzaNetoMes37))
                binding.etDesvTipNetoM37.setText(desvTipicaNetoM37.toString())
                val promedioNetoMes37 = promedioNetoMes(listaNetoMes37)
                binding.etPromNetoM37.setText(promedioNetoMes37.toString())
                var valoresIntervaloConfianzaM37 = calculoIntervaloConfianza95porc(listaNetoMes37)
                binding.etMinM37.setText(valoresIntervaloConfianzaM37[0].toString())
                binding.etMaxM37.setText(valoresIntervaloConfianzaM37[1].toString())
                binding.etDesvTipNetoM37.setText(desvTipicaNetoM37.toString())


                val listaNetoMes38 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes37
                )
                val varianzaNetoMes38 = calculoVarianzaNetoMes(listaNetoMes38)
                var desvTipicaNetoM38 = r.redondear(sqrt(varianzaNetoMes38))
                binding.etDesvTipNetoM38.setText(desvTipicaNetoM38.toString())
                val promedioNetoMes38 = promedioNetoMes(listaNetoMes38)
                binding.etPromNetoM38.setText(promedioNetoMes38.toString())
                var valoresIntervaloConfianzaM38 = calculoIntervaloConfianza95porc(listaNetoMes38)
                binding.etMinM38.setText(valoresIntervaloConfianzaM38[0].toString())
                binding.etMaxM38.setText(valoresIntervaloConfianzaM38[1].toString())
                binding.etDesvTipNetoM38.setText(desvTipicaNetoM38.toString())

                val listaNetoMes39 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes38
                )
                val varianzaNetoMes39 = calculoVarianzaNetoMes(listaNetoMes39)
                var desvTipicaNetoM39 = r.redondear(sqrt(varianzaNetoMes39))
                binding.etDesvTipNetoM39.setText(desvTipicaNetoM39.toString())
                val promedioNetoMes39 = promedioNetoMes(listaNetoMes39)
                binding.etPromNetoM39.setText(promedioNetoMes39.toString())
                var valoresIntervaloConfianzaM39 = calculoIntervaloConfianza95porc(listaNetoMes39)
                binding.etMinM39.setText(valoresIntervaloConfianzaM39[0].toString())
                binding.etMaxM39.setText(valoresIntervaloConfianzaM39[1].toString())
                binding.etDesvTipNetoM39.setText(desvTipicaNetoM39.toString())

                val listaNetoMes40 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes39
                )
                val varianzaNetoMes40 = calculoVarianzaNetoMes(listaNetoMes40)
                var desvTipicaNetoM40 = r.redondear(sqrt(varianzaNetoMes40))
                binding.etDesvTipNetoM40.setText(desvTipicaNetoM40.toString())
                val promedioNetoMes40 = promedioNetoMes(listaNetoMes40)
                binding.etPromNetoM40.setText(promedioNetoMes40.toString())
                var valoresIntervaloConfianzaM40 = calculoIntervaloConfianza95porc(listaNetoMes40)
                binding.etMinM40.setText(valoresIntervaloConfianzaM40[0].toString())
                binding.etMaxM40.setText(valoresIntervaloConfianzaM40[1].toString())
                binding.etDesvTipNetoM40.setText(desvTipicaNetoM40.toString())

                val listaNetoMes41 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes40
                )
                val varianzaNetoMes41 = calculoVarianzaNetoMes(listaNetoMes41)
                var desvTipicaNetoM41 = r.redondear(sqrt(varianzaNetoMes41))
                binding.etDesvTipNetoM41.setText(desvTipicaNetoM41.toString())
                val promedioNetoMes41 = promedioNetoMes(listaNetoMes41)
                binding.etPromNetoM41.setText(promedioNetoMes41.toString())
                var valoresIntervaloConfianzaM41 = calculoIntervaloConfianza95porc(listaNetoMes41)
                binding.etMinM41.setText(valoresIntervaloConfianzaM41[0].toString())
                binding.etMaxM41.setText(valoresIntervaloConfianzaM41[1].toString())
                binding.etDesvTipNetoM41.setText(desvTipicaNetoM41.toString())


                val listaNetoMes42 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes41
                )
                val varianzaNetoMes42 = calculoVarianzaNetoMes(listaNetoMes42)
                var desvTipicaNetoM42 = r.redondear(sqrt(varianzaNetoMes42))
                binding.etDesvTipNetoM42.setText(desvTipicaNetoM42.toString())
                val promedioNetoMes42 = promedioNetoMes(listaNetoMes42)
                binding.etPromNetoM42.setText(promedioNetoMes42.toString())
                var valoresIntervaloConfianzaM42 = calculoIntervaloConfianza95porc(listaNetoMes42)
                binding.etMinM42.setText(valoresIntervaloConfianzaM42[0].toString())
                binding.etMaxM42.setText(valoresIntervaloConfianzaM42[1].toString())
                binding.etDesvTipNetoM42.setText(desvTipicaNetoM42.toString())

                val listaNetoMes43 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes42
                )
                val varianzaNetoMes43 = calculoVarianzaNetoMes(listaNetoMes43)
                var desvTipicaNetoM43 = r.redondear(sqrt(varianzaNetoMes43))
                binding.etDesvTipNetoM43.setText(desvTipicaNetoM43.toString())
                val promedioNetoMes43 = promedioNetoMes(listaNetoMes43)
                binding.etPromNetoM43.setText(promedioNetoMes43.toString())
                var valoresIntervaloConfianzaM43 = calculoIntervaloConfianza95porc(listaNetoMes43)
                binding.etMinM43.setText(valoresIntervaloConfianzaM43[0].toString())
                binding.etMaxM43.setText(valoresIntervaloConfianzaM43[1].toString())
                binding.etDesvTipNetoM43.setText(desvTipicaNetoM43.toString())

                val listaNetoMes44 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes43
                )
                val varianzaNetoMes44= calculoVarianzaNetoMes(listaNetoMes44)
                var desvTipicaNetoM44 = r.redondear(sqrt(varianzaNetoMes44))
                binding.etDesvTipNetoM44.setText(desvTipicaNetoM44.toString())
                val promedioNetoMes44 = promedioNetoMes(listaNetoMes44)
                binding.etPromNetoM44.setText(promedioNetoMes44.toString())
                var valoresIntervaloConfianzaM44 = calculoIntervaloConfianza95porc(listaNetoMes44)
                binding.etMinM44.setText(valoresIntervaloConfianzaM44[0].toString())
                binding.etMaxM44.setText(valoresIntervaloConfianzaM44[1].toString())
                binding.etDesvTipNetoM44.setText(desvTipicaNetoM44.toString())

                val listaNetoMes45 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes44
                )
                val varianzaNetoMes45 = calculoVarianzaNetoMes(listaNetoMes45)
                var desvTipicaNetoM45 = r.redondear(sqrt(varianzaNetoMes45))
                binding.etDesvTipNetoM45.setText(desvTipicaNetoM45.toString())
                val promedioNetoMes45 = promedioNetoMes(listaNetoMes45)
                binding.etPromNetoM45.setText(promedioNetoMes45.toString())
                var valoresIntervaloConfianzaM45 = calculoIntervaloConfianza95porc(listaNetoMes45)
                binding.etMinM45.setText(valoresIntervaloConfianzaM45[0].toString())
                binding.etMaxM45.setText(valoresIntervaloConfianzaM45[1].toString())
                binding.etDesvTipNetoM45.setText(desvTipicaNetoM45.toString())

                val listaNetoMes46 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes45
                )
                val varianzaNetoMes46 = calculoVarianzaNetoMes(listaNetoMes46)
                var desvTipicaNetoM46 = r.redondear(sqrt(varianzaNetoMes46))
                binding.etDesvTipNetoM46.setText(desvTipicaNetoM46.toString())
                val promedioNetoMes46= promedioNetoMes(listaNetoMes46)
                binding.etPromNetoM46.setText(promedioNetoMes46.toString())
                var valoresIntervaloConfianzaM46 = calculoIntervaloConfianza95porc(listaNetoMes46)
                binding.etMinM46.setText(valoresIntervaloConfianzaM46[0].toString())
                binding.etMaxM46.setText(valoresIntervaloConfianzaM46[1].toString())
                binding.etDesvTipNetoM46.setText(desvTipicaNetoM46.toString())

                val listaNetoMes47 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes46
                )
                val varianzaNetoMes47 = calculoVarianzaNetoMes(listaNetoMes47)
                var desvTipicaNetoM47 = r.redondear(sqrt(varianzaNetoMes47))
                binding.etDesvTipNetoM47.setText(desvTipicaNetoM47.toString())
                val promedioNetoMes47 = promedioNetoMes(listaNetoMes47)
                binding.etPromNetoM47.setText(promedioNetoMes47.toString())
                var valoresIntervaloConfianzaM47 = calculoIntervaloConfianza95porc(listaNetoMes47)
                binding.etMinM47.setText(valoresIntervaloConfianzaM47[0].toString())
                binding.etMaxM47.setText(valoresIntervaloConfianzaM47[1].toString())
                binding.etDesvTipNetoM47.setText(desvTipicaNetoM47.toString())


                val listaNetoMes48 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes47
                )
                val varianzaNetoMes48 = calculoVarianzaNetoMes(listaNetoMes48)
                var desvTipicaNetoM48 = r.redondear(sqrt(varianzaNetoMes48))
                binding.etDesvTipNetoM48.setText(desvTipicaNetoM48.toString())
                val promedioNetoMes48 = promedioNetoMes(listaNetoMes48)
                binding.etPromNetoM48.setText(promedioNetoMes48.toString())
                var valoresIntervaloConfianzaM48 = calculoIntervaloConfianza95porc(listaNetoMes48)
                binding.etMinM48.setText(valoresIntervaloConfianzaM48[0].toString())
                binding.etMaxM48.setText(valoresIntervaloConfianzaM48[1].toString())
                binding.etDesvTipNetoM48.setText(desvTipicaNetoM48.toString())

                val listaNetoMes49 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes48
                )
                val varianzaNetoMes49 = calculoVarianzaNetoMes(listaNetoMes49)
                var desvTipicaNetoM49 = r.redondear(sqrt(varianzaNetoMes49))
                binding.etDesvTipNetoM49.setText(desvTipicaNetoM49.toString())
                val promedioNetoMes49 = promedioNetoMes(listaNetoMes49)
                binding.etPromNetoM49.setText(promedioNetoMes9.toString())
                var valoresIntervaloConfianzaM49 = calculoIntervaloConfianza95porc(listaNetoMes49)
                binding.etMinM49.setText(valoresIntervaloConfianzaM49[0].toString())
                binding.etMaxM49.setText(valoresIntervaloConfianzaM49[1].toString())
                binding.etDesvTipNetoM49.setText(desvTipicaNetoM49.toString())

                val listaNetoMes50 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes49
                )
                val varianzaNetoMes50 = calculoVarianzaNetoMes(listaNetoMes50)
                var desvTipicaNetoM50 = r.redondear(sqrt(varianzaNetoMes50))
                binding.etDesvTipNetoM50.setText(desvTipicaNetoM50.toString())
                val promedioNetoMes50 = promedioNetoMes(listaNetoMes50)
                binding.etPromNetoM50.setText(promedioNetoMes50.toString())
                var valoresIntervaloConfianzaM50 = calculoIntervaloConfianza95porc(listaNetoMes50)
                binding.etMinM50.setText(valoresIntervaloConfianzaM50[0].toString())
                binding.etMaxM50.setText(valoresIntervaloConfianzaM50[1].toString())
                binding.etDesvTipNetoM50.setText(desvTipicaNetoM50.toString())

                val listaNetoMes51 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes50
                )
                val varianzaNetoMes51 = calculoVarianzaNetoMes(listaNetoMes51)
                var desvTipicaNetoM51 = r.redondear(sqrt(varianzaNetoMes51))
                binding.etDesvTipNetoM51.setText(desvTipicaNetoM51.toString())
                val promedioNetoMes51 = promedioNetoMes(listaNetoMes51)
                binding.etPromNetoM51.setText(promedioNetoMes1.toString())
                var valoresIntervaloConfianzaM51 = calculoIntervaloConfianza95porc(listaNetoMes51)
                binding.etMinM51.setText(valoresIntervaloConfianzaM51[0].toString())
                binding.etMaxM51.setText(valoresIntervaloConfianzaM51[1].toString())
                binding.etDesvTipNetoM51.setText(desvTipicaNetoM51.toString())


                val listaNetoMes52 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes51
                )
                val varianzaNetoMes52 = calculoVarianzaNetoMes(listaNetoMes52)
                var desvTipicaNetoM52 = r.redondear(sqrt(varianzaNetoMes52))
                binding.etDesvTipNetoM52.setText(desvTipicaNetoM52.toString())
                val promedioNetoMes52 = promedioNetoMes(listaNetoMes52)
                binding.etPromNetoM52.setText(promedioNetoMes52.toString())
                var valoresIntervaloConfianzaM52 = calculoIntervaloConfianza95porc(listaNetoMes52)
                binding.etMinM52.setText(valoresIntervaloConfianzaM52[0].toString())
                binding.etMaxM52.setText(valoresIntervaloConfianzaM52[1].toString())
                binding.etDesvTipNetoM52.setText(desvTipicaNetoM52.toString())

                val listaNetoMes53 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes52
                )
                val varianzaNetoMes53 = calculoVarianzaNetoMes(listaNetoMes53)
                var desvTipicaNetoM53 = r.redondear(sqrt(varianzaNetoMes53))
                binding.etDesvTipNetoM53.setText(desvTipicaNetoM53.toString())
                val promedioNetoMes53 = promedioNetoMes(listaNetoMes53)
                binding.etPromNetoM53.setText(promedioNetoMes53.toString())
                var valoresIntervaloConfianzaM53 = calculoIntervaloConfianza95porc(listaNetoMes53)
                binding.etMinM53.setText(valoresIntervaloConfianzaM53[0].toString())
                binding.etMaxM53.setText(valoresIntervaloConfianzaM53[1].toString())
                binding.etDesvTipNetoM3.setText(desvTipicaNetoM53.toString())

                val listaNetoMes54 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes53
                )
                val varianzaNetoMes54 = calculoVarianzaNetoMes(listaNetoMes54)
                var desvTipicaNetoM54 = r.redondear(sqrt(varianzaNetoMes54))
                binding.etDesvTipNetoM54.setText(desvTipicaNetoM54.toString())
                val promedioNetoMes54 = promedioNetoMes(listaNetoMes54)
                binding.etPromNetoM54.setText(promedioNetoMes54.toString())
                var valoresIntervaloConfianzaM54 = calculoIntervaloConfianza95porc(listaNetoMes54)
                binding.etMinM54.setText(valoresIntervaloConfianzaM54[0].toString())
                binding.etMaxM54.setText(valoresIntervaloConfianzaM54[1].toString())
                binding.etDesvTipNetoM54.setText(desvTipicaNetoM54.toString())

                val listaNetoMes55 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes54
                )
                val varianzaNetoMes55 = calculoVarianzaNetoMes(listaNetoMes55)
                var desvTipicaNetoM55 = r.redondear(sqrt(varianzaNetoMes55))
                binding.etDesvTipNetoM55.setText(desvTipicaNetoM55.toString())
                val promedioNetoMes55 = promedioNetoMes(listaNetoMes55)
                binding.etPromNetoM55.setText(promedioNetoMes55.toString())
                var valoresIntervaloConfianzaM55 = calculoIntervaloConfianza95porc(listaNetoMes55)
                binding.etMinM55.setText(valoresIntervaloConfianzaM55[0].toString())
                binding.etMaxM55.setText(valoresIntervaloConfianzaM55[1].toString())
                binding.etDesvTipNetoM55.setText(desvTipicaNetoM55.toString())

                val listaNetoMes56 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes55
                )
                val varianzaNetoMes56 = calculoVarianzaNetoMes(listaNetoMes56)
                var desvTipicaNetoM56 = r.redondear(sqrt(varianzaNetoMes56))
                binding.etDesvTipNetoM56.setText(desvTipicaNetoM56.toString())
                val promedioNetoMes56= promedioNetoMes(listaNetoMes56)
                binding.etPromNetoM56.setText(promedioNetoMes56.toString())
                var valoresIntervaloConfianzaM56 = calculoIntervaloConfianza95porc(listaNetoMes56)
                binding.etMinM56.setText(valoresIntervaloConfianzaM56[0].toString())
                binding.etMaxM56.setText(valoresIntervaloConfianzaM56[1].toString())
                binding.etDesvTipNetoM56.setText(desvTipicaNetoM56.toString())

                val listaNetoMes57 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes56
                )
                val varianzaNetoMes57 = calculoVarianzaNetoMes(listaNetoMes57)
                var desvTipicaNetoM57 = r.redondear(sqrt(varianzaNetoMes57))
                binding.etDesvTipNetoM57.setText(desvTipicaNetoM57.toString())
                val promedioNetoMes57 = promedioNetoMes(listaNetoMes57)
                binding.etPromNetoM57.setText(promedioNetoMes57.toString())
                var valoresIntervaloConfianzaM57 = calculoIntervaloConfianza95porc(listaNetoMes57)
                binding.etMinM57.setText(valoresIntervaloConfianzaM57[0].toString())
                binding.etMaxM57.setText(valoresIntervaloConfianzaM57[1].toString())
                binding.etDesvTipNetoM57.setText(desvTipicaNetoM57.toString())


                val listaNetoMes58 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes57
                )
                val varianzaNetoMes58 = calculoVarianzaNetoMes(listaNetoMes58)
                var desvTipicaNetoM58 = r.redondear(sqrt(varianzaNetoMes58))
                binding.etDesvTipNetoM58.setText(desvTipicaNetoM58.toString())
                val promedioNetoMes58 = promedioNetoMes(listaNetoMes58)
                binding.etPromNetoM58.setText(promedioNetoMes58.toString())
                var valoresIntervaloConfianzaM58 = calculoIntervaloConfianza95porc(listaNetoMes58)
                binding.etMinM58.setText(valoresIntervaloConfianzaM58[0].toString())
                binding.etMaxM58.setText(valoresIntervaloConfianzaM58[1].toString())
                binding.etDesvTipNetoM58.setText(desvTipicaNetoM58.toString())

                val listaNetoMes59 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes58
                )
                val varianzaNetoMes59 = calculoVarianzaNetoMes(listaNetoMes59)
                var desvTipicaNetoM59 = r.redondear(sqrt(varianzaNetoMes59))
                binding.etDesvTipNetoM59.setText(desvTipicaNetoM59.toString())
                val promedioNetoMes59 = promedioNetoMes(listaNetoMes59)
                binding.etPromNetoM59.setText(promedioNetoMes59.toString())
                var valoresIntervaloConfianzaM59 = calculoIntervaloConfianza95porc(listaNetoMes59)
                binding.etMinM59.setText(valoresIntervaloConfianzaM59[0].toString())
                binding.etMaxM59.setText(valoresIntervaloConfianzaM59[1].toString())
                binding.etDesvTipNetoM59.setText(desvTipicaNetoM59.toString())

                val listaNetoMes60 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes59
                )
                val varianzaNetoMes60 = calculoVarianzaNetoMes(listaNetoMes60)
                var desvTipicaNetoM60= r.redondear(sqrt(varianzaNetoMes60))
                binding.etDesvTipNetoM60.setText(desvTipicaNetoM60.toString())
                val promedioNetoMes60 = promedioNetoMes(listaNetoMes60)
                binding.etPromNetoM60.setText(promedioNetoMes60.toString())
                var valoresIntervaloConfianzaM60 = calculoIntervaloConfianza95porc(listaNetoMes60)
                binding.etMinM60.setText(valoresIntervaloConfianzaM60[0].toString())
                binding.etMaxM60.setText(valoresIntervaloConfianzaM60[1].toString())
                binding.etDesvTipNetoM60.setText(desvTipicaNetoM60.toString())





            }
            else{
                val totalSueldoefectivoFinalProy: Double =0.0
                binding.etSaldoInicial.setText(totalSueldoefectivoFinalProy.toString())
                val entrada1: Double = 0.0
                val entrada2: Double = 0.0
                val entrada3: Double = 0.0

                val salida1: Double = 0.0
                val salida2: Double = 0.0
                val salida3: Double = 0.0
                val listaEntradas = mutableListOf<Double>(entrada1, entrada2, entrada3)
                val tamEnt: Double = listaEntradas.size.toDouble()
                var sumaEnt = 0.0
                var sumaSal = 0.0
                var varianzaEnt = 0.0
                var varianzaSal = 0.0


                val listaSalidas = mutableListOf<Double>(salida1, salida2, salida3)
                val tamSalida: Double = listaSalidas.size.toDouble()

                listaEntradas.forEach {
                    sumaEnt = it + sumaEnt
                }
                //calculo medias
                val mediaEntradas = r.redondear(sumaEnt / tamEnt)
                listaSalidas.forEach {
                    sumaSal = sumaSal + it
                }
                val mediaSalidas = r.redondear(sumaSal / tamSalida)

                listaEntradas.forEach {
                    varianzaEnt = ((mediaEntradas - it).pow(2.0) / tamEnt) + varianzaEnt
                }
                //calculo de las desv tipicas
                var desvTipicaEnt = r.redondear(sqrt(varianzaEnt))
                binding.etDesvTipEnt.setText(desvTipicaEnt.toString())

                listaSalidas.forEach {
                    varianzaSal = ((mediaSalidas - it).pow(2.0) / tamSalida) + varianzaSal
                }
                var desvTipicaSal = r.redondear(sqrt(varianzaSal))
                binding.etDesvTipSal.setText(desvTipicaSal.toString())

                binding.etMediaEnt.setText(mediaEntradas.toString())
                binding.etMediaSal.setText(mediaSalidas.toString())


                val listaNetoMes1 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    totalSueldoefectivoFinalProy
                )
                val varianzaNetoMes1 = calculoVarianzaNetoMes(listaNetoMes1)
                var desvTipicaNetoM1 = r.redondear(sqrt(varianzaNetoMes1))
                binding.etDesvTipNetoM1.setText(desvTipicaNetoM1.toString())
                val promedioNetoMes1 = promedioNetoMes(listaNetoMes1)
                binding.etPromNetoM1.setText(promedioNetoMes1.toString())
                var valoresIntervaloConfianzaM1 = calculoIntervaloConfianza95porc(listaNetoMes1)
                binding.etMinM1.setText(valoresIntervaloConfianzaM1[0].toString())
                binding.etMaxM1.setText(valoresIntervaloConfianzaM1[1].toString())
                binding.etDesvTipNetoM1.setText(desvTipicaNetoM1.toString())


                val listaNetoMes2 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes1
                )
                val varianzaNetoMes2 = calculoVarianzaNetoMes(listaNetoMes2)
                var desvTipicaNetoM2 = r.redondear(sqrt(varianzaNetoMes2))
                binding.etDesvTipNetoM2.setText(desvTipicaNetoM2.toString())
                val promedioNetoMes2 = promedioNetoMes(listaNetoMes2)
                binding.etPromNetoM2.setText(promedioNetoMes2.toString())
                var valoresIntervaloConfianzaM2 = calculoIntervaloConfianza95porc(listaNetoMes2)
                binding.etMinM2.setText(valoresIntervaloConfianzaM2[0].toString())
                binding.etMaxM2.setText(valoresIntervaloConfianzaM2[1].toString())
                binding.etDesvTipNetoM2.setText(desvTipicaNetoM2.toString())

                val listaNetoMes3 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes2
                )
                val varianzaNetoMes3 = calculoVarianzaNetoMes(listaNetoMes3)
                var desvTipicaNetoM3 = r.redondear(sqrt(varianzaNetoMes3))
                binding.etDesvTipNetoM3.setText(desvTipicaNetoM3.toString())
                val promedioNetoMes3 = promedioNetoMes(listaNetoMes3)
                binding.etPromNetoM3.setText(promedioNetoMes3.toString())
                var valoresIntervaloConfianzaM3 = calculoIntervaloConfianza95porc(listaNetoMes3)
                binding.etMinM3.setText(valoresIntervaloConfianzaM3[0].toString())
                binding.etMaxM3.setText(valoresIntervaloConfianzaM3[1].toString())
                binding.etDesvTipNetoM3.setText(desvTipicaNetoM3.toString())

                val listaNetoMes4 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes3
                )
                val varianzaNetoMes4 = calculoVarianzaNetoMes(listaNetoMes4)
                var desvTipicaNetoM4 = r.redondear(sqrt(varianzaNetoMes4))
                binding.etDesvTipNetoM4.setText(desvTipicaNetoM4.toString())
                val promedioNetoMes4 = promedioNetoMes(listaNetoMes4)
                binding.etPromNetoM4.setText(promedioNetoMes4.toString())
                var valoresIntervaloConfianzaM4 = calculoIntervaloConfianza95porc(listaNetoMes4)
                binding.etMinM4.setText(valoresIntervaloConfianzaM4[0].toString())
                binding.etMaxM4.setText(valoresIntervaloConfianzaM4[1].toString())
                binding.etDesvTipNetoM4.setText(desvTipicaNetoM4.toString())

                val listaNetoMes5 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes4
                )
                val varianzaNetoMes5 = calculoVarianzaNetoMes(listaNetoMes5)
                var desvTipicaNetoM5 = r.redondear(sqrt(varianzaNetoMes5))
                binding.etDesvTipNetoM5.setText(desvTipicaNetoM5.toString())
                val promedioNetoMes5 = promedioNetoMes(listaNetoMes5)
                binding.etPromNetoM5.setText(promedioNetoMes5.toString())
                var valoresIntervaloConfianzaM5 = calculoIntervaloConfianza95porc(listaNetoMes5)
                binding.etMinM4.setText(valoresIntervaloConfianzaM5[0].toString())
                binding.etMaxM4.setText(valoresIntervaloConfianzaM5[1].toString())
                binding.etDesvTipNetoM4.setText(desvTipicaNetoM5.toString())

                val listaNetoMes6 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes5
                )
                val varianzaNetoMes6 = calculoVarianzaNetoMes(listaNetoMes6)
                var desvTipicaNetoM6 = r.redondear(sqrt(varianzaNetoMes6))
                binding.etDesvTipNetoM4.setText(desvTipicaNetoM4.toString())
                val promedioNetoMes6= promedioNetoMes(listaNetoMes6)
                binding.etPromNetoM6.setText(promedioNetoMes6.toString())
                var valoresIntervaloConfianzaM6 = calculoIntervaloConfianza95porc(listaNetoMes6)
                binding.etMinM6.setText(valoresIntervaloConfianzaM6[0].toString())
                binding.etMaxM6.setText(valoresIntervaloConfianzaM6[1].toString())
                binding.etDesvTipNetoM6.setText(desvTipicaNetoM6.toString())


                val listaNetoMes7 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes6
                )
                val varianzaNetoMes7 = calculoVarianzaNetoMes(listaNetoMes7)
                var desvTipicaNetoM7 = r.redondear(sqrt(varianzaNetoMes7))
                binding.etDesvTipNetoM7.setText(desvTipicaNetoM7.toString())
                val promedioNetoMes7 = promedioNetoMes(listaNetoMes7)
                binding.etPromNetoM7.setText(promedioNetoMes7.toString())
                var valoresIntervaloConfianzaM7 = calculoIntervaloConfianza95porc(listaNetoMes7)
                binding.etMinM7.setText(valoresIntervaloConfianzaM7[0].toString())
                binding.etMaxM7.setText(valoresIntervaloConfianzaM7[1].toString())
                binding.etDesvTipNetoM7.setText(desvTipicaNetoM7.toString())


                val listaNetoMes8 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes7
                )
                val varianzaNetoMes8 = calculoVarianzaNetoMes(listaNetoMes8)
                var desvTipicaNetoM8 = r.redondear(sqrt(varianzaNetoMes8))
                binding.etDesvTipNetoM8.setText(desvTipicaNetoM8.toString())
                val promedioNetoMes8 = promedioNetoMes(listaNetoMes8)
                binding.etPromNetoM8.setText(promedioNetoMes8.toString())
                var valoresIntervaloConfianzaM8 = calculoIntervaloConfianza95porc(listaNetoMes8)
                binding.etMinM8.setText(valoresIntervaloConfianzaM8[0].toString())
                binding.etMaxM8.setText(valoresIntervaloConfianzaM8[1].toString())
                binding.etDesvTipNetoM8.setText(desvTipicaNetoM8.toString())

                val listaNetoMes9 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes8
                )
                val varianzaNetoMes9 = calculoVarianzaNetoMes(listaNetoMes9)
                var desvTipicaNetoM9 = r.redondear(sqrt(varianzaNetoMes9))
                binding.etDesvTipNetoM3.setText(desvTipicaNetoM9.toString())
                val promedioNetoMes9 = promedioNetoMes(listaNetoMes9)
                binding.etPromNetoM9.setText(promedioNetoMes9.toString())
                var valoresIntervaloConfianzaM9 = calculoIntervaloConfianza95porc(listaNetoMes9)
                binding.etMinM9.setText(valoresIntervaloConfianzaM9[0].toString())
                binding.etMaxM9.setText(valoresIntervaloConfianzaM9[1].toString())
                binding.etDesvTipNetoM9.setText(desvTipicaNetoM9.toString())

                val listaNetoMes10 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes9
                )
                val varianzaNetoMes10 = calculoVarianzaNetoMes(listaNetoMes10)
                var desvTipicaNetoM10 = r.redondear(sqrt(varianzaNetoMes10))
                binding.etDesvTipNetoM10.setText(desvTipicaNetoM10.toString())
                val promedioNetoMes10 = promedioNetoMes(listaNetoMes10)
                binding.etPromNetoM10.setText(promedioNetoMes10.toString())
                var valoresIntervaloConfianzaM10 = calculoIntervaloConfianza95porc(listaNetoMes10)
                binding.etMinM10.setText(valoresIntervaloConfianzaM10[0].toString())
                binding.etMaxM10.setText(valoresIntervaloConfianzaM10[1].toString())
                binding.etDesvTipNetoM10.setText(desvTipicaNetoM10.toString())

                val listaNetoMes11 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes10
                )
                val varianzaNetoMes11 = calculoVarianzaNetoMes(listaNetoMes11)
                var desvTipicaNetoM11 = r.redondear(sqrt(varianzaNetoMes11))
                binding.etDesvTipNetoM11.setText(desvTipicaNetoM11.toString())
                val promedioNetoMes11 = promedioNetoMes(listaNetoMes11)
                binding.etPromNetoM11.setText(promedioNetoMes11.toString())
                var valoresIntervaloConfianzaM11 = calculoIntervaloConfianza95porc(listaNetoMes11)
                binding.etMinM11.setText(valoresIntervaloConfianzaM11[0].toString())
                binding.etMaxM11.setText(valoresIntervaloConfianzaM11[1].toString())
                binding.etDesvTipNetoM11.setText(desvTipicaNetoM11.toString())

                val listaNetoMes12 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes11
                )
                val varianzaNetoMes12 = calculoVarianzaNetoMes(listaNetoMes12)
                var desvTipicaNetoM12 = r.redondear(sqrt(varianzaNetoMes12))
                binding.etDesvTipNetoM12.setText(desvTipicaNetoM12.toString())
                val promedioNetoMes12= promedioNetoMes(listaNetoMes12)
                binding.etPromNetoM12.setText(promedioNetoMes12.toString())
                var valoresIntervaloConfianzaM12 = calculoIntervaloConfianza95porc(listaNetoMes12)
                binding.etMinM12.setText(valoresIntervaloConfianzaM12[0].toString())
                binding.etMaxM12.setText(valoresIntervaloConfianzaM12[1].toString())
                binding.etDesvTipNetoM12.setText(desvTipicaNetoM12.toString())

                val listaNetoMes13 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes12
                )

                val varianzaNetoMes13 = calculoVarianzaNetoMes(listaNetoMes13)
                var desvTipicaNetoM13 = r.redondear(sqrt(varianzaNetoMes13))
                binding.etDesvTipNetoM13.setText(desvTipicaNetoM13.toString())
                val promedioNetoMes13 = promedioNetoMes(listaNetoMes13)
                binding.etPromNetoM13.setText(promedioNetoMes13.toString())
                var valoresIntervaloConfianzaM13 = calculoIntervaloConfianza95porc(listaNetoMes13)
                binding.etMinM13.setText(valoresIntervaloConfianzaM13[0].toString())
                binding.etMaxM13.setText(valoresIntervaloConfianzaM13[1].toString())
                binding.etDesvTipNetoM13.setText(desvTipicaNetoM13.toString())

                val listaNetoMes14 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes13
                )
                val varianzaNetoMes14 = calculoVarianzaNetoMes(listaNetoMes14)
                var desvTipicaNetoM14 = r.redondear(sqrt(varianzaNetoMes14))
                binding.etDesvTipNetoM14.setText(desvTipicaNetoM14.toString())
                val promedioNetoMes14 = promedioNetoMes(listaNetoMes14)
                binding.etPromNetoM14.setText(promedioNetoMes14.toString())
                var valoresIntervaloConfianzaM14 = calculoIntervaloConfianza95porc(listaNetoMes14)
                binding.etMinM14.setText(valoresIntervaloConfianzaM14[0].toString())
                binding.etMaxM14.setText(valoresIntervaloConfianzaM14[1].toString())
                binding.etDesvTipNetoM14.setText(desvTipicaNetoM14.toString())

                val listaNetoMes15 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes14
                )
                val varianzaNetoMes15 = calculoVarianzaNetoMes(listaNetoMes15)
                var desvTipicaNetoM15 = r.redondear(sqrt(varianzaNetoMes15))
                binding.etDesvTipNetoM15.setText(desvTipicaNetoM15.toString())
                val promedioNetoMes15 = promedioNetoMes(listaNetoMes15)
                binding.etPromNetoM15.setText(promedioNetoMes15.toString())
                var valoresIntervaloConfianzaM15 = calculoIntervaloConfianza95porc(listaNetoMes15)
                binding.etMinM15.setText(valoresIntervaloConfianzaM15[0].toString())
                binding.etMaxM15.setText(valoresIntervaloConfianzaM15[1].toString())
                binding.etDesvTipNetoM15.setText(desvTipicaNetoM15.toString())

                val listaNetoMes16 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes15
                )
                val varianzaNetoMes16 = calculoVarianzaNetoMes(listaNetoMes16)
                var desvTipicaNetoM16 = r.redondear(sqrt(varianzaNetoMes16))
                binding.etDesvTipNetoM16.setText(desvTipicaNetoM16.toString())
                val promedioNetoMes16= promedioNetoMes(listaNetoMes16)
                binding.etPromNetoM16.setText(promedioNetoMes16.toString())
                var valoresIntervaloConfianzaM16 = calculoIntervaloConfianza95porc(listaNetoMes16)
                binding.etMinM16.setText(valoresIntervaloConfianzaM16[0].toString())
                binding.etMaxM16.setText(valoresIntervaloConfianzaM16[1].toString())
                binding.etDesvTipNetoM16.setText(desvTipicaNetoM16.toString())

                val listaNetoMes17 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes16
                )
                val varianzaNetoMes17 = calculoVarianzaNetoMes(listaNetoMes17)
                var desvTipicaNetoM17 = r.redondear(sqrt(varianzaNetoMes17))
                binding.etDesvTipNetoM17.setText(desvTipicaNetoM17.toString())
                val promedioNetoMes17 = promedioNetoMes(listaNetoMes17)
                binding.etPromNetoM17.setText(promedioNetoMes17.toString())
                var valoresIntervaloConfianzaM17 = calculoIntervaloConfianza95porc(listaNetoMes17)
                binding.etMinM17.setText(valoresIntervaloConfianzaM17[0].toString())
                binding.etMaxM17.setText(valoresIntervaloConfianzaM17[1].toString())
                binding.etDesvTipNetoM17.setText(desvTipicaNetoM17.toString())


                val listaNetoMes18 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes17
                )
                val varianzaNetoMes18 = calculoVarianzaNetoMes(listaNetoMes18)
                var desvTipicaNetoM18 = r.redondear(sqrt(varianzaNetoMes18))
                binding.etDesvTipNetoM18.setText(desvTipicaNetoM18.toString())
                val promedioNetoMes18 = promedioNetoMes(listaNetoMes18)
                binding.etPromNetoM18.setText(promedioNetoMes18.toString())
                var valoresIntervaloConfianzaM18 = calculoIntervaloConfianza95porc(listaNetoMes18)
                binding.etMinM18.setText(valoresIntervaloConfianzaM18[0].toString())
                binding.etMaxM18.setText(valoresIntervaloConfianzaM18[1].toString())
                binding.etDesvTipNetoM18.setText(desvTipicaNetoM18.toString())

                val listaNetoMes19 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes18
                )
                val varianzaNetoMes19 = calculoVarianzaNetoMes(listaNetoMes19)
                var desvTipicaNetoM19 = r.redondear(sqrt(varianzaNetoMes19))
                binding.etDesvTipNetoM19.setText(desvTipicaNetoM19.toString())
                val promedioNetoMes19 = promedioNetoMes(listaNetoMes19)
                binding.etPromNetoM19.setText(promedioNetoMes19.toString())
                var valoresIntervaloConfianzaM19 = calculoIntervaloConfianza95porc(listaNetoMes19)
                binding.etMinM19.setText(valoresIntervaloConfianzaM19[0].toString())
                binding.etMaxM19.setText(valoresIntervaloConfianzaM19[1].toString())
                binding.etDesvTipNetoM19.setText(desvTipicaNetoM19.toString())

                val listaNetoMes20 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes19
                )
                val varianzaNetoMes20 = calculoVarianzaNetoMes(listaNetoMes20)
                var desvTipicaNetoM20 = r.redondear(sqrt(varianzaNetoMes20))
                binding.etDesvTipNetoM20.setText(desvTipicaNetoM20.toString())
                val promedioNetoMes20 = promedioNetoMes(listaNetoMes20)
                binding.etPromNetoM20.setText(promedioNetoMes20.toString())
                var valoresIntervaloConfianzaM20 = calculoIntervaloConfianza95porc(listaNetoMes20)
                binding.etMinM20.setText(valoresIntervaloConfianzaM20[0].toString())
                binding.etMaxM20.setText(valoresIntervaloConfianzaM20[1].toString())
                binding.etDesvTipNetoM20.setText(desvTipicaNetoM20.toString())

                val listaNetoMes21 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes20
                )
                val varianzaNetoMes21 = calculoVarianzaNetoMes(listaNetoMes21)
                var desvTipicaNetoM21 = r.redondear(sqrt(varianzaNetoMes21))
                binding.etDesvTipNetoM21.setText(desvTipicaNetoM21.toString())
                val promedioNetoMes21 = promedioNetoMes(listaNetoMes21)
                binding.etPromNetoM21.setText(promedioNetoMes21.toString())
                var valoresIntervaloConfianzaM21 = calculoIntervaloConfianza95porc(listaNetoMes21)
                binding.etMinM21.setText(valoresIntervaloConfianzaM21[0].toString())
                binding.etMaxM21.setText(valoresIntervaloConfianzaM21[1].toString())
                binding.etDesvTipNetoM21.setText(desvTipicaNetoM21.toString())

                val listaNetoMes22 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes21
                )
                val varianzaNetoMes22 = calculoVarianzaNetoMes(listaNetoMes22)
                var desvTipicaNetoM22 = r.redondear(sqrt(varianzaNetoMes22))
                binding.etDesvTipNetoM22.setText(desvTipicaNetoM22.toString())
                val promedioNetoMes22 = promedioNetoMes(listaNetoMes22)
                binding.etPromNetoM22.setText(promedioNetoMes22.toString())
                var valoresIntervaloConfianzaM22 = calculoIntervaloConfianza95porc(listaNetoMes22)
                binding.etMinM22.setText(valoresIntervaloConfianzaM22[0].toString())
                binding.etMaxM22.setText(valoresIntervaloConfianzaM22[1].toString())
                binding.etDesvTipNetoM22.setText(desvTipicaNetoM22.toString())

                val listaNetoMes23 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes22
                )
                val varianzaNetoMes23 = calculoVarianzaNetoMes(listaNetoMes23)
                var desvTipicaNetoM23 = r.redondear(sqrt(varianzaNetoMes23))
                binding.etDesvTipNetoM23.setText(desvTipicaNetoM23.toString())
                val promedioNetoMes23 = promedioNetoMes(listaNetoMes23)
                binding.etPromNetoM23.setText(promedioNetoMes23.toString())
                var valoresIntervaloConfianzaM23 = calculoIntervaloConfianza95porc(listaNetoMes23)
                binding.etMinM23.setText(valoresIntervaloConfianzaM23[0].toString())
                binding.etMaxM23.setText(valoresIntervaloConfianzaM23[1].toString())
                binding.etDesvTipNetoM23.setText(desvTipicaNetoM23.toString())

                val listaNetoMes24 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes23
                )
                val varianzaNetoMes24 = calculoVarianzaNetoMes(listaNetoMes24)
                var desvTipicaNetoM24 = r.redondear(sqrt(varianzaNetoMes24))
                binding.etDesvTipNetoM24.setText(desvTipicaNetoM24.toString())
                val promedioNetoMes24 = promedioNetoMes(listaNetoMes24)
                binding.etPromNetoM24.setText(promedioNetoMes24.toString())
                var valoresIntervaloConfianzaM24 = calculoIntervaloConfianza95porc(listaNetoMes24)
                binding.etMinM24.setText(valoresIntervaloConfianzaM24[0].toString())
                binding.etMaxM24.setText(valoresIntervaloConfianzaM24[1].toString())
                binding.etDesvTipNetoM24.setText(desvTipicaNetoM24.toString())

                val listaNetoMes25 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes24
                )
                val varianzaNetoMes25 = calculoVarianzaNetoMes(listaNetoMes25)
                var desvTipicaNetoM25 = r.redondear(sqrt(varianzaNetoMes25))
                binding.etDesvTipNetoM25.setText(desvTipicaNetoM25.toString())
                val promedioNetoMes25 = promedioNetoMes(listaNetoMes25)
                binding.etPromNetoM25.setText(promedioNetoMes25.toString())
                var valoresIntervaloConfianzaM25 = calculoIntervaloConfianza95porc(listaNetoMes25)
                binding.etMinM25.setText(valoresIntervaloConfianzaM25[0].toString())
                binding.etMaxM25.setText(valoresIntervaloConfianzaM25[1].toString())
                binding.etDesvTipNetoM25.setText(desvTipicaNetoM25.toString())

                val listaNetoMes26 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes25
                )
                val varianzaNetoMes26 = calculoVarianzaNetoMes(listaNetoMes26)
                var desvTipicaNetoM26 = r.redondear(sqrt(varianzaNetoMes6))
                binding.etDesvTipNetoM26.setText(desvTipicaNetoM26.toString())
                val promedioNetoMes26= promedioNetoMes(listaNetoMes26)
                binding.etPromNetoM26.setText(promedioNetoMes26.toString())
                var valoresIntervaloConfianzaM26 = calculoIntervaloConfianza95porc(listaNetoMes26)
                binding.etMinM26.setText(valoresIntervaloConfianzaM26[0].toString())
                binding.etMaxM26.setText(valoresIntervaloConfianzaM26[1].toString())
                binding.etDesvTipNetoM26.setText(desvTipicaNetoM26.toString())


                val listaNetoMes27 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes26
                )
                val varianzaNetoMes27 = calculoVarianzaNetoMes(listaNetoMes27)
                var desvTipicaNetoM27 = r.redondear(sqrt(varianzaNetoMes27))
                binding.etDesvTipNetoM27.setText(desvTipicaNetoM27.toString())
                val promedioNetoMes27 = promedioNetoMes(listaNetoMes27)
                binding.etPromNetoM27.setText(promedioNetoMes27.toString())
                var valoresIntervaloConfianzaM27 = calculoIntervaloConfianza95porc(listaNetoMes27)
                binding.etMinM27.setText(valoresIntervaloConfianzaM27[0].toString())
                binding.etMaxM27.setText(valoresIntervaloConfianzaM27[1].toString())
                binding.etDesvTipNetoM27.setText(desvTipicaNetoM27.toString())


                val listaNetoMes28 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes27
                )
                val varianzaNetoMes28 = calculoVarianzaNetoMes(listaNetoMes28)
                var desvTipicaNetoM28 = r.redondear(sqrt(varianzaNetoMes28))
                binding.etDesvTipNetoM28.setText(desvTipicaNetoM28.toString())
                val promedioNetoMes28 = promedioNetoMes(listaNetoMes28)
                binding.etPromNetoM28.setText(promedioNetoMes28.toString())
                var valoresIntervaloConfianzaM28 = calculoIntervaloConfianza95porc(listaNetoMes28)
                binding.etMinM28.setText(valoresIntervaloConfianzaM28[0].toString())
                binding.etMaxM28.setText(valoresIntervaloConfianzaM28[1].toString())
                binding.etDesvTipNetoM28.setText(desvTipicaNetoM28.toString())

                val listaNetoMes29 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes28
                )
                val varianzaNetoMes29 = calculoVarianzaNetoMes(listaNetoMes29)
                var desvTipicaNetoM29 = r.redondear(sqrt(varianzaNetoMes29))
                binding.etDesvTipNetoM29.setText(desvTipicaNetoM29.toString())
                val promedioNetoMes29 = promedioNetoMes(listaNetoMes29)
                binding.etPromNetoM29.setText(promedioNetoMes29.toString())
                var valoresIntervaloConfianzaM29 = calculoIntervaloConfianza95porc(listaNetoMes29)
                binding.etMinM29.setText(valoresIntervaloConfianzaM29[0].toString())
                binding.etMaxM29.setText(valoresIntervaloConfianzaM29[1].toString())
                binding.etDesvTipNetoM29.setText(desvTipicaNetoM29.toString())

                val listaNetoMes30 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes29
                )
                val varianzaNetoMes30 = calculoVarianzaNetoMes(listaNetoMes30)
                var desvTipicaNetoM30 = r.redondear(sqrt(varianzaNetoMes30))
                binding.etDesvTipNetoM30.setText(desvTipicaNetoM30.toString())
                val promedioNetoMes30 = promedioNetoMes(listaNetoMes30)
                binding.etPromNetoM30.setText(promedioNetoMes30.toString())
                var valoresIntervaloConfianzaM30 = calculoIntervaloConfianza95porc(listaNetoMes30)
                binding.etMinM30.setText(valoresIntervaloConfianzaM30[0].toString())
                binding.etMaxM30.setText(valoresIntervaloConfianzaM30[1].toString())
                binding.etDesvTipNetoM30.setText(desvTipicaNetoM30.toString())

                val listaNetoMes31 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes30
                )
                val varianzaNetoMes31 = calculoVarianzaNetoMes(listaNetoMes31)
                var desvTipicaNetoM31 = r.redondear(sqrt(varianzaNetoMes31))
                binding.etDesvTipNetoM31.setText(desvTipicaNetoM31.toString())
                val promedioNetoMes31 = promedioNetoMes(listaNetoMes31)
                binding.etPromNetoM31.setText(promedioNetoMes31.toString())
                var valoresIntervaloConfianzaM31 = calculoIntervaloConfianza95porc(listaNetoMes31)
                binding.etMinM31.setText(valoresIntervaloConfianzaM31[0].toString())
                binding.etMaxM31.setText(valoresIntervaloConfianzaM31[1].toString())
                binding.etDesvTipNetoM31.setText(desvTipicaNetoM31.toString())


                val listaNetoMes32 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes31
                )
                val varianzaNetoMes32 = calculoVarianzaNetoMes(listaNetoMes32)
                var desvTipicaNetoM32 = r.redondear(sqrt(varianzaNetoMes32))
                binding.etDesvTipNetoM32.setText(desvTipicaNetoM32.toString())
                val promedioNetoMes32 = promedioNetoMes(listaNetoMes32)
                binding.etPromNetoM32.setText(promedioNetoMes32.toString())
                var valoresIntervaloConfianzaM32 = calculoIntervaloConfianza95porc(listaNetoMes32)
                binding.etMinM32.setText(valoresIntervaloConfianzaM32[0].toString())
                binding.etMaxM32.setText(valoresIntervaloConfianzaM32[1].toString())
                binding.etDesvTipNetoM32.setText(desvTipicaNetoM32.toString())

                val listaNetoMes33 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes32
                )
                val varianzaNetoMes33 = calculoVarianzaNetoMes(listaNetoMes33)
                var desvTipicaNetoM33 = r.redondear(sqrt(varianzaNetoMes33))
                binding.etDesvTipNetoM33.setText(desvTipicaNetoM33.toString())
                val promedioNetoMes33 = promedioNetoMes(listaNetoMes33)
                binding.etPromNetoM33.setText(promedioNetoMes33.toString())
                var valoresIntervaloConfianzaM33 = calculoIntervaloConfianza95porc(listaNetoMes33)
                binding.etMinM33.setText(valoresIntervaloConfianzaM33[0].toString())
                binding.etMaxM33.setText(valoresIntervaloConfianzaM33[1].toString())
                binding.etDesvTipNetoM33.setText(desvTipicaNetoM33.toString())

                val listaNetoMes34 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes33
                )
                val varianzaNetoMes34 = calculoVarianzaNetoMes(listaNetoMes34)
                var desvTipicaNetoM34 = r.redondear(sqrt(varianzaNetoMes34))
                binding.etDesvTipNetoM34.setText(desvTipicaNetoM34.toString())
                val promedioNetoMes34 = promedioNetoMes(listaNetoMes34)
                binding.etPromNetoM34.setText(promedioNetoMes4.toString())
                var valoresIntervaloConfianzaM34 = calculoIntervaloConfianza95porc(listaNetoMes34)
                binding.etMinM34.setText(valoresIntervaloConfianzaM34[0].toString())
                binding.etMaxM34.setText(valoresIntervaloConfianzaM34[1].toString())
                binding.etDesvTipNetoM34.setText(desvTipicaNetoM34.toString())

                val listaNetoMes35 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes34
                )
                val varianzaNetoMes35 = calculoVarianzaNetoMes(listaNetoMes35)
                var desvTipicaNetoM35 = r.redondear(sqrt(varianzaNetoMes35))
                binding.etDesvTipNetoM35.setText(desvTipicaNetoM35.toString())
                val promedioNetoMes35 = promedioNetoMes(listaNetoMes35)
                binding.etPromNetoM35.setText(promedioNetoMes35.toString())
                var valoresIntervaloConfianzaM35 = calculoIntervaloConfianza95porc(listaNetoMes35)
                binding.etMinM35.setText(valoresIntervaloConfianzaM35[0].toString())
                binding.etMaxM35.setText(valoresIntervaloConfianzaM35[1].toString())
                binding.etDesvTipNetoM35.setText(desvTipicaNetoM35.toString())

                val listaNetoMes36 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes35
                )
                val varianzaNetoMes36 = calculoVarianzaNetoMes(listaNetoMes36)
                var desvTipicaNetoM36 = r.redondear(sqrt(varianzaNetoMes36))
                binding.etDesvTipNetoM36.setText(desvTipicaNetoM36.toString())
                val promedioNetoMes36= promedioNetoMes(listaNetoMes36)
                binding.etPromNetoM36.setText(promedioNetoMes36.toString())
                var valoresIntervaloConfianzaM36 = calculoIntervaloConfianza95porc(listaNetoMes36)
                binding.etMinM36.setText(valoresIntervaloConfianzaM36[0].toString())
                binding.etMaxM36.setText(valoresIntervaloConfianzaM36[1].toString())
                binding.etDesvTipNetoM36.setText(desvTipicaNetoM36.toString())

                val listaNetoMes37 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes36
                )
                val varianzaNetoMes37 = calculoVarianzaNetoMes(listaNetoMes37)
                var desvTipicaNetoM37 = r.redondear(sqrt(varianzaNetoMes37))
                binding.etDesvTipNetoM37.setText(desvTipicaNetoM37.toString())
                val promedioNetoMes37 = promedioNetoMes(listaNetoMes37)
                binding.etPromNetoM37.setText(promedioNetoMes37.toString())
                var valoresIntervaloConfianzaM37 = calculoIntervaloConfianza95porc(listaNetoMes37)
                binding.etMinM37.setText(valoresIntervaloConfianzaM37[0].toString())
                binding.etMaxM37.setText(valoresIntervaloConfianzaM37[1].toString())
                binding.etDesvTipNetoM37.setText(desvTipicaNetoM37.toString())


                val listaNetoMes38 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes37
                )
                val varianzaNetoMes38 = calculoVarianzaNetoMes(listaNetoMes38)
                var desvTipicaNetoM38 = r.redondear(sqrt(varianzaNetoMes38))
                binding.etDesvTipNetoM38.setText(desvTipicaNetoM38.toString())
                val promedioNetoMes38 = promedioNetoMes(listaNetoMes38)
                binding.etPromNetoM38.setText(promedioNetoMes38.toString())
                var valoresIntervaloConfianzaM38 = calculoIntervaloConfianza95porc(listaNetoMes38)
                binding.etMinM38.setText(valoresIntervaloConfianzaM38[0].toString())
                binding.etMaxM38.setText(valoresIntervaloConfianzaM38[1].toString())
                binding.etDesvTipNetoM38.setText(desvTipicaNetoM38.toString())

                val listaNetoMes39 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes38
                )
                val varianzaNetoMes39 = calculoVarianzaNetoMes(listaNetoMes39)
                var desvTipicaNetoM39 = r.redondear(sqrt(varianzaNetoMes39))
                binding.etDesvTipNetoM39.setText(desvTipicaNetoM39.toString())
                val promedioNetoMes39 = promedioNetoMes(listaNetoMes39)
                binding.etPromNetoM39.setText(promedioNetoMes39.toString())
                var valoresIntervaloConfianzaM39 = calculoIntervaloConfianza95porc(listaNetoMes39)
                binding.etMinM39.setText(valoresIntervaloConfianzaM39[0].toString())
                binding.etMaxM39.setText(valoresIntervaloConfianzaM39[1].toString())
                binding.etDesvTipNetoM39.setText(desvTipicaNetoM39.toString())

                val listaNetoMes40 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes39
                )
                val varianzaNetoMes40 = calculoVarianzaNetoMes(listaNetoMes40)
                var desvTipicaNetoM40 = r.redondear(sqrt(varianzaNetoMes40))
                binding.etDesvTipNetoM40.setText(desvTipicaNetoM40.toString())
                val promedioNetoMes40 = promedioNetoMes(listaNetoMes40)
                binding.etPromNetoM40.setText(promedioNetoMes40.toString())
                var valoresIntervaloConfianzaM40 = calculoIntervaloConfianza95porc(listaNetoMes40)
                binding.etMinM40.setText(valoresIntervaloConfianzaM40[0].toString())
                binding.etMaxM40.setText(valoresIntervaloConfianzaM40[1].toString())
                binding.etDesvTipNetoM40.setText(desvTipicaNetoM40.toString())

                val listaNetoMes41 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes40
                )
                val varianzaNetoMes41 = calculoVarianzaNetoMes(listaNetoMes41)
                var desvTipicaNetoM41 = r.redondear(sqrt(varianzaNetoMes41))
                binding.etDesvTipNetoM41.setText(desvTipicaNetoM41.toString())
                val promedioNetoMes41 = promedioNetoMes(listaNetoMes41)
                binding.etPromNetoM41.setText(promedioNetoMes41.toString())
                var valoresIntervaloConfianzaM41 = calculoIntervaloConfianza95porc(listaNetoMes41)
                binding.etMinM41.setText(valoresIntervaloConfianzaM41[0].toString())
                binding.etMaxM41.setText(valoresIntervaloConfianzaM41[1].toString())
                binding.etDesvTipNetoM41.setText(desvTipicaNetoM41.toString())


                val listaNetoMes42 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes41
                )
                val varianzaNetoMes42 = calculoVarianzaNetoMes(listaNetoMes42)
                var desvTipicaNetoM42 = r.redondear(sqrt(varianzaNetoMes42))
                binding.etDesvTipNetoM42.setText(desvTipicaNetoM42.toString())
                val promedioNetoMes42 = promedioNetoMes(listaNetoMes42)
                binding.etPromNetoM42.setText(promedioNetoMes42.toString())
                var valoresIntervaloConfianzaM42 = calculoIntervaloConfianza95porc(listaNetoMes42)
                binding.etMinM42.setText(valoresIntervaloConfianzaM42[0].toString())
                binding.etMaxM42.setText(valoresIntervaloConfianzaM42[1].toString())
                binding.etDesvTipNetoM42.setText(desvTipicaNetoM42.toString())

                val listaNetoMes43 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes42
                )
                val varianzaNetoMes43 = calculoVarianzaNetoMes(listaNetoMes43)
                var desvTipicaNetoM43 = r.redondear(sqrt(varianzaNetoMes43))
                binding.etDesvTipNetoM43.setText(desvTipicaNetoM43.toString())
                val promedioNetoMes43 = promedioNetoMes(listaNetoMes43)
                binding.etPromNetoM43.setText(promedioNetoMes43.toString())
                var valoresIntervaloConfianzaM43 = calculoIntervaloConfianza95porc(listaNetoMes43)
                binding.etMinM43.setText(valoresIntervaloConfianzaM43[0].toString())
                binding.etMaxM43.setText(valoresIntervaloConfianzaM43[1].toString())
                binding.etDesvTipNetoM43.setText(desvTipicaNetoM43.toString())

                val listaNetoMes44 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes43
                )
                val varianzaNetoMes44= calculoVarianzaNetoMes(listaNetoMes44)
                var desvTipicaNetoM44 = r.redondear(sqrt(varianzaNetoMes44))
                binding.etDesvTipNetoM44.setText(desvTipicaNetoM44.toString())
                val promedioNetoMes44 = promedioNetoMes(listaNetoMes44)
                binding.etPromNetoM44.setText(promedioNetoMes44.toString())
                var valoresIntervaloConfianzaM44 = calculoIntervaloConfianza95porc(listaNetoMes44)
                binding.etMinM44.setText(valoresIntervaloConfianzaM44[0].toString())
                binding.etMaxM44.setText(valoresIntervaloConfianzaM44[1].toString())
                binding.etDesvTipNetoM44.setText(desvTipicaNetoM44.toString())

                val listaNetoMes45 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes44
                )
                val varianzaNetoMes45 = calculoVarianzaNetoMes(listaNetoMes45)
                var desvTipicaNetoM45 = r.redondear(sqrt(varianzaNetoMes45))
                binding.etDesvTipNetoM45.setText(desvTipicaNetoM45.toString())
                val promedioNetoMes45 = promedioNetoMes(listaNetoMes45)
                binding.etPromNetoM45.setText(promedioNetoMes45.toString())
                var valoresIntervaloConfianzaM45 = calculoIntervaloConfianza95porc(listaNetoMes45)
                binding.etMinM45.setText(valoresIntervaloConfianzaM45[0].toString())
                binding.etMaxM45.setText(valoresIntervaloConfianzaM45[1].toString())
                binding.etDesvTipNetoM45.setText(desvTipicaNetoM45.toString())

                val listaNetoMes46 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes45
                )
                val varianzaNetoMes46 = calculoVarianzaNetoMes(listaNetoMes46)
                var desvTipicaNetoM46 = r.redondear(sqrt(varianzaNetoMes46))
                binding.etDesvTipNetoM46.setText(desvTipicaNetoM46.toString())
                val promedioNetoMes46= promedioNetoMes(listaNetoMes46)
                binding.etPromNetoM46.setText(promedioNetoMes46.toString())
                var valoresIntervaloConfianzaM46 = calculoIntervaloConfianza95porc(listaNetoMes46)
                binding.etMinM46.setText(valoresIntervaloConfianzaM46[0].toString())
                binding.etMaxM46.setText(valoresIntervaloConfianzaM46[1].toString())
                binding.etDesvTipNetoM46.setText(desvTipicaNetoM46.toString())

                val listaNetoMes47 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes46
                )
                val varianzaNetoMes47 = calculoVarianzaNetoMes(listaNetoMes47)
                var desvTipicaNetoM47 = r.redondear(sqrt(varianzaNetoMes47))
                binding.etDesvTipNetoM47.setText(desvTipicaNetoM47.toString())
                val promedioNetoMes47 = promedioNetoMes(listaNetoMes47)
                binding.etPromNetoM47.setText(promedioNetoMes47.toString())
                var valoresIntervaloConfianzaM47 = calculoIntervaloConfianza95porc(listaNetoMes47)
                binding.etMinM47.setText(valoresIntervaloConfianzaM47[0].toString())
                binding.etMaxM47.setText(valoresIntervaloConfianzaM47[1].toString())
                binding.etDesvTipNetoM47.setText(desvTipicaNetoM47.toString())


                val listaNetoMes48 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes47
                )
                val varianzaNetoMes48 = calculoVarianzaNetoMes(listaNetoMes48)
                var desvTipicaNetoM48 = r.redondear(sqrt(varianzaNetoMes48))
                binding.etDesvTipNetoM48.setText(desvTipicaNetoM48.toString())
                val promedioNetoMes48 = promedioNetoMes(listaNetoMes48)
                binding.etPromNetoM48.setText(promedioNetoMes48.toString())
                var valoresIntervaloConfianzaM48 = calculoIntervaloConfianza95porc(listaNetoMes48)
                binding.etMinM48.setText(valoresIntervaloConfianzaM48[0].toString())
                binding.etMaxM48.setText(valoresIntervaloConfianzaM48[1].toString())
                binding.etDesvTipNetoM48.setText(desvTipicaNetoM48.toString())

                val listaNetoMes49 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes48
                )
                val varianzaNetoMes49 = calculoVarianzaNetoMes(listaNetoMes49)
                var desvTipicaNetoM49 = r.redondear(sqrt(varianzaNetoMes49))
                binding.etDesvTipNetoM49.setText(desvTipicaNetoM49.toString())
                val promedioNetoMes49 = promedioNetoMes(listaNetoMes49)
                binding.etPromNetoM49.setText(promedioNetoMes9.toString())
                var valoresIntervaloConfianzaM49 = calculoIntervaloConfianza95porc(listaNetoMes49)
                binding.etMinM49.setText(valoresIntervaloConfianzaM49[0].toString())
                binding.etMaxM49.setText(valoresIntervaloConfianzaM49[1].toString())
                binding.etDesvTipNetoM49.setText(desvTipicaNetoM49.toString())

                val listaNetoMes50 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes49
                )
                val varianzaNetoMes50 = calculoVarianzaNetoMes(listaNetoMes50)
                var desvTipicaNetoM50 = r.redondear(sqrt(varianzaNetoMes50))
                binding.etDesvTipNetoM50.setText(desvTipicaNetoM50.toString())
                val promedioNetoMes50 = promedioNetoMes(listaNetoMes50)
                binding.etPromNetoM50.setText(promedioNetoMes50.toString())
                var valoresIntervaloConfianzaM50 = calculoIntervaloConfianza95porc(listaNetoMes50)
                binding.etMinM50.setText(valoresIntervaloConfianzaM50[0].toString())
                binding.etMaxM50.setText(valoresIntervaloConfianzaM50[1].toString())
                binding.etDesvTipNetoM50.setText(desvTipicaNetoM50.toString())

                val listaNetoMes51 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes50
                )
                val varianzaNetoMes51 = calculoVarianzaNetoMes(listaNetoMes51)
                var desvTipicaNetoM51 = r.redondear(sqrt(varianzaNetoMes51))
                binding.etDesvTipNetoM51.setText(desvTipicaNetoM51.toString())
                val promedioNetoMes51 = promedioNetoMes(listaNetoMes51)
                binding.etPromNetoM51.setText(promedioNetoMes1.toString())
                var valoresIntervaloConfianzaM51 = calculoIntervaloConfianza95porc(listaNetoMes51)
                binding.etMinM51.setText(valoresIntervaloConfianzaM51[0].toString())
                binding.etMaxM51.setText(valoresIntervaloConfianzaM51[1].toString())
                binding.etDesvTipNetoM51.setText(desvTipicaNetoM51.toString())


                val listaNetoMes52 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes51
                )
                val varianzaNetoMes52 = calculoVarianzaNetoMes(listaNetoMes52)
                var desvTipicaNetoM52 = r.redondear(sqrt(varianzaNetoMes52))
                binding.etDesvTipNetoM52.setText(desvTipicaNetoM52.toString())
                val promedioNetoMes52 = promedioNetoMes(listaNetoMes52)
                binding.etPromNetoM52.setText(promedioNetoMes52.toString())
                var valoresIntervaloConfianzaM52 = calculoIntervaloConfianza95porc(listaNetoMes52)
                binding.etMinM52.setText(valoresIntervaloConfianzaM52[0].toString())
                binding.etMaxM52.setText(valoresIntervaloConfianzaM52[1].toString())
                binding.etDesvTipNetoM52.setText(desvTipicaNetoM52.toString())

                val listaNetoMes53 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes52
                )
                val varianzaNetoMes53 = calculoVarianzaNetoMes(listaNetoMes53)
                var desvTipicaNetoM53 = r.redondear(sqrt(varianzaNetoMes53))
                binding.etDesvTipNetoM53.setText(desvTipicaNetoM53.toString())
                val promedioNetoMes53 = promedioNetoMes(listaNetoMes53)
                binding.etPromNetoM53.setText(promedioNetoMes53.toString())
                var valoresIntervaloConfianzaM53 = calculoIntervaloConfianza95porc(listaNetoMes53)
                binding.etMinM53.setText(valoresIntervaloConfianzaM53[0].toString())
                binding.etMaxM53.setText(valoresIntervaloConfianzaM53[1].toString())
                binding.etDesvTipNetoM3.setText(desvTipicaNetoM53.toString())

                val listaNetoMes54 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes53
                )
                val varianzaNetoMes54 = calculoVarianzaNetoMes(listaNetoMes54)
                var desvTipicaNetoM54 = r.redondear(sqrt(varianzaNetoMes54))
                binding.etDesvTipNetoM54.setText(desvTipicaNetoM54.toString())
                val promedioNetoMes54 = promedioNetoMes(listaNetoMes54)
                binding.etPromNetoM54.setText(promedioNetoMes54.toString())
                var valoresIntervaloConfianzaM54 = calculoIntervaloConfianza95porc(listaNetoMes54)
                binding.etMinM54.setText(valoresIntervaloConfianzaM54[0].toString())
                binding.etMaxM54.setText(valoresIntervaloConfianzaM54[1].toString())
                binding.etDesvTipNetoM54.setText(desvTipicaNetoM54.toString())

                val listaNetoMes55 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes54
                )
                val varianzaNetoMes55 = calculoVarianzaNetoMes(listaNetoMes55)
                var desvTipicaNetoM55 = r.redondear(sqrt(varianzaNetoMes55))
                binding.etDesvTipNetoM55.setText(desvTipicaNetoM55.toString())
                val promedioNetoMes55 = promedioNetoMes(listaNetoMes55)
                binding.etPromNetoM55.setText(promedioNetoMes55.toString())
                var valoresIntervaloConfianzaM55 = calculoIntervaloConfianza95porc(listaNetoMes55)
                binding.etMinM55.setText(valoresIntervaloConfianzaM55[0].toString())
                binding.etMaxM55.setText(valoresIntervaloConfianzaM55[1].toString())
                binding.etDesvTipNetoM55.setText(desvTipicaNetoM55.toString())

                val listaNetoMes56 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes55
                )
                val varianzaNetoMes56 = calculoVarianzaNetoMes(listaNetoMes56)
                var desvTipicaNetoM56 = r.redondear(sqrt(varianzaNetoMes56))
                binding.etDesvTipNetoM56.setText(desvTipicaNetoM56.toString())
                val promedioNetoMes56= promedioNetoMes(listaNetoMes56)
                binding.etPromNetoM56.setText(promedioNetoMes56.toString())
                var valoresIntervaloConfianzaM56 = calculoIntervaloConfianza95porc(listaNetoMes56)
                binding.etMinM56.setText(valoresIntervaloConfianzaM56[0].toString())
                binding.etMaxM56.setText(valoresIntervaloConfianzaM56[1].toString())
                binding.etDesvTipNetoM56.setText(desvTipicaNetoM56.toString())

                val listaNetoMes57 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes56
                )
                val varianzaNetoMes57 = calculoVarianzaNetoMes(listaNetoMes57)
                var desvTipicaNetoM57 = r.redondear(sqrt(varianzaNetoMes57))
                binding.etDesvTipNetoM57.setText(desvTipicaNetoM57.toString())
                val promedioNetoMes57 = promedioNetoMes(listaNetoMes57)
                binding.etPromNetoM57.setText(promedioNetoMes57.toString())
                var valoresIntervaloConfianzaM57 = calculoIntervaloConfianza95porc(listaNetoMes57)
                binding.etMinM57.setText(valoresIntervaloConfianzaM57[0].toString())
                binding.etMaxM57.setText(valoresIntervaloConfianzaM57[1].toString())
                binding.etDesvTipNetoM57.setText(desvTipicaNetoM57.toString())


                val listaNetoMes58 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes57
                )
                val varianzaNetoMes58 = calculoVarianzaNetoMes(listaNetoMes58)
                var desvTipicaNetoM58 = r.redondear(sqrt(varianzaNetoMes58))
                binding.etDesvTipNetoM58.setText(desvTipicaNetoM58.toString())
                val promedioNetoMes58 = promedioNetoMes(listaNetoMes58)
                binding.etPromNetoM58.setText(promedioNetoMes58.toString())
                var valoresIntervaloConfianzaM58 = calculoIntervaloConfianza95porc(listaNetoMes58)
                binding.etMinM58.setText(valoresIntervaloConfianzaM58[0].toString())
                binding.etMaxM58.setText(valoresIntervaloConfianzaM58[1].toString())
                binding.etDesvTipNetoM58.setText(desvTipicaNetoM58.toString())

                val listaNetoMes59 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes58
                )
                val varianzaNetoMes59 = calculoVarianzaNetoMes(listaNetoMes59)
                var desvTipicaNetoM59 = r.redondear(sqrt(varianzaNetoMes59))
                binding.etDesvTipNetoM59.setText(desvTipicaNetoM59.toString())
                val promedioNetoMes59 = promedioNetoMes(listaNetoMes59)
                binding.etPromNetoM59.setText(promedioNetoMes59.toString())
                var valoresIntervaloConfianzaM59 = calculoIntervaloConfianza95porc(listaNetoMes59)
                binding.etMinM59.setText(valoresIntervaloConfianzaM59[0].toString())
                binding.etMaxM59.setText(valoresIntervaloConfianzaM59[1].toString())
                binding.etDesvTipNetoM59.setText(desvTipicaNetoM59.toString())

                val listaNetoMes60 = listaNeto(
                    mediaEntradas,
                    desvTipicaEnt,
                    mediaSalidas,
                    desvTipicaSal,
                    promedioNetoMes59
                )
                val varianzaNetoMes60 = calculoVarianzaNetoMes(listaNetoMes60)
                var desvTipicaNetoM60= r.redondear(sqrt(varianzaNetoMes60))
                binding.etDesvTipNetoM60.setText(desvTipicaNetoM60.toString())
                val promedioNetoMes60 = promedioNetoMes(listaNetoMes60)
                binding.etPromNetoM60.setText(promedioNetoMes60.toString())
                var valoresIntervaloConfianzaM60 = calculoIntervaloConfianza95porc(listaNetoMes60)
                binding.etMinM60.setText(valoresIntervaloConfianzaM60[0].toString())
                binding.etMaxM60.setText(valoresIntervaloConfianzaM60[1].toString())
                binding.etDesvTipNetoM60.setText(desvTipicaNetoM60.toString())

            }
        }

    }

    //devuelve una lista de los calculos de las 200 corridas(suma de entradas + saldo final mes anterior-salidas)
    private fun listaNeto(mediaEnt:Double,desvTipEnt:Double, mediaSal:Double,desvTipSal:Double,totalSueldoEfFinal:Double): MutableList<Double> {
        val ListaVADistNormalEnt=mutableListOf<Double>()
        val ListaVADistNormalSal=mutableListOf<Double>()
        val dN=DistribucionNormal()
        var nroCorridas=200
        for (num in 1..nroCorridas)
        {

            ListaVADistNormalEnt.add(dN.NormalDistribution(mediaEnt,desvTipEnt))
            //println(ListaVADistNormalEntM1)
        }
        //Realizando 200 corridas para las salidas mes1
        for (num in 1..200)
        {
            ListaVADistNormalSal.add(dN.NormalDistribution(mediaSal,desvTipSal))
            //println(ListaVADistNormalSalM1)
        }

        var cont=0
        val listaNeto= mutableListOf<Double>()
        do {
            listaNeto.add(ListaVADistNormalEnt[cont] + totalSueldoEfFinal - ListaVADistNormalSal[cont])
            cont++
        }while(cont<nroCorridas)
        return listaNeto
    }

    private fun calculoVarianzaNetoMes(listaNeto: MutableList<Double>): Double {

        var nroCorridas=200

        var sumaNeto=0.0
        listaNeto.forEach {
            sumaNeto=sumaNeto+it
        }
        val mediaNeto=r.redondear(sumaNeto/nroCorridas)

        var varianzaNetoMes=0.0
        listaNeto.forEach {
            varianzaNetoMes=((mediaNeto-it).pow(2.0)/nroCorridas) +varianzaNetoMes
        }

        return varianzaNetoMes
    }
    private fun promedioNetoMes(listaNeto: MutableList<Double>): Double {

        var nroCorridas=200
        var sumaNeto=0.0
        listaNeto.forEach {
            sumaNeto=sumaNeto+it
        }
        val mediaNeto=r.redondear(sumaNeto/nroCorridas)
        return mediaNeto
    }
    private fun calculoIntervaloConfianza95porc(listaNeto: MutableList<Double>): MutableList<Double> {

        val r:RedondeoDecimal = RedondeoDecimal()
        // calculate the mean value (= average)
        var sum = 0.0
        for (num in listaNeto) {
            sum += num.toDouble()
        }
        val mean = sum / listaNeto.size

        // calculate standard deviation
        var squaredDifferenceSum = 0.0
        for (num in listaNeto) {
            squaredDifferenceSum += (num - mean) * (num - mean)
        }
        val variance = squaredDifferenceSum / listaNeto.size
        val standardDeviation = Math.sqrt(variance)


        val confidenceLevel = 1.96
        val temp = confidenceLevel * standardDeviation / Math.sqrt(listaNeto.size.toDouble())
        return mutableListOf<Double>(r.redondear(mean - temp), r.redondear(mean + temp))
    }







}