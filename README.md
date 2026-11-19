# StudyMate AI

StudyMate AI is an Android application built with Kotlin and Jetpack Compose. The Android module uses Room for local persistence and integrates libraries for Firebase services, Gemini AI, networking, and document/image presentation.

## Requirements

- Android Studio with the Android SDK required by the Gradle configuration (compile SDK 36.1).
- JDK 21.
- Git and an internet connection for the initial Gradle/dependency download.

The app's minimum Android SDK is 24.

## Build locally

Clone the repository and open it in Android Studio, or use the Gradle wrapper from the repository root.

```bash
git clone https://github.com/kabeesh395-oss/STUDY-MATE.git
cd STUDY-MATE
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

On Windows PowerShell, use `gradlew.bat` instead of `./gradlew`.

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Local configuration

1. Copy `.env.example` to `.env`.
2. Replace the placeholder `GEMINI_API_KEY` with a key for your own development environment when using Gemini-backed functionality.
3. Keep `.env` and all real credentials out of Git.

The Android Gradle configuration allows builds to proceed when Firebase's Google Services configuration is absent. Firebase-backed runtime features may still require a matching `app/google-services.json` from your own Firebase project. Do not commit private credentials or service-account keys.

## Project layout

- `app/` — Android application source, resources, and tests.
- `gradle/` — Gradle wrapper and version catalog.
- `assets/` — project assets.
- `.github/workflows/android.yml` — Android unit-test and debug-APK CI workflow.
- `codemagic.yaml` — Codemagic build configuration.

## Continuous integration

The Android CI workflow runs:

- `./gradlew testDebugUnitTest --no-daemon --stacktrace`
- `./gradlew assembleDebug --stacktrace`

See the [Android CI workflow](.github/workflows/android.yml) for the authoritative build steps. CI results do not replace testing the installed APK on a physical device.
