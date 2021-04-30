package com.allexandresantos.politicalpreparedness.voterinfo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.data.models.VoterInfoResponse
import com.allexandresantos.politicalpreparedness.data.repository.elections.ElectionsRepository
import com.allexandresantos.politicalpreparedness.data.repository.voterinfo.VoterInfoRepository
import com.allexandresantos.politicalpreparedness.data.models.Result
import com.allexandresantos.politicalpreparedness.util.Event
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val electionsRepository: ElectionsRepository, private val voterInfoRepository: VoterInfoRepository) : ViewModel() {

    private var election: Election? = null

    private val isElectionSaved = MutableLiveData<Boolean>()

    val webActionUrl = MutableLiveData<Event<String>>()

    val buttonState = Transformations.map(isElectionSaved) {
        if (it)
            ButtonState.UNSAVE
        else
            ButtonState.SAVE
    }

    val isVoterInfoReady = MutableLiveData<Event<Boolean>>()

    var voterInfoResponse: VoterInfoResponse? = null

    fun initElection(election: Election?) {
        this.election = election
        election?.let {
            isElectionInDatabase(it)
            getVoterInfo(it)
        }
    }

    private fun isElectionInDatabase(election: Election?) = viewModelScope.launch {
        isElectionSaved.value = election?.let { electionsRepository.getElectionById(it.id) } is Result.Success<*>
    }

    fun saveUnsaveElection() {
        if (buttonState.value == ButtonState.SAVE) {
            saveElection(this.election)
        } else {
            removeElection(this.election)
        }
    }

    private fun saveElection(election: Election?) = viewModelScope.launch {
        election?.let {
            electionsRepository.saveElection(it)
            isElectionInDatabase(it)
        }
    }

    private fun removeElection(election: Election?) = viewModelScope.launch {
        election?.let {
            electionsRepository.deleteElection(it)
            isElectionInDatabase(it)
        }
    }

    private fun getVoterInfo(election: Election) = viewModelScope.launch {
        when (val result = voterInfoRepository.getVoterInfo(election)) {
            is Result.Success<VoterInfoResponse> -> {
                voterInfoResponse = result.data
                isVoterInfoReady.value = Event(true)
            }
            else -> Log.e(TAG, "getVoterInfo: post Error")
        }
    }

    fun goToVotingLocations() {
        val url = voterInfoResponse?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
        webActionUrl.value = url?.let { Event(it) }
    }

    fun goToBallotInformation() {
        val url = voterInfoResponse?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
        webActionUrl.value = url?.let { Event(it) }
    }

    enum class ButtonState {
        SAVE, UNSAVE
    }

    companion object {
        private val TAG = VoterInfoResponse::class.java.simpleName
    }
}