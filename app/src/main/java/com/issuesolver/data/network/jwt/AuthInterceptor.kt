package com.issuesolver.data.network.jwt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.navigation.NavController
import com.google.gson.JsonObject
import com.issuesolver.data.network.auth.LoginService
import com.issuesolver.domain.entity.networkModel.login.LoginResponse
import com.issuesolver.domain.entity.networkModel.login.RefreshTokenRequest
import com.issuesolver.domain.entity.networkModel.login.RefreshTokenResponse
import com.issuesolver.presentation.MainActivity
import com.issuesolver.presentation.navigation.AuthScreen
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

//class AuthInterceptor(private val sharedPreferences: SharedPreferences): Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request = chain.request()
//        val token = sharedPreferences.getString("accessToken", null)
//
//        if (token != null) {
//            request = request.newBuilder()
//                .header("Authorization", "Bearer $token")
//                .build()
//        }
//        return chain.proceed(request)    }
//}

class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString("access_token", null)
//        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJpbGtpbnN1bGV5bWFub3YyMDBAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiVVNFUiIsIkdPVkVSTUVOVCIsIlNVUEVSX1NUQUZGIiwiU1RBRkYiLCJBRE1JTiJdLCJuYmYiOjE3MTk0Nzg3NTYsImV4cCI6MjAzNTAxMTU0NSwiaXNzIjoiaWxraW5AU3VsZXltYW5vdi5jb20iLCJhdWQiOiJpbGtpbkBTdWxleW1hbm92LmNvbSJ9.QHD7oSGvy3h-zKMbhQvo1ieREFhW2ic_4WMaWVMtnL0"
        val request = chain.request().newBuilder()
        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}


/*
class AuthAuthenticator @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            sharedPreferences.getString("refresh_token", null)
        }
        return runBlocking {
            val newTokenResponse = getNewToken(refreshToken)
            if (!newTokenResponse.isSuccessful || newTokenResponse.body() == null) {

                sharedPreferences.edit().clear().apply()
                null
            } else {
                val newAccessToken = newTokenResponse.body()?.data?.accessToken
                newAccessToken?.let {
                    sharedPreferences.edit().putString("access_token", it).apply()
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $it")
                        .build()
                }
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<LoginResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gatewayy-f20db7ab0323.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(LoginService::class.java)
        return service.refreshToken("Bearer $refreshToken")
    }
}

*/


class AuthAuthenticator @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : Authenticator {

    //private lateinit var navigation: NavController = NavController(sharedPreferences)
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = sharedPreferences.getString("refresh_token", null)
        if (refreshToken == null) {
            // Логирование, если refreshToken равен null
            Log.e("AuthAuthenticator", "Refresh token is null")
            return null
        }

        return runBlocking {
            val newTokenResponse = getNewToken(refreshToken)
            if (!newTokenResponse.isSuccessful || newTokenResponse.body() == null) {
                // Логирование неуспешного ответа
                Log.e("AuthAuthenticator", "Failed to refresh token: ${newTokenResponse.errorBody()?.string()}")
                sharedPreferences.edit().clear().apply()
                //navigation.navigate(AuthScreen.Login.route)
                val navIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(navIntent)
                null
            } else {
                val newAccessToken = newTokenResponse.body()?.data?.token
                if (newAccessToken == null) {
                    // Логирование, если новый accessToken равен null
                    Log.e("AuthAuthenticator", "New access token is null")
                    return@runBlocking null
                }
                sharedPreferences.edit().putString("access_token", newAccessToken).apply()
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String): retrofit2.Response<RefreshTokenResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gatewayy-f20db7ab0323.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(LoginService::class.java)
        return service.refreshToken(RefreshTokenRequest(refreshToken))
    }



    /*

    private suspend fun getNewToken(refreshToken: String?):
            retrofit2.Response<LoginResponse>? {
        val loggingInterceptor = HttpLoggingInterceptor()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                val request = requestBuilder.build()

                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gatewayy-f20db7ab0323.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(LoginService::class.java)

        val body = JsonObject()
        body.addProperty("refresh_token", refreshToken)

        val bearerToken = refreshToken


        if (refreshToken?.isEmpty() == true) {
            return null
        }
        return service.refreshToken(bearerToken)
    }

    private fun newRequest(request: Request, accessToken: String): Request {
        return request.newBuilder()
            .header("Authorization", accessToken)
            .build()
    }*/
}












//@Singleton
//class DGAuthenticator(
//    private val context: Context,
//    private val preferences: DGEncryptedSharedPreferences
//) : Authenticator {
//
//    val mutex = Mutex()
//
//    override fun authenticate(route: Route?, response: Response): Request? {
//
//
//        return synchronized(this) {
//
//            runBlocking {
//                refreshToken()?.let {
//                    mutex.withLock {
//                        if (it.isSuccessful) {
//
//                            val loginAndRefreshResponse = it.body()
//
//                            preferences.accessToken =
//                                loginAndRefreshResponse?.accessToken
//                                    ?: preferences.accessToken
//                            preferences.refreshToken =
//                                loginAndRefreshResponse?.refreshToken
//                                    ?: preferences.refreshToken
//
//
//                            return@runBlocking newRequest(
//                                response.request,
//                                preferences.accessToken
//                            )
//
//                        } else if (it.code() == 401) {
//
//                            preferences.clear()
//                            val intent = Intent(context,
//                                LauncherActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            context.startActivity(intent)
//
//                            return@runBlocking null
//
//                        } else {
//                            return@runBlocking null
//                        }
//                    }
//                }
//
//
//                return@runBlocking null
//            }
//
//        }
//
//    }
//
//    private suspend fun refreshToken():
//            retrofit2.Response<LoginAndRefreshResponse>? {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor.Level.BODY
//        } else {
//            HttpLoggingInterceptor.Level.NONE
//        }
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .addInterceptor { chain ->
//                val requestBuilder = chain.request().newBuilder()
//                    .addHeader("Content-Type", "application/json;charset=utf-8")
//                    .addHeader("X-Platform", "Mobile")
//                    .addHeader("X-Client-Type", "Android")
//                    .addHeader("X-AppVersion", BuildConfig.VERSION_NAME)
//                    .addHeader("Accept-Language", "az")
//                val request = requestBuilder.build()
//
//                chain.proceed(request)
//            }
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build()
//        val service = retrofit.create(DGAuthorizationService::class.java)
//
//        val body = JsonObject()
//        body.addProperty("refresh_token", preferences.refreshToken)
//        body.addProperty("certificate", preferences.certificate)
//
//        val bearerToken = preferences.accessToken
//
//
//        if (preferences.refreshToken.isEmpty()) {
//            return null
//        }
//        return service.refreshAccessToken(bearerToken, body)
//    }
//
//    private fun newRequest(request: Request, accessToken: String): Request {
//        return request.newBuilder()
//            .header("Authorization", accessToken)
//            .build()
//    }
//}