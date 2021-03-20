package com.example.android.politicalpreparedness.repository.remote

import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRemoteDataSource(
        private val civicsApiService: CivicsApiService
) : RemoteDataSource {

    override suspend fun getUpcomingElections(): Result<List<Election>> {
        return withContext(Dispatchers.IO) {
            try {
                val electionResponse = civicsApiService.getElections()
                Result.Success(electionResponse.elections)
            } catch (e: Exception) {
                Result.Error("Error to get upcoming Elections")
            }
        }
    }

    override suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val voterResponse = civicsApiService.getVoterInfo(address, electionId)
                Result.Success(voterResponse)
            } catch (e: Exception) {
                Result.Error("Error to get info of voter info")
            }
        }
    }

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val representativeResponse = civicsApiService.getRepresentatives(address)
                Result.Success(representativeResponse)
            } catch (e: Exception) {
                Result.Error(e.message)
            }
        }
    }
}