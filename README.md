# 💊 Farmacia AppTech

![Java](https://img.shields.io/badge/code-Java-blue.svg)
![Firebase](https://img.shields.io/badge/backend-Firebase-orange)
![SQLite](https://img.shields.io/badge/database-SQLite-lightblue)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![License](https://img.shields.io/badge/license-CC%20BY--NC--ND%204.0-blue)

**Aplicativo educacional desenvolvido para auxiliar estudantes de Farmácia. O app oferece ferramentas variadas, como simulados, comparador de medicamentos, fórum com moderação, conteúdos por módulos, modo offline, integração com vídeos e muito mais.**

---

## ✅ Funcionalidades Principais

- 📚 Módulo de estudo com conteúdos organizados
- ❓ Simulados e quizzes interativos
- 💬 Fórum com comentários e moderação de denúncias
- 🔔 Notificações Push via Firebase Cloud Messaging
- 🌙 Modo noturno (Dark Mode)
- 📡 Modo Offline com banco SQLite
- 🧪 Comparador de medicamentos
- 🎥 Integração com vídeos e aulas online
- 🤖 IA para sugestão de medicamentos via chat
- 📱 Interface gráfica leve e intuitiva
- 🧠 Chatbot com IA e voz para sugestões de medicamentos e atendimento simulado
- 🏥 Simulação de atendimento ao paciente para prática clínica
- 📝 Histórico de progresso nos estudos com sistema de acompanhamento
- 🧾 Biblioteca de artigos científicos para aprofundamento teórico
- 💬 Chat em tempo real entre usuários com canal direto para o desenvolvedor
- 🔐 Login por senha simples, sem necessidade de e-mail
- 📦 Armazenamento online com Firebase Realtime Database
- 📊 Análise de interações medicamentosas com alertas
- 🌐 Sistema de FAQ inteligente com busca automática
- 🎯 Modo de simulação de exame com feedback instantâneo


---

## 🛠️ Tecnologias Utilizadas

- **Java** (Android – IntelliJ IDEA)
- **XML** para layouts visuais
- **Firebase** (Auth, Realtime Database, FCM)
- **SQLite** para modo offline
- **Gradle** para gerenciamento de builds
- **Keystore**: `mykeystore.jks` com alias `mykey`
- **Licença**: Creative Commons BY-NC-ND 4.0 (uso educacional)

---

## 📁 Estrutura do Projeto

farmacia_app/
├── app/
│ ├── src/java/com.example.farmacia_app
│ │ ├── activities/
│ │ ├── database/
│ │ │  └──bromatologia.db
│ │ │  └──farmacia_app.db
│ │ │  └──Medicamento.db
│ │ │
│ │ ├──desativados/
│ │ │  └──CameraActivity
│ │ │  └──VideoPagerAdapter
│ │ │
│ │ ├──jogos/
│ │ └──Formula
│ │ └──GameManager
│ │ └──Ingrediente
│ │ └──Main
│ │ └──Misturador
│ │ └──MisturadorActivity
│ │
│ ├──res/                 # Recursos visuais e de UI do Android
│   ├── layout/          # XMLs de layouts (ex: farmaciadesign.xml)
│   ├── drawable/        # Imagens, ícones e gráficos
│   ├── values/          # Strings, cores, estilos, dimensões etc.
│   ├── mipmap/          # Arquivos de menu
│   ├── values/          # Strings, cores, estilos, dimensões etc.
│   ├── menu/            # Arquivos de menu
│   ├── font/            # Fontes personalizadas
│   ├── raw/             # Arquivos de mídia (áudio, vídeo etc.)
│   │        
│   ├── values-night/    # Estilos para o modo noturno
│   ├── xml/             # Configurações adicionais (ex: preferências)
│   ├── values-sw600dp/  # Layouts adaptados para telas grandes
│   │
│   │
│   ├── assets/
│   └── medicamento.json # Arquivos JSON e outros recursos externos
├── gradle/
├── tess-two/ (OCR - opcional)
├── build.gradle
├── gradle.properties
├── local.properties
├── mykeystore.jks
└── README.md
└── AndroidManifest.xml  # Manifesto do Android

---

## 📁 Estrutura das Activities Principais

### Educação e Estudo
- `EstudoActivity`
- `BromatologiaActivity`
- `BromatoQuizActivity`
- `QuizActivity`
- `VideoEstudoActivity`
- `ProgressoEstudo`
- `AtualidadesActivity`
- `CalculadoraSoro`
- `CaloriasActivity`

### Fórum e Chat
- `ForumActivity`
- `ChatbotActivity`
- `ChatbotFarmacia`
- `ChatbotVoz`
- `VoiceBroadcasterActivity`
- `WhatsappNotificationListener`

### Perfil e Usuário
- `LoginActivity`
- `PerfilActivity`
- `EditarPerfilActivity`
- `SettingsActivity`

### Farmácia e Medicamentos
- `ListaMedicamentoAdapter`
- `MedicamentosDB`
- `MedicamentosManager`
- `MedicamentoDatabaseHelper`

### Sistema e Recursos
- `MainActivity`
- `AppStarter`
- `MyFirebaseMessagingService`
- `AuthActivity`
- `BaseActivity`
- `MyApp` / `MyApplication`
- `CryptoManager`
- `DatabaseHelper`

### Extras e Utilidades
- `Atualizador`
- `CalendarActivity`
- `DownloadPdfActivity`
- `FeedbackActivity`
- `FAQMaster`
- `ContatoDesenvolvedorActivity`
- `MenuActivity`
- `AppMonitorService`

---