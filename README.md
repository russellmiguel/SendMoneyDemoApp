# Send Money Demo App

A secure and intuitive Android application for managing a digital wallet and sending money. This project demonstrates modern Android development practices, including Clean Architecture, Jetpack Compose, and robust security measures.

## Features
- **Secure Authentication**: User sign-up and login with BCrypt password hashing.
- **Wallet Management**: View balance, toggle balance visibility, and add funds securely.
- **Money Transfer**: Send money to recipients.
- **Transaction History**: View a combined list of local and remote transactions, sorted by date.
- **Security**: SSL Pinning for network requests and sensitive data masking.

## Tech Stack
- **UI**: Jetpack Compose
- **Architecture**: Clean Architecture with MVVM
- **Dependency Injection**: Hilt
- **Local Database**: Room DB
- **Networking**: Retrofit & OkHttp
- **Security**: 
    - **BCrypt**: Secure password hashing.
    - **SSL Pinning**: Certificate pinning for secure API communication.
- **Concurrency**: Kotlin Coroutines & Flow
- **Testing**: JUnit 4, MockK, Robolectric, Coroutines Test

## Architecture
This project follows **Clean Architecture** principles to ensure separation of concerns, testability, and maintainability.

- **Presentation Layer**: Jetpack Compose and ViewModels.
- **Domain Layer**: Business logic via Use Cases and Repository interfaces.
- **Data Layer**: Room database for local persistence and Retrofit for remote API communication.

For detailed architectural diagrams, see [ARCHITECTURE.md](./ARCHITECTURE.md).

*Note: To view the architecture and send_money_sequence diagrams, Android Studio might need to install PlantUML and OmniViewer plugins.*

## Setup Instructions

### 1. SSL Pinning Configuration
To ensure secure communication, the app requires SSL pins to be defined in your `local.properties` file. 

Create or edit `local.properties` in the root directory and add:
```properties
SSL_PIN=your_primary_ssl_pin_here
SSL_PIN_BACKUP=your_backup_ssl_pin_here
```
*Note: These values are injected into the `BuildConfig` during compilation. SSL pins are extracted from SSL Labs, will also provide the once I used in testing via email.*

### 2. Running the App
1. Open the project in **Android Studio Ladybug** or newer.
2. Sync the project with Gradle files.
3. Select an emulator or physical device.
4. Click **Run 'app'**.

## Testing & Coverage
This project maintains high code quality with a comprehensive unit test suite covering ViewModels, Use Cases, Repositories, and Utilities.

- **Total Unit Tests**: 46
- **Business Logic Coverage**: ~100%
- **Total Coverage**: >80%

For detailed information on the testing strategy, see [TESTING.md](./TESTING.md).

### Running Tests
Run all unit tests via terminal:
```bash
./gradlew test
```

## Project Structure
- `data/`: Local (Room) and Remote (Retrofit) data sources and repository implementations.
- `domain/`: Business logic, Use Cases, and Repository interfaces.
- `presentation/`: UI components (Compose) and ViewModels.
- `di/`: Hilt modules for dependency injection.
- `util/`: Helper functions and common utilities.
