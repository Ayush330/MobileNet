package com.example.apivalidator


import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

public interface RetrofitService
{
    @Multipart
    //@FormUrlEncoded
    @POST("/image_upload")
    fun uploadData(@Part image: MultipartBody.Part):Call<dataReturned>
    //fun uploadData(@Field("imageName") title:String,@Field("Image") image:String):Call<dataReturned>

}
