package com.arya.bookmydoc.data.mapper

import com.arya.bookmydoc.data.dto.CategoryDto
import com.arya.bookmydoc.domain.model.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id ?: "",
        name = name ?: "",
        picture = picture ?: ""
    )
}

fun Category.toDto(): CategoryDto {
    return CategoryDto(
        id = id,
        name = name,
        picture = picture
    )
}