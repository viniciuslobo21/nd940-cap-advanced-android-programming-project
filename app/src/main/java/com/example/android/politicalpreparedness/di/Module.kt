package com.example.android.politicalpreparedness.di

import com.example.android.politicalpreparedness.data.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.repository.ElectionsRepositoryImpl
import com.example.android.politicalpreparedness.repository.local.ElectionsLocalDataSource
import com.example.android.politicalpreparedness.repository.local.LocalDataSource
import com.example.android.politicalpreparedness.repository.remote.ElectionsRemoteDataSource
import com.example.android.politicalpreparedness.repository.remote.RemoteDataSource
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

object Module {
    fun getModule() = module {
        single { ElectionDatabase.getInstance(get()).electionDao }

        single {
            return@single CivicsApi.retrofitService
        }

        single<LocalDataSource>(StringQualifier("cache")) { ElectionsLocalDataSource(get()) }
        single<RemoteDataSource>(StringQualifier("remote")) { ElectionsRemoteDataSource(get()) }

        single<ElectionsRepository> {
            ElectionsRepositoryImpl(
                    get(StringQualifier("cache")), get(StringQualifier("remote")))
        }

        viewModel { ElectionsViewModel(get(), get()) }
        viewModel { (election: Election) -> VoterInfoViewModel(get(), election) }
        viewModel { RepresentativeViewModel(get()) }
    }
}
