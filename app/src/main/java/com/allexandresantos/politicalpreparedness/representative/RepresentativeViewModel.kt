package com.allexandresantos.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allexandresantos.politicalpreparedness.R
import com.allexandresantos.politicalpreparedness.data.models.Address
import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.data.models.Official
import com.allexandresantos.politicalpreparedness.data.models.Result
import com.allexandresantos.politicalpreparedness.data.repository.representatives.RepresentativesRepository
import com.allexandresantos.politicalpreparedness.election.ElectionsViewModel
import com.allexandresantos.politicalpreparedness.representative.model.Representative
import com.allexandresantos.politicalpreparedness.util.Event
import kotlinx.coroutines.launch

class RepresentativeViewModel(val representativesRepository: RepresentativesRepository): ViewModel() {

    val error = MutableLiveData<Event<Int>>()

    var address = MutableLiveData<Address>()

    val representativesList = MutableLiveData<List<Representative>>()

    init {
        address.value = Address()
    }

    fun setSearchAddress(address: Address){
        this.address.value = address
    }

    fun launchFindRepresentativesFlow(address: Address){
        if (validateData(address)){
            loadRepresentatives(address)
        }
    }

    private fun validateData(address: Address): Boolean{
        if (address.line1.isEmpty()) {
            error.value = Event(R.string.fill_address_line_1)
            return false
        }
        if (address.city.isEmpty()) {
            error.value = Event(R.string.fill_city)
            return false
        }
        if (address.zip.isEmpty()) {
            error.value = Event(R.string.fill_zip_code)
            return false
        }
        if (address.state == "State") {
            error.value = Event(R.string.select_state)
            return false
        }
        return true
    }

    private fun loadRepresentatives(address: Address) {

        viewModelScope.launch {

            val result = representativesRepository.getRepresentatives(address)

            when (result) {
                is Result.Success<*> -> {
                    if (representativesList.value != result.data as List<Representative>) {
                        representativesList.value = result.data as List<Representative>
                    }
                }
                is Result.Error -> Log.e(TAG, "loadElections: " + result.message)
            }
        }
    }

    companion object{
        private val TAG = RepresentativeViewModel::class.java.simpleName
    }

}
