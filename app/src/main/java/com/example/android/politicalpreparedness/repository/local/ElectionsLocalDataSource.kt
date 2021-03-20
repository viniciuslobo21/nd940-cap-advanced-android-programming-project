package com.example.android.politicalpreparedness.repository.local

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsLocalDataSource(
        private val electionDao: ElectionDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource {

    override fun getElections(): LiveData<List<Election>> {
        return electionDao.getElections()
    }

    override suspend fun saveElection(election: Election) =
            withContext(ioDispatcher) {
                electionDao.saveElection(election = election)
            }

    override suspend fun removeElection(electionId: Int) = withContext(ioDispatcher) {
        return@withContext electionDao.unfollowElection(electionId)
    }

    override fun getElection(id: Int): Election {
        return electionDao.getElection(id)
    }

    override suspend fun clear() {
        withContext(ioDispatcher) {
            electionDao.clear()
        }
    }

    override suspend fun isElectionSaved(election: Election): Boolean {
        val e: Election
        withContext(Dispatchers.IO) {
            e = electionDao.getElection(election.id)
        }
        return e == election
    }
}