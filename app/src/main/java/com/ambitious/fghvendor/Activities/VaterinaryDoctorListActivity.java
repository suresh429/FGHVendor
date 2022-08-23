package com.ambitious.fghvendor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghvendor.Adapters.VaterinaryDoctorsApdapter;
import com.ambitious.fghvendor.Model.Vaterinary;
import com.ambitious.fghvendor.R;
import com.ambitious.fghvendor.Utils.AlertConnection;
import com.ambitious.fghvendor.Utils.AppConfig;
import com.ambitious.fghvendor.Utils.CustomSnakbar;
import com.ambitious.fghvendor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class VaterinaryDoctorListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Head, tv_Notavailable;
    private RecyclerView rv_Vaterinarydoctor;
    private LinearLayout ll_Login;
    private EditText et_Search;
    private String type = "";
    private RelativeLayout rl_Loader;
    private ArrayList<Vaterinary> vaterinaryArrayList;
    private VaterinaryDoctorsApdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaterinary_doctor_list);

        finds();

        tv_Head.setText(getIntent().getStringExtra("head"));

        type = "vaterinary";

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            getUsers(uid, type, iv_Bck);
        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (vaterinaryArrayList.size() > 0) {
                    filter(s.toString());
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Medical shop is not available.");
                }

            }
        });
    }

    private void filter(String text) {

        ArrayList<Vaterinary> temp = new ArrayList();
        for (Vaterinary d : vaterinaryArrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getUser_name().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getAddress().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Vaterinarydoctor.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adapter.updateList(temp);
        } else {
            rv_Vaterinarydoctor.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    private void getUsers(String uid, final String type, final ImageView view) {
        String latitude = Utility.getSharedPreferences(mContext, "latitude");
        String longitude = Utility.getSharedPreferences(mContext, "longitude");

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getUsers(type,latitude,longitude);
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
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            et_Search.setVisibility(View.VISIBLE);
                            rv_Vaterinarydoctor.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            vaterinaryArrayList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String name = result.optString("name");
                                String username = result.optString("username");
                                String user_image = result.optString("user_image");
                                String email = result.optString("email");
                                String address = result.optString("address");
                                String password = result.optString("password");
                                String mobile = result.optString("mobile");
                                String rating = result.optString("rating");
                                String user_type = result.optString("user_type");
                                String available = result.optString("available");
                                String active_status = result.optString("status");
                                String images = result.optString("images");

                                String img = "";

                                if (images.contains(",")) {

                                    String[] imgs = images.split(",");
                                    img = imgs[0];

                                } else {

                                    img = images;
                                }


                                Vaterinary vat = new Vaterinary();
                                vat.setId(user_id);
                                vat.setName(username);
                                vat.setUser_name(name);
                                vat.setAddress(address);
                                vat.setRating(rating);
                                vat.setImage(img);
                                vat.setAvailable(available);
                                vat.setObj(result.toString());

                                if (active_status.equalsIgnoreCase("1")) {
                                    vaterinaryArrayList.add(vat);
                                }

                            }

                            if (vaterinaryArrayList.size() > 0) {

                                adapter = new VaterinaryDoctorsApdapter(mContext, vaterinaryArrayList);
                                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                                manager.setOrientation(RecyclerView.VERTICAL);
                                rv_Vaterinarydoctor.setLayoutManager(manager);
                                rv_Vaterinarydoctor.setAdapter(adapter);

                            } else {
                                et_Search.setVisibility(View.GONE);
                                rv_Vaterinarydoctor.setVisibility(View.GONE);
                                tv_Notavailable.setVisibility(View.VISIBLE);
                            }


                        } else {
                            et_Search.setVisibility(View.GONE);
                            rv_Vaterinarydoctor.setVisibility(View.GONE);
                            tv_Notavailable.setVisibility(View.VISIBLE);
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Profile " + e.getMessage());
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

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Head = findViewById(R.id.tv_Head);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        rv_Vaterinarydoctor = findViewById(R.id.rv_Vaterinarydoctor);
        ll_Login = findViewById(R.id.ll_Login);
        et_Search = findViewById(R.id.et_Search);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);
        ll_Login.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.ll_Login:
                startActivity(new Intent(mContext, VaterinaryLoginActivity.class));
                Animatoo.animateCard(mContext);
                break;
        }
    }
}