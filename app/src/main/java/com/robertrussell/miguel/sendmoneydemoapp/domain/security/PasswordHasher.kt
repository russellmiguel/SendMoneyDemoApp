package com.robertrussell.miguel.sendmoneydemoapp.domain.security

interface PasswordHasher {
    fun hash(password: String): String
    fun verify(password: String, hash: String): Boolean
}
