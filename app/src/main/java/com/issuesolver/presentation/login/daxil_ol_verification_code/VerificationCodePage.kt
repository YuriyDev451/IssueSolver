package com.issuesolver.presentation.login.daxil_ol_verification_code


import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.issuesolver.R
import com.issuesolver.common.AlertDialogExample
import com.issuesolver.common.StatusR
import com.issuesolver.domain.entity.networkModel.login.RequestOtp
import com.issuesolver.presentation.common.AuthButton
import com.issuesolver.presentation.common.ErrorText
import com.issuesolver.presentation.common.LoadingOverlay
import com.issuesolver.presentation.navigation.AuthScreen
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun VerificationCodePage(
    navController: NavController,
    viewModel: VerificationCodePageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var otpValue by remember { mutableStateOf(TextFieldValue("")) }
    var isOtpFilled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val uiState by viewModel.uiState.collectAsState()
    val isOtpValueError = uiState.otpValueError != null

    val keyboardController = LocalSoftwareKeyboardController.current
    val otpTrustState by viewModel.otpTrustState.collectAsState()

    when(otpTrustState?.status){
        StatusR.LOADING -> {
            CircularProgressIndicator()
        }
        StatusR.SUCCESS -> {
            navController.navigate(AuthScreen.PasswordChange.route)
            viewModel.clearOtpTrustState()
        }
        StatusR.ERROR -> {

        }
        else -> {

        }
    }
    val resendOtpState by viewModel.resendOtpState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(resendOtpState) {
        if (resendOtpState?.status == StatusR.ERROR) {
            showDialog = true
        }
    }

    when(resendOtpState?.status){
        StatusR.LOADING -> {
            LoadingOverlay()        }
        StatusR.SUCCESS -> {

        }
        StatusR.ERROR -> {
            //Dialog view
            resendOtpState?.message?.let {
                if (showDialog) {
                    AlertDialogExample(
                        message = it,
//                        onDismiss = { showDialog = false },
                        onConfirmation = { showDialog = false }
                    )
                }
            }
        }
        else -> {

        }
    }




    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    (LocalView.current.context as Activity)

    var remainingTime by remember { mutableLongStateOf(180) }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
    }
    val minutes = remainingTime / 60
    val seconds = remainingTime % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Scaffold(content = { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                Column(
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color.White)
                            .clickable {
                                navController.popBackStack()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.backarray),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Təsdiq Kodu",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Start,
                            color = Color(0xFF2981FF)
                        )
                        Row {
                            Image(
                                painter = painterResource(R.drawable.timer),
                                contentDescription = "timer",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Text(
                                " $formattedTime",
                                fontSize = 18.sp,
                                color = Color(0xFF4D96FF),
                                style = MaterialTheme.typography.headlineMedium,
                            )
                        }
                    }
                    Text(
                        "E-poçtunuza gələn təsdiq kodunu daxil edin.",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        color = Color(0xFF9D9D9D),
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 20.dp),
                    )
                    Divider(
                        thickness = 0.5.dp,
                        color = Color(0xFF2981FF)
                    )
                    Column(
                        Modifier
                            .padding(top = 28.dp)
                            .verticalScroll(rememberScrollState())

                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = Color.Transparent,
                        ) {
                            OtpInputField(
                                modifier = Modifier
                                    .focusRequester(focusRequester),
                                otpText = otpValue.text,
//                                shouldCursorBlink = false,
                                isOtpValueError=isOtpValueError,
                                onOtpModified = { value, otpFilled ->
                                    otpValue =
                                        TextFieldValue(value, selection = TextRange(value.length))
                                    isOtpFilled = otpFilled
                                    if (otpFilled) {
                                        keyboardController?.hide()
                                    }
                                }
                            )
                        }
                    }
                    ErrorText(
                        errorMessage = uiState.otpValueError,
//                        isVisible = isEmailError
                    )
                }
            }
            Spacer(modifier = Modifier.height(150.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                AuthButton(
                    text = "Təsdiqlə",
                    onClick = {
                        viewModel.otpTrust(
                            RequestOtp(
                            otpCode = otpValue.text
                        )
                        )
                              },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 26.dp)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .clickable(
                            enabled = remainingTime <= 0,
                            onClick = {
                                remainingTime = 180
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ),
                    text = "Kodu yenidən göndər",
                    fontSize = 15.sp,
                    color = if (remainingTime > 0) Color.Gray else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    )
}


@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int = 6,
    isOtpValueError: Boolean,
    onOtpModified: (String, Boolean) -> Unit
) {
    val text = remember { mutableStateOf(otpText) }


    BasicTextField(
        value = text.value,
        onValueChange = {
            if (it.length <= otpLength && it.all { char -> char.isDigit() }) {
                text.value = it
                onOtpModified(it, it.length == otpLength)
            }
        },
        modifier = modifier,
        textStyle = TextStyle(textAlign = TextAlign.Center,
            fontSize = 17.sp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until otpLength) {
                    CharacterBox(
                        character = text.value.getOrNull(i)?.toString() ?: "",
                        isFocused = i == text.value.length,
                        isOtpValueError= isOtpValueError,
                        modifier = Modifier
                            .weight(1f, fill = true)
                            .padding(horizontal = 4.dp)
                    )
                    if (i == 2) {
                        Text(
                            "-",
                            style = TextStyle(
                                color =
                                Color(0xFF2981FF),
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CharacterBox(
    character: String,
    isFocused: Boolean,
    isOtpValueError: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isOtpValueError) Color.Red else if (isFocused) Color(0xFF2981FF) else Color.White
    val errorColor = if (isOtpValueError) Color.Red else Color.Black
    val backgroundColor = if (isOtpValueError) Color(0xFFf8ecec) else Color.White



    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .defaultMinSize(minWidth = 52.dp, minHeight = 65.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .border(
                1.dp,
//                if (isFocused)
//                    Color(0xFF2981FF)
                    borderColor,
//                else Color.White,
                RoundedCornerShape(12.dp)
            )
    ) {
        Text(
            text = character,
            style = TextStyle(fontSize = 17.sp, textAlign = TextAlign.Center, color = errorColor
            )
        )
    }
}


@Composable
internal fun CharacterContainer(
    index: Int,
    text: String,
    shouldShowCursor: Boolean,
    shouldCursorBlink: Boolean,
) {
    val isFocused = text.length == index
    val character = when {
        index < text.length -> text[index].toString()
        else -> ""
    }

    val cursorVisible = remember { mutableStateOf(shouldShowCursor) }

    LaunchedEffect(key1 = isFocused) {
        if (isFocused && shouldShowCursor && shouldCursorBlink) {
            while (true) {
                delay(800)
                cursorVisible.value = !cursorVisible.value
            }
        }
    }

    Box(contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier
                .width(36.dp)
                .border(
                    width = when {
                        isFocused -> 2.dp
                        else -> 1.dp
                    },
                    color = Color.Transparent,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(2.dp),
            text = character,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        AnimatedVisibility(visible = isFocused && cursorVisible.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(2.dp)
                    .height(24.dp)
            )
        }
    }
}





