package com.example.personas.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Personas")
class Persona (
    @PrimaryKey
    val PersonaId: Int?=null,
    var Nombre: String = "",
    var Telefono: String = "",
    var Celular: String = "",
    var Email: String = "",
    var FechaNacimiento: String = "",
    var Ocupacion: String = ""

)


