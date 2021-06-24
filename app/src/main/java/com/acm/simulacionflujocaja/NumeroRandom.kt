package com.acm.simulacionflujocaja
import java.util.Random
class aleatorioUniforme {



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

}