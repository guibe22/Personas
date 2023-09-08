package com.example.personas.data.repository

import com.example.personas.data.PersonasDb
import com.example.personas.data.local.entity.Persona
import javax.inject.Inject

class PersonaRepository @Inject constructor(
    private val PersonasDb: PersonasDb)
{
    suspend fun  save(persona: Persona) = PersonasDb.personaDao().save(persona)
    fun getAll() = PersonasDb.personaDao().getAll()
    suspend fun delete(persona: Persona)= PersonasDb.personaDao().delete(persona)
}