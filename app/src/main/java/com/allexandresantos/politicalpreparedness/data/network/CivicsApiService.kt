package com.allexandresantos.politicalpreparedness.data.network

import com.allexandresantos.politicalpreparedness.data.models.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    @GET("elections")
    suspend fun getElections(): ElectionResponse?

    @GET("representatives")
    suspend fun getRepresentatives(@Query("address") address: Address): RepresentativeResponse?

    @GET("voterinfo")
    suspend fun getVoterInfo(@Query("electionId") electionId: Int, @Query("address") address: StateCountry): VoterInfoResponse?

}
