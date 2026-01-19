package com.arya.bookmydoc.domain.model



data class Doctor(
    val id: String,
    val name: String,
    val role: UserRole,
    val profileImage: String?,
    val biography: String,
    val experience: Int,
    val specialization: String,
    val address: String,
    val location: String,
    val phoneNumber: String,
    val rating: Double,
    val totalPatients: Int,
    val consultationFee: Double,
    val available: Boolean,
    val site: String?,
    val appointments:List<String>
)