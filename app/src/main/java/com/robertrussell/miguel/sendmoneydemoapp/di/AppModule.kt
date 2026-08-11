package com.robertrussell.miguel.sendmoneydemoapp.di

import android.content.Context
import androidx.room.Room
import com.robertrussell.miguel.sendmoneydemoapp.data.local.AppDatabase
import com.robertrussell.miguel.sendmoneydemoapp.data.local.TransactionDao
import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserDao
import com.robertrussell.miguel.sendmoneydemoapp.data.repository.AuthRepositoryImpl
import com.robertrussell.miguel.sendmoneydemoapp.data.repository.TransactionRepositoryImpl
import com.robertrussell.miguel.sendmoneydemoapp.data.security.BCryptPasswordHasher
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.security.PasswordHasher
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.GetTransactionsUseCase
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.LoginUseCase
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "send_money_db"
        ).build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao

    @Provides
    @Singleton
    fun providePasswordHasher(): PasswordHasher = BCryptPasswordHasher()

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        passwordHasher: PasswordHasher
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, passwordHasher)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao)
    }

    @Provides
    fun provideSignUpUseCase(repository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(repository)
    }

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideGetTransactionsUseCase(repository: TransactionRepository): GetTransactionsUseCase {
        return GetTransactionsUseCase(repository)
    }
}
