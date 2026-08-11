package com.robertrussell.miguel.sendmoneydemoapp.data.security

import at.favre.lib.crypto.bcrypt.BCrypt
import com.robertrussell.miguel.sendmoneydemoapp.domain.security.PasswordHasher
import javax.inject.Inject

class BCryptPasswordHasher @Inject constructor() : PasswordHasher {
    override fun hash(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    override fun verify(password: String, hash: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified
    }
}
