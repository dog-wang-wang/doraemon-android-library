package com.dora.travel.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("travel_plan")
class TravelPlanEntity {
    @PrimaryKey(true)
    @ColumnInfo("travel_plan_id_local")
    var id: Long = 0L
}