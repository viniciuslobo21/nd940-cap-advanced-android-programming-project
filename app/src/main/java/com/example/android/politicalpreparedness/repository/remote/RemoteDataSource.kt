package com.example.android.politicalpreparedness.repository.remote

import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse

interface RemoteDataSource {

    suspend fun getUpcomingElections(): Result<List<Election>>
    suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse>
    suspend fun getRepresentatives(address: String): Result<RepresentativeResponse>

}