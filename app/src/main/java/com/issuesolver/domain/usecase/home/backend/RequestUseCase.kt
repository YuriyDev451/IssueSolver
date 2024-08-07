package com.issuesolver.domain.usecase.home.backend

import androidx.paging.PagingData
import com.issuesolver.data.repository.home.RequestInterface
import com.issuesolver.domain.entity.networkModel.home.FilterData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//class RequestUseCase @Inject constructor(private val filter: RequestInterface) {
//
//
//    suspend operator fun invoke( page:Int, size:Int) = flow {
//
//        emit(Resource.Loading())
//        try {
//            val response = filter.request(page, size)
//            if (response.isSuccessful) {
//                emit(Resource.Success(response.body()))
//            } else {
//                val errorResponse = response.errorBody()?.string()?.let {
//                    parseErrorResponse(it)
//                }
//                emit(Resource.Error(errorResponse?.message ?: "Unknown Error"))
//            }
//        } catch (e: IOException) {
//            emit(Resource.Error("Network Error: ${e.localizedMessage}"))
//        } catch (e: HttpException) {
//            emit(Resource.Error("HTTP Error: ${e.localizedMessage}"))
//        } catch (e: Exception) {
//            emit(Resource.Error("Unexpected Error: ${e.localizedMessage}"))
//        }
//    }
//
//    private fun parseErrorResponse(json: String): FilterResponseModel? {
//        // Use your preferred JSON library here (e.g., Gson)
//        // Assuming you're using Gson:
//        return try {
//            val gson = Gson()
//            gson.fromJson(json, FilterResponseModel::class.java)
//        } catch (e: Exception) {
//            null
//        }
//    }
//}


class RequestUseCase @Inject constructor(
    private val request: RequestInterface
) {
    operator fun invoke(): Flow<PagingData<FilterData>> {
        return request.request().flow
    }
}