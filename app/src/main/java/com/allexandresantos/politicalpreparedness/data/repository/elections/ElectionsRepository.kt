package com.allexandresantos.politicalpreparedness.data.repository.elections

import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.data.models.Result

interface ElectionsRepository {
    suspend fun getElections(): Result<List<Election>>
    suspend fun getSavedElections(): Result<List<Election>>
    suspend fun getElectionById(id: Int): Result<Election>
    suspend fun saveElection(election: Election)
    suspend fun deleteElection(election: Election)
    suspend fun insertElections(vararg elections: Election)
}