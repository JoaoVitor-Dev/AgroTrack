package com.joaovitor.agrotrack.data.repository

import com.joaovitor.agrotrack.data.dao.RegistroAtividadeDao
import com.joaovitor.agrotrack.data.dao.TarefaDao
import com.joaovitor.agrotrack.data.entity.RegistroAtividade
import com.joaovitor.agrotrack.data.entity.StatusTarefa
import com.joaovitor.agrotrack.data.entity.Tarefa

class AgroTrackRepository(
    private val tarefaDao: TarefaDao,
    private val registroDao: RegistroAtividadeDao
) {

    fun getTarefasHoje() = tarefaDao.getTarefasHoje()
    fun getAllTarefas() = tarefaDao.getAllTarefas()

    suspend fun insertTarefa(tarefa: Tarefa) = tarefaDao.insertTarefa(tarefa)
    suspend fun updateTarefa(tarefa: Tarefa) = tarefaDao.updateTarefa(tarefa)
    suspend fun deleteTarefa(tarefa: Tarefa) = tarefaDao.deleteTarefa(tarefa)
    suspend fun updateStatusTarefa(tarefaId: Long, status: StatusTarefa) =
        tarefaDao.updateStatus(tarefaId, status)

    fun getAllRegistros() = registroDao.getAllRegistros()
    fun getRegistrosHoje() = registroDao.getRegistrosHoje()

    suspend fun insertRegistro(registro: RegistroAtividade) = registroDao.insertRegistro(registro)
    suspend fun updateRegistro(registro: RegistroAtividade) = registroDao.updateRegistro(registro)
    suspend fun deleteRegistro(registro: RegistroAtividade) = registroDao.deleteRegistro(registro)


}