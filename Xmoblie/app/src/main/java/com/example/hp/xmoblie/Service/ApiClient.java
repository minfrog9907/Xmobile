package com.example.hp.xmoblie.Service;

/**
 * Created by HP on 2017-10-16.
 */

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.JustRequestItem;
import com.example.hp.xmoblie.Items.LoginItem;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jungjune on 2016-07-22.
 */
public interface ApiClient {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://xmobile2.lfconfig.xyz")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiClient service = retrofit.create(ApiClient.class);

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
    @POST("/file")
    Call<JustRequestItem>repoUpload(
            @Header("token") String token,
            @Field("file")File file,
            @Field("path")String path
    );

    @FormUrlEncoded
    @DELETE("/file")
    Call<List<FileItem>>repoDelete(
            @Header("token") String token,
            @Field("list")List<FileItem> list
    );

}


