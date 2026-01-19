package com.arya.bookmydoc.domain.repository

import com.arya.bookmydoc.domain.model.Category

interface CategoryRepository {
    suspend fun fetchCategories(): Result<List<Category>>
}