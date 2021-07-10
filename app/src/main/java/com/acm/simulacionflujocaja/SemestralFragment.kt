package com.acm.simulacionflujocaja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acm.simulacionflujocaja.databinding.FragmentSemestralBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.pow
import kotlin.math.sqrt


class SemestralFragment : Fragment(R.layout.fragment_semestral) {

    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentSemestralBinding? = null
    private val binding get() = _binding!!
    val r:RedondeoDecimal = RedondeoDecimal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle("SIMULACION SEMESTRAL")
        val user = FirebaseAuth.getInstance().currentUser
        _binding = FragmentSemestralBinding.inflate(inflater, container, false)
        val view = binding.root
        CalculoCorridasSimulacion()
        binding.btnSimularSem.setOnClickListener{
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