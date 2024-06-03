package in.imast.impact.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.imast.impact.R;


public class MasterProductDetailsActivity extends AppCompatActivity {

    LinearLayout linearBack;
    TextView tvTitle, tvSave, tvItem, tvProductCode, tvUnit,edtRate;
    EditText tvQuantity;
    int LAUNCH_SECOND_ACTIVITY = 1;
    String from = "";

    String id = "";
    int pos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initViews();
    }

    private void initViews() {

        tvTitle = findViewById(R.id.tvTitle);
        tvSave = findViewById(R.id.tvSave);
        tvItem = findViewById(R.id.tvItem);
        tvProductCode = findViewById(R.id.tvProductCode);
        edtRate = findViewById(R.id.edtRate);
        tvUnit = findViewById(R.id.tvUnit);
        tvQuantity = findViewById(R.id.tvQuantity);
        linearBack = findViewById(R.id.linearBack);

        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        from = getIntent().getStringExtra("from");
        if (getIntent().getStringExtra("action").equals("add")) {
            Intent i = new Intent(this, MasterProductActivity.class);
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        } else {
            tvItem.setText(getIntent().getStringExtra("name"));
            tvProductCode.setText(getIntent().getStringExtra("code"));
            edtRate.setText(getIntent().getStringExtra("value"));
            tvQuantity.setText(getIntent().getStringExtra("qty"));
            tvUnit.setText(getIntent().getStringExtra("unit"));

            if(from.equals("order")){
                edtRate.setFocusable(false);
            }

            pos = getIntent().getIntExtra("pos", 0);
        }

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (edtRate.getText().toString().equals("")) {
                    Toast.makeText(MasterProductDetailsActivity.this, "Please enter Rate", Toast.LENGTH_SHORT).show();
                } else*/
                if (tvQuantity.getText().toString().equals("")) {
                    Toast.makeText(MasterProductDetailsActivity.this, "Please enter Quantity", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("name", tvItem.getText().toString());
                    intent.putExtra("code", tvProductCode.getText().toString());
                    intent.putExtra("value", edtRate.getText().toString());
                    intent.putExtra("unit", tvUnit.getText().toString() + "");
                    intent.putExtra("qty", tvQuantity.getText().toString() + "");
                    Log.v("akram","pos "+getIntent().getStringExtra("action"));
                    if (getIntent().getStringExtra("action").equals("edit")) {
                        intent.putExtra("pos", pos);
                        intent.putExtra("action", "edit");

                    } else {
                        intent.putExtra("id", id);
                        intent.putExtra("action", "add");

                    }
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                tvItem.setText(data.getStringExtra("name"));
                tvQuantity.setText(data.getStringExtra("qty"));
                tvProductCode.setText(data.getStringExtra("code"));
                tvUnit.setText(data.getStringExtra("unit"));
                id = data.getStringExtra("id");
                String value = data.getStringExtra("value");
                edtRate.setText(value);

                if (from.equals("order")){
                    edtRate.setText(data.getStringExtra("value"));
                    edtRate.setFocusable(false);
                }
            }
        }
    }


}
