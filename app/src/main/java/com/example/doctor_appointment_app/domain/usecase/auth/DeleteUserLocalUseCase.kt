package com.arya.bookmydoc.domain.usecase.auth

import com.arya.bookmydoc.domain.repository.RoomRepository
import javax.inject.Inject

class DeleteUserLocalUseCase@Inject constructor(
    private val repo: RoomRepository
) {
    suspend operator fun invoke(id: String) = repo.deleteUser(id)
}