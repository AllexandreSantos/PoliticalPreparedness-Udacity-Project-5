package com.allexandresantos.politicalpreparedness.data.database

import androidx.room.*
import com.allexandresantos.politicalpreparedness.data.models.Election

@Dao
interface ElectionDao {

    @Query("SELECT * FROM election_table")
    suspend fun getElections(): List<Election>

    @Query("SELECT * FROM election_table where id = :electionId")
    suspend fun getElectionById(electionId: Int): Election?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElection(election: Election)

    @Delete
    suspend fun deleteElection(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(vararg elections: Election)


}