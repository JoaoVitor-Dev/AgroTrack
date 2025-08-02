package com.joaovitor.agrotrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.joaovitor.agrotrack.data.entity.RegistroAtividade

import com.joaovitor.agrotrack.ui.viewmodels.RegistroAtividadeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaRegistroAtividade(
    viewModel: RegistroAtividadeViewModel,
    onNavigateBack: () -> Unit
) {
    var currentTab by remember { mutableStateOf(0) }
    val tabs = listOf("Novo Registro", "Histórico")

    val registros by viewModel.todosRegistros.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val sucessoMessage by viewModel.sucessoMessage.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Atividades") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = currentTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = currentTab == index,
                        onClick = { currentTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (currentTab) {
                0 -> FormularioNovoRegistro(
                    viewModel = viewModel,
                    isLoading = isLoading
                )
                1 -> HistoricoRegistros(
                    registros = registros,
                    onDeleteRegistro = { registro ->
                        viewModel.excluirRegistro(registro)
                    }
                )
            }
        }

        LaunchedEffect(sucessoMessage) {
            sucessoMessage?.let {
                viewModel.limparMensagens()
            }
        }

        LaunchedEffect(errorMessage) {
            errorMessage?.let {
                viewModel.limparMensagens()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioNovoRegistro(
    viewModel: RegistroAtividadeViewModel,
    isLoading: Boolean
) {
    var tipoAtividade by remember { mutableStateOf("") }
    var talhao by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFim by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    val tiposAtividade = listOf(
        "Plantio", "Irrigação", "Pulverização", "Colheita",
        "Preparo do Solo", "Adubação", "Capina", "Outro"
    )

    var expandedDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registrar Nova Atividade",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        ExposedDropdownMenuBox(
            expanded = expandedDropdown,
            onExpandedChange = { expandedDropdown = !expandedDropdown }
        ) {
            OutlinedTextField(
                value = tipoAtividade,
                onValueChange = { },
                readOnly = true,
                label = { Text("Tipo de Atividade") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expandedDropdown,
                onDismissRequest = { expandedDropdown = false }
            ) {
                tiposAtividade.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo) },
                        onClick = {
                            tipoAtividade = tipo
                            expandedDropdown = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = talhao,
            onValueChange = { talhao = it },
            label = { Text("Talhão/Área") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.LocationOn, contentDescription = null)
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = horaInicio,
                onValueChange = { horaInicio = it },
                label = { Text("Hora Início") },
                placeholder = { Text("08:00") },
                modifier = Modifier.weight(1f),
                leadingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            )

            OutlinedTextField(
                value = horaFim,
                onValueChange = { horaFim = it },
                label = { Text("Hora Fim") },
                placeholder = { Text("12:00") },
                modifier = Modifier.weight(1f),
                leadingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            )
        }

        OutlinedTextField(
            value = observacoes,
            onValueChange = { observacoes = it },
            label = { Text("Observações") },
            placeholder = { Text("Detalhes sobre a atividade...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5,
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = null)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (tipoAtividade.isNotBlank() && talhao.isNotBlank() &&
                    horaInicio.isNotBlank() && horaFim.isNotBlank()) {
                    viewModel.adicionarRegistro(
                        tipoAtividade = tipoAtividade,
                        talhao = talhao,
                        horaInicio = horaInicio,
                        horaFim = horaFim,
                        observacoes = observacoes
                    )

                    tipoAtividade = ""
                    talhao = ""
                    horaInicio = ""
                    horaFim = ""
                    observacoes = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && tipoAtividade.isNotBlank() && talhao.isNotBlank() &&
                    horaInicio.isNotBlank() && horaFim.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Salvar")
        }
    }
}

@Composable
fun HistoricoRegistros(
    registros: List<RegistroAtividade>,
    onDeleteRegistro: (RegistroAtividade) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Histórico de Registros",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (registros.isEmpty()) {
            TelaRegistrosVazia()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(registros) { registro ->
                    CardRegistro(
                        registro = registro,
                        onDelete = { onDeleteRegistro(registro) }
                    )
                }
            }
        }
    }
}

@Composable
fun CardRegistro(
    registro: RegistroAtividade,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

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
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = registro.tipoAtividade,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                            text = registro.talhao,
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
                            text = "${registro.horaInicio} - ${registro.horaFim}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    if (registro.observacoes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = registro.observacoes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
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
                                text = { Text("Excluir", color = Color.Red) },
                                onClick = {
                                    onDelete()
                                    showMenu = false
                                }
                            )
                        }
                    }

                    Text(
                        text = formatDate(registro.dataRegistro),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun TelaRegistrosVazia() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Create,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhum registro encontrado",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Use a aba 'Novo Registro' para adicionar atividades",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}