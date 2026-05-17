# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build and install on connected device/emulator
./gradlew installDebug

# Build release APK
./gradlew assembleRelease

# Clean build outputs
./gradlew clean
```

Requires JDK 17, Android SDK with API 34 (compileSdk) and API 26 (minSdk). There are no automated tests in this project.

## Architecture

**DormirBien** is a single-module Android app (Jetpack Compose + Material 3) that calculates optimal 90-minute sleep cycle wake times and fires alarms that bypass Do Not Disturb mode and appear over the lock screen.

### Layer structure

```
alarm/          BroadcastReceiver → ForegroundService → Activity (lock screen UI)
data/local/     DataStore (AlarmPreferences) + Room (SleepDatabase)
data/repository SleepRepository interface + OfflineFirstSleepRepository
di/             Hilt modules (SingletonComponent)
ui/             Route/ViewModel/Screen per tab + shared components
MainActivity    Permission chain + alarm lifecycle callbacks
AppRoot         NavHost + BottomNavigationBar
```

### Key patterns

**Route-Screen separation**: Each tab has a `*Route` composable (holds the `HiltViewModel`, maps actions to callbacks) and a `*Screen` composable (pure UI, receives state + lambdas). `HomeRoute` delegates alarm scheduling up to `MainActivity` via callback lambdas rather than calling system APIs directly.

**Sealed UiState**: Every ViewModel exposes `StateFlow<*UiState>` with `Loading` and `Success` variants. `combine()` merges multiple flows into a single state object using `stateIn(WhileSubscribed(5_000))`.

**Dual persistence for alarm data**: `AlarmPreferences` writes alarm state to both DataStore (for reactive UI) and `SharedPreferences("db_sync")` (for synchronous access from `AlarmService`/`AlarmReceiver`, which cannot use coroutines). Always update both when writing alarm/sound state.

**Dual alarm delivery paths**: `AlarmService` fires both `setFullScreenIntent` (PATH A — stock Android: OS launches `AlarmActivity` over lock screen) and `startActivity()` (PATH B — Xiaomi/MIUI: requires `SYSTEM_ALERT_WINDOW` + two MIUI-specific permissions). Both paths are attempted simultaneously.

**`AlarmScheduler` is a singleton `object`**, not Hilt-injected. Call it directly from `MainActivity` and `AlarmReceiver`. It also registers alarms with the system Clock app via `AlarmClock.ACTION_SET_ALARM` so they appear in the Reloj app.

### Permission chain

`MainActivity.askPermissions()` runs sequentially on `onCreate` and after every `ActivityResult`. Order matters:
1. `POST_NOTIFICATIONS` (Android 13+)
2. `SCHEDULE_EXACT_ALARM` (Android 12+)
3. `SYSTEM_ALERT_WINDOW` (all versions — required for lock screen overlay)
4. DND bypass (`isNotificationPolicyAccessGranted`)
5. One-time Xiaomi MIUI guide (stored in `SharedPreferences("db_sync", "miui_guide_shown")`)

### ReviewDialog flow

When `AlarmService` starts it sets `pending_review = true` in `SharedPreferences("db_sync")`. `MainActivity.onResume()` checks this flag and triggers the star/feeling rating dialog after a 400 ms delay. The dialog save writes back to the Room `SleepRecord` for that day's `dateKey`.

### Sleep record model

Room stores one `SleepRecordEntity` per day keyed by `"yyyy-MM-dd"`. `hours` is calculated from `cycles × 90 / 60`. Stars and feeling are filled in by the post-wake `ReviewDialog`. `SleepRepository.upsert()` is used for both initial creation (when alarm is set) and review updates — always pass the existing record's stars/hours when you only want to update one field.

### Navigation

Four bottom-nav tabs: `home`, `cycles`, `history`, `tips`. All routes are flat (no nested navigation). Tab transitions use a shared 180 ms `fadeIn`/`fadeOut`. The `NavHost` is in `AppRoot.kt`.

### Notable Android compatibility details

- `AlarmScheduler.exact()` has three code paths by API level: `setAlarmClock` (≥31), `setExactAndAllowWhileIdle` (≥23), `setExact` (legacy). Wrap in try/catch and fall back to `am.set()`.
- `AlarmReceiver` must reschedule alarms after device reboot (reads saved alarm from `SharedPreferences("db_sync")`).
- `SleepDatabase` uses `fallbackToDestructiveMigration()` — schema changes will wipe the database.
