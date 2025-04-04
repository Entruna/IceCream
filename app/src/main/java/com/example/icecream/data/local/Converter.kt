package com.example.icecream.data.local

import androidx.room.TypeConverter
import com.example.icecream.domain.model.Status

class Converter {
    @TypeConverter
    fun fromRoomStatus(status: Status): String {
        return status.name
    }

    @TypeConverter
    fun toRoomStatus(value: String): Status {
        return Status.valueOf(value)
    }
}