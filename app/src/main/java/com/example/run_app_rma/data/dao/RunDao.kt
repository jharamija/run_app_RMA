package com.example.run_app_rma.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.run_app_rma.domain.model.RunEntity

@Dao
interface RunDao {
    @Insert suspend fun insert(run: RunEntity): Long
    @Update suspend fun update(run: RunEntity)
    @Query("SELECT * FROM runs") suspend fun getAllRuns(): List<RunEntity>
}