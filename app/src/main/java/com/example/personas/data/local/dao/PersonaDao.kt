package com.example.personas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.personas.data.local.entity.Persona
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(Persona: Persona)

    @Query(
        """
        SELECT * 
        FROM Personas
        WHERE PersonaId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): Persona?

    @Delete
    suspend fun delete(ticket: Persona)

    @Query("SELECT * FROM Personas")
    fun getAll(): Flow<List<Persona>>



}