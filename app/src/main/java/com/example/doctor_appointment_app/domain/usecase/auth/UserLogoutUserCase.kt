package com.arya.bookmydoc.domain.usecase.auth

import com.arya.bookmydoc.domain.repository.UserRepository
import javax.inject.Inject

class UserLogoutUserCase@Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.logOut()
}