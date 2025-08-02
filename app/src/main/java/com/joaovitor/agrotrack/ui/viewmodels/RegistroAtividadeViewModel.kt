package com.joaovitor.agrotrack.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaovitor.agrotrack.data.entity.RegistroAtividade
import com.joaovitor.agrotrack.data.repository.AgroTrackRepository
import kotlinx.coroutines.launch

class RegistroAtividadeViewModel(private val repository: AgroTrackRepository) : ViewModel() {

    val todosRegistros = repository.getAllRegistros()
    val registrosHoje = repository.getRegistrosHoje()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _sucessoMessage = MutableLiveData<String?>()
    val sucessoMessage: LiveData<String?> = _sucessoMessage

    fun adicionarRegistro(
        tipoAtividade: String,
        talhao: String,
        horaInicio: String,
        horaFim: String,
        observacoes: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val novoRegistro = RegistroAtividade(
                    tipoAtividade = tipoAtividade,
                    talhao = talhao,
                    horaInicio = horaInicio,
                    horaFim = horaFim,
                    observacoes = observacoes
                )
                repository.insertRegistro(novoRegistro)
                _sucessoMessage.value = "Registro salvo com sucesso!"
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao salvar registro: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun excluirRegistro(registro: RegistroAtividade) {
        viewModelScope.launch {
            try {
                repository.deleteRegistro(registro)
                _sucessoMessage.value = "Registro excluído com sucesso!"
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao excluir registro: ${e.message}"
            }
        }
    }

    fun limparMensagens() {
        _errorMessage.value = null
        _sucessoMessage.value = null
    }
}