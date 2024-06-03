package in.imast.impact.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.Connection.EmployeInterface;
import in.imast.impact.R;
import in.imast.impact.adapter.CostumerAdapter;
import in.imast.impact.adapter.GroupAdapter;
import in.imast.impact.adapter.GroupTagAdapter;
import in.imast.impact.adapter.PaginationAdapterDemo;
import in.imast.impact.adapter.PaginationScrollListener;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import in.imast.impact.model.CustomerModel;
import in.imast.impact.model.DemoModal;
import in.imast.impact.model.GetCategory;
import in.imast.impact.model.GetGroupTag;
import in.imast.impact.model.GetSize;
import in.imast.impact.model.ProductModal;
import retrofit2.Response;


public class MasterProductActivity extends AppCompatActivity {

    private static final int PAGE_START = 1;
    LinearLayout linearBack;
    RecyclerView recyclerView;
    TextView tvNo, tvTitle;

    Utilities utilities;
    DialogClass dialogClass;
    RelativeLayout relative;

    private PaginationAdapterDemo plannedCostumerAdapter;


    public static Dialog dialog;

    private DemoModal.CustomerInfo unplannedCustomerInfo;
    ArrayList<DemoModal.CustomerInfo> unplannedCustomerModalArrayList = new ArrayList<DemoModal.CustomerInfo>();
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private int TOTAL_PAGES = 5;
    private int currentpage_;
    private int lastpage_;
    private String start_visit_text = "present_date";



    ArrayList<CustomerModel.Customer> customerArrs = new ArrayList<>();
    private ArrayList<GetCategory> CostumerModalArrayList = new ArrayList<GetCategory>();
    private ArrayList<GetSize> getSizeModalArrayList = new ArrayList<GetSize>();

    private ArrayList<GetGroupTag> getGroupTagArrayList = new ArrayList<GetGroupTag>();

    private RelativeLayout select_category_rl;
    private RelativeLayout select_size_rl;
    private RelativeLayout select_thikness_rl;


    TextView category_tv, size_tv, select_thikness_tv;
    private LinearLayout search_btn;
    private RecyclerView recyclerView_Dailoge;
    private ArrayList<ProductModal> ProductModalArrayList;

    private JSONObject data_;
    private JSONArray data123;
    private Dialog dialog1;
    private ProductModal productModal;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coustumer_create_fragment);
        dialogClass = new DialogClass();
        dialog = new Dialog(this);

        initViews();
    }

    private void initViews() {
        GetBaseCategory();
        GetThickness();
        GetGroup();

        relative = findViewById(R.id.relative);
        tvTitle = findViewById(R.id.tvTitle);
        recyclerView = findViewById(R.id.recyclerView);
        linearBack = findViewById(R.id.linearBack);
        select_category_rl = findViewById(R.id.select_category_rl);
        select_size_rl = findViewById(R.id.select_size_rl);

        category_tv = findViewById(R.id.category_tv);
        size_tv = findViewById(R.id.size_tv);
        select_thikness_tv = findViewById(R.id.select_thikness_tv);
        select_thikness_rl = findViewById(R.id.select_thikness_rl);

        search_btn = findViewById(R.id.search_btn);


        tvNo = findViewById(R.id.tvNo);
        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //relative.setBackgroundColor(Color.parseColor("#e7e7e7"));
        tvTitle.setText("Select Product");

        utilities = new Utilities(this);
        dialogClass = new DialogClass();



        select_category_rl.setOnClickListener(view -> {
            dialog.setContentView(R.layout.select_costumer);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            LinearLayout okay_text = dialog.findViewById(R.id.closed);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            CostumerAdapter coustumer_adapter = new CostumerAdapter(this, CostumerModalArrayList, buttonListener);
            recyclerView.setAdapter(coustumer_adapter);

            okay_text.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            dialog.show();

        });

        select_size_rl.setOnClickListener(view -> {
            dialog.setContentView(R.layout.select_costumer);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            LinearLayout okay_text = dialog.findViewById(R.id.closed);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            GroupAdapter coustumer_adapter = new GroupAdapter(this, getSizeModalArrayList, buttonListener);
            recyclerView.setAdapter(coustumer_adapter);

            okay_text.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            dialog.show();

        });

        select_thikness_rl.setOnClickListener(view -> {
            dialog.setContentView(R.layout.select_costumer);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            LinearLayout okay_text = dialog.findViewById(R.id.closed);
            recyclerView_Dailoge = dialog.findViewById(R.id.recyclerView);
            recyclerView_Dailoge.setLayoutManager(new LinearLayoutManager(this));

            GroupTagAdapter groupTagAdapter = new GroupTagAdapter(getApplicationContext(), getGroupTagArrayList, buttonListener);
            recyclerView_Dailoge.setAdapter(groupTagAdapter);

            okay_text.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            dialog.show();

        });

        search_btn.setOnClickListener(view -> {
            GetCouAPI();
        });

    }

   /* private void getProducts() {

        dialog1 = dialogClass.progresesDialog(MasterProductActivity.this);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("base_category_id", "");
        queryParams.put("group_tag_id[]", "12");
        queryParams.put("current_page", "1");


        Log.e("queryParams>>", "" + queryParams);

        Log.e("accessToken>>", "" + StaticSharedpreference.getInfo("AccessToken", MasterProductActivity.this));
        ApiClient.getProducts(StaticSharedpreference.getInfo("AccessToken", MasterProductActivity.this), queryParams, new APIResultLitener<JsonObject>() {

            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                try {
                    Log.e("responsePlanned>>", "" + response.body());
                    //  String status = String.valueOf(response.body().get("status"));

                    String currentpage = String.valueOf(response.body().get("current_page"));
                    String lastpage = String.valueOf(response.body().get("last_page"));

                    //    String status1 = status.replace("\"", "");
                    String dataCheck = String.valueOf(response.body().get("data"));


                    dialog1.dismiss();
                    dialog.dismiss();

                    ProductModalArrayList = new ArrayList<ProductModal>();

                    if (dataCheck.equals("null")) {

                        dialog1.dismiss();
                        dialog.dismiss();

                        isLoading = false;
                        // travelPlanTv.setVisibility(View.VISIBLE);
                    } else {
                        dialog1.dismiss();
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                            Log.e("jsonObject>>", "" + jsonObject);
                            JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                            Log.e("jsonObject343>>", "" + jsonObject1);


                            Log.e("dataN>>", "" + jsonObject1);

                            for (int j = 0; j < jsonObject1.length(); j++) {
                                JSONObject data_state = jsonObject1.getJSONObject(j);
                                String productId = data_state.getString("id");
                                String product_name = data_state.getString("product_name");


                                String product_quantity = data_state.getString("product_quantity");
                                String product_value = data_state.getString("product_value");
                                String product_unit = data_state.getString("product_unit");
                                String product_code = data_state.getString("product_code");


                                ProductModal productLoistmodal = new ProductModal();
                                productLoistmodal.setProductId(productId);
                                productLoistmodal.setProductName(product_name);
                                productLoistmodal.setProductQuantity(product_quantity);
                                productLoistmodal.setProductValue(product_value);
                                productLoistmodal.setProductUnit(product_unit);
                                productLoistmodal.setProductCode(product_code);
                                ProductModalArrayList.add(productLoistmodal);
                            }


                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MasterProductActivity.this, LinearLayoutManager.VERTICAL, false);
                            PaginationAdapter plannedCostumerAdapter = new PaginationAdapter(MasterProductActivity.this, ProductModalArrayList);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(plannedCostumerAdapter);
                            plannedCostumerAdapter.notifyDataSetChanged();

                            recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                                @Override
                                protected void loadMoreItems() {
                                    isLoading = true;
                                    currentPage += 1;
                                    Log.e("page_counter>>>>", "" + currentPage);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ProductModalArrayList.clear();
                                            if (currentpage_ <= lastpage_) {
                                                getScheduledCustomer1(currentPage);
                                            }

                                        }
                                    }, 1000);
                                }

                                @Override
                                public int getTotalPageCount() {
                                    return TOTAL_PAGES;
                                }

                                @Override
                                public boolean isLastPage() {
                                    return isLastPage;
                                }

                                @Override
                                public boolean isLoading() {
                                    return isLoading;
                                }
                            });


                            //plannedCostumerAdapter.addAll(ProductModalArrayList);

                            currentpage_ = Integer.parseInt(currentpage);
                            lastpage_ = Integer.parseInt(lastpage);

                            if (currentpage_ <= lastpage_)
                                plannedCostumerAdapter.addLoadingFooter();
                            // else isLastPage = true;

                        } catch (JSONException e) {
                            dialog1.dismiss();
                            dialog.dismiss();

                            Log.e("error>>>", "" + e);
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    dialog.dismiss();
                    dialog1.dismiss();
                    e.printStackTrace();
                }

            }
        }, this);


    }*/


    private void GetBaseCategory() {
        Map<String, String> queryParams = new HashMap<String, String>();
        ApiClient.getCategory(StaticSharedpreference.getInfo("AccessToken", MasterProductActivity.this), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                Log.e("response123>>", "" + response);
                try {
                    String status = String.valueOf(response.body().get("status"));

                    Log.e("current_date>>", status);

                    String status1 = status.replace("\"", "");

                    if (status1.equals("success")) {

                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));

                            JSONArray data = jsonObject.getJSONArray("base_category");

                            for (int j = 0; j < data.length(); j++) {
                                JSONObject data_state = data.getJSONObject(j);
                                String name = data_state.getString("base_category_name");
                                String id = data_state.getString("id");

                                GetCategory category_modal = new GetCategory();
                                category_modal.setId(id);
                                category_modal.setName(name);
                                CostumerModalArrayList.add(category_modal);
                            }

                            Log.e("CostumerModalList>>>", "" + CostumerModalArrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, this);
    }

    private void GetGroup() {
        Map<String, String> queryParams = new HashMap<String, String>();

        ApiClient.getGroup(StaticSharedpreference.getInfo("AccessToken", MasterProductActivity.this), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                Log.e("responseSize>>", "" + response);
                try {
                    String status = String.valueOf(response.body().get("status"));

                    Log.e("current_date>>", status);

                    String status1 = status.replace("\"", "");

                    if (status1.equals("success")) {

                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));

                            JSONArray data = jsonObject.getJSONArray("group");

                            for (int j = 0; j < data.length(); j++) {
                                JSONObject data_state = data.getJSONObject(j);
                                String name = data_state.getString("group_name");
                                String id = data_state.getString("id");

                                GetSize getSize = new GetSize();
                                getSize.setId(id);
                                getSize.setName(name);
                                getSizeModalArrayList.add(getSize);
                            }

                            Log.e("CostumerModalList>>>", "" + getSizeModalArrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, this);
    }

    private void GetThickness() {
        Map<String, String> queryParams = new HashMap<String, String>();

      //  queryParams.put("group_id", groupId);
        ApiClient.getThickness(StaticSharedpreference.getInfo("AccessToken", MasterProductActivity.this), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                Log.e("responseSize>>", "" + response);
                try {
                    String status = String.valueOf(response.body().get("status"));

                    Log.e("status", status);

                    String status1 = status.replace("\"", "");

                    if (status1.equals("success")) {

                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));


                            JSONArray data = jsonObject.getJSONArray("group_tag");

                            Log.e("data>>>", "" + data);

                            for (int j = 0; j < data.length(); j++) {
                                JSONObject data_state = data.getJSONObject(j);
                                String name = data_state.getString("group_tag_name");
                                String id = data_state.getString("id");

                                GetGroupTag getGroupTag = new GetGroupTag();
                                getGroupTag.setId(id);
                                getGroupTag.setName(name);
                                getGroupTagArrayList.add(getGroupTag);
                            }

                           /* GroupTagAdapter groupTagAdapter = new GroupTagAdapter(getApplicationContext(), getGroupTagArrayList, buttonListener);
                            recyclerView_Dailoge.setAdapter(groupTagAdapter);*/

                            Log.e("CostumerModalList>>>", "" + getGroupTagArrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, this);
    }


    private String category_id_="", size_Idd="", thiknees_id_="";
    EmployeInterface buttonListener = new EmployeInterface() {

        @Override
        public void foo(String employee_idd, String employee_name) {

        }

        @Override
        public void foo1(String category_id, String state_name) {
            category_tv.setText("" + state_name);
            category_id_ = category_id;
            // GetDist(state_id);
            dialog.dismiss();
        }

        @Override
        public void foo2(String size_id, String size_name) {
            size_tv.setText("" + size_name);
            size_Idd = size_id;
            //

            /*StaticSharedpreference.getInfo("state_id", MasterProductActivity.this);*/
            dialog.dismiss();
        }

        @Override
        public void foo3(String thiknees_id, String costumer_name) {
            select_thikness_tv.setText("" + costumer_name);
            thiknees_id_ = thiknees_id;
            dialog.dismiss();

            Log.e("costumer_id>", thiknees_id);
        }

        @Override
        public void foo4(String city_id, String city_name) {

        }

        @Override
        public void foo5(String city_id, String city_name) {

        }
    };


    private void GetCouAPI() {

        unplannedCustomerModalArrayList.clear();

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("base_category_id", category_id_);
        queryParams.put("group_id", size_Idd);
        queryParams.put("group_tag_id[]", thiknees_id_);

        queryParams.put("current_page", "1");
        Log.e("queryParams>>", "" + queryParams);
        
        ApiClient.getProducts(StaticSharedpreference.getInfo("AccessToken", MasterProductActivity.this), queryParams, new APIResultLitener<JsonObject>() {

            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                try {
                    Log.e("responsePlanned>>", "" + response.body());
                    //String status = String.valueOf(response.body().get("status"));

                    String currentpage = String.valueOf(response.body().get("current_page"));
                    String lastpage = String.valueOf(response.body().get("last_page"));

                    String dataCheck = String.valueOf(response.body().get("data"));

                    Log.e("dataCheck>>", "" + dataCheck.length());

                    if (dataCheck.equals("")) {
                        isLoading = false;
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                            Log.e("jsonObject>>", "" + jsonObject);

                            JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                            Log.e("jsonObject343>>", "" + jsonObject1);

                            Log.e("dataN>>", "" + jsonObject1);

                            for (int j = 0; j < jsonObject1.length(); j++) {
                                JSONObject data_state = jsonObject1.getJSONObject(j);
                                String customerId = data_state.getString("id");
                                String product_name = data_state.getString("product_name");

                                String product_quantity = data_state.getString("product_quantity");
                                String product_value = data_state.getString("product_value");
                                String product_unit = data_state.getString("product_unit");
                                String product_code = data_state.getString("product_code");

                                unplannedCustomerInfo = new DemoModal.CustomerInfo();
                                unplannedCustomerInfo.setFirmName(product_name);
                                unplannedCustomerInfo.setId(customerId);
                                unplannedCustomerInfo.setProductQuanty(product_quantity);
                                unplannedCustomerInfo.setValue(product_value);
                                unplannedCustomerInfo.setUnit(product_unit);
                                unplannedCustomerInfo.setCode(product_code);

                                unplannedCustomerModalArrayList.add(unplannedCustomerInfo);
                            }

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MasterProductActivity.this, LinearLayoutManager.VERTICAL, false);
                            plannedCostumerAdapter = new PaginationAdapterDemo(MasterProductActivity.this, unplannedCustomerModalArrayList);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(plannedCostumerAdapter);
                            plannedCostumerAdapter.notifyDataSetChanged();

                            recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                                @Override
                                protected void loadMoreItems() {
                                    isLoading = true;
                                    currentPage += 1;
                                    Log.e("page_counter>>>>", "" + currentPage);
                                    Log.e("currentpage_>>>>", "" + currentpage_);
                                    Log.e("lastpage_>>>>", "" + lastpage_);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            unplannedCustomerModalArrayList.clear();
                                            if (currentpage_ <= lastpage_) {
                                                getProdcutsAgain(currentPage);
                                            }

                                        }
                                    }, 1000);
                                }

                                @Override
                                public int getTotalPageCount() {
                                    return TOTAL_PAGES;
                                }

                                @Override
                                public boolean isLastPage() {
                                    return isLastPage;
                                }

                                @Override
                                public boolean isLoading() {
                                    return isLoading;
                                }
                            });

                            plannedCostumerAdapter.addAll(unplannedCustomerModalArrayList);

                            currentpage_ = Integer.parseInt(currentpage);
                            lastpage_ = Integer.parseInt(lastpage);

                            Log.e("lastpage_>>", "" + lastpage_);
                            Log.e("currentpage_>>", "" + currentpage_);

                            if (currentpage_ <= lastpage_)
                                plannedCostumerAdapter.addLoadingFooter();
                            else isLastPage = true;

                        } catch (JSONException e) {
                            Log.e("error>>>", "" + e);
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, MasterProductActivity.this);
    }
    private void getProdcutsAgain(int currentPage) {
        unplannedCustomerModalArrayList.clear();
        Map<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("base_category_id", category_id_);
        queryParams.put("group_tag_id[]", thiknees_id_);
        queryParams.put("group_id", size_Idd);
        queryParams.put("current_page", "" + currentPage);

        Log.e("queryParams321>>", "" + queryParams);

        Log.e("accessToken>>", "" + StaticSharedpreference.getInfo("AccessToken", MasterProductActivity.this));
        ApiClient.getProducts(StaticSharedpreference.getInfo("AccessToken", MasterProductActivity.this), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                try {
                    Log.e("responsePlanned>>", "" + response.body());

                    String dataCheck = String.valueOf(response.body().get("data"));

                    Log.e("dataCheck>>", "" + dataCheck);

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        JSONArray data = jsonObject.getJSONArray("data");
                        Log.e("jsonObject222>>", "" + data);


                        for (int j = 0; j < data.length(); j++) {
                            JSONObject data_state = data.getJSONObject(j);
                            String customerId = data_state.getString("id");
                            String product_name = data_state.getString("product_name");

                            unplannedCustomerInfo = new DemoModal.CustomerInfo();
                            unplannedCustomerInfo.setFirmName(product_name);
                            unplannedCustomerModalArrayList.add(unplannedCustomerInfo);
                        }

                        plannedCostumerAdapter.removeLoadingFooter();
                        isLoading = false;

                        plannedCostumerAdapter.addAll(unplannedCustomerModalArrayList);
                        if (MasterProductActivity.this.currentPage != TOTAL_PAGES)
                            plannedCostumerAdapter.addLoadingFooter();
                        else isLastPage = true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, MasterProductActivity.this);

    }

}
