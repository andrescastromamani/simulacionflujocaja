package com.acm.simulacionflujocaja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acm.simulacionflujocaja.databinding.FragmentTrimestralBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Double.parseDouble
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class TrimestralFragment : Fragment(R.layout.fragment_trimestral) {

    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email
    private var _binding: FragmentTrimestralBinding? = null
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
        _binding = FragmentTrimestralBinding.inflate(inflater, container, false)
        val view = binding.root

        CalculoCorridasSimulacion()

        return view
    }


    private fun CalculoCorridasSimulacion(){
        db.collection("Users").document(user?.email.toString()).collection("Entradas").document("DatosPresupuesto").get().addOnSuccessListener {


            val totalSueldoefectivoFinalProy:Double=r.redondear(parseDouble((it.get("totalSueldoefectivoFinalProy") as String?)))
            binding.etSaldoInicial.setText(totalSueldoefectivoFinalProy.toString())
            val entrada1:Double=parseDouble((it.get("totalEntradas1") as String?))
            val entrada2:Double=parseDouble((it.get("totalEntradas2") as String?))
            val entrada3:Double=parseDouble((it.get("totalEntradas3") as String?))

            val salida1:Double=parseDouble((it.get("totalSalidas1") as String?))
            val salida2:Double=parseDouble((it.get("totalSalidas2") as String?))
            val salida3:Double=parseDouble((it.get("totalSalidas3") as String?))
               //Calculo de las medias

            val listaEntradas= mutableListOf<Double>(entrada1,entrada2,entrada3)
            val tamEnt:Double=listaEntradas.size.toDouble()
            var sumaEnt=0.0
            var sumaSal=0.0
            var varianzaEnt=0.0
            var varianzaSal=0.0
            var nroCorridas=200

            val listaSalidas= mutableListOf<Double>(salida1,salida2,salida3)
            val tamSalida:Double= listaSalidas.size.toDouble()

            listaEntradas.forEach{
                sumaEnt=it+sumaEnt
            }
            //calculo medias
            val mediaEntradas=r.redondear(sumaEnt/tamEnt)
            listaSalidas.forEach {
                sumaSal=sumaSal+it
            }
            val mediaSalidas=r.redondear(sumaSal/tamSalida)

            listaEntradas.forEach {
                varianzaEnt=((mediaEntradas-it).pow(2.0)/tamEnt) +varianzaEnt
            }
            //calculo de las desv tipicas
            var desvTipicaEnt=r.redondear(sqrt(varianzaEnt))
            binding.etDesvTipEnt.setText(desvTipicaEnt.toString())

            listaSalidas.forEach {
                varianzaSal=((mediaSalidas-it).pow(2.0)/tamSalida) +varianzaSal
            }
            var desvTipicaSal=r.redondear(sqrt(varianzaSal))
            binding.etDesvTipSal.setText(desvTipicaSal.toString())
            /*val mediaEntradas:Double=r.redondear((entrada1+entrada2+entrada3)/3)
            val mediaSalidas:Double=r.redondear((salida1+salida2+salida3)/3)*/
            binding.etMediaEnt.setText(mediaEntradas.toString())
            binding.etMediaSal.setText(mediaSalidas.toString())


            //val nr:aleatorioUniforme=aleatorioUniforme()
            val ListaVADistNormalEntM1=mutableListOf<Double>()
            val ListaVADistNormalSalM1=mutableListOf<Double>()
            val ListaVADistNormalEntM2=mutableListOf<Double>()
            val ListaVADistNormalSalM2=mutableListOf<Double>()
            val ListaVADistNormalEntM3=mutableListOf<Double>()
            val ListaVADistNormalSalM3=mutableListOf<Double>()

            //Realizando 200 corridas para las entradas mes1
            for (num in 1..200)
            {

                ListaVADistNormalEntM1.add(NormalDistribution(mediaEntradas,desvTipicaEnt))
                //println(ListaVADistNormalEntM1)
            }
            //Realizando 200 corridas para las salidas mes1
            for (num in 1..200)
            {
                ListaVADistNormalSalM1.add(NormalDistribution(mediaSalidas,desvTipicaSal))
                //println(ListaVADistNormalSalM1)
            }
            var neto:Double=0.0
            var cont=0
            val listaNeto= mutableListOf<Double>()
            do{
                listaNeto.add(ListaVADistNormalEntM1[cont]+totalSueldoefectivoFinalProy-ListaVADistNormalSalM1[cont])
                        cont++

            }
            while(cont<nroCorridas)
            var sumaNeto=0.0
            listaNeto.forEach {
                sumaNeto=sumaNeto+it
            }
            val mediaNeto=r.redondear(sumaNeto/nroCorridas)
            binding.etPromNetoM1.setText(mediaNeto.toString())
            var varianzaNetoM1=0.0
            listaNeto.forEach {
                varianzaNetoM1=((mediaNeto-it).pow(2.0)/nroCorridas) +varianzaNetoM1
            }

            var desvTipicaNetoM1=r.redondear(sqrt(varianzaNetoM1))
            binding.etDesvTipNetoM1.setText(desvTipicaNetoM1.toString())

            var valoresIntervaloConfianza=calculateLowerUpperConfidenceBoundary95Percent(listaNeto)
            binding.etMinM1.setText(valoresIntervaloConfianza[0].toString())
            binding.etMaxM1.setText(valoresIntervaloConfianza[1].toString())



            //Realizando 200 corridas para las entradas mes2
          /*  for (num in 1..200)
            {
                ListaVADistNormalEntM2.add(NormalDistribution(mediaEntradas,mediaEntradas))
                println(ListaVADistNormalEntM2)
            }

            //Realizando 200 corridas para las salidas mes2
            for (num in 1..200)
            {

                ListaVADistNormalSalM2.add(NormalDistribution(mediaSalidas,mediaSalidas))
                println(ListaVADistNormalSalM2)
            }
*/


        }

    }
    private fun calculateLowerUpperConfidenceBoundary95Percent(givenNumbers: MutableList<Double>): MutableList<Double> {

        // calculate the mean value (= average)
        var sum = 0.0
        for (num in givenNumbers) {
            sum += num.toDouble()
        }
        val mean = sum / givenNumbers.size

        // calculate standard deviation
        var squaredDifferenceSum = 0.0
        for (num in givenNumbers) {
            squaredDifferenceSum += (num - mean) * (num - mean)
        }
        val variance = squaredDifferenceSum / givenNumbers.size
        val standardDeviation = Math.sqrt(variance)

        // value for 95% confidence interval, source: https://en.wikipedia.org/wiki/Confidence_interval#Basic_Steps
        val confidenceLevel = 1.96
        val temp = confidenceLevel * standardDeviation / Math.sqrt(givenNumbers.size.toDouble())
        return mutableListOf<Double>(r.redondear(mean - temp), r.redondear(mean + temp))
    }

    fun NormalDistribution(media: Double, DesvTip: Double): Double {
        val random = Random()
        return DesvTip * random.nextGaussian() + media
    }





}