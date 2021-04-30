package com.allexandresantos.politicalpreparedness.data.repository.representatives

import android.util.Log
import com.allexandresantos.politicalpreparedness.data.models.Address
import com.allexandresantos.politicalpreparedness.data.models.Official
import com.allexandresantos.politicalpreparedness.data.models.RepresentativeResponse
import com.allexandresantos.politicalpreparedness.data.models.Result
import com.allexandresantos.politicalpreparedness.data.network.CivicsApiService
import com.allexandresantos.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class RepresentativesRepositoryImpl(
        private val civicsApiService: CivicsApiService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): RepresentativesRepository {

    override suspend fun getRepresentatives(address: Address): Result<List<Representative>> = withContext(ioDispatcher) {
        try {
            val representativesResponse = civicsApiService.getRepresentatives(address)
            if (representativesResponse != null){
                val representatives = mapResponseToRepresentatives(representativesResponse)
                return@withContext Result.Success(representatives)
            }
            Log.e(TAG, "refreshElections: Could not get representatives")
            return@withContext Result.Error("Could not get representatives")
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

    private fun mapResponseToRepresentatives(representativesResponse: RepresentativeResponse): List<Representative> {
        val offices = representativesResponse.offices
        val officials = representativesResponse.officials
        return offices.flatMap { office ->  office.getRepresentatives(officials)}
    }

    companion object{
        val TAG = RepresentativesRepositoryImpl::class.java.simpleName
    }
}