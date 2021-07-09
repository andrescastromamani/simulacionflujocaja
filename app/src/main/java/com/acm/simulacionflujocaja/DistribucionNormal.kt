package com.acm.simulacionflujocaja
import java.util.Random
class DistribucionNormal{



    var random:Random= Random()
    fun numeroAletorio(){
        this.random=Random(System.currentTimeMillis()
        )
    }
    @JvmName("setRandom1")
    fun setRandom(semilla:Long){
        this.random=Random(semilla)

    }
   fun getRandom(): Double {
      return this.random.nextDouble()
    }
    fun NormalDistribution(media: Double, DesvTip: Double): Double {
        val random = Random()
        return DesvTip * random.nextGaussian() + media
    }

}