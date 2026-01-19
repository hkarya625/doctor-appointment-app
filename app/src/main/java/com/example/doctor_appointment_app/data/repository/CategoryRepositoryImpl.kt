package com.arya.bookmydoc.data.repository

import com.arya.bookmydoc.data.remote.FirebaseDataSource
import com.arya.bookmydoc.domain.model.Category
import com.arya.bookmydoc.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl@Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): CategoryRepository {
    override suspend fun fetchCategories(): Result<List<Category>> {
        return firebaseDataSource.fetchCategories()
    }
}