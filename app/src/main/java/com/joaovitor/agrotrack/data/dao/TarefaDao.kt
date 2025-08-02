package com.joaovitor.agrotrack.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.joaovitor.agrotrack.data.entity.StatusTarefa
import com.joaovitor.agrotrack.data.entity.Tarefa

@Dao
interface TarefaDao {
    @Query("SELECT * FROM tarefas ORDER BY horaPrevista ASC")
    fun getAllTarefas(): LiveData<List<Tarefa>>

    @Query("SELECT * FROM tarefas WHERE DATE(dataCriacao/1000, 'unixepoch') = DATE('now') ORDER BY horaPrevista ASC")
    fun getTarefasHoje(): LiveData<List<Tarefa>>

    @Insert
    suspend fun insertTarefa(tarefa: Tarefa): Long

    @Update
    suspend fun updateTarefa(tarefa: Tarefa)

    @Delete
    suspend fun deleteTarefa(tarefa: Tarefa)

    @Query("UPDATE tarefas SET status = :novoStatus WHERE id = :tarefaId")
    suspend fun updateStatus(tarefaId: Long, novoStatus: StatusTarefa)
}