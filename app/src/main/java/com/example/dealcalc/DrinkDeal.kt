package com.example.dealcalc


data class DrinkDeal(var drinkName: String, var totalPrice: Float, var amountOfCans: Float, var millilitres: Float)
    : Comparable<DrinkDeal>{
    var costPerML: Float = 0.0f

    fun calculateCostPerML(){
        val totalML = amountOfCans * millilitres
        costPerML = totalPrice / totalML
    }

    override fun toString(): String {
        return "${this.drinkName}, Cost per ML: ${this.costPerML}"
    }

    override fun compareTo(other: DrinkDeal): Int {
        return if(this.costPerML > other.costPerML){
            1
        } else if(this.costPerML < other.costPerML){
            -1
        } else{
            0
        }
    }
}



