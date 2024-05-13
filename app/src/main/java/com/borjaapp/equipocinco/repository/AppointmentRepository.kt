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

    suspend fun getListAppointment(): MutableList<Appointment> {
        return withContext(Dispatchers.IO) {
            appointmentDao.getListAppointment()
        }
    }

    suspend fun deleteAppointment(appointment: Appointment){
        withContext(Dispatchers.IO){
            appointmentDao.deleteAppointment(appointment)
        }
    }

    suspend fun updateRepository(appointment: Appointment){
        withContext(Dispatchers.IO){
            appointmentDao.updateAppointment(appointment)
        }
    }
}