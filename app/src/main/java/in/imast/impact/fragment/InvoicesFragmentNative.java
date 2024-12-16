package in.imast.impact.fragment;

import static android.app.Activity.RESULT_OK;
import static in.imast.impact.helper.Utilities.INTENTCAMERA;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.Connection.InvoiceInterface;
import in.imast.impact.R;
import in.imast.impact.activity.CameraActivity;
import in.imast.impact.activity.CustomerSelectActivity;
import in.imast.impact.activity.DataManager;
import in.imast.impact.activity.MainActivity;
import in.imast.impact.activity.MasterProductDetailsActivity;
import in.imast.impact.adapter.AsmAdapter;
import in.imast.impact.adapter.DistributerAdapter;
import in.imast.impact.adapter.MasterSelectedProductAdapter;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import in.imast.impact.model.AreaSalesModal;
import in.imast.impact.model.DistributerModal;
import in.imast.impact.model.MasterProductReqModel;
import in.imast.impact.model.ProductSelectedModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class InvoicesFragmentNative extends Fragment implements MasterSelectedProductAdapter.EventListener {
    Utilities utilities;
    DialogClass dialogClass;
    Dialog dialog;
    EditText edtSeller;

    private static final int camera = 1, gallery = 2;

    LinearLayout linearSellerDetails;

    private static final int MY_PERMISSION_CONSTANT = 5;
    private static final int SELECT_FILE = 2;
    private static final int SELECT_FILE1 = 3;
    private static final int PICK_PDF_REQUEST = 1011;

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    View view;
    RelativeLayout relativeEmail;
    int LAUNCH_SECOND_ACTIVITY = 1;
    int LAUNCH_MASTER_ACTIVITY = 2;
    TextView tvAddress, tvEmail, tvMobile, tvInvoiceDate;
    public static ArrayList<File> files = new ArrayList<>();
    public static ArrayList<String> productId = new ArrayList<>();
    RelativeLayout relativeAddNew;
    ArrayList<ProductSelectedModel> productSelectedModels = new ArrayList<>();

    RelativeLayout relativeImagesSecond, relativeImagesFirst;

    ImageView img1, img2, img3, img4, img5;

    CardView imgAttachment;

    EditText edtInvoiceNumber;
    public static TextView tvTotalQty, tvTotalRate, tvPath;
    RecyclerView rvProduct;
    private String document_image_path = "";
    private Bitmap aadharBitmap;
    private ImageView image_view;
    private CardView cardSubmit;
    private ArrayList<DistributerModal> DistributerModalArrayList = new ArrayList<>();

    private ArrayList<AreaSalesModal> AareaSalesModaalAraaylist  = new ArrayList<>();

    private DistributerModal distributer_modal;

    private AreaSalesModal areaSalesModal;

    private TextView edtdistributer, remove_tv;
    private DistributerAdapter coustumer_adapter;

    private AsmAdapter asmAdapter;

    private AreaSalesModal areasalaes_modal;
    private TextView edtSm;
    private RequestBody invoice_date;
    private RequestBody edtInvoiceNumber__;
    private RequestBody distributer_iddd__;
    private RequestBody asm_iddd__;
    private MultipartBody.Part attachment;
    private String mediaText = "";


    public InvoicesFragmentNative() {

    }

    MasterSelectedProductAdapter masterSelectedProductAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.invoice_fragment_naative, container, false);

        // Toast.makeText(getContext(), "123", Toast.LENGTH_SHORT).show();
        utilities = new Utilities(getActivity());
        dialogClass = new DialogClass();

        productId.clear();
        productSelectedModels.clear();
        initViews();

        if (Utilities.isNetworkAvailable(getContext())) {
            getdistributer();
            getareasslesmanager();
        }
        else
        {
            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private String distributer_iddd = "";
    private String asm_iddd = "";

    InvoiceInterface buttonListener1 = new InvoiceInterface() {

        @Override
        public void foo(String distributer_idd, String distributer_name) {

            edtdistributer.setText("" + distributer_name);
            distributer_iddd = distributer_idd;
            dialog.dismiss();
        }

        @Override
        public void foo1(String asm_id, String asm_name) {
            edtSm.setText("" + asm_name);
            asm_iddd = asm_id;
            dialog.dismiss();
        }

        @Override
        public void foo2(String dist_id, String dist_name) {
            dialog.dismiss();
        }
    };

    private void getareasslesmanager() {
        Map<String, String> queryParams = new HashMap<String, String>();
        ApiClient.get_area_sales(StaticSharedpreference.getInfo("AccessToken", getActivity()), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                //Log.e("response123>>", "" + response);
                try {
                    if (response.code() != 401) {
                        try {
                            String status = String.valueOf(response.body().get("status"));

                            //Log.e("current_date>>", status);

                            String status1 = status.replace("\"", "");

                            if (status1.equals("success")) {

                                AareaSalesModaalAraaylist.clear();

                                try {
                                    JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));

                                    JSONArray data = jsonObject.getJSONArray("data");

                                    for (int j = 0; j < data.length(); j++) {
                                        JSONObject data_state = data.getJSONObject(j);
                                        String name = data_state.getString("asm_name");
                                        String id = data_state.getString("asm_id");

                                        areasalaes_modal = new AreaSalesModal();
                                        areasalaes_modal.setId(id);
                                        areasalaes_modal.setName(name);
                                        AareaSalesModaalAraaylist.add(areasalaes_modal);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, getActivity());
    }

    private void getdistributer() {
        Map<String, String> queryParams = new HashMap<String, String>();
        ApiClient.get_distributer(StaticSharedpreference.getInfo("AccessToken", getActivity()), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                Log.e("response123>>", "" + response);
                try {
                    if (response.code() != 401) {
                        try {
                            String status = String.valueOf(response.body().get("status"));

                            //Log.e("current_date>>", status);

                            String status1 = status.replace("\"", "");

                            if (status1.equals("success")) {

                                DistributerModalArrayList.clear();

                                try {
                                    JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));

                                    JSONArray data = jsonObject.getJSONArray("data");

                                    for (int j = 0; j < data.length(); j++) {
                                        JSONObject data_state = data.getJSONObject(j);
                                        String name = data_state.getString("distributor_name");
                                        String id = data_state.getString("distributor_id");

                                        distributer_modal = new DistributerModal();
                                        distributer_modal.setId(id);
                                        distributer_modal.setName(name);
                                        DistributerModalArrayList.add(distributer_modal);
                                    }

                                    //Log.e("CostumerModalList>>>", "" + CostumerModalArrayList);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, getActivity());

    }


    private void initViews() {
        edtInvoiceNumber = view.findViewById(R.id.edtInvoiceNumber);
        tvTotalRate = view.findViewById(R.id.tvTotalRate);
        tvInvoiceDate = view.findViewById(R.id.tvInvoiceDate);
        tvTotalQty = view.findViewById(R.id.tvTotalQty);
        rvProduct = view.findViewById(R.id.rvProduct);
        relativeAddNew = view.findViewById(R.id.relativeAddNew);
        cardSubmit = view.findViewById(R.id.cardSubmit);
        edtdistributer = view.findViewById(R.id.edtdistributer);
        remove_tv = view.findViewById(R.id.remove_tv);
        tvPath = view.findViewById(R.id.tvPath);

        edtSm = view.findViewById(R.id.edtSm);

        remove_tv.setOnClickListener(v -> {

            image_view.setImageBitmap(null);
            remove_tv.setVisibility(View.GONE);
            tvPath.setVisibility(View.GONE);
            tvPath.setText("");

            Glide.with(getContext())
                    .load(R.drawable.ic_attachment)
                    .into(image_view);

            document_image_path = "";
        });


        edtdistributer.setOnClickListener(view1 -> {
            if (Utilities.isNetworkAvailable(getContext())) {

                    if (!DistributerModalArrayList.isEmpty()) {
                        distributerDialog();
                    } else {

                       // Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                        if (Utilities.isNetworkAvailable(getContext())) {
                            getdistributer();
                        }
                    }


            }
            else {
                Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
            }

        });

        edtSm.setOnClickListener(view1 -> {

            if (Utilities.isNetworkAvailable(getContext())) {

                if (!AareaSalesModaalAraaylist.isEmpty()) {
                    asmDialog();
                } else {

                    //Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                    if (Utilities.isNetworkAvailable(getContext())) {
                        getareasslesmanager();
                    }
                }
            }
            else {
                Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
            }
        });

        tvAddress = view.findViewById(R.id.tvAddress);
        image_view = view.findViewById(R.id.image_view);

        tvEmail = view.findViewById(R.id.tvEmail);
        tvMobile = view.findViewById(R.id.tvMobile);
        relativeEmail = view.findViewById(R.id.relativeEmail);
        edtSeller = view.findViewById(R.id.edtSeller);
        linearSellerDetails = view.findViewById(R.id.linearSellerDetails);
        imgAttachment = view.findViewById(R.id.cardImageSelect);


        imgAttachment.setOnClickListener(view1 -> {

            gotoAddProfileImage(camera, gallery);

        });

        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);
        img4 = view.findViewById(R.id.img4);
        img5 = view.findViewById(R.id.img5);

        relativeImagesFirst = view.findViewById(R.id.relativeImagesFirst);
        relativeImagesSecond = view.findViewById(R.id.relativeImagesSecond);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProduct.setLayoutManager(linearLayoutManager);
        masterSelectedProductAdapter = new MasterSelectedProductAdapter(getContext(), productSelectedModels, this);
        rvProduct.setAdapter(masterSelectedProductAdapter);

        rvProduct.setNestedScrollingEnabled(false);
        edtSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), CustomerSelectActivity.class);
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
            }
        });

        relativeAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MasterProductDetailsActivity.class);
                intent.putExtra("action", "add");
                intent.putExtra("from", "invoice");
                startActivityForResult(intent, LAUNCH_MASTER_ACTIVITY);
            }
        });

        tvInvoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.datepicker(getContext(), tvInvoiceDate);
            }
        });

        cardSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInvoiceDate.getText().toString().equals("DD/MM/YYYY")) {
                    Toast.makeText(getActivity(), "Please select invoice date", Toast.LENGTH_SHORT).show();
                } else if (edtInvoiceNumber.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter invoice number", Toast.LENGTH_SHORT).show();
                } else if (distributer_iddd.equals("")) {
                    Toast.makeText(getActivity(), "Please select distributer", Toast.LENGTH_SHORT).show();
                } else if (asm_iddd.equals("")) {
                    Toast.makeText(getActivity(), "Please select area sales manager", Toast.LENGTH_SHORT).show();
                } else if (document_image_path.equals("")) {
                    Toast.makeText(getActivity(), "Please select invoice image", Toast.LENGTH_SHORT).show();
                } else if (edtInvoiceNumber.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter invoice number", Toast.LENGTH_SHORT).show();
                } else {
                    InvoiceSubmit();
                    // siteMedia(true);
                }
            }
        });
    }

    private void distributerDialog() {
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.select_costumer);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        LinearLayout okay_text = dialog.findViewById(R.id.closed);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        coustumer_adapter = new DistributerAdapter(getActivity(), DistributerModalArrayList, buttonListener1);
        recyclerView.setAdapter(coustumer_adapter);

        okay_text.setOnClickListener(v1 -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void asmDialog()
    {
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.select_costumer);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        LinearLayout okay_text = dialog.findViewById(R.id.closed);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        asmAdapter = new AsmAdapter(getActivity(), AareaSalesModaalAraaylist, buttonListener1);
        recyclerView.setAdapter(asmAdapter);

        okay_text.setOnClickListener(v1 -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void InvoiceSubmit() {
        String tvInvoiceDate_ = tvInvoiceDate.getText().toString();
        String edtInvoiceNumber_ = edtInvoiceNumber.getText().toString();

        if (!utilities.isOnline())
            return;

        dialog = dialogClass.progresesDialog(getActivity());
        invoice_date = RequestBody.create(MediaType.parse("text/plain"), tvInvoiceDate_);
        edtInvoiceNumber__ = RequestBody.create(MediaType.parse("text/plain"), edtInvoiceNumber_);
        distributer_iddd__ = RequestBody.create(MediaType.parse("text/plain"), distributer_iddd);
        asm_iddd__ = RequestBody.create(MediaType.parse("text/plain"), asm_iddd);


        Log.v("test : ", "dis" + distributer_iddd + "asm" + asm_iddd);
        Log.v("test : ", "dis" + tvInvoiceDate_);

        File file = new File(document_image_path);

        File compressedImageFile = null;

        if (mediaText.equalsIgnoreCase("image")) {
            try {
                compressedImageFile = new Compressor(getContext()).setQuality(50).compressToFile(file);
                if (!document_image_path.equalsIgnoreCase("")) {
                    RequestBody reqBody = RequestBody.create(MediaType.parse("image/*"), compressedImageFile);
                    attachment = MultipartBody.Part.createFormData("invoice_image", compressedImageFile.getName(), reqBody);

                } else {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            Uri fileUri = Uri.parse(document_image_path);
            File file1 = getFileFromUri(fileUri);

            String fileName = file1.getName();
            String cleanedFileName = removeAfterPdf(fileName);


            RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), file1);
            attachment = MultipartBody.Part.createFormData("invoice_image", cleanedFileName, requestBody);
        }

        Log.v("test : ", "attachment " + attachment);

        ApiClient.add_invoice(StaticSharedpreference.getInfo("AccessToken", getActivity()), distributer_iddd__, asm_iddd__, edtInvoiceNumber__, invoice_date
                , attachment, new APIResultLitener<JsonObject>() {
                    @Override
                    public void onAPIResult(Response<JsonObject> response, String errorMessage) {

                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                mediaText = "";
                                String status = String.valueOf(response.body().get("status")).replace("\"", "");

                                if (status.equals("success")) {
                                    dialog.dismiss();
                                    DataClear();
                                    Toast.makeText(getContext(), "Successfully Submitted", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("error")) {
                                    JsonObject data = response.body().getAsJsonObject("response");
                                    JsonArray invoiceNumberErrors = data.getAsJsonArray("invoice_number");
                                    String invoiceMessage = invoiceNumberErrors.get(0).getAsString(); // Get the first error message
                                    Toast.makeText(getContext(), invoiceMessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } else {
                                Log.e("API Error", "Response unsuccessful or body is null. Error message: " + errorMessage);
                                Toast.makeText(getContext(), "Submission failed. Please try again.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, getActivity());
    }

    private void DataClear() {
        tvInvoiceDate.setText("DD/MM/YYYY");
        edtInvoiceNumber.setText("");
        edtdistributer.setText("");
        edtSm.setText("");
        tvPath.setText("");
        tvPath.setVisibility(View.GONE);
        remove_tv.setVisibility(View.GONE);
        Glide.with(getContext())
                .load(R.drawable.ic_attachment)
                .into(image_view);

        document_image_path = "";

    }

    private void gotoAddProfileImage(final int camera, final int gallery) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection1);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        LinearLayout layoutPdf = (LinearLayout) dialog.findViewById(R.id.layoutPdf);

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaText = "image";
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });

        layoutGallary.setOnClickListener(view -> {
            mediaText = "image";
            dialog.dismiss();
            dialog.cancel();
            getPhotoFromGallary();
        });

        layoutPdf.setOnClickListener(view -> {
            mediaText = "pdf";
            dialog.dismiss();
            dialog.cancel();
            getPDFFile();
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    private void openCamera() {
        Intent intent = new Intent(getContext(), CameraActivity.class);
        intent.putExtra("camera", "1");
        startActivityForResult(intent, INTENTCAMERA);
    }

    ArrayList<String> attachmentArr = new ArrayList<>();
    public static ArrayList<File> filesTemp = new ArrayList<>();


    private void siteDetails() {
        if (!utilities.isOnline())
            return;

        MasterProductReqModel masterProductReqModel = new MasterProductReqModel();
        masterProductReqModel.setInvoice_buyer_id(StaticSharedpreference.getInfo("UserId", getActivity()));
        masterProductReqModel.setInvoice_seller_id(sellerId);
        masterProductReqModel.setInvoice_status("1");

        int qty = 0;
        int rate = 0;
        ArrayList<String> productId = new ArrayList<>();
        ArrayList<String> productQty = new ArrayList<>();
        ArrayList<String> productAmount = new ArrayList<>();
        ArrayList<String> productSubTotal = new ArrayList<>();
        for (int i = 0; i < productSelectedModels.size(); i++) {


            productId.add(productSelectedModels.get(i).getId());
            productQty.add(productSelectedModels.get(i).getQty() + "");
            productAmount.add(productSelectedModels.get(i).getRate());
            productSubTotal.add((Integer.parseInt(productSelectedModels.get(i).getQty()) *
                    Integer.parseInt(productSelectedModels.get(i).getRate())) + "");

            rate = rate + (Integer.parseInt(productSelectedModels.get(i).getQty()) *
                    Integer.parseInt(productSelectedModels.get(i).getRate()));

            qty = qty + Integer.parseInt(productSelectedModels.get(i).getQty());
        }

        masterProductReqModel.setProduct_id(productId);
        masterProductReqModel.setProduct_qty(productQty);
        masterProductReqModel.setProduct_amount(productAmount);
        masterProductReqModel.setProduct_subtotal(productSubTotal);


        String date = "";
        try {
            date = tvInvoiceDate.getText().toString();
            SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
            Date newDate = spf.parse(date);
            spf = new SimpleDateFormat("yyyy-MM-dd");
            date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        masterProductReqModel.setInvoice_date(date + "");
        masterProductReqModel.setInvoice_number(edtInvoiceNumber.getText().toString() + "");
        masterProductReqModel.setInvoice_value(rate + "");
        masterProductReqModel.setInvoice_quantity(qty + "");

        if (attachmentArr.size() > 0)
            masterProductReqModel.setImage(attachmentArr);

        ApiClient.saveInvoice(StaticSharedpreference.getInfo("AccessToken", getActivity())
                , masterProductReqModel, new APIResultLitener<JsonObject>() {
                    @Override
                    public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                        Log.e("response>>", "" + response);
                        if (response != null && errorMessage == null) {
                            if (response.code() == 200) {
                                dialog.dismiss();
                                InvoicesFragmentNative.productId.clear();
                                Toast.makeText(getContext(), "Invoice Submitted Successfully", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getActivity(), MainActivity.class)
                                        .putExtra("status", "invoice"));
                                getActivity().finishAffinity();

                            } else {
                                dialog.dismiss();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());

                                    dialogClass.alertDialog(jsonObject.getString("status"), jsonObject.getString("message")
                                            , getActivity(), false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }, getActivity());
    }


    String sellerId = "";
    private ArrayList<String> mResults = new ArrayList<>();
    private static final int REQUEST_CODE = 123;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                document_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file1 = new File(document_image_path);
                aadharBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                image_view.setImageBitmap(aadharBitmap);

                if (aadharBitmap != null) {
                    remove_tv.setVisibility(View.VISIBLE);
                } else {
                    remove_tv.setVisibility(View.GONE);
                }


            } else if (requestCode == INTENTCAMERA) {
                if (resultCode == Activity.RESULT_OK) {

                    document_image_path = data.getSerializableExtra("image").toString();
                    File file1 = new File(document_image_path);
                    aadharBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                    image_view.setImageBitmap(aadharBitmap);

                    if (aadharBitmap != null) {
                        remove_tv.setVisibility(View.VISIBLE);
                    } else {
                        remove_tv.setVisibility(View.GONE);
                    }
                }
            } else if (requestCode == PICK_PDF_REQUEST) {
                document_image_path = data.getData().toString();
                Uri uri = data.getData();
                File file1 = new File(document_image_path);

                if (uri != null) {
                    try {
                        long fileSize = getFileSize(uri);
                        if (fileSize > MAX_FILE_SIZE) {
                            Toast.makeText(getContext(), "File size exceeds 2 MB", Toast.LENGTH_LONG).show();
                        } else {
                            if (file1.exists() || !file1.getName().equalsIgnoreCase("")) {
                                remove_tv.setVisibility(View.VISIBLE);
                                tvPath.setVisibility(View.VISIBLE);

                                tvPath.setText(file1.getName());
                            } else {
                                remove_tv.setVisibility(View.GONE);
                                tvPath.setVisibility(View.GONE);
                            }

                            Log.v("akshay ", "test :" + file1.getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onEvent(int pos) {
        Intent intent = new Intent(getContext(), MasterProductDetailsActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("from", "invoice");
        intent.putExtra("name", "" + productSelectedModels.get(pos).getName());
        intent.putExtra("code", "" + productSelectedModels.get(pos).getProductCode());
        intent.putExtra("pos", pos);
        intent.putExtra("unit", "" + productSelectedModels.get(pos).getUnit());
        intent.putExtra("qty", "" + productSelectedModels.get(pos).getQty());
        intent.putExtra("rate", "" + productSelectedModels.get(pos).getRate());
        startActivityForResult(intent, 2);
    }

    private void getPDFFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }


    private File getFileFromUri(Uri uri) {
        File tempFile = null;
        try {

            String fileName = getFileNameFromUri(uri);
            tempFile = File.createTempFile(fileName, null, getContext().getCacheDir());


            try (InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
            cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                return cursor.getString(nameIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "tempfile"; // Fallback if name cannot be determined
    }


    private long getFileSize(Uri uri) throws IOException {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            long size = cursor.getLong(sizeIndex);
            cursor.close();
            return size;
        }
        return 0;
    }

    public String removeAfterPdf(String fileName) {
        StringBuffer sb = new StringBuffer(fileName);
        int pdfIndex = sb.indexOf(".pdf");
        if (pdfIndex != -1) {
            sb.setLength(pdfIndex + 4); // 4 is the length of ".pdf"
        }
        return sb.toString();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkPermission()
    {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if (!hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermission();
    }
}
