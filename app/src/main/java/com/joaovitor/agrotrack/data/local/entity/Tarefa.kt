package com.joaovitor.agrotrack.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarefas")
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nomeAtividade: String,
    val talhao: String,
    val horaPrevista: String,
    val status: StatusTarefa,
    val dataCriacao: Long = System.currentTimeMillis()
)

enum class StatusTarefa {
    PENDENTE,
    EM_ANDAMENTO,
    FINALIZADA
}
