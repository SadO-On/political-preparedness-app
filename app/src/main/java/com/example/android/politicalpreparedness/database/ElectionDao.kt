package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)

    @Query("SELECT * FROM election_table")
    fun selectAllElection(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id= :idElection")
    fun selectElection(idElection: Int): LiveData<Election>

    @Query("DELETE FROM election_table WHERE id = :idElection")
    fun deleteElection(idElection: Int)

    @Query("DELETE FROM election_table")
    fun clear()

    @Query("SELECT EXISTS(SELECT * FROM election_table WHERE id= :idElection)")
    fun doesExist(idElection: Int): Boolean

}