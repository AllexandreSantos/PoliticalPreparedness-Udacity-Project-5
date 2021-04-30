package com.allexandresantos.politicalpreparedness.data.database

import android.content.Context
import androidx.room.Room

object ElectionDatabaseObj {

    fun createElectionDao(context: Context): ElectionDao{
        return Room.databaseBuilder(
                context.applicationContext,
                ElectionDatabase::class.java,
                "election_database"
        ).fallbackToDestructiveMigration().build().electionDao()
    }

}