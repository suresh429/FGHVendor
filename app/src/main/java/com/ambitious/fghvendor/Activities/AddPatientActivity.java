package com.ambitious.fghvendor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ambitious.fghvendor.R;
import com.ambitious.fghvendor.Utils.AlertConnection;
import com.ambitious.fghvendor.Utils.AppConfig;
import com.ambitious.fghvendor.Utils.CustomSnakbar;
import com.ambitious.fghvendor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddPatientActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_Bck;
    private Button btn_Add;
    private EditText et_name, et_Mobile, et_Purposevisit, et_Fees, et_Arialfees;
    private Context mContext = this;
    private String type = "";
    private RelativeLayout rl_Loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        finds();

        type = getIntent().getStringExtra("type");
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        btn_Add = findViewById(R.id.btn_Add);
        et_name = findViewById(R.id.et_name);
        et_Mobile = findViewById(R.id.et_Mobile);
        et_Purposevisit = findViewById(R.id.et_Purposevisit);
        et_Fees = findViewById(R.id.et_Fees);
        et_Arialfees = findViewById(R.id.et_Arialfees);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);
        btn_Add.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.btn_Add:
                if (Utility.isNetworkConnected(mContext)) {
                    validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

        }

    }

    private void validate(View v) {

        String parent_id = Utility.getSharedPreferences(mContext, "u_id");
        String name = et_name.getText().toString();
        String number = et_Mobile.getText().toString();
        String purpose = et_Purposevisit.getText().toString();
        String fee = et_Fees.getText().toString();
        String arialfee = et_Arialfees.getText().toString();
        String user_type = type + "patient";

        if (name.equalsIgnoreCase("")) {
            et_name.setError("Can't be Empty!");
            et_name.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can't be Empty!");
            et_Mobile.requestFocus();
        } else if (purpose.equalsIgnoreCase("")) {
            et_Purposevisit.setError("Can't be Empty!");
            et_Purposevisit.requestFocus();
        } else if (fee.equalsIgnoreCase("")) {
            et_Fees.setError("Can't be Empty!");
            et_Fees.requestFocus();
        } else if (arialfee.equalsIgnoreCase("")) {
            et_Arialfees.setError("Can't be Empty!");
            et_Arialfees.requestFocus();
        } else {

            requestToAdd(parent_id, name, number, purpose, fee, arialfee, user_type, v);

        }
    }

    private void requestToAdd(String parent_id, String name, String number, String purpose, String fee, String arialfee, String user_type, View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().addPatient(parent_id, name, number, purpose, fee, arialfee, user_type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("Addpatient" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Added Successfully.");

                            /*JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");*/

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onBackPressed();
                                }
                            }, 1500);

                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Addpatient " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}