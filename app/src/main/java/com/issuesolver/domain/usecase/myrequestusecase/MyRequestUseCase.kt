package com.issuesolver.domain.usecase.myrequestusecase

import androidx.paging.PagingData
import com.google.gson.Gson
import com.issuesolver.common.Resource
import com.issuesolver.data.repository.home.RequestInterface
import com.issuesolver.data.repository.myrequestrepo.MyRequestRepositoryInterface
import com.issuesolver.domain.entity.networkModel.home.FilterData
import com.issuesolver.domain.entity.networkModel.home.FilterResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
//
//class MyRequestUseCase@Inject constructor(private val myRequest: MyRequestRepositoryInterface) {
//
//    suspend operator fun invoke( page:Int, size:Int) = flow {
//
//        emit(Resource.Loading())
//        try {
//            val response = myRequest.myRequest(page, size)
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

class MyRequestUseCase @Inject constructor(
    private val repository: MyRequestRepositoryInterface
) {
    operator fun invoke(): Flow<PagingData<FilterData>> {
        return repository.getMyRequests().flow
    }
}
