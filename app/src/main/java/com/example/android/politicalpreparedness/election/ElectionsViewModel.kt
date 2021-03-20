package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.State
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber

class ElectionsViewModel(
        private val repository: ElectionsRepository,
        private val app: Application
) : AndroidViewModel(app) {

    val state = MutableLiveData<State>()
    val representatives = MutableLiveData<List<Representative>>()
    val upcomingElections = MutableLiveData<List<Election>>()
    val savedElections = repository.getElections()

    private val _errorOnFetchData = MutableLiveData(false)
    val errorOnFetchData: LiveData<Boolean>
        get() = _errorOnFetchData

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean>
        get() = _networkError

    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo: LiveData<Election>
        get() = _navigateToVoterInfo

    fun displayNetworkErrorCompleted() {
        _errorOnFetchData.value = false
    }

    fun onElectionClicked(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun navigationToVoterInfoComplete() {
        _navigateToVoterInfo.value = null
    }

    init {
        viewModelScope.launch {
            getUpcomingElections()
        }
    }

    private fun getUpcomingElections() {
        viewModelScope.launch {
            when (val result = repository.getUpcomingElections()) {
                is Result.Success -> {
                    _errorOnFetchData.value = false
                    upcomingElections.value = result.data
                }
                is Result.Error -> {
                    _errorOnFetchData.value = true
                    Timber.i(result.message)
                }
            }
        }
    }

    fun onClick(election: Election) {
        _navigateToVoterInfo.value = election
    }

    suspend fun followElection(election: Election) {
        repository.saveElection(election)
    }

    suspend fun unfollowElection(election: Election) {
        repository.removeElection(electionId = election.id)
    }
}