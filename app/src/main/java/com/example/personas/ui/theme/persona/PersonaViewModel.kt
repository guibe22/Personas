package com.example.personas.ui.theme.persona

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personas.data.local.entity.Persona
import com.example.personas.data.repository.PersonaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonaViewModel @Inject constructor(
    private val PersonaRepository: PersonaRepository
) : ViewModel()  {
    var nombre by mutableStateOf("")
    var nombreError by mutableStateOf(false)

    var telefono by mutableStateOf("")
    var telefonoError by mutableStateOf(false)

    var celular by mutableStateOf("")
    var celularError by mutableStateOf(false)

    var email by mutableStateOf("")
    var emailError by mutableStateOf(false)

    var fechaNac by mutableStateOf("")
    var fechaNacError by mutableStateOf(false)

    var ocupacion by mutableStateOf("")
    var ocupacionError by mutableStateOf(false)

    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()

    fun setMessageShown() {
        viewModelScope.launch {
            _isMessageShown.emit(true)
        }
    }
    fun onNombreChanged(valor: String) {
        nombre = valor
        nombreError = valor.isBlank();
    }
    fun onTelefonoChanged(valor: String) {
        telefono= valor
        telefonoError = valor.isBlank();
    }

    fun oncelularChanged(valor: String) {
        celular= valor
        celularError = valor.isBlank();
    }
    fun onEmailChanged(valor: String) {
        email= valor
        emailError = valor.isBlank();
    }

    fun onfechaNacChanged(valor: String) {
        fechaNac= valor
        fechaNacError = valor.isBlank();
    }
    fun onOcupacionChanged(valor: String) {
        ocupacion= valor
       ocupacionError = valor.isBlank();
    }


    val Personas: StateFlow<List<Persona>> = PersonaRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    fun savePersona() {
        viewModelScope.launch {
            val persona = Persona(
                Nombre = nombre,
                Telefono = telefono,
                Celular = celular,
                Email = email,
                FechaNacimiento = fechaNac,
                Ocupacion = ocupacion
            )
            PersonaRepository.save(persona)
            limpiar()
        }
    }

    private fun limpiar() {
        nombre=""
        telefono=""
        celular=""
        email=""
        fechaNac=""
        ocupacion=""
    }

    fun deletePersona(persona: Persona)
    {
        viewModelScope.launch {

            PersonaRepository.delete(persona)
            limpiar()
        }
    }





}