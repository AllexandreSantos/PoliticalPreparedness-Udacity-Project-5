package com.allexandresantos.politicalpreparedness.data.repository.representatives

import com.allexandresantos.politicalpreparedness.data.models.Address
import com.allexandresantos.politicalpreparedness.data.models.Official
import com.allexandresantos.politicalpreparedness.data.models.RepresentativeResponse
import com.allexandresantos.politicalpreparedness.data.models.Result
import com.allexandresantos.politicalpreparedness.representative.model.Representative

interface RepresentativesRepository {

    suspend fun getRepresentatives(address: Address): Result<List<Representative>>
}