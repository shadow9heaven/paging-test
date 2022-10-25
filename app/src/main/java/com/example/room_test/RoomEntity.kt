package com.example.room_test

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.room_test.RoomEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
class RoomEntity {

    companion object {
        const val TABLE_NAME = "room_entity"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String = ""

    @NonNull
    @ColumnInfo(name = "timestamp", typeAffinity = ColumnInfo.TEXT)
    var time = ""

    @Ignore
    var ignoreText = ""
}