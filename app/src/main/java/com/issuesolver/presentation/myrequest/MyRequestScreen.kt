package com.issuesolver.presentation.myrequest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.issuesolver.R
import com.issuesolver.common.PlaceholderShimmerCard
import com.issuesolver.common.SnackBar
import com.issuesolver.domain.entity.networkModel.home.FilterData
import com.issuesolver.presentation.home.home.RequestsCard
import com.issuesolver.presentation.navigation.DetailsScreen
import kotlinx.coroutines.delay
import com.issuesolver.presentation.navigation.DetailsScreen

@Composable
fun MyRequestScreen(navController: NavController,
    viewModel: MyRequestViewModel = hiltViewModel()
) {

//    LaunchedEffect {
//        viewModel.getMovies()
//    }

    LaunchedEffect(Unit) {
         viewModel.getMovies() // Асинхронный запрос данных
    }

    //val lazyPagingItems = viewModel.myRequests.collectAsLazyPagingItems()

    //val moviesState = viewModel.moviesState.collectAsLazyPagingItems()
    val moviePagingItems: LazyPagingItems<FilterData> = viewModel.moviesState.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 25.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {

    LazyColumn {
        items(moviePagingItems.itemCount) { index ->
            moviePagingItems[index]?.let { filterData ->
                UserCard(
                    fullName = filterData.fullName,
                    status = filterData.status,
                    description = filterData.description,
                    categoryName = filterData.category.categoryName,
                    viewModel = viewModel,
                    requestId = filterData.requestId,
                    likeSuccess=filterData.likeSuccess,
                    onClick = {

                        //navController.navigate("requestDetail/${filterData.requestId}")
                        navController.navigate(DetailsScreen.RequestById.route+ "/${filterData.requestId}")
                    }
                )
            }
        }

        // Include your loading and error handling as before
        moviePagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        // Display loading state
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        // Display loading at the end of the list
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = moviePagingItems.loadState.refresh as LoadState.Error
                    item {
                        Text(text = "Error: ${e.error.localizedMessage}")
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = moviePagingItems.loadState.append as LoadState.Error
                    item {
                        Text(text = "Error: ${e.error.localizedMessage}")
            Text(
                "Mənim sorğularım",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Start,
                color = Color(0xFF2981FF),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider(
                thickness = 0.5.dp,
                color = Color(0xFF2981FF),
                modifier = Modifier.padding(bottom = 27.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(moviePagingItems.itemCount) { index ->
                    moviePagingItems[index]?.let { filterData ->
                        UserCard(
                            fullName = filterData.fullName,
                            status = filterData.status,
                            description = filterData.description,
                            categoryName = filterData.category?.categoryName
                        )
                    }
                }
                moviePagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            items(5) {
                                PlaceholderShimmerCard()
                            }
                        }
                        loadState.append is LoadState.Loading -> {
                            item {
                            }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val e = moviePagingItems.loadState.refresh as LoadState.Error
                            item {
                                Text(text = "Error: ${e.error.localizedMessage}")
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            val e = moviePagingItems.loadState.append as LoadState.Error
                            item {
                                Text(text = "Error: ${e.error.localizedMessage}")
                            }
                        }
                    }
                }
            }
        }
    }





//    LazyColumn {
//        items(moviePagingItems.itemCount) {  item ->
//            when (item) {
//                null -> {
//                    // Показать shimmer effect или индикатор загрузки для элемента
//                }
//                else -> {
//                    // Показать элемент списка
//                    UserCard(fullName = moviePagingItems.itemSnapshotList.items.firstOrNull()?.fullName,
//                        status = moviePagingItems.itemSnapshotList.items.firstOrNull()?.status,
//                        description = moviePagingItems.itemSnapshotList.items.firstOrNull()?.description,
//                        categoryName = moviePagingItems.itemSnapshotList.items.firstOrNull()?.category?.categoryName,
//                        viewModel= viewModel)
//                }
//            }
//        }
//
//        moviePagingItems.apply {
//            when {
//                loadState.refresh is LoadState.Loading -> {
//                    item {
//                        // Показать shimmer effect или индикатор загрузки для всей страницы
//                    }
//                }
//                loadState.append is LoadState.Loading -> {
//                    item {
//                        // Показать индикатор загрузки при подгрузке данных
//                    }
//                }
//                loadState.refresh is LoadState.Error -> {
//                    val e = moviePagingItems.loadState.refresh as LoadState.Error
//                    item {
//                        // Показать сообщение об ошибке
//                        Text(text = "Error: ${e.error.localizedMessage}")
//                    }
//                }
//                loadState.append is LoadState.Error -> {
//                    val e = moviePagingItems.loadState.append as LoadState.Error
//                    item {
//                        // Показать сообщение об ошибке при подгрузке данных
//                        Text(text = "Error: ${e.error.localizedMessage}")
//                    }
//                }
//            }
//        }
//    }

}