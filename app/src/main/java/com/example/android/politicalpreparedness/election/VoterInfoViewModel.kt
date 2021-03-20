package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.State
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

private const val UNFOLLOW_ELECTION = "UNFOLLOW ELECTION"
private const val FOLLOW_ELECTION = "FOLLOW ELECTION"

class VoterInfoViewModel(
        val repository: ElectionsRepository,
        val election: Election) :
        ViewModel() {

    val state = MutableLiveData<State>()

    private val _buttonText = MutableLiveData<String>()
    val buttonText: LiveData<String>
        get() = _buttonText

    private val _errorInfo = MutableLiveData<String?>()
    private val _voterInfo = MutableLiveData<VoterInfoResponse?>()

    private val _electionInfoUrl = MutableLiveData<String>()
    val electionInfoUrl: LiveData<String>
        get() = _electionInfoUrl

    private val _votingLocationFinderUrl = MutableLiveData<String>()
    val votingLocationFinderUrl: LiveData<String>
        get() = _votingLocationFinderUrl

    private val _ballotInfoUrl = MutableLiveData<String>()
    val ballotInfoUrl: LiveData<String>
        get() = _ballotInfoUrl

    private val _errorOnFetchingNetworkData = MutableLiveData<Boolean>(false)
    val errorOnFetchingNetworkData: LiveData<Boolean>
        get() = _errorOnFetchingNetworkData

    val hasVotingLocationsInformation = Transformations.map(state) { state ->
        state.electionAdministrationBody.votingLocationFinderUrl != null
    }

    val hasBallotInformation = Transformations.map(state) { state ->
        state.electionAdministrationBody.ballotInfoUrl != null
    }

    val hasCorrespondenceInformation = Transformations.map(state) { state ->
        state.electionAdministrationBody.correspondenceAddress != null
    }

    val correspondenceAddress = Transformations.map(state) { state ->
        state.electionAdministrationBody.correspondenceAddress?.toFormattedString()
    }

    init {

        viewModelScope.launch {
            toggleButtonText()
            try {
                val address = getAddress()
                val response = repository.getVoterInfo(address, election.id)
                handleVoterInfo(response)
            } catch (e: Exception) {
                _errorInfo.value = "Something is wrong! Erro to get Voter info"
            }
        }
    }

    private fun getAddress() = if (election.division.state.isNotBlank())
        "${election.division.country}, ${election.division.state}"
    else election.division.country

    private fun handleVoterInfo(response: Result<VoterInfoResponse>) {
        when (response) {
            is Result.Success -> {
                _voterInfo.value = response.data
                state.value = response.data.state?.get(0)
            }
            is Result.Error -> {
                _errorInfo.value = response.message
            }
        }
    }

    fun openElectionInformationUrl() {
        _votingLocationFinderUrl.value = state.value?.electionAdministrationBody?.electionInfoUrl
    }

    fun onOpenElectionInformationUrlComplete() {
        _votingLocationFinderUrl.value = null
    }

    fun openVotingLocationsUrl() {
        _votingLocationFinderUrl.value = state.value?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun onOpenVotingLocationsUrlComplete() {
        _votingLocationFinderUrl.value = null
    }

    fun openBallotInfoUrl() {
        _ballotInfoUrl.value = state.value?.electionAdministrationBody?.ballotInfoUrl
    }

    fun onOpenBallotInfoUrlComplete() {
        _ballotInfoUrl.value = null
    }

    fun displayNetworkErrorComplete() {
        _errorOnFetchingNetworkData.value = false
    }

    private suspend fun toggleButtonText() {
        if (repository.isElectionSaved(election)) {
            _buttonText.value = UNFOLLOW_ELECTION
        } else {
            _buttonText.value = FOLLOW_ELECTION
        }
    }

    fun toggleFollow() {
        viewModelScope.launch {
            if (repository.isElectionSaved(election)) {
                repository.removeElection(election.id)
            } else {
                repository.saveElection(election)
            }
            toggleButtonText()
        }
    }
}