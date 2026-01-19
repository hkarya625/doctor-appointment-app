package com.arya.bookmydoc.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.bookmydoc.domain.model.Category
import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.domain.usecase.category.FetchCategoriesUseCase
import com.arya.bookmydoc.domain.usecase.doctor.FetchDoctorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val fetchDoctorsUseCase: FetchDoctorsUseCase
): ViewModel() {

    private val _doctorUiState = MutableStateFlow(DoctorUiState())
    val doctorUiState: StateFlow<DoctorUiState> = _doctorUiState

    private val _categoryUiState = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState


    init {
        loadDoctor()
        getCategories()
    }

    fun loadDoctor() {
        viewModelScope.launch {

            _doctorUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val result = fetchDoctorsUseCase.invoke()
            result.fold(
                onSuccess = { doctors->
                    _doctorUiState.update {
                        it.copy(
                            isLoading = false,
                            doctors = doctors
                        )
                    }
                },
                onFailure = {exception ->
                    _doctorUiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
            )

        }
    }

    fun getCategories(){
        viewModelScope.launch {
            _categoryUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val result = fetchCategoriesUseCase.invoke()
            result.fold(
                onSuccess = { categories->
                    _categoryUiState.update {
                        it.copy(
                            isLoading = false,
                            categories = categories
                        )
                    }
                },
                onFailure = { exception ->
                    _categoryUiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
            )
        }
    }

    fun retryDoctors(){
        loadDoctor()
    }
    fun retryCategories(){
        getCategories()
    }

}

data class DoctorUiState(
    val isLoading: Boolean = false,
    val doctors:List<Doctor> = emptyList(),
    val error:String? = null
)
data class CategoryUiState(
    val isLoading: Boolean = false,
    val categories:List<Category> = emptyList(),
    val error:String? = null
)