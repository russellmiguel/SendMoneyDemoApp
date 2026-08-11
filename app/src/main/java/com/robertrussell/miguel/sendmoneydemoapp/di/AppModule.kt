package com.robertrussell.miguel.sendmoneydemoapp.di

import android.content.Context
import androidx.room.Room
import com.robertrussell.miguel.sendmoneydemoapp.BuildConfig
import com.robertrussell.miguel.sendmoneydemoapp.data.local.AppDatabase
import com.robertrussell.miguel.sendmoneydemoapp.data.local.TransactionDao
import com.robertrussell.miguel.sendmoneydemoapp.data.local.UserDao
import com.robertrussell.miguel.sendmoneydemoapp.data.remote.JsonPlaceholderApi
import com.robertrussell.miguel.sendmoneydemoapp.data.repository.AuthRepositoryImpl
import com.robertrussell.miguel.sendmoneydemoapp.data.repository.TransactionRepositoryImpl
import com.robertrussell.miguel.sendmoneydemoapp.data.security.BCryptPasswordHasher
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.AuthRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.repository.TransactionRepository
import com.robertrussell.miguel.sendmoneydemoapp.domain.security.PasswordHasher
import com.robertrussell.miguel.sendmoneydemoapp.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val hostname = "jsonplaceholder.typicode.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, BuildConfig.SSL_PIN)
            .add(hostname, BuildConfig.SSL_PIN_BACKUP)
            .build()

        return OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(JsonPlaceholderApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideJsonPlaceholderApi(retrofit: Retrofit): JsonPlaceholderApi {
        return retrofit.create(JsonPlaceholderApi::class.java)
    }

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
    fun provideTransactionRepository(
        transactionDao: TransactionDao,
        userDao: UserDao,
        api: JsonPlaceholderApi
    ): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao, userDao, api)
    }

    @Provides
    fun provideSignUpUseCase(repository: AuthRepository): SignUpUseCase = SignUpUseCase(repository)

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase = LoginUseCase(repository)

    @Provides
    fun provideGetTransactionsUseCase(repository: TransactionRepository): GetTransactionsUseCase = GetTransactionsUseCase(repository)

    @Provides
    fun provideGetBalanceUseCase(repository: AuthRepository): GetBalanceUseCase = GetBalanceUseCase(repository)

    @Provides
    fun provideAddBalanceUseCase(repository: TransactionRepository): AddBalanceUseCase = AddBalanceUseCase(repository)

    @Provides
    fun provideSendMoneyUseCase(repository: TransactionRepository): SendMoneyUseCase = SendMoneyUseCase(repository)
}
