package com.smart.smartbalibackpaker.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smart.smartbalibackpaker.core.data.source.local.entity.*

@Database(
    entities = [TourismDataEntity::class, GuideMapsEntity::class, UploadResultEntity::class, AccomDataEntity::class, RecordGuideEntity::class, RecordVacationListEntity::class],
    version = 2,
    exportSchema = false
)
//@TypeConverters(Converter::class)
abstract class TourismDatabase : RoomDatabase() {
    abstract fun tourismDao(): TourismDao
    abstract fun guideDao(): GuideDao
    abstract fun uploadResultDao(): UploadResultDao
    abstract fun accomDataDao(): AccomDataDao
    abstract fun recordGuideDao(): RecordGuideDao
    abstract fun recordVacationDao(): RecordVacationDao

    companion object {
        @Volatile
        private var INSTANCE: TourismDatabase? = null

        fun getInstance(context: Context): TourismDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TourismDatabase::class.java,
                    "tourism_database"
                ).fallbackToDestructiveMigration().build().apply {
                    INSTANCE = this
                }
            }
    }
}