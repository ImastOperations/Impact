package in.imast.impact.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.imast.impact.R;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.LanguageHelper;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;


public class ChooseLanguageActivity extends AppCompatActivity {

    LinearLayout linearSubmit;
    Utilities utilities;
    DialogClass dialogClass;
    Dialog dialog;

    RelativeLayout relativeEnglish, relativeHindi, relativeTamil, relativeTelugu, relativeKannada,relativeMalayalam,relativeBangla;
    TextView tvEnglish, tvHindi, tvTamil, tvTelugu, tvKannada,tvMalayalam,tvBangla, tvHindi2, tvTamil2, tvTelugu2, tvKannada2,tvMalayalam2,tvBangla2;
    String selectedLanguage = "m-en";

    ImageView imgEnglish, imgHindi, imgTamil, imgTelugu, imgKannada,imgMalayalam,imgBangla;
    private String app_language;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_language);
        utilities = new Utilities(ChooseLanguageActivity.this);
        dialogClass = new DialogClass();

        imgEnglish = findViewById(R.id.imgEnglish);
        imgHindi = findViewById(R.id.imgHindi);
        imgTamil = findViewById(R.id.imgTamil);
        imgTelugu = findViewById(R.id.imgTelugu);
        imgKannada = findViewById(R.id.imgKannada);
        imgMalayalam = findViewById(R.id.imgMalayalam);
        imgBangla = findViewById(R.id.imgBangla);

        relativeEnglish = findViewById(R.id.relativeEnglish);
        relativeHindi = findViewById(R.id.relativeHindi);
        relativeTamil = findViewById(R.id.relativeTamil);
        relativeTelugu = findViewById(R.id.relativeTelugu);
        relativeKannada = findViewById(R.id.relativeKannada);
        relativeMalayalam = findViewById(R.id.relativeMalayalam);
        relativeBangla = findViewById(R.id.relativeBangla);
        linearSubmit = findViewById(R.id.linearSubmit);

        tvHindi2 = findViewById(R.id.tvHindi2);
        tvTelugu2 = findViewById(R.id.tvTelugu2);
        tvKannada2 = findViewById(R.id.tvKannada2);
        tvTamil2 = findViewById(R.id.tvTamil2);
        tvMalayalam2 = findViewById(R.id.tvMalayalam2);
        tvBangla2 = findViewById(R.id.tvBangla2);

        tvEnglish = findViewById(R.id.tvEnglish);
        tvHindi = findViewById(R.id.tvHindi);
        tvTamil = findViewById(R.id.tvTamil);
        tvTelugu = findViewById(R.id.tvTelugu);
        tvKannada = findViewById(R.id.tvKannada);
        tvMalayalam = findViewById(R.id.tvMalayalam);
        tvBangla = findViewById(R.id.tvBangla);

        if (StaticSharedpreference.getInfo("language", this).equals("m-en") ||
                StaticSharedpreference.getInfo("language", this).equals("")) {
            unselectedViews();
            selectedViews(relativeEnglish, tvEnglish, null, imgEnglish);

        } else if (StaticSharedpreference.getInfo("language", this).equals("m-hi")) {
            selectedLanguage = "hindi";
            unselectedViews();
            selectedViews(relativeHindi, tvHindi, tvHindi2, imgHindi);

        } else if (StaticSharedpreference.getInfo("language", this).equals("m-ta")) {
            selectedLanguage = "tamil";
            unselectedViews();
            selectedViews(relativeTamil, tvTamil, tvTamil2, imgTamil);

        } else if (StaticSharedpreference.getInfo("language", this).equals("m-te")) {
            selectedLanguage = "telugu";
            unselectedViews();
            selectedViews(relativeTelugu, tvTelugu, tvTelugu2, imgTelugu);

        } else if (StaticSharedpreference.getInfo("language", this).equals("m-kn")) {
            selectedLanguage = "kannada";
            unselectedViews();
            selectedViews(relativeKannada, tvKannada, tvKannada2, imgKannada);

        }
        else if (StaticSharedpreference.getInfo("language", this).equals("m-ml")) {
            selectedLanguage = "malayalam";
            unselectedViews();
            selectedViews(relativeMalayalam, tvMalayalam, tvMalayalam2, imgMalayalam);

        }

        else if (StaticSharedpreference.getInfo("language", this).equals("m-bn")) {
            selectedLanguage = "bangla";
            unselectedViews();
            selectedViews(relativeBangla, tvBangla, tvBangla2, imgBangla);

        }

        linearSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedLanguage.equals("english")) {
                    selectedLanguage = "m-en";
                    LanguageHelper.storeUserLanguage(ChooseLanguageActivity.this, "en");
                   LanguageHelper.updateLanguage(ChooseLanguageActivity.this, "en");
                } else if (selectedLanguage.equals("hindi")) {
                    selectedLanguage = "m-hi";
                   LanguageHelper.storeUserLanguage(ChooseLanguageActivity.this, "hi");
                   LanguageHelper.updateLanguage(ChooseLanguageActivity.this, "hi");
                } else if (selectedLanguage.equals("tamil")) {
                    selectedLanguage = "m-ta";
                   LanguageHelper.storeUserLanguage(ChooseLanguageActivity.this, "ta");
                   LanguageHelper.updateLanguage(ChooseLanguageActivity.this, "ta");
                } else if (selectedLanguage.equals("telugu")) {
                    selectedLanguage = "m-te";
                    LanguageHelper.storeUserLanguage(ChooseLanguageActivity.this, "te");
                    LanguageHelper.updateLanguage(ChooseLanguageActivity.this, "te");
                    //  LocaleHelper.setLocale(ChooseLanguageActivity.this, "m-te");
                } else if (selectedLanguage.equals("kannada")) {
                    selectedLanguage = "m-kn";
                    LanguageHelper.storeUserLanguage(ChooseLanguageActivity.this, "kn");
                    LanguageHelper.updateLanguage(ChooseLanguageActivity.this, "kn");
                    //LocaleHelper.setLocale(ChooseLanguageActivity.this, "m-kn");
                }

                else if (selectedLanguage.equals("malayalam")) {
                    selectedLanguage = "m-ml";
                    LanguageHelper.storeUserLanguage(ChooseLanguageActivity.this, "ml");
                    LanguageHelper.updateLanguage(ChooseLanguageActivity.this, "ml");
                    //LocaleHelper.setLocale(ChooseLanguageActivity.this, "m-kn");
                }

                else if (selectedLanguage.equals("bangla")) {
                    selectedLanguage = "m-bn";
                    LanguageHelper.storeUserLanguage(ChooseLanguageActivity.this, "bn");
                    LanguageHelper.updateLanguage(ChooseLanguageActivity.this, "bn");
                    //LocaleHelper.setLocale(ChooseLanguageActivity.this, "m-kn");
                }


                StaticSharedpreference.saveInfo("language", selectedLanguage, ChooseLanguageActivity.this);
                StaticSharedpreference.saveInfo("language_status", "selected", ChooseLanguageActivity.this);


                startActivity(new Intent(ChooseLanguageActivity.this, MainActivity.class)
                        .putExtra("fromLanguage", "yes")
                        .putExtra("status",""));

            }
        });


        relativeEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLanguage = "english";
                unselectedViews();
                selectedViews(relativeEnglish, tvEnglish, null, imgEnglish);

            }
        });

        relativeHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLanguage = "hindi";
                unselectedViews();
                selectedViews(relativeHindi, tvHindi, tvHindi2, imgHindi);


                Log.v("akram", "hini");

                // ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });

        relativeTamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectedViews();
                selectedViews(relativeTamil, tvTamil, tvTamil2, imgTamil);

                selectedLanguage = "tamil";

                // ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });

        relativeTelugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectedViews();
                selectedViews(relativeTelugu, tvTelugu, tvTelugu2, imgTelugu);

                selectedLanguage = "telugu";

                // ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });

        relativeKannada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectedViews();
                selectedViews(relativeKannada, tvKannada, tvKannada2, imgKannada);

                selectedLanguage = "kannada";

                // ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });

        relativeMalayalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectedViews();
                selectedViews(relativeMalayalam, tvMalayalam, tvMalayalam2, imgMalayalam);

                selectedLanguage = "malayalam";

                // ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });

        relativeBangla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectedViews();
                selectedViews(relativeBangla, tvBangla, tvBangla2, imgBangla);

                selectedLanguage = "bangla";

                // ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });

    }



    private void unselectedViews() {

        relativeEnglish.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));
        relativeTamil.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));
        relativeHindi.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));
        relativeTelugu.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));
        relativeKannada.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));
        relativeMalayalam.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));
        relativeBangla.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));

        tvHindi.setTextColor(Color.parseColor("#000000"));
        tvHindi2.setTextColor(Color.parseColor("#000000"));
        tvEnglish.setTextColor(Color.parseColor("#000000"));
        tvTamil2.setTextColor(Color.parseColor("#000000"));
        tvTelugu2.setTextColor(Color.parseColor("#000000"));
        tvKannada2.setTextColor(Color.parseColor("#000000"));
        tvMalayalam.setTextColor(Color.parseColor("#000000"));
        tvMalayalam2.setTextColor(Color.parseColor("#000000"));
        tvBangla.setTextColor(Color.parseColor("#000000"));
        tvBangla2.setTextColor(Color.parseColor("#000000"));

        imgTamil.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
        imgHindi.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
        imgEnglish.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
        imgTelugu.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
        imgKannada.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
        imgMalayalam.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
        imgBangla.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
    }

    private void selectedViews(RelativeLayout relative, TextView tv1, TextView tv2, ImageView img) {

        relative.setBackground(getResources().getDrawable(R.drawable.rectangle_select));

        tv1.setTextColor(Color.parseColor("#813D3C"));
        if (tv2 != null)
            tv2.setTextColor(Color.parseColor("#813D3C"));

        img.setBackground(getResources().getDrawable(R.drawable.ic_radio_button));

    }

}

