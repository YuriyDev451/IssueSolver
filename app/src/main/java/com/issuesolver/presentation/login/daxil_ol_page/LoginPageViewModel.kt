package com.issuesolver.presentation.login.daxil_ol_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issuesolver.common.Resource
import com.issuesolver.common.State
import com.issuesolver.domain.entity.networkModel.LoginRequest
import com.issuesolver.domain.useCase.SignInUseCase
import com.issuesolver.domain.usecase.login.ValidatePasswordUseCase
import com.issuesolver.domain.usecase.login.LoginUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginUseCase: LoginUseCase,
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginPageState())
    val uiState: StateFlow<LoginPageState> = _uiState.asStateFlow()

    private val _signInState: MutableStateFlow<State?> =  MutableStateFlow(null)
    val signInState: StateFlow<State?> = _signInState

    fun signIn(request: LoginRequest) {
        viewModelScope.launch {
            signInUseCase(request).collect { resource ->
                when(resource){
                    is Resource.Loading -> {
                        _signInState.emit(State.loading())
                    }
                    is Resource.Success -> {
                        _signInState.emit(State.success())
                    }
                    is Resource.Error -> {
                        _signInState.emit(State.error(resource.message))
                        _uiState.value = uiState.value.copy(emailError = resource.message)
                    }
                }
            }
        }
    }

    fun clearLoginState() {
        _signInState.value = null
    }

    fun handleEvent(event: LoginPageEvent) {
        when (event) {
            is LoginPageEvent.EmailChanged -> {
                val result = loginUseCase.execute(event.email)
                _uiState.value = uiState.value.copy(
                    email = event.email,
                    emailError = result.errorMessage,
                    isInputValid = result.successful &&
                            validatePasswordUseCase.execute(uiState.value.password).successful
                )
            }

            is LoginPageEvent.PasswordChanged -> {
                val result = validatePasswordUseCase.execute(event.password)
                _uiState.value = uiState.value.copy(
                    password = event.password,
                    passwordError = result.errorMessage,
                    isInputValid = result.successful && loginUseCase.execute(uiState.value.email).successful
                )
            }

            is LoginPageEvent.Submit -> {
                if (uiState.value.isInputValid) {

                }
            }
        }
    }
}
