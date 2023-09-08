package com.example.personas.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.personas.data.local.dao.PersonaDao
import com.example.personas.data.local.entity.Persona

@Database(
    entities = [Persona::class ],
    version = 1,
    exportSchema = false
)
abstract class PersonasDb: RoomDatabase(){
    abstract fun personaDao(): PersonaDao
}