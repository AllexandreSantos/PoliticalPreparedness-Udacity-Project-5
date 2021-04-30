package com.allexandresantos.politicalpreparedness.data.repository.voterinfo

import android.util.Log
import com.allexandresantos.politicalpreparedness.data.database.ElectionDao
import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.data.models.Result
import com.allexandresantos.politicalpreparedness.data.models.StateCountry
import com.allexandresantos.politicalpreparedness.data.models.VoterInfoResponse
import com.allexandresantos.politicalpreparedness.data.network.CivicsApiService
import com.allexandresantos.politicalpreparedness.data.repository.elections.ElectionsRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class VoterInfoRepositoryImpl(
        private val civicsApiService: CivicsApiService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): VoterInfoRepository {

    override suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse> = withContext(ioDispatcher) {

        val electionId = election.id

        val stateCountry = StateCountry(election.division.state, election.division.country)

        try {
            val voterInfoResponse = civicsApiService.getVoterInfo(electionId, stateCountry)
            if (voterInfoResponse != null){
                return@withContext Result.Success(voterInfoResponse)
            }
            Log.e(TAG, "refreshElections: Could not refresh elections")
            return@withContext Result.Error("Could not get voterInfo")
        }
        catch (e: Exception){
            if (e is HttpException) {
                Log.e(TAG, "voterInfo: ${e.response()}")
                return@withContext Result.Error(e.response().toString())
            }
            Log.e(TAG, "voterInfo: ", e)
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    override suspend fun saveVoterInfo() {
        TODO("Not yet implemented")
    }

    companion object{
        private val TAG = VoterInfoRepository::class.java.simpleName
    }
}
