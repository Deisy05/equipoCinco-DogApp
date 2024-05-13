package com.borjaapp.equipocinco.repository

import android.content.Context
import com.borjaapp.equipocinco.data.AppointmentDB
import com.borjaapp.equipocinco.data.AppointmentDao
import com.borjaapp.equipocinco.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppointmentRepository (val context: Context){
    private var appointmentDao: AppointmentDao = AppointmentDB.getDatabase(context).appointmentDao()
    suspend fun saveAppointment(appointment: Appointment){
        withContext(Dispatchers.IO){
            appointmentDao.saveAppointment(appointment)
        }
    }
}