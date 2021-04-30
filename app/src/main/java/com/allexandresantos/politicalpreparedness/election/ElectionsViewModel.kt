package com.allexandresantos.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.data.models.Result
import com.allexandresantos.politicalpreparedness.data.repository.elections.ElectionsRepository
import com.allexandresantos.politicalpreparedness.util.Event
import kotlinx.coroutines.launch

class ElectionsViewModel(private val electionsRepository: ElectionsRepository) : ViewModel() {

    val networkElectionsList = MutableLiveData<List<Election>>()

    val savedElectionsList = MutableLiveData<List<Election>>()

    val showNoData = MutableLiveData<Boolean>()

    private val showLoadingEvent = MutableLiveData<Event<Boolean>>()

    val showLoading = Transformations.map(showLoadingEvent){ event ->
        event.getContentIfNotHandled()
    }



    fun loadNetworkElections() {
        if (networkElectionsList.value.isNullOrEmpty())
            showLoadingEvent.value = Event(true)
        else
            showLoadingEvent.value = Event(false)

        viewModelScope.launch {

            val result = electionsRepository.getElections()

            showLoadingEvent.postValue(Event(false))

            when (result) {
                is Result.Success<*> -> {
                    if (networkElectionsList.value != result.data as List<Election>) {
                        networkElectionsList.value = result.data as List<Election>
                    }
                }
                is Result.Error -> Log.e(TAG, "loadElections: " + result.message)
            }

        }

    }

    fun loadSavedElections(){

        viewModelScope.launch {
            val result = electionsRepository.getSavedElections()

            when (result) {
                is Result.Success<*> -> {
                    if (savedElectionsList.value != result.data as List<Election>) {
                        savedElectionsList.value = result.data as List<Election>
                    }
                }
                is Result.Error -> {
                    Log.e(TAG, "loadElections: " + result.message)
                }
            }

            invalidateShowNoData()
        }

    }

    private fun invalidateShowNoData() {
        showNoData.value = savedElectionsList.value == null || savedElectionsList.value!!.isEmpty()
        Log.d(TAG, "invalidateShowNoData: " + showNoData.value)
    }

    companion object {
        private val TAG = ElectionsViewModel::class.java.simpleName
    }
}