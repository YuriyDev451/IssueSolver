package com.issuesolver.domain.entity.networkModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



@Parcelize
data class DeleteAccountResponse(
    var success: Boolean? = null,
    var message: String? = null
) : Parcelable