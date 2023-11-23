package com.example.farmshop;

import static com.example.farmshop.GenerateValues.generateDate;
import static com.example.farmshop.GenerateValues.generatePassword;
import static com.example.farmshop.Result.ResponseCode;
import static com.example.farmshop.StkPush.stkpush;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class PayActivity extends AppCompatActivity {
    private EditText totalAmountEditText;
    private EditText phoneNumber;
    private Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);


        }

        phoneNumber = findViewById(R.id.phoneEditText);
        totalAmountEditText = findViewById(R.id.totalAmountEditText);
        payButton = findViewById(R.id.payButton);

        //settings;
        SandBox.setAccess_token_url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials");
        SandBox.setBusinessShortCode("174379");
        SandBox.setConsumerKey("3TTt1Y03GDlQCOjGdjl3nJuh0yq85fcX");//enter consumer key
        SandBox.setConsumerSecret("vDBIFFEhckDGSGsJ");//enter consumer secret
        SandBox.setPassKey("bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919");//enter passkey
        SandBox.setStk_push_url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest");
        SandBox.setC2bSimulation_url("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/simulate");
//254708374149

        // Retrieve total amount from the intent
        int totalAmount = getIntent().getIntExtra("TOTAL_AMOUNT", 0);

        // Set total amount to the amount EditText
        totalAmountEditText.setText("Ksh " + totalAmount);
        totalAmountEditText.setEnabled(false);

        payButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                request(String.valueOf(totalAmount), phoneNumber.getText().toString());
            }
        });


    }

    private boolean checkPhoneCode(String phone) {
        return phone.startsWith("254");
    }

    private boolean checkPhone(String phone) {
        return phone.length() == 12;
    }

    private boolean checkAmount(String amount) {
        return amount.isEmpty();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void request(String amount, String phone) {
        if (!checkPhoneCode(phone)) {
            phoneNumber.requestFocus();
            phoneNumber.setError("Wrong format");
        } else if (!checkPhone(phone)) {
            phoneNumber.requestFocus();
            phoneNumber.setError("Wrong format");
        } else if (checkAmount(amount)) {
            totalAmountEditText.requestFocus();
            totalAmountEditText.setError("Cannot be empty");
        } else {
            try {
                stkpush(SandBox.businessShortCode, generatePassword(), generateDate(), "CustomerPayBillOnline", amount, phone, "254708374149", "174379", "https://drive.google.com/drive/folders/1vSYlciRi_fx-8xxqe0lYtCXbaJcNNlSg", "Kimathi Farm Shop", "Farm Produce");
                Toast.makeText(PayActivity.this, "Wait as we process your request", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}