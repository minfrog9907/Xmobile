package com.example.hp.xmoblie.Service;

/**
 * Created by HP on 2017-10-16.
 */

import com.example.hp.xmoblie.Items.DeleteItem;
import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.JustRequestItem;
import com.example.hp.xmoblie.Items.LoginItem;
import com.example.hp.xmoblie.Items.OCRDataItem;
import com.example.hp.xmoblie.Items.StarItem;
import com.example.hp.xmoblie.Utill.UnsafeOkHttpClient;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by jungjune on 2016-07-22.
 */
public interface ApiClient {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://xmobile.lfconfig.xyz")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiClient service = retrofit.create(ApiClient.class);

    Retrofit retrofitTest= new Retrofit.Builder()
            .baseUrl("http://lfconfig.xyz")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiClient serviceTest = retrofitTest.create(ApiClient.class);

    Retrofit filesever = new Retrofit.Builder()
            .baseUrl("http://xstream.lfconfig.xyz")//.baseUrl("https://10.1.21.228")
            //.client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
            .build();

    ApiClient severService = filesever.create(ApiClient.class);
    @FormUrlEncoded
    @POST("/login")
    Call<LoginItem> repoContributors(
            @Field("userid") String userid
            , @Field("password") String password
            , @Field("deviceid") String deviceid

    );

    @GET("/file")
    Call<List<FileItem>>repoFileNodes(
            @Header("token") String token
            , @Query("dir") String dir
        );

    @GET("/file_list.json")
    Call<List<FileItem>>test();

    @FormUrlEncoded
    @DELETE("/file")
    Call<List<DeleteItem>>repoDelete(
            @Header("token") String token,
            @Field("list")List<DeleteItem> list
    );

    @FormUrlEncoded
    @PUT("/file")
    Call<JustRequestItem>repoRename(
            @Header("token") String token,
            @Field("originalName")String originalName,
            @Field("path")String path,
            @Field("newName")String newName);


    @FormUrlEncoded
    @POST("/file/move")
    Call<JustRequestItem>repoMove(
            @Header("token") String token,
            @Field("filename")String filename,
            @Field("path")String path,
            @Field("targetPath")String targetPath);

    @GET("/winfile/mkdir")
    Call<JustRequestItem>repoMkDir(
            @Header("token") String token
            , @Query("dirname") String dirname
            , @Query("path") String path
    );

    @FormUrlEncoded
    @POST("/image")
    Call<OCRDataItem>repoOCR(
            @Header("token") String token,
            @Field("data")String data
    );

    @Multipart
    @POST("/image/re")
    Call<ResponseBody>repoUploadBills(
            @Header("token") String token,
            @Part("image") RequestBody image,
            @Part MultipartBody.Part file,
            @Query("location") String location,
            @Query("price")int price
    );

    @GET("/shortcut")
    Call<List<StarItem>>repoStar(
            @Header("token") String header,
            @Query("offset")int offset,
            @Query("limit")int limit

    );
    @GET("/xmobile/awef.json.txt")
    Call<OCRDataItem>repoOCRT(    );

    @POST("/file")
    Call<ResponseBody>repoDownload(
            @Body RequestBody bytes
    );

    @Multipart
    @POST("/file")
    Call<ResponseBody>repoUpload(
            @Header("token") String token,
            @Part("file") RequestBody file,
            @Part MultipartBody.Part body,
            @Query("path")String path
    );
}


