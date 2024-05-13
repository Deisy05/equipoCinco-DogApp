package com.borjaapp.equipocinco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.borjaapp.equipocinco.model.Appointment
import com.borjaapp.equipocinco.repository.AppointmentRepository
import kotlinx.coroutines.launch

//import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


class AppointmentViewModel (application: Application) : AndroidViewModel(application){
    val context = getApplication<Application>()
    private val appointmentRepository = AppointmentRepository(context)
    fun saveAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appointmentRepository.saveAppointment(appointment)

        }
    }
}