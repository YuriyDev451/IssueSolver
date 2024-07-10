package com.issuesolver.presentation.profile.enter_password

data class DeleteAccountState (

    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false
)