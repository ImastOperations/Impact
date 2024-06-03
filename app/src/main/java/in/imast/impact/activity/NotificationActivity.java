package in.imast.impact.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.imast.impact.R;
import in.imast.impact.adapter.NotificationAdapter;
import in.imast.impact.model.NotificationModel;
import com.google.gson.Gson;
import in.imast.impact.helper.StaticSharedpreference;
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
 }
