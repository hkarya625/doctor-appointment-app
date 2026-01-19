package com.arya.bookmydoc.domain.usecase.category

import com.arya.bookmydoc.domain.model.Category
import com.arya.bookmydoc.domain.repository.CategoryRepository
import com.arya.bookmydoc.domain.repository.DoctorRepository
import javax.inject.Inject

class FetchCategoriesUseCase@Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> = categoryRepository.fetchCategories()
}