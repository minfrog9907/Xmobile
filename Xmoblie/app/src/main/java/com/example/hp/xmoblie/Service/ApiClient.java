package com.example.hp.xmoblie.Service;

/**
 * Created by HP on 2017-10-16.
 */

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.JustRequestItem;
import com.example.hp.xmoblie.Items.RollbackItem;
import com.example.hp.xmoblie.Items.LoginItem;
import com.example.hp.xmoblie.Items.OCRDataItem;
import com.example.hp.xmoblie.Items.ShortCutItem;
import com.example.hp.xmoblie.Items.TagItem;
import com.example.hp.xmoblie.Http.UnsafeOkHttpClient;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

import retrofit2.http.HTTP;
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
            //.baseUrl("http://10.1.21.228")
            .baseUrl("http://xmobile.lfconfig.xyz")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiClient service = retrofit.create(ApiClient.class);


    Retrofit filesever = new Retrofit.Builder()
            .baseUrl("https://xstream.lfconfig.xyz")
            //.baseUrl("https://10.1.21.228")
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
            .build();

    ApiClient severService =  filesever.create(ApiClient.class);

    @FormUrlEncoded
    @POST("/login")
    Call<LoginItem> repoContributors(
            @Field("userid") String userid
            , @Field("password") String password
            , @Field("deviceid") String deviceid

    );

    @GET("/file")
    Call<List<FileItem>> repoFileNodes(
            @Header("token") String token
            , @Query("dir") String dir
    );

    @GET("/file/rollbackinfo")
    Call<List<RollbackItem>> repoFileLog(
            @Header("token") String token
            , @Query("path") String path
            , @Query("filename") String filename

    );

    @GET("/file_list.json")
    Call<List<FileItem>> test();

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/file", hasBody = true)
    Call<ResponseBody> repoFileDelete(
            @Header("token") String token,
            @Field("list") String list
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/file/dir", hasBody = true)
    Call<ResponseBody> repoFolderDelete(
            @Header("token") String token,
            @Field("path") String path,
            @Field("dirname") String filename

    );

    @FormUrlEncoded
    @PUT("/file")
    Call<ResponseBody> repoRename(
            @Header("token") String token,
            @Field("originalName") String originalName,
            @Field("path") String path,
            @Field("newName") String newName);


    @FormUrlEncoded
    @POST("/file/move")
    Call<JustRequestItem> repoMove(
            @Header("token") String token,
            @Field("filename") String filename,
            @Field("path") String path,
            @Field("targetPath") String targetPath);

    @GET("/winfile/mkdir")
    Call<ResponseBody>repoMkDir(
            @Header("token") String token
            , @Query("dirname") String dirname
            , @Query("path") String path
    );

    @FormUrlEncoded
    @POST("/image")
    Call<OCRDataItem> repoOCR(
            @Header("token") String token,
            @Field("data") String data
    );

    @Multipart
    @POST("/image/re")
    Call<ResponseBody> repoUploadBills(
            @Header("token") String token,
            @Part("image") RequestBody image,
            @Part MultipartBody.Part file,
            @Query("location") String location,
            @Query("price") int price
    );

    @GET("/shortcut")
    Call<List<ShortCutItem>> repoStar(
            @Header("token") String header,
            @Query("offset") int offset,
            @Query("limit") int limit

    );

    @POST("/file")
    Call<ResponseBody> repoDownload(
            @Body RequestBody bytes
    );

    @Multipart
    @POST("/file")
    Call<ResponseBody> repoUpload(
            @Header("token") String token,
            @Part("file") RequestBody file,
            @Part MultipartBody.Part body,
            @Query("path") String path
    );

    @GET("/shortcut")
    Call<List<ShortCutItem>> repoShortCut(
            @Header("token") String token,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @GET("/file/rollbackinfo")
    Call<ResponseBody>repoRollback(
            @Header("token")String token,
            @Query("path")String path,
            @Query("filename")String filename
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/shortcut", hasBody = true)
    Call<ResponseBody>repoSHCDelete(
            @Header("token")String token,
            @Field("path")String path,
            @Field("filename")String filename
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/file/dir", hasBody = true)
    Call<List<RollbackItem>>repoDeleteFolder(
            @Header("token")String token,
            @Field("path")String path,
            @Field("filename")String filename
    );

    @FormUrlEncoded
    @POST("/file/tag")
    Call<ResponseBody>repoAddTag(
      @Header("token")String token,
      @Field("tag")String tag,
      @Field("filename")String filename,
      @Field("path")String path
    );

    @GET("/file/tag")
    Call<List<FileItem>>repoFindTag(
            @Header("token")String token,
            @Query("tag")String tag
    );

    @POST("/")
    Call<ResponseBody> repoUpload(
            @Body RequestBody bytes
    );


    @POST("/")
    Call<ResponseBody> repoUploadService(
            @Body RequestBody bytes
    );

}


