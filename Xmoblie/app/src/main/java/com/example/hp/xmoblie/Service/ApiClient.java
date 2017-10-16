package com.example.hp.xmoblie.Service;

/**
 * Created by HP on 2017-10-16.
 */

import com.example.hp.xmoblie.Items.FileItem;
import com.example.hp.xmoblie.Items.LoginItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
            @Field("token") String token
            , @Field("path") String path
    );
}


