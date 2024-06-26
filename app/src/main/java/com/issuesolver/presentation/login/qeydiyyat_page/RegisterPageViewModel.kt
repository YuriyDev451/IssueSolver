package com.issuesolver.presentation.login.qeydiyyat_page
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issuesolver.common.Resource
import com.issuesolver.domain.entity.networkModel.RegisterRequestModel
import com.issuesolver.domain.useCase.RegisterUseCase
import com.issuesolver.domain.useCase.login.ValidateEmailUseCase
import com.issuesolver.domain.useCase.login.ValidatePasswordUseCase
import com.issuesolver.domain.useCase.login.ValidateRepeatedPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateRepeatedPasswordUseCase: ValidateRepeatedPasswordUseCase
    ): ViewModel() {


    private val _registerState = MutableStateFlow<Resource<String?>>(Resource.Loading())
    val registerState: StateFlow<Resource<String?>> = _registerState


    private val _uiState = MutableStateFlow(RegisterPageState())
    val uiState: StateFlow<RegisterPageState> = _uiState.asStateFlow()




    fun register(request: RegisterRequestModel) {
        viewModelScope.launch {
            registerUseCase(request).collect { resource ->
                _registerState.value = resource
            }
        }
    }





    fun handleEvent(event: RegisterPageEvent) {
        when (event) {
            is RegisterPageEvent.EmailChanged -> {
                val result = validateEmailUseCase.execute(event.email)
                _uiState.value = uiState.value.copy(
                    email = event.email,
                    emailError = result.errorMessage,
                    isInputValid = result.successful && validatePasswordUseCase.execute(uiState.value.password).successful
                )
            }
            is RegisterPageEvent.PasswordChanged -> {
                val result = validatePasswordUseCase.execute(event.password)
                _uiState.value = uiState.value.copy(
                    password = event.password,
                    passwordError = result.errorMessage,
                    isInputValid = result.successful && validateEmailUseCase.execute(uiState.value.email).successful
                )
            }
            is RegisterPageEvent.RepeatedPasswordChanged -> {
                val result = validateRepeatedPasswordUseCase.execute(_uiState.value.password, event.repeatedPassword)
                _uiState.value = _uiState.value.copy(
                    repeatedPassword = event.repeatedPassword,
                    repeatedPasswordError = result.errorMessage,
                    isInputValid = validatePasswordUseCase.execute(_uiState.value.password).successful && result.successful
                )
            }

            is RegisterPageEvent.FullNameChanged -> {
                _uiState.value = _uiState.value.copy(
                    fullName = event.fullName,
                    isInputValid = true
                )
                
            }


            is RegisterPageEvent.Submit -> {
                if (uiState.value.isInputValid) {

                }
            }
        }
    }



}