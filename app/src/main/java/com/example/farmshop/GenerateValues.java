package com.example.farmshop;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

public class GenerateValues {

    public static String password;
    public static String date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String generatePassword() throws UnsupportedEncodingException {
        String payBill = SandBox.getBusinessShortCode();
        String secretKey = SandBox.getPassKey();
        String time = generateDate();
        String psd = payBill + secretKey + time;

        byte[] bytes = psd.getBytes("ISO-8859-1");

        password = Base64.getEncoder().encodeToString(bytes);
        System.out.println("The password is: " + password);
        return password;
    }

    public static String generateDate() {
        date = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        System.out.println("Date: " + date);
        return date;
    }
}
