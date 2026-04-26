# DiscRoom TV

DiscRoom TV é um aplicativo para **Android TV / Google TV** que oferece experiência de "DVD virtual" para reprodução de imagens ISO de DVD-Video selecionadas pelo usuário via Storage Access Framework.

> **Aviso legal:** o projeto foi estruturado para uso com ISOs pessoais/autoriais e backups legítimos sem proteção anticópia ativa. O app não implementa bypass de CSS/DRM/região de mídias comerciais.

## Stack
- Kotlin + MVVM
- Jetpack Compose (UI para TV com `androidx.tv:tv-material`)
- Storage Access Framework (`content://`)
- DataStore (recentes e configurações)
- libVLC Android (`libvlc-all`) como engine inicial

## Arquitetura
```
UI (Compose TV)
  ├── HomeScreen
  ├── PlayerScreen
  └── SettingsScreen

ViewModel
  ├── HomeViewModel
  └── PlayerViewModel

Domínio/serviços
  ├── File Access (SAF)
  ├── DvdSessionManager
  ├── PlayerEngine (abstração)
  │     └── VlcPlayerEngine (implementação atual)
  └── SettingsRepository / RecentIsoRepository
```

## Funcionalidades implementadas
1. **Tela inicial premium (dark/cinematográfica)**
   - botão "Abrir ISO"
   - lista de ISOs recentes
   - navegação por D-pad
2. **Acesso a arquivo via SAF**
   - abertura com `OpenDocument`
   - suporte `content://`
   - persistência de URI preparada (`IsoPicker.takePersistableReadPermission`)
3. **Reprodução inicial com libVLC**
   - abrir URI da ISO
   - play/pause/stop
   - capítulo seguinte/anterior
   - atalho para menu do DVD (quando disponível na mídia/engine)
4. **Experiência DVD**
   - estado "Lendo disco..."
   - overlay de controle para remoto
   - mapeamento de teclas de mídia e D-pad
5. **Configurações**
   - base de preferências de áudio/legenda
   - modo de proporção (original/fit/fill)
   - retomada automática
   - modo desempenho
6. **Robustez inicial**
   - mensagens amigáveis de erro de reprodução
   - ponto de extensão para tratar remoção de mídia, codec e validação ISO

## Como rodar no Android Studio
1. Abra a pasta do projeto no Android Studio (Koala+ recomendado).
2. Aguarde sincronização Gradle.
3. Use um dispositivo Android TV/Google TV (físico ou emulador TV).
4. Build em `debug` e execute.

## Limitações conhecidas (MVP atual)
- Reprodução de ISO depende da capacidade da combinação **dispositivo + libVLC + URI provider**.
- Menus completos de DVD-Video (navegação `dvdnav`) podem variar por build/codec.
- Seleção explícita de trilhas de áudio/legenda ainda está no engine, sem UI dedicada.
- Ainda não há monitoramento ativo de desconexão de USB durante playback.
- Não implementa nem pretende implementar circumvenção de DRM/CSS/região.

## Próximos passos recomendados
1. Adicionar scanner de metadados de ISO para validar estrutura DVD (`VIDEO_TS`).
2. Implementar controle completo de menus com estados e highlights.
3. Adicionar UI de trilhas de áudio/legenda e persistência por título.
4. Implementar serviço de sessão com retomada por timestamp/capítulo.
5. Adicionar telemetria local de falhas (sem dados sensíveis) para diagnóstico.
6. Preparar segunda implementação de `PlayerEngine` (FFmpeg/libdvdnav via NDK) para comparação.
