package com.dora.travel.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dora.travel.db.dao.TravelPlan
import com.dora.travel.model.TravelPlanEntity
import com.doraemon.foundation.DoraBaseApplication

@Database(
    version = 1,
    entities = [TravelPlanEntity::class]
)
abstract class DoraemonDatabase : RoomDatabase() {

    abstract fun audio(): TravelPlan

    companion object {
        private const val DB_NAME = "doraemon.db"
        val INSTANCE = Room.databaseBuilder(DoraBaseApplication.app, DoraemonDatabase::class.java, DB_NAME)
            //            .createFromAsset(DB_NAME)
            .fallbackToDestructiveMigration(true)
            .build()
    }
}