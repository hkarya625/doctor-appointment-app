package com.arya.bookmydoc.data.dto



data class DoctorDto(
    val id: String? = null,
    val name: String? = null,
    val role: String? = null,
    val profileImage: String? = null,
    val biography: String? = null,
    val experience: Int? = null,
    val specialization: String? = null,
    val address: String? = null,
    val location: String? = null,
    val phoneNumber: String? = null,
    val rating: Double? = null,
    val totalPatients: Int? = null,
    val consultationFee: Double? = null,
    val available: Boolean? = null,
    val site: String? = null,
    val appointments: List<String>? = null
)
