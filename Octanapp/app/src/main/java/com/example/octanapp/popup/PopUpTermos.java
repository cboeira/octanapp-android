package com.example.octanapp.popup;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.octanapp.R;

public class PopUpTermos extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceCreate) {
        super.onCreate(savedInstanceCreate);

        setContentView(R.layout.popwindow_termos);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;


        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }
}
