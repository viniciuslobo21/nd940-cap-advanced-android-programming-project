package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.local.ElectionsLocalDataSource
import com.example.android.politicalpreparedness.repository.remote.ElectionsRemoteDataSource

class ElectionsRepositoryImpl(
        private val electionsLocalDataSource: ElectionsLocalDataSource,
        private val electionsRemoteDataSource: ElectionsRemoteDataSource
) : ElectionsRepository {

    override fun getElections(): LiveData<List<Election>> {
        return electionsLocalDataSource.getElections()
    }

    override suspend fun saveElection(election: Election) {
        electionsLocalDataSource.saveElection(election)
    }

    override suspend fun removeElection(electionId: Int) {
        electionsLocalDataSource.removeElection(electionId)
    }

    override fun getElection(id: Int): Election {
        return electionsLocalDataSource.getElection(id)
    }

    override suspend fun clear() {
        electionsLocalDataSource.clear()
    }

    override suspend fun getUpcomingElections(): Result<List<Election>> {
        return electionsRemoteDataSource.getUpcomingElections()
    }

    override suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse> {
        return electionsRemoteDataSource.getVoterInfo(address, electionId)
    }

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> {
        return electionsRemoteDataSource.getRepresentatives(address)
    }

    override suspend fun isElectionSaved(election: Election): Boolean {
        return electionsLocalDataSource.isElectionSaved(election)
    }

}