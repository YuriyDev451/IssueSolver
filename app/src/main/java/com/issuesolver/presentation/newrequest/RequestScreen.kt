package com.issuesolver.presentation.newrequest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun RequestScreen(viewModel: RequestScreenViewModel = hiltViewModel()) {
    val suggestions = listOf("Kotlin", "Java", "Dart", "Python")



    Scaffold { padding ->

        Column(
            Modifier
                .imePadding()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            Navigation()

            Divider(
                thickness = 0.5.dp,
                color = Color(0xFF2981FF),
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = 16.dp
                )
            )

            AddLocation(viewModel)

            DropDownCategory(
                category = "Problemin Kateqoriyası",
                placeHolder = "Kateqoriya",
                list = suggestions
            )

            DropDownCategory(
                category = "Problemin yönləndiriləcəyi qurum",
                placeHolder = "Qurum",
                list = suggestions
            )

            Description()

            Column(
                modifier = Modifier
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 20.dp, end = 20.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2981FF),
                        disabledContainerColor = Color(0xFF9AC2FB)
                    )
                ) {
                    Text(
                        text = "Paylaş",
                        fontWeight = FontWeight.W500,
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(top = 14.dp, bottom = 14.dp)
                    )
                }

                Button(
                    onClick = {
//                              viewModel.sendRequest(
//
//                                NewRequest(
//                                    address = "",
//                                    description = "",
//                                    organizationName = ""
//                                )
//                              )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF),
                        disabledContainerColor = Color(0xFFFFFFFF)
                    )
                ) {
                    Text(
                        text = "Sıfırla",
                        fontWeight = FontWeight.W500,
                        fontSize = 15.sp,
                        color = Color(0xFF2981FF),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(top = 14.dp, bottom = 14.dp)
                    )
                }
            }
        }


    }
}


//
//@Composable
//fun RequestScreen() {
//    val suggestions = listOf("Kotlin", "Java", "Dart", "Python")
//
//    Scaffold { padding ->
//
//        Box(
//            Modifier
//                .statusBarsPadding()
//                .fillMaxSize()
//                .navigationBarsPadding()
//        ) {
//            ConstraintLayout(Modifier.fillMaxSize()) {
//                val (content, buttons) = createRefs()
//
//                Column(
//                    modifier = Modifier
//                        .verticalScroll(rememberScrollState())
//                        .constrainAs(content) {
//                            top.linkTo(parent.top)
//                            bottom.linkTo(buttons.top)
//                            height = Dimension.fillToConstraints
//                        }
//                ) {
//                    Navigation()
//
//                    Divider(
//                        thickness = 0.5.dp,
//                        color = Color(0xFF2981FF),
//                        modifier = Modifier.padding(
//                            start = 20.dp,
//                            end = 20.dp,
//                            top = 20.dp,
//                            bottom = 16.dp
//                        )
//                    )
//
//                    AddLocation()
//
//                    DropDownMenu(
//                        category = "Problemin Kateqoriyası",
//                        placeHolder = "Kateqoriya",
//                        list = suggestions
//                    )
//
//                    DropDownMenu(
//                        category = "Problemin yönləndiriləcəyi qurum",
//                        placeHolder = "Qurum",
//                        list = suggestions
//                    )
//
//                    Description()
//                }
//
//                Column(
//                    modifier = Modifier
//                        .constrainAs(buttons) {
//                            bottom.linkTo(parent.bottom)
//                        }
//                        .imePadding()
//                        .background(Color(0xFFF0F4F9))
//                ) {
//                    Button(
//                        onClick = { },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 8.dp, start = 20.dp, end = 20.dp),
//                        shape = RoundedCornerShape(50.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFF2981FF),
//                            disabledContainerColor = Color(0xFF9AC2FB)
//                        )
//                    ) {
//                        Text(
//                            text = "Paylaş",
//                            fontWeight = FontWeight.W500,
//                            fontSize = 15.sp,
//                            color = Color.White,
//                            modifier = Modifier
//                                .align(Alignment.CenterVertically)
//                                .padding(top = 14.dp, bottom = 14.dp)
//                        )
//                    }
//
//                    Button(
//                        onClick = { },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
//                        shape = RoundedCornerShape(50.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFFFFFFFF),
//                            disabledContainerColor = Color(0xFFFFFFFF)
//                        )
//                    ) {
//                        Text(
//                            text = "Sıfırla",
//                            fontWeight = FontWeight.W500,
//                            fontSize = 15.sp,
//                            color = Color(0xFF2981FF),
//                            modifier = Modifier
//                                .align(Alignment.CenterVertically)
//                                .padding(top = 14.dp, bottom = 14.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}