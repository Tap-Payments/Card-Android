package company.tap.tapcardformkit.open.web_wrapper

import androidx.annotation.RestrictTo
import company.tap.tapcardformkit.open.web_wrapper.data.network.model.CardConfigurationResponse
import company.tap.tapcardformkit.open.web_wrapper.data.network.model.GeoLocationResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
@RestrictTo(RestrictTo.Scope.LIBRARY)
object ApiService {
  const val BASE_URL = "https://mw-sdk.dev.tap.company/v2/checkout/"
}
interface UserApi {
  @GET("/card-sdk/CardSDKsUrls/cardSDKss.json")
  suspend fun getCardConfiguration(): CardConfigurationResponse


}
interface IPaddressApi{
  @GET("/json/")
  suspend fun getGeoLocation(): GeoLocationResponse
}

object RetrofitClient {
  private const val BASE_URL = "https://tap-assets.b-cdn.net"
  private const val BASE_URL_2 = "https://geolocation-db.com/"

  val okHttpClient = OkHttpClient()
    .newBuilder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .addInterceptor(RequestInterceptor)
    .build()
  fun getClient(): Retrofit =
    Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  fun getClient2(): Retrofit =
    Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(BASE_URL_2)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

}

object RequestInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    println("Outgoing request to ${request.url}")
    return chain.proceed(request)
  }
}
