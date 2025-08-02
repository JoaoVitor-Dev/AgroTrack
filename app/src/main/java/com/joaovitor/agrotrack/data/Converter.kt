package com.joaovitor.agrotrack.data

import androidx.room.TypeConverter
import com.joaovitor.agrotrack.data.entity.StatusTarefa

class Converter {
    @TypeConverter
    fun fromStatusTarefa(status: StatusTarefa): String {
        return status.name
    }

    @TypeConverter
    fun toStatusTarefa(status: String): StatusTarefa {
        return StatusTarefa.valueOf(status)
    }
}