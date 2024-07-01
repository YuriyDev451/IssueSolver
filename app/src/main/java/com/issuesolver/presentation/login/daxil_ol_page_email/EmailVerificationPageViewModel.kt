package com.issuesolver.presentation.login.daxil_ol_page_email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issuesolver.common.Resource
import com.issuesolver.domain.entity.networkModel.ResendOtpModel
import com.issuesolver.domain.useCase.ForgetPasswordUseCase
import com.issuesolver.domain.usecase.login.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerificationPageViewModel @Inject constructor(

    private val validateEmailUseCase: ValidateEmailUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailVerificationPageState())
    val uiState: StateFlow<EmailVerificationPageState> = _uiState.asStateFlow()


    private val _forgetPasswordState = MutableStateFlow<Resource<String?>>(Resource.Loading())
    val forgetPasswordState: StateFlow<Resource<String?>> = _forgetPasswordState


    fun forgetPassword(request: ResendOtpModel){
        viewModelScope.launch {
            forgetPasswordUseCase(request).collect{resource->
                _forgetPasswordState.value=resource
            }
        }
    }

    fun handleEvent(event: VerificationCodePageEvent) {
        when (event) {
            is VerificationCodePageEvent.EmailChanged -> {
                val result = validateEmailUseCase.execute(event.email)
                _uiState.value = uiState.value.copy(
                    email = event.email,
                    emailError = result.errorMessage,
                    isInputValid = result.successful
                )
            }

            is VerificationCodePageEvent.Submit -> {
                if (uiState.value.isInputValid) {

                }
            }

        }
    }
}



