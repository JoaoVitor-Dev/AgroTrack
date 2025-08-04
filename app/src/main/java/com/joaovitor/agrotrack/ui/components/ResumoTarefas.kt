package com.joaovitor.agrotrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.joaovitor.agrotrack.data.entity.StatusTarefa
import com.joaovitor.agrotrack.data.entity.Tarefa

@Composable
fun ResumoTarefas(tarefas: List<Tarefa>) {
    val pendentes = tarefas.count { it.status == StatusTarefa.PENDENTE }
    val emAndamento = tarefas.count { it.status == StatusTarefa.EM_ANDAMENTO }
    val finalizadas = tarefas.count { it.status == StatusTarefa.FINALIZADA }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Tarefas de Hoje",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusChip(label = "Pendentes", count = pendentes, color = Color(0xFFFF9800))
                StatusChip(label = "Em Andamento", count = emAndamento, color = Color(0xFF2196F3))
                StatusChip(label = "Finalizadas", count = finalizadas, color = Color(0xFF4CAF50))
            }
        }
    }
}