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

    private val _listAppointment = MutableLiveData<MutableList<Appointment>>()
    val listAppointment: LiveData<MutableList<Appointment>> get() = _listAppointment

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState
    fun saveAppointment(appointment: Appointment) {
        viewModelScope.launch {
            appointmentRepository.saveAppointment(appointment)

        }
    }

    fun getListAppointment() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listAppointment.value = appointmentRepository.getListAppointment()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }
    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.deleteAppointment(appointment)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.updateRepository(appointment)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }
}