package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel constructor(private val repository: ElectionsRepository) :
        ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()

    val representative: LiveData<List<Representative>>
        get() = _representatives

    private val _loading = MutableLiveData<Boolean>()

    val loading: LiveData<Boolean>
        get() = _loading

    private val _address = MutableLiveData(Address("", "", "", "", ""))
    val address: LiveData<Address>
        get() = _address

    init {
        _loading.value = false
    }


    fun callGetRepresentative(address: Address) {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.getRepresentatives(address = address.toFormattedString())
                when (response) {
                    is Result.Success -> {
                        handleSuccessRepresentatives(response)
                    }
                    is Result.Error -> {
                        Timber.e(response.message)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e.message)
            }
            _loading.postValue(false)
        }
    }

    private fun handleSuccessRepresentatives(response: Result.Success<RepresentativeResponse>) {
        val data = response.data
        _representatives.value = data.offices.flatMap { office ->
            office.getRepresentatives(data.officials)
        }
    }

    fun onClickFindMyRepresentatives() {
        _address.value?.let { callGetRepresentative(it) }
    }

    fun onClickUseMyLocation(address: Address) {
        _address.value = address
        callGetRepresentative(address)
    }
}
