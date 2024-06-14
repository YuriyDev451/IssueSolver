package com.issuesolver.presentation.login.daxil_ol_verification_code





import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.toArgb
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
import com.issuesolver.R
import com.issuesolver.presentation.common.AuthButton
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VerificationCodePage() {
    val context = LocalContext.current
    var otpValue by remember { mutableStateOf(TextFieldValue("")) }
    var isOtpFilled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    (LocalView.current.context as Activity)

    var remainingTime by remember { mutableStateOf(30) } // 30 seconds countdown

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
                .padding(20.dp)
                .imePadding()

        ) {

            Column(

            ) {
                Column(
                    Modifier.padding(bottom = 20.dp)

                ) {
                    Box(
                        modifier = Modifier

                            .padding(top = 20.dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(100.dp)) // Apply rounded corners to the background
                            .background(Color.White) // Set the background color
                            .clickable { /* Handle back press */ },
                        contentAlignment = Alignment.Center // Center the content inside the Box

                    ) {
                        Image(
                            painter = painterResource(R.drawable.backarray),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp) // Ensures the image fills the Box
                        )
                    }
            Text("Təsdiq Kodu",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 24.dp),


            )
            Text("E-poçtunuza gələn təsdiq kodunu daxil edin.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                color = Color(0xFF9D9D9D),
                modifier = Modifier
                    .padding(top = 10.dp,bottom = 20.dp),

                )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        thickness = 0.5.dp,
                        color=Color(0xFF2981FF)

                        )
                    Spacer(modifier = Modifier.height(8.dp))





                    Column(
                        Modifier.padding(top = 20.dp)

                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                        color = Color.Transparent,  // This makes the Surface background match the Scaffold
                        ) {
                            OtpInputField(
                                modifier = Modifier
                                    .focusRequester(focusRequester),
                                otpText = otpValue.text,
                                shouldCursorBlink = false,
                                onOtpModified = { value, otpFilled ->
                                    otpValue = TextFieldValue(value, selection = TextRange(value.length))
                                    isOtpFilled = otpFilled
                                    if (otpFilled) {
                                        keyboardController?.hide()
                                    }
                                }
                            )
                        }
                    }


            Spacer(modifier = Modifier.height(16.dp))
            Text("Qalan vaxt: $remainingTime", style = MaterialTheme.typography.bodyMedium,
                fontSize = 17.sp,
                color=Color(0xFF2981FF))
                    }


            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                AuthButton(
                    text = "Təsdiqlə",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(modifier = Modifier.clickable {

                }, text = "Kodu yenidən göndər",
                    fontSize = 15.sp,


                    color = MaterialTheme.colorScheme.primary)
            }



            }
        }
    }
}

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int = 6, // Assume OTP length is 6
    shouldShowCursor: Boolean = false,
    shouldCursorBlink: Boolean = false,
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
        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 17.sp),
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
                        modifier = Modifier.weight(1f, fill = true).padding(horizontal = 4.dp)
                    )
                    if (i == 2) { // To add a dash or space between the groups of characters
                        Text("-", style = TextStyle(color = Color(0xFF2981FF), fontSize = 24.sp, textAlign = TextAlign.Center))
                    }
                }
            }
        }
    )
}

@Composable
fun CharacterBox(character: String, isFocused: Boolean, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .defaultMinSize(minWidth = 52.dp, minHeight = 65.dp) // Ensure each box has a minimum size
            .background(Color.White, RoundedCornerShape(12.dp)) // Background color of the box
            .border(1.dp, if (isFocused) Color(0xFF2981FF) else Color.White, RoundedCornerShape(12.dp)) // Border color changes when focused
    ) {
        Text(
            text = character,
            style = TextStyle(fontSize = 17.sp, textAlign = TextAlign.Center)
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

    // Cursor visibility state
    val cursorVisible = remember { mutableStateOf(shouldShowCursor) }

    // Blinking effect for the cursor
    LaunchedEffect(key1 = isFocused) {
        if (isFocused && shouldShowCursor && shouldCursorBlink) {
            while (true) {
                delay(800) // Adjust the blinking speed here
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
                    color = Color.Transparent,  // Add a color for the border here
                    shape = RoundedCornerShape(6.dp)
                )

                .padding(2.dp),
            text = character,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        // Display cursor when focused
        AnimatedVisibility(visible = isFocused && cursorVisible.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(2.dp)
                    .height(24.dp) // Adjust height according to your design
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun VerificationCodePagePreview() {
   MaterialTheme {
        VerificationCodePage()
    }
}




//
//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun VerificationCodePage() {
//    val context = LocalContext.current
//    var otpValue by remember { mutableStateOf(TextFieldValue("")) }
//    var isOtpFilled by remember { mutableStateOf(false) }
//    val focusRequester = remember { FocusRequester() }
//    val keyboardController = LocalSoftwareKeyboardController.current
//
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//        keyboardController?.show()
//    }
//
//    (LocalView.current.context as Activity).window.statusBarColor = Color.White.toArgb()
//
//    var remainingTime by remember { mutableStateOf(30) } // 30 seconds countdown
//
//    LaunchedEffect(Unit) {
//        while (remainingTime > 0) {
//            delay(1000)
//            remainingTime--
//        }
//    }
//
//    Scaffold { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Təsdiq Kodu", style = MaterialTheme.typography.headlineMedium)
//            Text("E-poçtunuza gələn təsdiq kodunu daxil edin.", style = MaterialTheme.typography.bodyMedium)
//            Spacer(modifier = Modifier.height(16.dp))
//            Divider(color = Color.Gray, thickness = 1.dp)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.White)
//                    .padding(24.dp),
//                color = Color.White
//            ) {
//                OtpInputField(
//                    modifier = Modifier
//                        .padding(top = 48.dp)
//                        .focusRequester(focusRequester),
//                    otpText = otpValue.text,
//                    shouldCursorBlink = false,
//                    onOtpModified = { value, otpFilled ->
//                        otpValue = TextFieldValue(value, selection = TextRange(value.length))
//                        isOtpFilled = otpFilled
//                        if (otpFilled) {
//                            keyboardController?.hide()
//                        }
//                    }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Text("Qalan vaxt: $remainingTime", style = MaterialTheme.typography.bodyMedium)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Bottom
//            ) {
//                Button(
//                    onClick = {},
//                    modifier = Modifier.fillMaxWidth(0.8f)
//                ) {
//                    Text("Təsdiqlə")
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                TextButton(onClick = { /* TODO: Add resend code logic */ }) {
//                    Text("Kodu yenidən göndər")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun OtpInputField(
//    modifier: Modifier = Modifier,
//    otpText: String,
//    otpLength: Int = 6, // Total length of 6 for OTP
//    shouldShowCursor: Boolean = false,
//    shouldCursorBlink: Boolean = false,
//    onOtpModified: (String, Boolean) -> Unit
//) {
//    // Assume OTP length is 6 for simplicity
//    val text = remember { mutableStateOf(otpText) }
//
//    BasicTextField(
//        value = text.value,
//        onValueChange = {
//            // Only accept changes if they are numbers and the total length doesn't exceed otpLength
//            if (it.length <= otpLength && it.all { char -> char.isDigit() }) {
//                text.value = it
//                onOtpModified(it, it.length == otpLength)
//            }
//        },
//        modifier = modifier,
//        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 24.sp),
//        keyboardOptions = KeyboardOptions.Default.copy(
//            keyboardType = KeyboardType.Number,
//            imeAction = ImeAction.Done
//        ),
//        decorationBox = { innerTextField ->
//            Row(
//                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                for (i in 0 until 3) {
//                    CharacterBox(character = if (i < text.value.length) text.value[i].toString() else "", isFocused = i == text.value.length)
//                    Spacer(modifier = Modifier.width(4.dp))
//                }
//                Text("-", style = TextStyle(color = Color.Gray, fontSize = 24.sp, textAlign = TextAlign.Center))
//                Spacer(modifier = Modifier.width(4.dp))
//                for (i in 3 until 6) {
//                    CharacterBox(character = if (i < text.value.length) text.value[i].toString() else "", isFocused = i == text.value.length)
//                    if (i < 5) Spacer(modifier = Modifier.width(4.dp))
//                }
//            }
//        }
//    )
//}
//
//@Composable
//fun CharacterBox(character: String, isFocused: Boolean) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .size(36.dp)
//            .border(1.dp, if (isFocused) Color.Black else Color.Gray, RoundedCornerShape(6.dp))
//    ) {
//        Text(text = character, style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center))
//    }
//}
//
//@Composable
//internal fun CharacterContainer(
//    index: Int,
//    text: String,
//    shouldShowCursor: Boolean,
//    shouldCursorBlink: Boolean,
//) {
//    val isFocused = text.length == index
//    val character = when {
//        index < text.length -> text[index].toString()
//        else -> ""
//    }
//
//    // Cursor visibility state
//    val cursorVisible = remember { mutableStateOf(shouldShowCursor) }
//
//    // Blinking effect for the cursor
//    LaunchedEffect(key1 = isFocused) {
//        if (isFocused && shouldShowCursor && shouldCursorBlink) {
//            while (true) {
//                delay(800) // Adjust the blinking speed here
//                cursorVisible.value = !cursorVisible.value
//            }
//        }
//    }
//
//    Box(contentAlignment = Alignment.Center) {
//        Text(
//            modifier = Modifier
//                .width(36.dp)
//                .border(
//                    width = when {
//                        isFocused -> 2.dp
//                        else -> 1.dp
//                    },
//                    color = Color.Gray,  // Add a color for the border here
//                    shape = RoundedCornerShape(6.dp)
//                )
//
//                .padding(2.dp),
//            text = character,
//            style = MaterialTheme.typography.headlineLarge,
//            textAlign = TextAlign.Center
//        )
//
//        // Display cursor when focused
//        AnimatedVisibility(visible = isFocused && cursorVisible.value) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .width(2.dp)
//                    .height(24.dp) // Adjust height according to your design
//            )
//        }
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun VerificationCodePagePreview() {
//    MaterialTheme {
//        VerificationCodePage()
//    }
//}
//


