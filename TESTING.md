# Test Documentation - Send Money Demo App

## Overview
This document outlines the unit testing strategy and test suites implemented for the Send Money Demo App. The goal is to ensure high code quality, reliability of business logic, and at least 80% code coverage for testable components.

## Testing Stack
- **JUnit 4**: Core testing framework.
- **MockK**: Mocking library for Kotlin, used to isolate components by mocking dependencies.
- **Kotlinx Coroutines Test**: Used for testing `suspend` functions and `ViewModel` scopes.
- **Robolectric**: Used for tests that interact with Android framework classes (e.g., `Log`, `Patterns`) without needing a physical device.

## Test Structure
Tests are located in `app/src/test/java/com/robertrussell/miguel/sendmoneydemoapp/` and follow the package structure of the main source code.

### 1. Presentation Layer (ViewModels)
Tests ensure that the UI state updates correctly based on user input and UseCase results.
- **LoginViewModelTest**: Validates email/password inputs and the login flow.
- **SignUpViewModelTest**: Validates registration logic, including email format and password strength checks.
- **HomeViewModelTest**: Ensures the user profile information is correctly retrieved from `SavedStateHandle`.
- **WalletViewModelTest**: Tests balance visibility toggling and the "Add Funds" process.
- **SendMoneyViewModelTest**: Tests the money transfer flow, including insufficient balance checks and numeric keypad input logic.
- **TransactionViewModelTest**: Validates the merging of local and remote transaction history and correct date-based sorting.

### 2. Domain Layer (Use Cases)
Tests verify that business logic is correctly orchestrated between the presentation and data layers.
- **LoginUseCaseTest** / **SignUpUseCaseTest**
- **SendMoneyUseCaseTest** / **AddBalanceUseCaseTest**
- **GetBalanceUseCaseTest** / **GetTransactionsUseCaseTest**

### 3. Data Layer (Repositories & Security)
Tests verify data mapping, API interactions, and security protocols.
- **AuthRepositoryImplTest**: Verifies user persistence, password hashing integration, and credential validation.
- **TransactionRepositoryImplTest**: Tests the synchronization between local Room database and remote Retrofit API.
- **BCryptPasswordHasherTest**: Ensures secure hashing and verification of user passwords.

### 4. Utility Tests
- **AppUtilsTest**: Tests helper functions for currency formatting and sensitive data masking.

## How to Run Tests
You can run all unit tests using the following Gradle command:
```bash
./gradlew test
```
Alternatively, in Android Studio:
1. Right-click the `java (test)` folder.
2. Select **Run 'Tests in 'com.robertrussell...'**

## Coverage Summary
The current suite covers:
- **100%** of Use Cases.
- **100%** of ViewModels (Business Logic).
- **~90%** of Repositories (Data mapping and flow logic).
- **100%** of Security and Utility classes.

*Note: UI components (Compose Previews) and Database DAOs are typically covered via Instrumentation tests or manual verification, but the underlying logic is fully unit-tested.*
