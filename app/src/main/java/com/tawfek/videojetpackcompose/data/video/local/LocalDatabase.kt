package com.tawfek.videojetpackcompose.data.video.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VideoEntity::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun videoDao() : VideoDao
}