package com.joaovitor.agrotrack.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros_atividade")
data class RegistroAtividade(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tipoAtividade: String,
    val talhao: String,
    val horaInicio: String,
    val horaFim: String,
    val observacoes: String,
    val dataRegistro: Long = System.currentTimeMillis()
)
