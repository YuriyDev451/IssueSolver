package com.issuesolver.presentation.profile.new_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issuesolver.common.Resource
import com.issuesolver.common.State
import com.issuesolver.domain.entity.networkModel.profile.UpdatePasswordRequest
import com.issuesolver.domain.usecase.login.backend.ResetPasswordUseCase
import com.issuesolver.domain.usecase.profile.backend.GetMeUseCase
import com.issuesolver.domain.usecase.profile.backend.UpdatePasswordUseCase
import com.issuesolver.domain.usecase.profile.local.ConfirmNewPasswordUseCase
import com.issuesolver.domain.usecase.profile.local.NewPasswordUseCase
import com.issuesolver.domain.usecase.profile.local.PreviousPasswordUseCase
import com.issuesolver.presentation.login.password_change_page.PasswordChangePageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordScreenViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val newPasswordUseCase: NewPasswordUseCase,
    private val confirmNewPasswordUseCase: ConfirmNewPasswordUseCase,
    private val previousPasswordUseCase: PreviousPasswordUseCase,



    ) : ViewModel() {

    private val _uiState = MutableStateFlow(NewPasswordScreenState())
    val uiState: StateFlow<NewPasswordScreenState> = _uiState.asStateFlow()
    private val _profileState: MutableStateFlow<State?> =  MutableStateFlow(null)
    val profileState: StateFlow<State?> = _profileState
    fun clearState() {
        _profileState.value = null
    }

    fun updatePassword(request:UpdatePasswordRequest) {
        viewModelScope.launch {
            updatePasswordUseCase(request).collect { resource ->
                when(resource){
                    is Resource.Loading -> {
                        _profileState.emit(State.loading())
                    }
                    is Resource.Success -> {
                        _profileState.emit(State.success())
                    }
                    is Resource.Error -> {
                        _profileState.emit(State.error(resource.message))
//                        _uiState.value = uiState.value.copy(currentPasswordError = resource.message)

                    }
                }
            }
        }
    }

    fun handleEvent(event: NewPasswordScreenEvent) {
        when (event) {
            is NewPasswordScreenEvent.NewPasswordChanged -> {
                val result = newPasswordUseCase.execute(
                    event.newPassword,
                    _uiState.value.confirmPassword
                )
                val result2 = confirmNewPasswordUseCase.execute(
                    event.newPassword,
                    _uiState.value.confirmPassword
                )
                _uiState.value = _uiState.value.copy(
                    newPassword = event.newPassword,
                    newPasswordError = if (result.successful) null else _uiState.value.newPasswordError,
                    isInputValid = result.successful &&
                            result2.successful &&
                            confirmNewPasswordUseCase.execute(
                                event.newPassword,
                                _uiState.value.confirmPassword
                            ).successful &&
                            previousPasswordUseCase.execute(
                                _uiState.value.currentPassword
                            ).successful
                )
            }

            is NewPasswordScreenEvent.ConfirmPasswordChanged -> {
                val result = confirmNewPasswordUseCase.execute(
                    _uiState.value.newPassword,
                    event.confirmPassword
                )
                val result2 = newPasswordUseCase.execute(
                    _uiState.value.newPassword,
                    event.confirmPassword
                )
                _uiState.value = _uiState.value.copy(
                    confirmPassword = event.confirmPassword,
                    confirmPasswordError = if (result.successful) null else _uiState.value.confirmPasswordError,
                    isInputValid = result.successful && result2.successful &&
                            newPasswordUseCase.execute(
                        _uiState.value.newPassword,
                        event.confirmPassword
                    ).successful &&
                            previousPasswordUseCase.execute(
                                _uiState.value.currentPassword
                            ).successful

                )
            }
            is NewPasswordScreenEvent.CurrentPasswordChanged -> {
                val result = previousPasswordUseCase.execute(
                    event.currentPassword
                )
                _uiState.value = _uiState.value.copy(
                    currentPassword = event.currentPassword,
                    currentPasswordError = result.errorMessage,
                    isInputValid = result.successful &&
                            newPasswordUseCase.execute(
                                _uiState.value.newPassword,
                                _uiState.value.confirmPassword
                            ).successful &&
                            confirmNewPasswordUseCase.execute(
                                _uiState.value.newPassword,
                                _uiState.value.confirmPassword
                            ).successful

                )
            }

            is NewPasswordScreenEvent.Submit -> {
                if (uiState.value.isInputValid) {
                }
            }

        }
    }
    }


