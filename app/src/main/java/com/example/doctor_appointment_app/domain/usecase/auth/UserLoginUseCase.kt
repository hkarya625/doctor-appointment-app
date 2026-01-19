package com.arya.bookmydoc.domain.usecase.auth

import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String):Result<User> =
        userRepository.login(email, password)
}