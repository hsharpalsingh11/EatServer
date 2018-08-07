package com.example.admin.eatserver.Remote;


import com.example.admin.eatserver.Model.MyResponse;
import com.example.admin.eatserver.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
   @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAADdobWsE:APA91bEzLb27m0HlA-JlA3IK_r7mFxsXjOKF9fIfO63ZPqqV07MDJDKfbhuGUzksFXLnu06J8ASLO-rekhNXbU_61hK1fNFw3ST1HXQV19pnhQLg1MnHym5RF0NwXEWjUibvotG7w9zd"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
