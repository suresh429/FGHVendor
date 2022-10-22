package com.ambitious.fghvendor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghvendor.Adapters.PaymentHistoryAdapter;
import com.ambitious.fghvendor.Model.HistoryModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PaymentHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext = this;
    private ImageView iv_Bck;
    private RelativeLayout rl_Loader;
    private TextView tv_Notavailable,txtTotal,txtTotalTransactions;
    EditText etFromDate,etToDate;
    private RecyclerView recyclerList;
    private ArrayList<HistoryModel> historyModelArrayList = new ArrayList<>();
    String uid;

    private DatePickerDialog.OnDateSetListener fromDate,toDate;
    private Calendar fromCalendar = Calendar.getInstance();
    private Calendar toCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        finds();

        if (Utility.isNetworkConnected(mContext)) {
            uid = Utility.getSharedPreferences(mContext, "u_id");
            getHistory(uid);

        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        fromDate = (view, year, monthOfYear, dayOfMonth) -> {
            fromCalendar.set(Calendar.YEAR, year);
            fromCalendar.set(Calendar.MONTH, monthOfYear);
            fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            etFromDate.setText(sdf.format(fromCalendar.getTime()));
            String dateString = sdf.format(fromCalendar.getTime());
        };

        toDate = (view, year, monthOfYear, dayOfMonth) -> {
            fromCalendar.set(Calendar.YEAR, year);
            fromCalendar.set(Calendar.MONTH, monthOfYear);
            fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            etToDate.setText(sdf.format(fromCalendar.getTime()));
            String dateString = sdf.format(fromCalendar.getTime());

            getHistory(uid);
        };




    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        rl_Loader = findViewById(R.id.rl_Loader);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        recyclerList = findViewById(R.id.recyclerList);

        etFromDate = findViewById(R.id.etFromDate);
        etToDate = findViewById(R.id.etToDate);
        txtTotal = findViewById(R.id.txtTotal);
        txtTotalTransactions = findViewById(R.id.txtTotalTransactions);

        iv_Bck.setOnClickListener(this);
        etFromDate.setOnClickListener(this);
        etToDate.setOnClickListener(this);




    }

    private void getHistory(String uid) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().vendorTxn(uid,etFromDate.getText().toString(),etToDate.getText().toString());
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
                        String total_amount = object.getString("total_amount");
                        String total_count = object.getString("total_count");
                        System.out.println("Login" + object);

                        txtTotal.setText("\u20b9 "+total_amount);
                        txtTotalTransactions.setText(""+total_count);

                        if (status.equalsIgnoreCase("1")) {

                            JSONArray array = object.optJSONArray("result");

                            historyModelArrayList.clear();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String payment_id = result.optString("payment_id");
                                String vendor_id = result.optString("vendor_id");
                                String user_id = result.optString("user_id");
                                String userName = result.optString("name");
                                String amount = result.optString("amount");
                                String txn_id = result.optString("txn_id");
                                String settled = result.optString("settled");
                                String datetime = result.optString("datetime");
                                String entrydt = result.optString("entrydt");

                                HistoryModel historyModel = new HistoryModel();
                                historyModel.setPayment_id(payment_id);
                                historyModel.setVendor_id(vendor_id);
                                historyModel.setUser_id(user_id);
                                historyModel.setUserName(userName);
                                historyModel.setAmount(amount);
                                historyModel.setTxn_id(txn_id);
                                historyModel.setSettled(settled);
                                historyModel.setDatetime(datetime);
                                historyModel.setEntrydt(entrydt);
                                historyModelArrayList.add(historyModel);
                            }


                            PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(mContext, historyModelArrayList);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            recyclerList.setLayoutManager(manager);
                            recyclerList.setAdapter(adapter);
                            tv_Notavailable.setVisibility(View.GONE);
                        } else {
                            tv_Notavailable.setVisibility(View.VISIBLE);
                            CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Payment History " + e.getMessage());
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
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.etFromDate:
                DatePickerDialog dialog = new DatePickerDialog(mContext, fromDate, fromCalendar
                        .get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
                        fromCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                dialog.show();
                break;

            case R.id.etToDate:

                if (!etFromDate.getText().toString().isEmpty() || !etFromDate.getText().toString().equalsIgnoreCase("")) {

                    DatePickerDialog dialog1 = new DatePickerDialog(mContext, toDate, toCalendar
                            .get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
                            toCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog1.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                    dialog1.show();



                }else {
                    Toast.makeText(PaymentHistoryActivity.this,"Please select from date first.",Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        Animatoo.animateCard(mContext);
    }
}