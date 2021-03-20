package com.example.android.politicalpreparedness.repository.local

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Election

/**
 * Main entry point for accessing election data.
 */
interface LocalDataSource {

    fun getElections(): LiveData<List<Election>>
    suspend fun saveElection(election: Election)
    suspend fun removeElection(electionId: Int)
    fun getElection(id: Int): Election
    suspend fun clear()
    suspend fun isElectionSaved(election: Election): Boolean

}