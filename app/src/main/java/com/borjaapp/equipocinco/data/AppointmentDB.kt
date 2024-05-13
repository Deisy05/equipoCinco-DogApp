package com.borjaapp.equipocinco.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.borjaapp.equipocinco.model.Appointment
import com.borjaapp.equipocinco.utils.Constants.NAME_BD

@Database(entities = [Appointment::class], version = 1)
abstract class AppointmentDB : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao

    companion object{
        fun getDatabase(context: Context): AppointmentDB {
            return Room.databaseBuilder(
                context.applicationContext,
                AppointmentDB::class.java,
                NAME_BD
            ).build()
        }
    }
}
