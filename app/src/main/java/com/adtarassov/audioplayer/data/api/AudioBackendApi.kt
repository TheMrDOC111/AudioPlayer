package com.adtarassov.audioplayer.data.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AudioBackendApi {

  @Headers("Accept: application/json")
  @FormUrlEncoded
  @POST("api/token/")
  suspend fun postAuthorizeUser(
    @Field("username")
    username: String,

    @Field("password")
    password: String
  ): Response<TokenModel>

  @Headers("Accept: application/json")
  @FormUrlEncoded
  @POST("api/users/")
  suspend fun postRegisterUser(
    @Field("username")
    username: String,

    @Field("password")
    password: String
  ): Response<TokenModel>

  @GET("api/audio/recommendation")
  suspend fun getAudioRecommendation(): Response<List<AudioResponseModel>>

  @GET("/api/{account_name}/audio")
  suspend fun getAudioProfile(
    @Path("account_name")
    accountName: String
  ): Response<List<AudioResponseModel>>

  companion object {
    const val BASE_URL = "http://185.233.82.123:8000/"
  }
}