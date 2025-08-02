package com.joaovitor.agrotrack.ui.screens

import com.joaovitor.agrotrack.data.entity.StatusTarefa
import com.joaovitor.agrotrack.data.entity.Tarefa
import com.joaovitor.agrotrack.ui.viewmodels.TarefaViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joaovitor.agrotrack.data.database.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaInicialTarefas(
    viewModel: TarefaViewModel,
    onNavigateToRegistros: () -> Unit
) {
    val tarefas by viewModel.tarefasHoje.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("AgroTrack", fontWeight = FontWeight.Bold)
                        Text(
                            text = getCurrentDate(),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToRegistros) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Registros")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Tarefa")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            ResumoTarefas(tarefas = tarefas)

            Spacer(modifier = Modifier.height(16.dp))

            // listagem das tarefas
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (tarefas.isEmpty()) {
                TelaTarefasVazia()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tarefas) { tarefa ->
                        CardTarefa(
                            tarefa = tarefa,
                            onStatusChange = { novoStatus ->
                                viewModel.atualizarStatusTarefa(tarefa.id, novoStatus)
                            },
                            onDelete = { viewModel.excluirTarefa(tarefa) }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            DialogAdicionarTarefa(
                onDismiss = { showAddDialog = false },
                onConfirm = { nome, talhao, hora ->
                    viewModel.adicionarTarefa(nome, talhao, hora)
                    showAddDialog = false
                }
            )
        }

        errorMessage?.let { message ->
            LaunchedEffect(message) {
                viewModel.limparErro()
            }
        }
    }
}

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

@Composable
fun StatusChip(label: String, count: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun CardTarefa(
    tarefa: Tarefa,
    onStatusChange: (StatusTarefa) -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = getStatusColor(tarefa.status),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = getStatusColor(tarefa.status).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tarefa.nomeAtividade,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tarefa.talhao,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tarefa.horaPrevista,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Mais opções")
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Pendente") },
                                onClick = {
                                    onStatusChange(StatusTarefa.PENDENTE)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Em Andamento") },
                                onClick = {
                                    onStatusChange(StatusTarefa.EM_ANDAMENTO)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Finalizada") },
                                onClick = {
                                    onStatusChange(StatusTarefa.FINALIZADA)
                                    showMenu = false
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Excluir", color = Color.Red) },
                                onClick = {
                                    onDelete()
                                    showMenu = false
                                }
                            )
                        }
                    }

                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = getStatusText(tarefa.status),
                                fontSize = 12.sp
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = getStatusColor(tarefa.status),
                            labelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun TelaTarefasVazia() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhuma tarefa para hoje",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Toque no botão + para adicionar uma nova tarefa",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

private fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
    return dateFormat.format(Date())
}

private fun getStatusColor(status: StatusTarefa): Color {
    return when (status) {
        StatusTarefa.PENDENTE -> Color(0xFFFF9800)
        StatusTarefa.EM_ANDAMENTO -> Color(0xFF2196F3)
        StatusTarefa.FINALIZADA -> Color(0xFF4CAF50)
    }
}

private fun getStatusText(status: StatusTarefa): String {
    return when (status) {
        StatusTarefa.PENDENTE -> "Pendente"
        StatusTarefa.EM_ANDAMENTO -> "Em Andamento"
        StatusTarefa.FINALIZADA -> "Finalizada"
    }
}

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
                        Icon(Icons.Default.Person, contentDescription = null)
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

private fun getCurrentDateFormatted(): String {
    val calendar = Calendar.getInstance()
    val today = calendar.time

    val dayOfWeek = SimpleDateFormat("EEEE", Locale("pt", "BR")).format(today)
    val dayOfMonth = SimpleDateFormat("dd", Locale("pt", "BR")).format(today)
    val month = SimpleDateFormat("MMMM", Locale("pt", "BR")).format(today)
    val year = SimpleDateFormat("yyyy", Locale("pt", "BR")).format(today)

    return "${dayOfWeek.replaceFirstChar { it.uppercase() }}, $dayOfMonth de $month de $year"
}

@Composable
fun ProgressoTarefas(tarefas: List<Tarefa>) {
    val total = tarefas.size
    val finalizadas = tarefas.count { it.status == StatusTarefa.FINALIZADA }
    val progresso = if (total > 0) finalizadas.toFloat() / total else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progresso do Dia",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$finalizadas de $total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progresso },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${(progresso * 100).toInt()}% concluído",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

//@Composable
//fun ErrorSnackbar(
//    errorMessage: String?,
//    onDismiss: () -> Unit
//) {
//    errorMessage?.let { message ->
//        LaunchedEffect(message) {
//            onDismiss()
//        }
//    }
//}