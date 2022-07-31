package com.code.weather.repository

import android.content.Context
import androidx.room.*

@Database(entities = [City::class], version = 1)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        private var instance: CityDatabase? = null

        fun getInstance(context: Context): CityDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    "city_database"
                ).fallbackToDestructiveMigration().build()
            return instance!!
        }
    }
}

@Dao
interface CityDao {
    @Insert
    suspend fun insertCity(city: City)

    @Delete
    suspend fun deleteCity(city: City)

    @androidx.room.Query("SELECT * FROM City_Table order by cityName")
    suspend fun getAllCities(): List<City>
}

@Entity(tableName = "City_Table")
data class City (
    @PrimaryKey
    @ColumnInfo(name = "cityName") val cityName: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
)
