# AgroTrack

Aplicativo Android nativo desenvolvido em Kotlin utilizando Jetpack Compose e Room Database.

## Funcionalidades

- Cadastro e listagem de tarefas agrícolas
- Marcação de tarefas como Pendentes, Em andamento e Finalizadas
- Armazenamento local de dados com Room
- Arquitetura MVVM com separação de responsabilidades

<img src="https://github.com/JoaoVitor-Dev/AgroTrack/blob/master/screenshots/Home.png" width=260/><img src="https://github.com/JoaoVitor-Dev/AgroTrack/blob/master/screenshots/Home-menu.png" width=260/><img src="https://github.com/JoaoVitor-Dev/AgroTrack/blob/master/screenshots/New-task.png" width=260/><img src="https://github.com/JoaoVitor-Dev/AgroTrack/blob/master/screenshots/New-register.png" width=260/><img src="https://github.com/JoaoVitor-Dev/AgroTrack/blob/master/screenshots/Historic.png" width=260/>

## Requisitos para rodar o projeto

Antes de rodar o projeto, certifique-se de que seu ambiente está corretamente configurado:

### Ambiente de Desenvolvimento

- Android Studio Giraffe ou mais recente
- JDK 17

### Dependências e SDK

- SDK mínimo: 24 
- SDK alvo: 34 (Android 14)
- Kotlin: 1.9+
- Jetpack Compose: Compatível com versão do Kotlin acima
- Room para persistência local
- Retrofit + Gson para chamadas à API de clima

### Permissões

- Acesso à internet (para API de clima)

## Como rodar o projeto

1. Clone o repositório: https://github.com/JoaoVitor-Dev/AgroTrack.git

2. Abra o projeto no Android Studio.

3. Aguarde o Android Studio sincronizar as dependências.

4. Rode o app em um emulador ou dispositivo físico com Android 7.0 ou superior.

## Estrutura do projeto

**data/local** – Camada de persistência com Room

**data/remote** - Camada de persistência com API **(não finalizado)**

/**ui/components** – Componentes da interface utilizando Jetpack Compose

/**ui/screens** - Telas de interface visual utilizando Jetpack Compose

/**ui/theme** - Arquivos de configuração do tema

/**ui/viewmodels** - Lógica de apresentação

