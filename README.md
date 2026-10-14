# WeekMark

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0%2B-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)
![Material 3](https://img.shields.io/badge/Design-Material%203-757575?style=flat-square&logo=materialdesign&logoColor=white)

**WeekMark** is a modern, feature-rich Android application built with **Jetpack Compose** and **Material 3** that helps developers, project managers, logistics planners, and professionals calculate and track ISO week numbers, week dates, day positioning, and calendar boundaries effortlessly.

---

## 🌟 Key Features

- **ISO Week & Year Calculation**: Accurate computation of ISO week numbers (`1–53`), week-based years, and ISO year length calculations using Java `java.time` (`WeekFields.ISO`).
- **Detailed Day & Week Insights**:
  - View exact start and end boundaries of any selected week (Monday to Sunday).
  - Track day positioning within the week (e.g., Day 3 of 7).
  - Calculate elapsed and remaining days until the weekend.
  - Detect calendar year vs. week-based year discrepancies (e.g., late December or early January edge cases).
- **Interactive Navigation**: Seamlessly navigate by day, week, month, or year using intuitive controls and date pickers.
- **Calculation History**: Keep track of recent calculations and view past entries.
- **Customizable Settings**: Tailor preferences, themes, and date formatting options.
- **Quick Copy & Share**: Instantly copy calculation summaries or share week details with colleagues and apps.
- **Robust Testing**: Comprehensive unit tests covering domain calculators, formatters, repositories, and ViewModels.

---

## 📱 Screenshots & UI

Built following **Material 3** guidelines, WeekMark offers a clean, adaptive, and modern UI with dynamic theming and smooth navigation.

---

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io/)
- **Architecture**: MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF) using `StateFlow`
- **Date & Time API**: `java.time` (`LocalDate`, `WeekFields`, `TemporalAdjusters`)
- **Navigation**: Jetpack Navigation Compose
- **Testing**: JUnit, Turbine, Coroutines Test, Compose UI Testing (`createComposeRule`)

---

## 📂 Project Structure

```text
com.example.weekmark
│
├── data/
│   ├── model/         # Data classes (CalculationState, UserSettings, WeekMarkResult, etc.)
│   └── repository/    # Repositories for History and Settings management
│
├── domain/            # Business logic (WeekMarkCalculator, WeekMarkFormatter)
│
├── ui/
│   ├── about/         # About screen
│   ├── history/       # Calculation history screen
│   ├── home/          # Main dashboard & UI components (Cards, Chips, Nav controls)
│   ├── navigation/    # Navigation graph & screens definition
│   ├── settings/      # Settings screen
│   ├── splash/        # Splash screen
│   ├── theme/         # Material 3 colors, typography, and theme
│   └── viewmodel/     # WeekMarkViewModel
│
└── MainActivity.kt    # Entry point hosting the navigation graph
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Hedgehog / Iguana / Jellyfish or newer recommended)
- **JDK 11** or higher
- **Android SDK** (Compile SDK 37, Min SDK 26)

### Installation & Running
1. Clone the repository:
   ```bash
   git clone https://github.com/abdullah-ateeq/WeekMark.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle and run the app on an Android emulator or physical device.

### Running Tests
To execute unit tests:
```bash
./gradlew test
```

To execute instrumented UI tests:
```bash
./gradlew connectedAndroidTest
```

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check issues page or submit a pull request.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
