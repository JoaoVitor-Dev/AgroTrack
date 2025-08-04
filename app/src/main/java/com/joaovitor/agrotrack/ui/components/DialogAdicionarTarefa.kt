package com.joaovitor.agrotrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAdicionarTarefa(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var nomeAtividade by remember { mutableStateOf("") }
    var talhao by remember { mutableStateOf("") }
    var horaPrevista by remember { mutableStateOf("") }
    var isHoraValida by remember { mutableStateOf(true) }

    val validarHora = { hora: String ->
        if (hora.isEmpty()) {
            isHoraValida = true
        } else {
            val regex = Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
            isHoraValida = regex.matches(hora)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Nova Tarefa")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = nomeAtividade,
                    onValueChange = { nomeAtividade = it },
                    label = { Text("Nome da Atividade") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Create, contentDescription = null)
                    }
                )

                OutlinedTextField(
                    value = talhao,
                    onValueChange = { talhao = it },
                    label = { Text("Talhão") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                    }
                )

                OutlinedTextField(
                    value = horaPrevista,
                    onValueChange = {
                        horaPrevista = it
                        validarHora(it)
                    },
                    label = { Text("Hora Prevista") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("08:00") },
                    leadingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                    },
                    isError = !isHoraValida,
                    supportingText = {
                        if (!isHoraValida) {
                            Text(
                                text = "Formato inválido. Use HH:MM",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nomeAtividade.isNotBlank() && talhao.isNotBlank() &&
                        horaPrevista.isNotBlank() && isHoraValida) {
                        onConfirm(nomeAtividade.trim(), talhao.trim(), horaPrevista.trim())
                    }
                },
                enabled = nomeAtividade.isNotBlank() && talhao.isNotBlank() &&
                        horaPrevista.isNotBlank() && isHoraValida
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}