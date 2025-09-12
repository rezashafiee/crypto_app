# Crypto App

Crypto App is an Android application designed to track cryptocurrency information. **It serves as a showcase project demonstrating how to implement an Android application using a domain-centric Clean Architecture and a modular structure.** It allows users to browse a list of cryptocurrencies and view details for specific coins.

## Features

*   **Browse Cryptocurrencies:** Displays a paginated list of available cryptocurrencies.
*   **View Coin Details:** Shows detailed information for a selected cryptocurrency, potentially including price charts.
*   **Offline Caching:** Utilizes a local database to cache cryptocurrency data for offline access and faster loading.
*   **Modern UI:** Built with Jetpack Compose for a modern, declarative UI.

## Architecture

The project follows a **Clean Architecture** approach, separating concerns into distinct layers. The core idea is that the **Domain layer is the center of the architecture**, and both the Presentation and Data layers depend on it, but the Domain layer does not depend on them.
```mermaid
graph LR
    P[Presentation Layer <br/> (UI, ViewModel, 
etc.)] --> D{Domain Layer <br/> (Use Cases, Entities, <br/> Repository Interfaces)}
    DA[Data Layer <br/> (Repository Impl., <br/> API, Database)] --> D

```
Or, as a simpler text diagram:
```

+---------------------+      +---------------------+      +---------------------+
|  Presentation Layer |----->|    Domain Layer     |<-----|     Data Layer      |
| (Compose UI, VMs)   |      | (Use Cases, Models) |      | (Repos, API, DB)    |
+---------------------+      +---------------------+      +---------------------+

```
*   **Data Layer:** Responsible for providing data to the application, whether from a remote API or a local database. It includes implementations of repository interfaces defined in the Domain layer.
    *   Modules: `core/data`, `feature/crypto/data`
*   **Domain Layer:** Contains the business logic of the application, including use cases, domain models, and repository interfaces. It is independent of the Android framework and any other layer.
    *   Modules: `core/domain`, `feature/crypto/domain`
*   **Presentation Layer:** Handles the UI and user interaction, using an **MVVM (Model-View-ViewModel)** pattern. It interacts with the Domain layer through use cases.
    *   Modules: `core/presentation`, `feature/crypto/presentation`

The project is also structured using **multi-module approach**, with a dedicated `app` module and feature-specific modules (e.g., `feature/crypto`) to promote separation of concerns, scalability, and faster build times.

## Tech Stack & Libraries

*   **Kotlin:** Primary programming language.
*   **Jetpack Compose:** For building the user interface.
*   **Coroutines & Flow:** For asynchronous programming.
*   **Ktor Client:** For making network requests to fetch cryptocurrency data.
*   **Room:** For local data persistence (caching).
*   **Paging 3:** For efficiently loading and displaying large lists of data.
*   **Koin:** For dependency injection.
*   **ViewModel:** Part of Android Jetpack, used to store and manage UI-related data in a lifecycle-conscious way.
*   **WorkManager:** For deferrable background tasks (potentially for data synchronization).
*   **Gradle (Kotlin DSL):** For build automation.

## Modules

*   **`app`:** The main application module, responsible for putting together all the features and core components.
*   **`core`:** Contains shared code and utilities used across multiple feature modules.
    *   `core/data`: Core data components, potentially including database setup or shared data sources.
    *   `core/domain`: Core domain logic or models.
    *   `core/presentation`: Core UI components, themes, or presentation utilities.
*   **`feature/crypto`:** A feature module dedicated to cryptocurrency-related functionalities.
    *   `feature/crypto/data`: Data sources and repositories specific to the crypto feature.
    *   `feature/crypto/domain`: Use cases and domain models for the crypto feature.
    *   `feature/crypto/presentation`: ViewModels, Composable screens, and UI models for the crypto feature.
