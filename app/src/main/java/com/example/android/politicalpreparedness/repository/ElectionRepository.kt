package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ElectionRepository(private val database: ElectionDatabase) {

    var civicsApi: CivicsApiService = CivicsApi.retrofitService

    suspend fun getElection() = civicsApi.getElections()

    suspend fun getVoterInfo(id: Int, address: String) = civicsApi.getVoterInfo(address, id)

    suspend fun saveElectionInDB(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.insertElection(election)
        }
    }
    suspend fun getRepresentatives(address: String) = civicsApi.getRepresentatives(address)
    val list = database.electionDao.selectAllElection()

    suspend fun getElections(): LiveData<List<Election>> {
        return withContext(Dispatchers.IO) {
            database.electionDao.selectAllElection()
        }
    }

    suspend fun doesExist(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            database.electionDao.doesExist(id)
        }

    }

    suspend fun deleteElection(id: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.deleteElection(id)
        }
    }
}