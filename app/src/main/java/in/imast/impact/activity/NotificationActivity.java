package in.imast.impact.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.adapter.NotificationAdapter;
import in.imast.impact.model.NotificationModel;
import com.google.gson.Gson;
import in.imast.impact.helper.StaticSharedpreference;
import retrofit2.Response;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class NotificationActivity extends AppCompatActivity {


    LinearLayout linearBack;
    RecyclerView recyclerView;
    TextView tvNo,tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        StaticSharedpreference.saveInt("notificationCount", 0, this);
        initViews();

        getNotificationApi();
    }

    private void initViews() {

        tvTitle = findViewById(R.id.tvTitle);
        recyclerView = findViewById(R.id.recyclerView);
        linearBack = findViewById(R.id.linearBack);
        tvNo = findViewById(R.id.tvNo);
        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                Gson gson = new Gson();
        String json2 = StaticSharedpreference.getInfo("notification", getApplicationContext());


        Type type = new TypeToken<ArrayList<NotificationModel>>() {
        }.getType();
        ArrayList<NotificationModel> arrayList = gson.fromJson(json2, type);

        if (arrayList != null && !arrayList.equals("") && arrayList.size() != 0) {
            NotificationAdapter notificationAdapter =new NotificationAdapter(this ,arrayList);
            recyclerView.setAdapter(notificationAdapter);

        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    private void getNotificationApi()
    {
        ApiClient.getNotification(StaticSharedpreference.getInfo("AccessToken", this), new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {

                try {
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200)
                        {
                            Log.v("test ", "test : "+ response.body());

                        }

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
 }
