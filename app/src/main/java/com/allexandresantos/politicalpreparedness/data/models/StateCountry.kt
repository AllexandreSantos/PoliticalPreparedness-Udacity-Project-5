package com.allexandresantos.politicalpreparedness.data.models

data class StateCountry(val state: String, val country: String){
    override fun toString(): String {
        return "$state $country"
    }
}