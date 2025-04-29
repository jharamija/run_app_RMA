package com.example.run_app_rma.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.run_app_rma.domain.model.LocationDataEntity

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocationData(data: LocationDataEntity)
}