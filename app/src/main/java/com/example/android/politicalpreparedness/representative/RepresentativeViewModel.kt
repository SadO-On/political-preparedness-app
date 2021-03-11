package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

private const val TAG = "RepresentativeViewModel"

class RepresentativeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getInstance(application)
    private val repository = ElectionRepository(database)

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representative: LiveData<List<Representative>>
        get() = _representatives


    fun getRepresentativeList() {
        viewModelScope.launch {
            try {
                val (offices, officials) = repository.getRepresentatives(_address.value!!.toFormattedString())
                _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
                Log.i(TAG, _representatives.value.toString())
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }

    }

    fun getAddress(address: Address) {
        _address.value = address
    }


}

class RepresentativesViewModelFactory(
        private val application: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (RepresentativeViewModel(application) as T)
}