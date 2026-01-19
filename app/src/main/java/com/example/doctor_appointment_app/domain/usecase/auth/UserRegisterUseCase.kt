package com.arya.bookmydoc.domain.usecase.auth

import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.repository.UserRepository
import javax.inject.Inject

class UserRegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User, email:String, password: String): Result<Boolean> =
        userRepository.register(user, email, password)
}