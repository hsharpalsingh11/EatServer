package com.example.admin.eatserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.eatserver.Common.Common;
import com.example.admin.eatserver.Model.MyResponse;
import com.example.admin.eatserver.Model.Notification;
import com.example.admin.eatserver.Model.Sender;
import com.example.admin.eatserver.Remote.APIService;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    EditText edtMessage,edtTitle;
    Button sendMessage;
    APIService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_message );
        mService = Common.getFCMClient();
        edtMessage = (MaterialEditText)findViewById( R.id.edtMessage);
        edtTitle = (MaterialEditText)findViewById( R.id.edtTitle );
        sendMessage = (Button) findViewById( R.id.btnSend );
        sendMessage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification notification = new Notification(edtTitle.getText().toString(),edtMessage.getText().toString());
                Sender toTopic = new Sender();
                toTopic.to = new StringBuilder("/topics/").append(Common.topicName).toString();
                toTopic.notification = notification;

                mService.sendNotification(toTopic).enqueue( new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if(response.isSuccessful())
                            Toast.makeText( MessageActivity.this, "Message Sent", Toast.LENGTH_SHORT ).show();
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Toast.makeText( MessageActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );

            }
        } );
    }
}
