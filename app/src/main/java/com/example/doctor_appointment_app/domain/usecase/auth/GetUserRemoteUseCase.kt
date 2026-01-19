package com.arya.bookmydoc.domain.usecase.auth

import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.repository.UserRepository
import javax.inject.Inject

class GetUserRemoteUseCase@Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User> = userRepository.getUserData(userId)
}