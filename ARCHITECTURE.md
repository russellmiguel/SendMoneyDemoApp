# Architecture Diagrams

This document contains the architectural diagrams for the Send Money Demo App, illustrating the Clean Architecture implementation and key business flows.

## Class Diagram
This diagram shows the relationship between the Presentation, Domain, and Data layers, following Clean Architecture principles.
(Source: [docs/architecture.puml](./docs/architecture.puml))

```plantuml
@startuml
skinparam packageStyle rectangle
skinparam shadowing false

package "Presentation Layer" {
    class SendMoneyScreen << (C,#ADD1B2) Compose >>
    class SendMoneyViewModel {
        - sendMoneyUseCase: SendMoneyUseCase
        - getBalanceUseCase: GetBalanceUseCase
        - authRepository: AuthRepository
        + amountText: String
        + isProcessing: Boolean
        + sendMoney(password, recipient, onResult)
    }
}

package "Domain Layer" {
    package "Use Cases" {
        class SendMoneyUseCase {
            - repository: TransactionRepository
            + invoke(email, amount, recipient)
        }
        class GetBalanceUseCase {
            - repository: AuthRepository
            + invoke(email)
        }
    }
    
    package "Repositories (Interfaces)" {
        interface TransactionRepository {
            + getTransactions(email): Flow<List<Transaction>>
            + sendMoney(email, amount, recipient): Result<Unit>
            + addBalance(email, amount): Result<Unit>
        }
        interface AuthRepository {
            + login(email, pass): Result<User>
            + observeBalance(email): Flow<Double>
            + signUp(name, email, pass): Result<Unit>
        }
    }
    
    package "Models" {
        class User {
            + email: String
            + name: String
            + balance: Double
        }
        class Transaction {
            + id: Int
            + amount: Double
            + recipient: String
            + type: String
        }
    }
}

package "Data Layer" {
    package "Repository Impls" {
        class TransactionRepositoryImpl {
            - transactionDao: TransactionDao
            - userDao: UserDao
            - api: JsonPlaceholderApi
        }
        class AuthRepositoryImpl {
            - userDao: UserDao
            - passwordHasher: PasswordHasher
        }
    }
    
    package "Local (Room)" {
        class AppDatabase
        interface UserDao
        interface TransactionDao
        entity UserEntity
        entity TransactionEntity
    }
    
    package "Remote (Retrofit)" {
        interface JsonPlaceholderApi
    }
}

' Relationships
SendMoneyViewModel --> SendMoneyUseCase
SendMoneyViewModel --> GetBalanceUseCase
SendMoneyUseCase --> TransactionRepository
GetBalanceUseCase --> AuthRepository
TransactionRepositoryImpl ..|> TransactionRepository
AuthRepositoryImpl ..|> AuthRepository
TransactionRepositoryImpl --> TransactionDao
TransactionRepositoryImpl --> UserDao
TransactionRepositoryImpl --> JsonPlaceholderApi
AuthRepositoryImpl --> UserDao

@enduml
```

## Sequence Diagram: Send Money Flow
This diagram illustrates the sequence of operations when a user initiates a money transfer.
(Source: [docs/send_money_sequence.puml](./docs/send_money_sequence.puml))

```plantuml
@startuml
actor User
participant SendMoneyScreen
participant SendMoneyViewModel
participant AuthRepository
participant GetBalanceUseCase
participant SendMoneyUseCase
participant TransactionRepository
participant JsonPlaceholderApi
database RoomDB

User -> SendMoneyScreen: Enter amount & recipient
User -> SendMoneyScreen: Tap "Send Money"
SendMoneyScreen -> User: Prompt for Password
User -> SendMoneyScreen: Enter Password
SendMoneyScreen -> SendMoneyViewModel: sendMoney(password, recipient)

group Authentication & Validation
    SendMoneyViewModel -> AuthRepository: login(email, password)
    AuthRepository -> RoomDB: Query UserEntity
    RoomDB --> AuthRepository: UserEntity (hashed pass)
    AuthRepository -> AuthRepository: Verify Hash
    AuthRepository --> SendMoneyViewModel: Result<User>

    SendMoneyViewModel -> GetBalanceUseCase: invoke(email)
    GetBalanceUseCase -> AuthRepository: observeBalance(email)
    AuthRepository -> RoomDB: Observe Balance
    RoomDB --> GetBalanceUseCase: Flow<Double>
    GetBalanceUseCase --> SendMoneyViewModel: Current Balance
    
    SendMoneyViewModel -> SendMoneyViewModel: Validate Amount <= Balance
end

group Transaction Execution
    SendMoneyViewModel -> SendMoneyUseCase: invoke(email, amount, recipient)
    SendMoneyUseCase -> TransactionRepository: sendMoney(email, amount, recipient)
    
    TransactionRepository -> JsonPlaceholderApi: POST /posts (Mock API call)
    JsonPlaceholderApi --> TransactionRepository: 201 Created
    
    TransactionRepository -> RoomDB: Update Balance (UserDao)
    TransactionRepository -> RoomDB: Insert Transaction (TransactionDao)
    
    TransactionRepository --> SendMoneyUseCase: Result.success(Unit)
    SendMoneyUseCase --> SendMoneyViewModel: Result.success(Unit)
end

SendMoneyViewModel --> SendMoneyScreen: onResult(Success)
SendMoneyScreen --> User: Show Success UI & Navigate Back

@enduml
```
