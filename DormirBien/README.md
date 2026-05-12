# 🌙 DormirBien

**DormirBien** es una app Android de control de sueño basada en ciclos de 90 minutos. Calcula automáticamente la hora óptima para despertar y programa alarmas reales que suenan aunque el móvil esté en silencio o modo No Molestar.

---

## ✨ Funcionalidades

- **Cálculo de ciclos de sueño** — Calcula las horas de despertar óptimas basándose en ciclos de 90 minutos y tu tiempo habitual de conciliar el sueño
- **Alarma doble** — Alarma principal + recordatorio automático +5 min de respaldo
- **Integración con app Reloj del sistema** — Las alarmas aparecen en la app Reloj de tu móvil
- **Pantalla de bloqueo** — Interfaz completa sobre la pantalla de bloqueo con estrellas animadas, hora grande y deslizamiento hacia arriba para detener
- **Sonido personalizable** — Elige cualquier tono de alarma del sistema
- **Bypass Do Not Disturb** — Suena aunque el móvil esté en silencio o modo No Molestar
- **Historial de sueño** — Calendario con código de colores (rojo/amarillo/verde) y estadísticas reales
- **Valoración diaria** — Popup de calificación al despertar (estrellas + estado de ánimo)
- **Información sobre ciclos** — Explicación de las fases del sueño N1/N2/N3/REM
- **Consejos de higiene del sueño** — Tips basados en evidencia científica

---

## 📱 Requisitos

- Android 8.0 (API 26) o superior
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17

---

## 🛠️ Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/DormirBien.git
cd DormirBien
```

### 2. Abrir en Android Studio

1. Abre Android Studio
2. **File → Open** → selecciona la carpeta `DormirBien`
3. Espera a que Gradle sincronice las dependencias

### 3. Compilar e instalar

```bash
# Debug
./gradlew installDebug

# O desde Android Studio: Run → Run 'app'
```

---

## ⚙️ Configuración de permisos (primera vez)

Al abrir la app por primera vez, se pedirán estos permisos en orden:

| Permiso | Para qué sirve |
|---------|---------------|
| **Notificaciones** | Mostrar la alarma en pantalla de bloqueo |
| **Alarma exacta** | Sonar a la hora exacta incluso en ahorro de batería |
| **Mostrar sobre otras apps** | Mostrar la interfaz sobre la pantalla de bloqueo |
| **No Molestar** | Sonar aunque el móvil esté en silencio |

> ✅ Acepta todos para que la app funcione correctamente.

---

## 📱 Configuración especial para Xiaomi/MIUI

MIUI tiene dos ajustes adicionales que **debes activar manualmente** una sola vez:

### Pasos:

1. Abre **Ajustes** → **Apps** → **Gestionar apps**
2. Busca **DormirBien**
3. Entra en **Otros permisos**
4. Activa ✅ **Mostrar ventana emergente en segundo plano**
5. Activa ✅ **Mostrar en pantalla de bloqueo**

> ⚠️ Sin estos dos ajustes, MIUI bloquea que cualquier app muestre pantallas sobre la pantalla de bloqueo, independientemente del código. Es una restricción del fabricante.

La app incluye un botón de guía ⚙️ en la pantalla principal que te lleva directamente a los ajustes de la app.

---

## 🏗️ Arquitectura

El proyecto sigue la arquitectura recomendada por Google y las guías de **Android Development Best Practices**:

```
app/
├── alarm/                    # Sistema de alarmas
│   ├── AlarmActivity.kt      # UI sobre pantalla de bloqueo (Compose)
│   ├── AlarmReceiver.kt      # BroadcastReceiver para AlarmManager
│   ├── AlarmScheduler.kt     # Lógica de scheduling (objeto singleton)
│   └── AlarmService.kt       # ForegroundService: sonido + vibración
│
├── data/
│   ├── local/
│   │   ├── AlarmPreferences.kt   # DataStore: configuración y estado de alarma
│   │   └── SleepDatabase.kt      # Room: historial de sueño
│   └── repository/
│       └── SleepRepository.kt    # Repositorio offline-first
│
├── di/
│   └── AppModule.kt          # Hilt: inyección de dependencias
│
├── ui/
│   ├── home/                 # Pantalla principal (Route + ViewModel + Screen)
│   ├── history/              # Historial con calendario
│   ├── cycles/               # Info sobre ciclos de sueño
│   ├── tips/                 # Consejos de higiene del sueño
│   └── components/           # Componentes compartidos (ReviewDialog)
│
├── DormirBienApp.kt          # @HiltAndroidApp
└── MainActivity.kt           # Entry point + cadena de permisos
```

### Stack tecnológico

| Capa | Tecnología |
|------|------------|
| UI | Jetpack Compose + Material 3 |
| Navegación | Navigation Compose |
| ViewModel | Hilt ViewModel + StateFlow |
| Base de datos | Room 2.6 |
| Preferencias | DataStore Preferences |
| DI | Hilt 2.50 |
| Build | Gradle KTS + Version Catalog |
| Min SDK | 26 (Android 8.0) |

### Patrones aplicados

- **Unidirectional Data Flow** — eventos bajan, estado sube
- **Offline-first** — Room como source of truth, Flow reactivo
- **Route-Screen pattern** — separación ViewModel ↔ UI pura
- **Sealed UiState** — `Loading` / `Success` en todos los ViewModels

---

## 🔔 Cómo funciona el sistema de alarmas

```
Usuario pulsa "Buenas noches"
        ↓
AlarmScheduler.schedule()
  ├── AlarmManager.setAlarmClock()     ← alarma exacta del sistema
  ├── AlarmClock.ACTION_SET_ALARM      ← aparece en app Reloj
  └── (repite para alarma +5 min)
        ↓
[A la hora programada]
AlarmReceiver.onReceive(ACTION_FIRE)
        ↓
AlarmService.onStartCommand()
  ├── PowerManager.WakeLock            ← despierta pantalla
  ├── startForeground(notif)           ← notificación MAX prioridad
  │     └── setFullScreenIntent()      ← PATH A: Android estándar
  ├── startActivity(AlarmActivity)     ← PATH B: Xiaomi/MIUI
  ├── MediaPlayer (USAGE_ALARM)        ← suena aunque haya silencio
  └── Vibrator
        ↓
AlarmActivity (sobre pantalla de bloqueo)
  ├── Fondo negro + estrellas animadas
  ├── Hora grande
  ├── Botón "Detener" (tarjeta blanca)
  └── Deslizar hacia arriba → detener
        ↓
[Usuario detiene alarma]
  ├── AlarmScheduler.cancelAll()
  ├── SharedPrefs: pending_review = true
  └── MainActivity.onResume() → ReviewDialog
```

---

## 📊 Historial de sueño

Los registros se guardan automáticamente en Room con:

| Campo | Descripción |
|-------|-------------|
| `dateKey` | Fecha en formato `yyyy-MM-dd` |
| `hours` | Horas de sueño planificadas (ciclos × 90 min) |
| `stars` | Valoración 0-5 estrellas |
| `feeling` | Estado al despertar (emoji + etiqueta) |

**Código de colores del calendario:**
- 🟢 Verde: ≥ 7 horas (ideal)
- 🟡 Amarillo: 5-7 horas (correcto)
- 🔴 Rojo: < 5 horas (insuficiente)

---

## 🤝 Contribuir

1. Fork del repositorio
2. Crea una rama: `git checkout -b feature/mi-mejora`
3. Commit: `git commit -m "Añade: descripción"`
4. Push: `git push origin feature/mi-mejora`
5. Abre un Pull Request

---

## 📄 Licencia

MIT License — consulta el archivo [LICENSE](LICENSE) para más detalles.

---

## 👤 Autor

Desarrollado como proyecto de fin de grado (TFG).

---

*Construido con ❤️ y Jetpack Compose*
