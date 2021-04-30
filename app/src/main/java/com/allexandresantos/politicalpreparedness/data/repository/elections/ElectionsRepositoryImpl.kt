package com.allexandresantos.politicalpreparedness.data.repository.elections

import android.util.Log
import com.allexandresantos.politicalpreparedness.data.database.ElectionDao
import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.data.models.Result
import com.allexandresantos.politicalpreparedness.data.models.StateCountry
import com.allexandresantos.politicalpreparedness.data.models.VoterInfoResponse
import com.allexandresantos.politicalpreparedness.data.network.CivicsApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ElectionsRepositoryImpl(
        private val electionDao: ElectionDao,
        private val civicsApiService: CivicsApiService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionsRepository {

    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher) {
        try {
            val electionsResponse = civicsApiService.getElections()?.elections
            if (!electionsResponse.isNullOrEmpty())
                return@withContext Result.Success(electionsResponse)
            else
                return@withContext Result.Error("Could not get elections")
        } catch (e: Exception) {
            if (e is HttpException) {
                Log.e(TAG, "voterInfo: ${e.response()}")
                return@withContext Result.Error(e.response().toString())
            } else {
                Log.e(TAG, "voterInfo: ", e)
                return@withContext Result.Error(e.localizedMessage)
            }
        }
    }

    override suspend fun getSavedElections(): Result<List<Election>> = withContext(ioDispatcher) {
        try {
            val electionsResponse = electionDao.getElections()
            return@withContext Result.Success(electionsResponse)
        } catch (e: Exception) {
            Log.e(TAG, "voterInfo: ", e)
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    override suspend fun getElectionById(id: Int): Result<Election> = withContext(ioDispatcher) {
        try {
            val election = electionDao.getElectionById(id)
            if (election != null) {
                return@withContext Result.Success(election)
            } else {
                return@withContext Result.Error("Election not found!")
            }
        } catch (e: Exception) {
            Log.e(TAG, "getElectionById: ", e)
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    override suspend fun saveElection(election: Election) = withContext(ioDispatcher) {
        electionDao.saveElection(election)
    }

    override suspend fun deleteElection(election: Election) = withContext(ioDispatcher) {
        electionDao.deleteElection(election)
    }

    override suspend fun insertElections(vararg elections: Election) {
        electionDao.insertElections(*elections)
    }

    companion object {
        private val TAG = ElectionsRepository::class.java.simpleName
    }

}

