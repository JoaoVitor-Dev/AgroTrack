package com.joaovitor.agrotrack.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaovitor.agrotrack.data.entity.StatusTarefa
import com.joaovitor.agrotrack.data.entity.Tarefa
import com.joaovitor.agrotrack.data.repository.AgroTrackRepository
import kotlinx.coroutines.launch

class TarefaViewModel(private val repository: AgroTrackRepository) : ViewModel() {

    val tarefasHoje = repository.getTarefasHoje()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun adicionarTarefa(
        nomeAtividade: String,
        talhao: String,
        horaPrevista: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val novaTarefa = Tarefa(
                    nomeAtividade = nomeAtividade,
                    talhao = talhao,
                    horaPrevista = horaPrevista,
                    status = StatusTarefa.PENDENTE
                )
                repository.insertTarefa(novaTarefa)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar tarefa: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun atualizarStatusTarefa(tarefaId: Long, novoStatus: StatusTarefa) {
        viewModelScope.launch {
            try {
                repository.updateStatusTarefa(tarefaId, novoStatus)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao atualizar status: ${e.message}"
            }
        }
    }

    fun excluirTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            try {
                repository.deleteTarefa(tarefa)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao excluir tarefa: ${e.message}"
            }
        }
    }

    fun limparErro() {
        _errorMessage.value = null
    }
}