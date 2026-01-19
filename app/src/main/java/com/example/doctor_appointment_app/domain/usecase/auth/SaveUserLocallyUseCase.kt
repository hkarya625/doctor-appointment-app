package com.arya.bookmydoc.domain.usecase.auth

import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.repository.RoomRepository
import javax.inject.Inject

class SaveUserLocallyUseCase@Inject constructor(
    private val repo: RoomRepository
) {
    suspend operator fun invoke(user: User) = repo.saveUser(user)
}