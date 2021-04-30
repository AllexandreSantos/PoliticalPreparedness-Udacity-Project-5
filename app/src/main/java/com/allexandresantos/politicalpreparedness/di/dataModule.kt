package com.allexandresantos.politicalpreparedness.di

import com.allexandresantos.politicalpreparedness.data.repository.elections.ElectionsRepository
import com.allexandresantos.politicalpreparedness.data.repository.elections.ElectionsRepositoryImpl
import com.allexandresantos.politicalpreparedness.data.database.ElectionDao
import com.allexandresantos.politicalpreparedness.data.database.ElectionDatabaseObj
import com.allexandresantos.politicalpreparedness.data.network.CivicsApiObj
import com.allexandresantos.politicalpreparedness.data.network.CivicsApiService
import com.allexandresantos.politicalpreparedness.data.repository.representatives.RepresentativesRepository
import com.allexandresantos.politicalpreparedness.data.repository.representatives.RepresentativesRepositoryImpl
import com.allexandresantos.politicalpreparedness.data.repository.voterinfo.VoterInfoRepository
import com.allexandresantos.politicalpreparedness.data.repository.voterinfo.VoterInfoRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single { ElectionDatabaseObj.createElectionDao(androidApplication().applicationContext) }
    single { CivicsApiObj.civicsApiService() }
    single { ElectionsRepositoryImpl(get() as ElectionDao, get() as CivicsApiService) as ElectionsRepository }
    single { VoterInfoRepositoryImpl(get() as CivicsApiService) as VoterInfoRepository}
    single { RepresentativesRepositoryImpl(get() as CivicsApiService) as RepresentativesRepository }
}