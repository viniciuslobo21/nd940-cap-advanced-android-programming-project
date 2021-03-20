package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse

interface ElectionsRepository {

    fun getElections(): LiveData<List<Election>>
    suspend fun saveElection(election: Election)
    suspend fun removeElection(electionId: Int)
    fun getElection(id: Int): Election
    suspend fun clear()

    suspend fun getUpcomingElections(): Result<List<Election>>
    suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse>
    suspend fun getRepresentatives(address: String): Result<RepresentativeResponse>
    suspend fun isElectionSaved(election: Election): Boolean

}