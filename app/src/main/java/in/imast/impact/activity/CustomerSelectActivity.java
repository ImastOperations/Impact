package in.imast.impact.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;

import in.imast.impact.R;
import in.imast.impact.adapter.CustomerSelectAdapter;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import in.imast.impact.model.CustomerModel;
import retrofit2.Response;


public class CustomerSelectActivity extends AppCompatActivity {


    LinearLayout linearBack;
    RecyclerView recyclerView;
    TextView tvNo,tvTitle;

    Utilities utilities;
    DialogClass dialogClass;
    RelativeLayout relative;
    Dialog dialog;
    EditText edtSearch;
    Handler handler;
    ArrayList<CustomerModel.Customer> customerArrs = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initViews();
    }

    private void initViews() {
        edtSearch = findViewById(R.id.edtSearch);

        relative = findViewById(R.id.relative);
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

        relative.setBackgroundColor(Color.parseColor("#e7e7e7"));
        tvTitle.setText("Select Seller");

        utilities = new Utilities(this);
        dialogClass = new DialogClass();

        edtSearch.setVisibility(View.VISIBLE);
        handler =new Handler();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filter(edtSearch.getText().toString());

                    }
                },1000);
            }
        });


        getCustomerList();
    }
    CustomerSelectAdapter customerSelectAdapter;
    private void getCustomerList() {
        if (!utilities.isOnline())
            return;
        dialog = dialogClass.progresesDialog(this);

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("customer_type","2");
        queryParams.put("city_id", StaticSharedpreference.getInfo("cityId",this));
        String acToken = StaticSharedpreference.getInfo("AccessToken", this);
        Log.e("acToken>>",acToken);

        ApiClient.getCustomerList("" + StaticSharedpreference.getInfo("AccessToken", this),
                queryParams, new APIResultLitener<CustomerModel>() {
                    @Override
                    public void onAPIResult(Response<CustomerModel> response, String errorMessage) {
                        dialog.dismiss();
                        if (response != null && errorMessage == null) {
                            if (response.code() == 200) {

                                ArrayList<CustomerModel.Customer> customerArrsTemp = new ArrayList<>();

                                customerArrs.clear();
                                customerArrsTemp.addAll(response.body().getDatas());

                                for (int i = 0; i < customerArrsTemp.size(); i++) {

                                    if(!StaticSharedpreference.getInfo("UserId",CustomerSelectActivity.this).equals(
                                            customerArrsTemp.get(i).getId()+""
                                            )){
                                        customerArrs.add(customerArrsTemp.get(i));
                                    }
                                }

                                Log.v("akram","arr "+customerArrs);


                                 customerSelectAdapter =new CustomerSelectAdapter(CustomerSelectActivity.this ,customerArrs);
                                recyclerView.setAdapter(customerSelectAdapter);

                            } else {


                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());

                                    dialogClass.alertDialog(jsonObject.getString("status"), jsonObject.getString("message")
                                            , CustomerSelectActivity.this, false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {

                        }
                    }
                });
    }

    private void filter(String text){

        ArrayList<CustomerModel.Customer> customerArrsFilter = new ArrayList<>();

        for (int i = 0; i < customerArrs.size(); i++) {
            if(customerArrs.get(i).getFirm_name().toLowerCase().contains(text.toLowerCase())||
                    customerArrs.get(i).getCustomer_mobile_number().toLowerCase().contains(text.toLowerCase())){

                customerArrsFilter.add(customerArrs.get(i));

            }
        }

        if(customerSelectAdapter!=null)
        customerSelectAdapter.filterList(customerArrsFilter);

    }


}
