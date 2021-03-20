package com.example.android.politicalpreparedness.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.data.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveElection(election: Election)

    @Query("select * from election_table order by electionDay asc")
    fun getElections(): LiveData<List<Election>>

    @Query("select * from election_table where id = :electionId")
    fun getElection(electionId: Int): Election

    @Query("delete from election_table where id = :electionId")
    fun unfollowElection(electionId: Int)

    @Query("delete from election_table")
    fun clear()

}