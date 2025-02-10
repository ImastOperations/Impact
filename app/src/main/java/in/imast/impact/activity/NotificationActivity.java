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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;


public class NotificationActivity extends AppCompatActivity {
    LinearLayout linearBack;
    RecyclerView recyclerView;
    TextView tvNo, tvTitle;

    ArrayList<NotificationModel> notificationModels = new ArrayList<>();
    NotificationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        StaticSharedpreference.saveInt("notificationCount", 0, this);
        initViews();

    }

    private void initViews() {

        getNotificationApi();

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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, notificationModels);
        recyclerView.setAdapter(adapter);

    }

    private void getNotificationApi() {
        ApiClient.getNotification(StaticSharedpreference.getInfo("AccessToken", this), new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {

                try {
                    if (response != null && errorMessage == null) {

                        if (response.code() == 200) {
                            notificationModels.clear();

                            String status = String.valueOf(Objects.requireNonNull(response.body()).get("status"));
                            String status1 = status.replace("\"", "");
                            String message = String.valueOf(Objects.requireNonNull(response.body()).get("status"));

                            JSONObject jsonObject = null;
                            if (status1.equalsIgnoreCase("success")) {

                                try {
                                    jsonObject = new JSONObject(String.valueOf(response.body()));
                                    JSONArray data_response = jsonObject.getJSONArray("data");

                                    if(data_response != null && data_response.length() > 0)
                                    {
                                        tvNo.setVisibility(View.GONE);

                                        for (int i = 0; i < data_response.length(); i++) {
                                            JSONObject data = data_response.getJSONObject(i);
                                            NotificationModel model = new NotificationModel();

                                            model.setTitle(data.optString("title"));
                                            model.setDescription(data.optString("description"));
                                            model.setDate_time(data.optString("date_time"));
                                            model.setImage(data.optString("image"));

                                            notificationModels.add(model);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                    else {
                                        tvNo.setVisibility(View.VISIBLE);
                                        tvNo.setText(message);
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
