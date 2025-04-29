package com.example.run_app_rma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.run_app_rma.data.dao.LocationDao
import com.example.run_app_rma.data.dao.RunDao
import com.example.run_app_rma.data.dao.SensorDao
import com.example.run_app_rma.domain.model.LocationDataEntity
import com.example.run_app_rma.domain.model.RunEntity
import com.example.run_app_rma.domain.model.SensorDataEntity

@Database(
    entities = [RunEntity::class, SensorDataEntity::class, LocationDataEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
    abstract fun sensorDao(): SensorDao
    abstract fun locationDao(): LocationDao
}