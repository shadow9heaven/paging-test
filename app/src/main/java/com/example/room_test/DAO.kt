package com.example.room_test


import androidx.room.*

interface DAO {

    @Dao
    interface RoomDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: RoomEntity): Long

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll(item: MutableList<RoomEntity>)

        @Query("SELECT * FROM room_entity WHERE name LIKE :name")
        fun findByName(name: String): RoomEntity


        @Query("SELECT * FROM room_entity")
        fun getAll(): MutableList<RoomEntity>
        //val ITEMEACHTIME = 10
        //val INITIALITEM = 30
        @Query("SELECT * FROM room_entity LIMIT :page*10, 10")
        fun getPage( page: Int): MutableList<RoomEntity>

        @Delete
        fun delete(item: RoomEntity)

        @Update
        fun update(item: RoomEntity)
    }
}