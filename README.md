# Daily Expense Tracker

A clean architecture-based Android app built with Kotlin, Jetpack Compose, Hilt, and Room.  
The app allows users to sign up and log in, track income and expenses, view reports, and manage their profile with theme switching support.

---

## Project Structure

│
├── core/ # Core utilities, extensions, constants, base classes
│
├── di/ # app modules for dependency injection
│
├── data/ # Data layer
│ ├── local/ # Room database or DataStore implementation
│ ├── model/ # Data Transfer Objects (DTOs), Firebase models
│ └── repository/ # Repository implementations interfacing data sources
│
├── domain/ # Business logic layer
│ ├── model/ # Business entities (clean models)
│ └── usecase/ # Use cases containing business rules
│
├── presentation/ # UI layer using Jetpack Compose
│ ├── navigation/ # Navigation setup for Compose screens
│ ├── ui/ # Screens
│ │ ├── screens 
│ ├── components/ # Reusable Compose components
│ └── viewmodel/ # ViewModels managing UI state
│
├── MainActivity.kt # Root activity hosting Compose UI
└── App.kt # Application class (Hilt setup)



---

## Features

### Authentication
- **Signup:** Users can create an account with a username and a password (minimum length 4).
- **Login:** Users can log in with their credentials.
  
### Home Screen
- Displays **total balance**, **income**, and **expenses**.
- Shows **recent transactions** with options to filter by category and date.
- Contains a **greeting message** personalized with the username.
- **See All Transactions:** Navigate to a detailed list of all transactions.
- **Floating Action Button (FAB):** Two actions for adding income and expenses.
- **Theme Switch:** Toggle between light and dark mode.

### Add Transaction
- Form to add income or expense:
  - Fields: Name, Amount, Date (mandatory), Note (optional), Image (optional).
- Stores transactions locally using **Room database**.
- Navigates back to Home screen with a **Lottie animation** upon submission.

### Reports Screen
- Displays transactions grouped by date and category.
- Shows a chart with the latest 7 transactions.

### Profile Screen
- Displays a user logo with the first letter of the username.
- Contains navigation buttons for Logout, Reports, and All Transactions.

---

# Tech Stack

This project uses the following technologies and libraries organized by category:

---

## UI / Jetpack Compose

- **Jetpack Compose Animation:** `androidx.compose.animation:animation`  
- **Jetpack Compose Foundation:** `androidx.compose.foundation:foundation`  
- **Jetpack Compose Material3:** `androidx.compose.material3:material3`  
- **Jetpack Compose Material Icons Extended:** `androidx.compose.material:material-icons-extended`  
- **Jetpack Compose Navigation:** `androidx.navigation:navigation-compose`  
- **Jetpack Compose UI:** `androidx.compose.ui:ui`  
- **Jetpack Compose UI Graphics:** `androidx.compose.ui:ui-graphics`  
- **Jetpack Compose UI Tooling Preview:** `androidx.compose.ui:ui-tooling-preview`  
- **Jetpack Compose ConstraintLayout:** `androidx.constraintlayout:constraintlayout-compose`  
- **Lottie for Compose animations:** `com.airbnb.android:lottie-compose`  
- **Coil Image Loading (Compose):** `io.coil-kt:coil-compose`  

---

## AndroidX Core & Lifecycle

- **Core KTX Extensions:** `androidx.core:core-ktx`  
- **Lifecycle Runtime KTX:** `androidx.lifecycle:lifecycle-runtime-ktx`  
- **Activity Compose Integration:** `androidx.activity:activity-compose`  
- **DataStore Preferences:** `androidx.datastore:datastore-preferences`  

---

## Dependency Injection

- **Hilt Android:** `com.google.dagger:hilt-android`  
- **Hilt Navigation Compose:** `androidx.hilt:hilt-navigation-compose`  
- **Hilt Compiler (annotation processor):** `com.google.dagger:hilt-compiler`  

---

## Database

- **Room Runtime:** `androidx.room:room-runtime`  
- **Room KTX Extensions:** `androidx.room:room-ktx`  
- **Room Compiler (annotation processor):** `androidx.room:room-compiler`  

---

## Concurrency & Serialization

- **Kotlinx Coroutines (Android):** `org.jetbrains.kotlinx:kotlinx-coroutines-android`  
- **Kotlinx Serialization (JSON):** `org.jetbrains.kotlinx:kotlinx-serialization-json`  

---

## Material Design

- **Google Material Components:** `com.google.android.material:material`  

---

## Build Plugins

- **Android Application Plugin:** `com.android.application`  
- **Kotlin Android Plugin:** `org.jetbrains.kotlin.android`  
- **Kotlin Compose Plugin:** `org.jetbrains.kotlin.plugin.compose`  
- **Hilt Plugin:** `com.google.dagger.hilt.android`  
- **Kotlin KAPT (annotation processor) Plugin:** `org.jetbrains.kotlin.kapt`  
- **Kotlin Serialization Plugin:** `org.jetbrains.kotlin.plugin.serialization`  

---


