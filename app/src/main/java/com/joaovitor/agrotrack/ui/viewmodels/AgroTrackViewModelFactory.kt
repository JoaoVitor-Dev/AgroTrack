package com.joaovitor.agrotrack.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joaovitor.agrotrack.data.repository.AgroTrackRepository

class AgroTrackViewModelFactory(private val repository: AgroTrackRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TarefaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TarefaViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegistroAtividadeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistroAtividadeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}