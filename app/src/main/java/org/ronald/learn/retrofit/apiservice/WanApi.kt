package org.ronald.learn.retrofit.apiservice

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * @author Penoder
 * @date 2018/9/5
 */
interface WanApi {

    @GET("{prePath}/{pageNum}/{afterPath}")
    fun getHomePage(@Path("prePath") prePath: String, @Path("pageNum") pageNum: Int, @Path("afterPath") afterPath: String): Observable<String>

    @GET("banner/json")
    fun getBanner(): Observable<String>
}