package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

enum class ElectionListStatus {LOADING, DONE, ERROR}
class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getInstance(application)
    private val repository = ElectionRepository(database)

    private val _navigateToVoterInfo = MutableLiveData<Election>()

    val savedElectionList = repository.list

    private val _status =MutableLiveData<ElectionListStatus>()
    val status : LiveData<ElectionListStatus>
        get() = _status

    val electionList = liveData(Dispatchers.IO) {
        try {
            _status.postValue( ElectionListStatus.LOADING)
            Log.i("ElectionsViewModel", repository.getElection().body().toString())
            val retrievedElection = repository.getElection().body()
            emit(retrievedElection?.elections)
            _status.postValue( ElectionListStatus.DONE)

        } catch (e: Exception) {
            _status.postValue( ElectionListStatus.ERROR)
            Log.e("ElectionsViewModel", e.message.toString())
        }
    }

    init {
        viewModelScope.launch {
            repository.getElections()
        }
    }

    val navigateToVoterInfo: LiveData<Election>
        get() = _navigateToVoterInfo

    fun displayVoterInfo(election: Election){
        _navigateToVoterInfo.value =election
    }
    fun displayVoterInfoComplete(){
        _navigateToVoterInfo.value =null
    }
}