package com.joaovitor.agrotrack.ui.screens

import com.joaovitor.agrotrack.ui.viewmodels.TarefaViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joaovitor.agrotrack.ui.components.CardTarefa
import com.joaovitor.agrotrack.ui.components.DialogAdicionarTarefa
import com.joaovitor.agrotrack.ui.components.ResumoTarefas
import com.joaovitor.agrotrack.ui.components.TelaTarefasVazia
import com.joaovitor.agrotrack.util.getCurrentDate

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




