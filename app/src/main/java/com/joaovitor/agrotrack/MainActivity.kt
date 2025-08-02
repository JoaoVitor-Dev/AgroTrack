package com.joaovitor.agrotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.joaovitor.agrotrack.data.database.AgroTrackDatabase
import com.joaovitor.agrotrack.data.repository.AgroTrackRepository
import com.joaovitor.agrotrack.ui.screens.TelaInicialTarefas
import com.joaovitor.agrotrack.ui.screens.TelaRegistroAtividade
import com.joaovitor.agrotrack.ui.theme.AgroTrackTheme
import com.joaovitor.agrotrack.ui.viewmodels.AgroTrackViewModelFactory
import com.joaovitor.agrotrack.ui.viewmodels.RegistroAtividadeViewModel
import com.joaovitor.agrotrack.ui.viewmodels.TarefaViewModel

class MainActivity : ComponentActivity() {

    private lateinit var database: AgroTrackDatabase
    private lateinit var repository: AgroTrackRepository
    private lateinit var viewModelFactory: AgroTrackViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        database = AgroTrackDatabase.getDatabase(this)
        repository = AgroTrackRepository(
            tarefaDao = database.tarefaDao(),
            registroDao = database.registroAtividadeDao()
        )
        viewModelFactory = AgroTrackViewModelFactory(repository)

        setContent {
            AgroTrackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AgroTrackApp(viewModelFactory = viewModelFactory)
                }
            }
        }
    }
}

@Composable
fun App(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

enum class Screen {
    TAREFAS,
    REGISTROS
}
@Composable
fun AgroTrackApp(viewModelFactory: AgroTrackViewModelFactory) {
    var currentScreen by remember { mutableStateOf(Screen.TAREFAS) }

    val tarefaViewModel = remember {
        ViewModelProvider(
            store = ViewModelStore(),
            factory = viewModelFactory
        )[TarefaViewModel::class.java]
    }

    val registroViewModel = remember {
        ViewModelProvider(
            store = androidx.lifecycle.ViewModelStore(),
            factory = viewModelFactory
        )[RegistroAtividadeViewModel::class.java]
    }

    when (currentScreen) {
        Screen.TAREFAS -> {
            TelaInicialTarefas(
                viewModel = tarefaViewModel,
                onNavigateToRegistros = {
                    currentScreen = Screen.REGISTROS
                }
            )
        }
        Screen.REGISTROS -> {
            TelaRegistroAtividade(
                viewModel = registroViewModel,
                onNavigateBack = {
                    currentScreen = Screen.TAREFAS
                }
            )
        }
    }
}

class AgroTrackApplication : android.app.Application() {
    val database by lazy { AgroTrackDatabase.getDatabase(this) }
    val repository by lazy {
        AgroTrackRepository(
            tarefaDao = database.tarefaDao(),
            registroDao = database.registroAtividadeDao()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AgroTrackTheme {
        App("Android")
    }
}