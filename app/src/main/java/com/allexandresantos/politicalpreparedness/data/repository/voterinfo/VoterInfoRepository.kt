package com.allexandresantos.politicalpreparedness.data.repository.voterinfo

import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.data.models.StateCountry
import com.allexandresantos.politicalpreparedness.data.models.VoterInfoResponse
import com.allexandresantos.politicalpreparedness.data.models.Result


interface VoterInfoRepository {
    suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse>
    suspend fun saveVoterInfo()
}