# 🎮 Gamopedia - Compose Multiplatform

**Gamopedia** is a modern, offline-first mobile application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. It runs natively on both **Android** and **iOS** while sharing over 99% of the codebase, including UI, business logic, and data handling.

The app allows users to discover video games, search the RAWG database, view detailed information, and manage a local wishlist—all with a robust caching strategy that works seamlessly offline.

## 🎥 Screen recordings
| Android                                                                                                  | iOS                                                                                                      |
|----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| <video src="https://github.com/user-attachments/assets/3d42b703-f42e-4fbc-98af-9336ed21b3c9" width=350/> | <video src="https://github.com/user-attachments/assets/0047c1ec-6b42-4a5c-8205-cef26e3540a6" width=350/> |

## ✨ Features

- ** Cross-Platform:** Single codebase running on Android & iOS.
- ** Offline-First Architecture:** Browsing history, game details, and search results are cached locally. The app remains fully functional without an internet connection.
- ** Smart Search:** Search for games with debounce handling and local caching.
- ** Local Wishlist:** Save your favorite games to a persistent local database (separated from cache to prevent data loss).
- ** Rich Media:** High-quality image loading with memory caching.
- ** Reactive UI:** Built entirely with Declarative UI (Compose) utilizing Unidirectional Data Flow (UDF).

## 🛠️ Tech Stack & Libraries

This project uses the latest libraries in the Kotlin Multiplatform ecosystem:

| Category | Library | Description |
| :--- | :--- | :--- |
| **Language** | [Kotlin](https://kotlinlang.org/) | 100% Kotlin codebase. |
| **UI Framework** | [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) | Declarative UI shared across Android & iOS. |
| **Architecture** | **MVVM + Repository** | Clean architecture with separation of concerns. |
| **Networking** | [Ktor](https://ktor.io/) | Asynchronous HTTP client for REST APIs. |
| **DI** | [Koin](https://insert-koin.io/) | Pragmatic lightweight dependency injection. |
| **Database** | [Room](https://developer.android.com/kotlin/multiplatform/room) | Persistence library over SQLite. |
| **Async** | [Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html) | Managing background threads and reactive streams. |
| **Navigation** | [Voyager](https://voyager.adriel.cafe/) | Multiplatform navigation library. |
| **Image Loading** | [Coil 3](https://github.com/coil-kt/coil) | Image loading for Compose Multiplatform. |
| **Logging** | [Kermit](https://github.com/touchlab/Kermit) | Centralized logging utility. |
| **Config** | [BuildKonfig](https://github.com/yshrsmz/BuildKonfig) | Type-safe access to build configurations (API Keys). |

## 🏗️ Architecture

The app follows the **Model-View-ViewModel (MVVM)** pattern combined with the **Repository Pattern** to ensure a clean separation of concerns and a robust offline-first experience.

### High-Level Overview

* **UI Layer (Compose):** Reactive UI that observes state from the ViewModel. It follows **Unidirectional Data Flow (UDF)**.
* **ViewModel:** Holds the UI state and exposes it via `StateFlow`. It acts as a bridge between the UI and the Data Layer.
* **Repository:** The Mediator. It abstracts the data sources (Local vs Remote) from the rest of the app.
* **Data Source:**
    * **Local (Room):** The **Single Source of Truth (SSOT)**. The UI always displays data from here.
    * **Remote (Ktor):** Used only to fetch fresh data and update the Local Database.

### 🔄 Data Flow (Offline-First Strategy)

This project implements a **"Stale-While-Revalidate"** strategy using Reactive Streams (`Flow`).

1.  The UI subscribes to the **Database** immediately (showing cached data instantly).
2.  A background network call fetches fresh data from the API.
3.  The new data is saved to the **Database**.
4.  The Database automatically emits the new update to the UI.

## 📂 Project Structure
commonMain/
├── di/              # Koin Dependency Injection modules
├── domain/          # Models and Entities
├── data/            # Repository implementation, API, and DB logic
├── database/        # Room Database setup and DAOs
├── network/         # Ktor HTTP client setup
├── viewmodel/       # Shared ViewModels (State Management)
└── ui/              # Shared Compose Multiplatform Screens
