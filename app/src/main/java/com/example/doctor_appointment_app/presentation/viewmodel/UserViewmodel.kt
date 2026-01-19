package com.arya.bookmydoc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.usecase.auth.DeleteUserLocalUseCase
import com.arya.bookmydoc.domain.usecase.auth.GetUserLocallyUseCase
import com.arya.bookmydoc.domain.usecase.auth.GetUserRemoteUseCase
import com.arya.bookmydoc.domain.usecase.auth.SaveUserLocallyUseCase
import com.arya.bookmydoc.domain.usecase.auth.UserLoginUseCase
import com.arya.bookmydoc.domain.usecase.auth.UserLogoutUserCase
import com.arya.bookmydoc.domain.usecase.auth.UserRegisterUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewmodel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase,
    private val userRegisterUseCase: UserRegisterUseCase,
    private val userLogoutUserCase: UserLogoutUserCase,
    private val getUserRemoteUseCase: GetUserRemoteUseCase,
    private val saveUserLocallyUseCase: SaveUserLocallyUseCase,
    private val deleteUserLocalUseCase: DeleteUserLocalUseCase,
    private val getUserLocallyUseCase: GetUserLocallyUseCase
): ViewModel(){


    init {
//        loadCurrentUser()
    }

    // Registration
    private val _registerState = MutableStateFlow(RegisterUiState())
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    fun register(user:User, email:String, password: String){
        viewModelScope.launch {
            _registerState.update {
                it.copy(
                    uiStatus = UiStatus(isLoading = true)
                )
            }
            val result = userRegisterUseCase.invoke(user, email, password)
            result.fold(
                onSuccess = {
                    _registerState.update {
                        it.copy(
                            uiStatus = UiStatus(isLoading = false, isSuccess = true)
                        )
                    }
                },
                onFailure = { exception ->
                    _registerState.update {
                        it.copy(
                            uiStatus = UiStatus(isLoading = false, errorMessage = exception.message)
                        )
                    }
                }
            )
        }
    }


    // Login
    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    fun login(email: String, password: String){
        viewModelScope.launch {
            _loginState.update {
                it.copy(
                    uiStatus = UiStatus(isLoading = true)
                )
            }
            val result = userLoginUseCase.invoke(email, password)
            result.fold(
                onSuccess = { user->
                    saveUserLocallyUseCase.invoke(user)
                    _loginState.update {
                        it.copy(
                            uiStatus = UiStatus(isLoading = false, isSuccess = true),
                            user = user
                        )
                    }
                },
                onFailure = { exception ->
                    _loginState.update {
                        it.copy(
                            uiStatus = UiStatus(isLoading = false, errorMessage = exception.message)
                        )
                    }
                }
            )
        }
    }


    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    fun logout(userId: String){
        viewModelScope.launch {
            _profileUiState.update {
                it.copy(
                    uiStatus = UiStatus(isLoading = true)
                )
            }
            deleteUserLocalUseCase.invoke(userId)
            userLogoutUserCase.invoke()
            _loginState.value = LoginUiState()
            _registerState.value = RegisterUiState()
            _profileUiState.value = ProfileUiState(
                uiStatus = UiStatus(isSuccess = true)
            )
        }
    }

    fun loadCurrentUser(){
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if(userId!=null){
                _profileUiState.update {
                    it.copy(
                        uiStatus = UiStatus(isLoading = true)
                    )
                }
                val user = userId.let { getUserLocallyUseCase.invoke(userId) }
                if (user != null){
                    _profileUiState.update {
                        it.copy(
                            user = user,
                            uiStatus = UiStatus(isLoading = false)
                        )
                    }
                }else{
                    _profileUiState.update {
                        it.copy(
                            uiStatus = UiStatus(isLoading = false, errorMessage = "No data found")
                        )
                    }
                }
            }
        }
    }

    fun fetchUserData(userId: String) = viewModelScope.launch {
        _profileUiState.update {
            it.copy(
                uiStatus = UiStatus(isLoading = true)
            )
        }
        val result = getUserRemoteUseCase.invoke(userId)
        result.fold(
            onSuccess = { user ->
                _profileUiState.update {
                    it.copy(
                        user = user,
                        uiStatus = UiStatus(isLoading = false)
                    )
                }
            },
            onFailure = { e ->
                _profileUiState.update {
                    it.copy(
                        uiStatus = UiStatus(isLoading = false, errorMessage = e.message)
                    )
                }
            }
        )
    }
}

data class LoginUiState(
    val uiStatus:UiStatus = UiStatus(),
    val user:User? = null
)

data class RegisterUiState(
    val uiStatus:UiStatus = UiStatus()
)

data class ProfileUiState(
    val user:User? = null,
    val uiStatus:UiStatus = UiStatus()
)

data class UiStatus(
    val isLoading:Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface ContactAction {
    data object Call : ContactAction
    data object Sms : ContactAction
    data object Website : ContactAction
    data object Share : ContactAction
}