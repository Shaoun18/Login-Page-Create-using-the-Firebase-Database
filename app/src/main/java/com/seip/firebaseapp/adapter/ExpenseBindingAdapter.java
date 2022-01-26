package com.seip.firebaseapp.adapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.google.firestore.v1.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseBindingAdapter {
    @BindingAdapter(value = "app:setDate")
    public static void setDate (TextView textView, long date){
        final String datestring = new SimpleDateFormat("hh:mm a EEE,MMM dd,yyyy").format(new Date(date));
        textView.setText("at" + datestring);
    }
}
