package com.allexandresantos.politicalpreparedness.di

import com.allexandresantos.politicalpreparedness.data.network.CivicsApiService
import com.allexandresantos.politicalpreparedness.data.repository.elections.ElectionsRepository
import com.allexandresantos.politicalpreparedness.data.repository.representatives.RepresentativesRepository
import com.allexandresantos.politicalpreparedness.data.repository.voterinfo.VoterInfoRepository
import com.allexandresantos.politicalpreparedness.election.ElectionsViewModel
import com.allexandresantos.politicalpreparedness.representative.RepresentativeViewModel
import com.allexandresantos.politicalpreparedness.voterinfo.VoterInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ElectionsViewModel(get()) }
    viewModel { VoterInfoViewModel(get() as ElectionsRepository, get() as VoterInfoRepository) }
    viewModel { RepresentativeViewModel(get() as RepresentativesRepository) }
}