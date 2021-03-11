package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoterInfoViewModel(app: Application, id: Int, division: Division) : AndroidViewModel(app) {
    private val database = ElectionDatabase.getInstance(app)
    private val repository = ElectionRepository(database)

    private val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _doesFollow = MutableLiveData<Boolean>()
    val doesFollow: LiveData<Boolean>
        get() = _doesFollow

    private val _status = MutableLiveData<ElectionListStatus>()
    val status: LiveData<ElectionListStatus>
        get() = _status


    init {
        viewModelScope.launch {
            _doesFollow.value = repository.doesExist(id)

        }
    }

    private val addressValue: String = checkStateAddressIfNull(division)
    val voterInfo = liveData<VoterInfoResponse>(Dispatchers.IO) {
        try {
            _status.postValue(ElectionListStatus.LOADING)
            val response = repository.getVoterInfo(id, addressValue).body()
            emit(response!!)
            _status.postValue(ElectionListStatus.DONE)

        } catch (e: Exception) {
            _status.postValue(ElectionListStatus.ERROR)

            Log.e("VoterInfoViewModel", e.message.toString())

        }
    }

    private fun followElection() {
        if (voterInfo.value == null) {
            return
        }
        viewModelScope.launch {
            repository.saveElectionInDB(voterInfo.value!!.election)
        }
    }

    private fun unFollowElection() {
        if (voterInfo.value == null) {
            return
        }
        viewModelScope.launch {
            repository.deleteElection(voterInfo.value!!.election.id)
        }
    }

    fun handleState() {
        if (_doesFollow.value!!) {
            _doesFollow.value = false
            unFollowElection()
        } else {
            _doesFollow.value = true
            followElection()
        }
    }


    private fun checkStateAddressIfNull(division: Division): String {
        return if (division.state.isEmpty()) {
            _errorMessage.value = "No data available Please try another election"
            " ${division.country} - "
        } else {
            "${division.country} - ${division.state}"
        }

    }


}