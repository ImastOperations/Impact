package in.imast.impact.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import id.zelory.compressor.Compressor;
import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;

import in.imast.impact.R;
import in.imast.impact.activity.CustomerSelectActivity;
import in.imast.impact.activity.MainActivity;
import in.imast.impact.activity.MasterProductDetailsActivity;
import in.imast.impact.adapter.MasterSelectedProductAdapter;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import in.imast.impact.model.MasterProductReqModel;
import in.imast.impact.model.ProductSelectedModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class InvoicesFragment extends Fragment implements MasterSelectedProductAdapter.EventListener {
    Utilities utilities;
    DialogClass dialogClass;
    Dialog dialog;
    EditText edtSeller;
    LinearLayout linearSellerDetails;
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
    CardView cardPhotos;

    CardView imgAttachment;

    EditText edtInvoiceNumber;
    public static TextView tvTotalQty, tvTotalRate;
    RecyclerView rvProduct;

    public InvoicesFragment() {
    }

    MasterSelectedProductAdapter masterSelectedProductAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invoice, container, false);

        // Toast.makeText(getContext(), "123", Toast.LENGTH_SHORT).show();
        utilities = new Utilities(getActivity());
        dialogClass = new DialogClass();

        productId.clear();
        productSelectedModels.clear();
        initViews();

        // getProducts();
        return view;
    }

    private void initViews() {
        edtInvoiceNumber = view.findViewById(R.id.edtInvoiceNumber);
        tvTotalRate = view.findViewById(R.id.tvTotalRate);
        tvInvoiceDate = view.findViewById(R.id.tvInvoiceDate);
        tvTotalQty = view.findViewById(R.id.tvTotalQty);
        rvProduct = view.findViewById(R.id.rvProduct);
        relativeAddNew = view.findViewById(R.id.relativeAddNew);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvMobile = view.findViewById(R.id.tvMobile);
        relativeEmail = view.findViewById(R.id.relativeEmail);
        edtSeller = view.findViewById(R.id.edtSeller);
        linearSellerDetails = view.findViewById(R.id.linearSellerDetails);
        imgAttachment = view.findViewById(R.id.cardImageSelect);

        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);
        img4 = view.findViewById(R.id.img4);
        img5 = view.findViewById(R.id.img5);
        cardPhotos = view.findViewById(R.id.cardPhotos);

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

        imgAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},

                        1);*/

                // start multiple photos selector
               /* Intent intent = new Intent(getContext(), ImagesSelectorActivity.class);
// max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                startActivityForResult(intent, REQUEST_CODE);*/
            }
        });

        MainActivity.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtSeller.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter seller", Toast.LENGTH_SHORT).show();
                } else if (productSelectedModels.size() == 0) {
                    Toast.makeText(getActivity(), "Please select product", Toast.LENGTH_SHORT).show();
                } else if (tvInvoiceDate.getText().toString().equals("DD/MM/YYYY")) {
                    Toast.makeText(getActivity(), "Please select invoice date", Toast.LENGTH_SHORT).show();
                } else if (edtInvoiceNumber.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter invoice number", Toast.LENGTH_SHORT).show();
                } else {

                    attachmentArr.clear();
                    filesTemp.clear();
                    filesTemp.addAll(files);
                    siteMedia(true);
                }

            }
        });

    }


    ArrayList<String> attachmentArr = new ArrayList<>();
    public static ArrayList<File> filesTemp = new ArrayList<>();

    private void siteMedia(boolean isDialog) {

        if (!utilities.isOnline())
            return;

        if (isDialog)
            dialog = dialogClass.progresesDialog(getActivity());


        if (filesTemp.size() == 0) {
            siteDetails();
            return;
        }


        Map<String, String> queryParams = new HashMap<String, String>();

        File compressedImageFile = null;
        String fileName = "file";

        try {
            compressedImageFile = new Compressor(getActivity()).setQuality(50).compressToFile(filesTemp.get(0));

        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqBody = RequestBody.create(MediaType.parse("image/*"), compressedImageFile);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData(fileName, compressedImageFile.getName(), reqBody);

        ApiClient.siteMedia(StaticSharedpreference.getInfo("AccessToken", getActivity()).toString(), fileToUpload, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {

                if (response != null && errorMessage == null) {
                    if (response.code() == 200) {

                        filesTemp.remove(0);
                        attachmentArr.add(response.body().get("name").getAsString());
                        if (filesTemp.size() == 0)
                            siteDetails();
                        else
                            siteMedia(false);


                    } else {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    dialog.dismiss();

                }
            }
        });

    }


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
                                InvoicesFragment.productId.clear();
                                Toast.makeText(getContext(), "Invoice Submitted Successfully", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getActivity(), MainActivity.class)
                                        .putExtra("status", "invoice"));
                                getActivity().finishAffinity();
                                //  dialogClass.scanPointsDialog(ActivityNewEntry.this,totalPoints,arrEntryId.size());

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("akram", "pos " + requestCode);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                linearSellerDetails.setVisibility(View.VISIBLE);

                edtSeller.setText(data.getStringExtra("firmName"));
                tvMobile.setText(data.getStringExtra("mobile"));
                tvAddress.setText(data.getStringExtra("address"));

                sellerId = data.getStringExtra("sellerId");

                if (!data.getStringExtra("email").equals("null") &&
                        !data.getStringExtra("email").equals("")) {

                    relativeEmail.setVisibility(View.VISIBLE);
                    tvEmail.setText(data.getStringExtra("email"));
                }
            }
        } else if (requestCode == LAUNCH_MASTER_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {

                ProductSelectedModel productSelectedModel = new ProductSelectedModel();
                productSelectedModel.setName(data.getStringExtra("name"));
                productSelectedModel.setProductCode(data.getStringExtra("code"));
                productSelectedModel.setRate(data.getStringExtra("value"));
                productSelectedModel.setUnit(data.getStringExtra("unit"));
                productSelectedModel.setQty(data.getStringExtra("qty"));


                Log.v("akram", "action" + data.getStringExtra("action"));
                if (data.getStringExtra("action").equals("add")) {
                    productSelectedModel.setId(data.getStringExtra("id"));

                    productSelectedModels.add(productSelectedModel);
                    productId.add(data.getStringExtra("id") + "");
                } else {

                    Log.v("akram", "pos" + data.getIntExtra("pos", 0));
                    productSelectedModels.set(data.getIntExtra("pos", 0), productSelectedModel);

                }

                int rate = 0;
                int qty = 0;
                for (int i = 0; i < productSelectedModels.size(); i++) {


                    rate = rate + (Integer.parseInt(productSelectedModels.get(i).getQty()) *
                            Integer.parseInt("" + productSelectedModels.get(i).getRate()));

                    qty = qty + Integer.parseInt(productSelectedModels.get(i).getQty());

                }

                tvTotalQty.setText(qty + "");
                tvTotalRate.setText(rate + "");

                masterSelectedProductAdapter.notifyDataSetChanged();


            }
        } else if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                files.clear();
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.GONE);
                cardPhotos.setVisibility(View.GONE);
               // mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;

                StringBuffer sb = new StringBuffer();
                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");

                for (int i = 0; i < mResults.size(); i++) {

                    File file = new File(mResults.get(i));

                    files.add(file);
                    if (i == 0) {
                        cardPhotos.setVisibility(View.VISIBLE);
                        relativeImagesFirst.setVisibility(View.VISIBLE);
                        relativeImagesSecond.setVisibility(View.GONE);
                        Bitmap compressedImageBitmap = null;
                        try {
                            compressedImageBitmap = new Compressor(getActivity()).compressToBitmap(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressedImageBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        img1.setImageBitmap(compressedImageBitmap);
                        img1.setVisibility(View.VISIBLE);
                    } else if (i == 1) {
                        Bitmap compressedImageBitmap = null;
                        try {
                            compressedImageBitmap = new Compressor(getActivity()).compressToBitmap(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressedImageBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        img2.setImageBitmap(compressedImageBitmap);
                        img2.setVisibility(View.VISIBLE);
                    } else if (i == 2) {
                        Bitmap compressedImageBitmap = null;
                        try {
                            compressedImageBitmap = new Compressor(getActivity()).compressToBitmap(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressedImageBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        img3.setImageBitmap(compressedImageBitmap);
                        img3.setVisibility(View.VISIBLE);
                    } else if (i == 3) {
                        relativeImagesSecond.setVisibility(View.VISIBLE);
                        Bitmap compressedImageBitmap = null;
                        try {
                            compressedImageBitmap = new Compressor(getActivity()).compressToBitmap(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressedImageBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        img4.setImageBitmap(compressedImageBitmap);
                        img4.setVisibility(View.VISIBLE);
                    } else if (i == 4) {
                        Bitmap compressedImageBitmap = null;
                        try {
                            compressedImageBitmap = new Compressor(getActivity()).compressToBitmap(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressedImageBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                        img5.setImageBitmap(compressedImageBitmap);
                        img5.setVisibility(View.VISIBLE);
                    }


                }
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.v("akram", "reCode " + requestCode);
        switch (requestCode) {
            case 1: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    // start multiple photos selector
                  /*  Intent intent = new Intent(getContext(), ImagesSelectorActivity.class);
// max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
// min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
                    startActivityForResult(intent, REQUEST_CODE);*/

                } else {

                    // Permission Denied
                }

            }
        }
    }

    private void getProducts() {
        if (!utilities.isOnline())
            return;
        dialog = dialogClass.progresesDialog(getActivity());
        Map<String, String> queryParams = new HashMap<String, String>();
        ApiClient.getProducts(StaticSharedpreference.getInfo("AccessToken", getActivity()), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                if (response != null && errorMessage == null) {
                    if (response.code() == 200) {
                        dialog.dismiss();
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
        }, getContext());

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


}
