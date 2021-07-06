package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SlotAvailaibility extends AppCompatActivity {

    private static String TAG = "SLOT_AVAILAIBILITY";
    private RadioButton distr,pin,locat;
    private LinearLayout layout;
    private EditText Edit_pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_availaibility);
        distr = findViewById(R.id.radiobtn_district);
        pin = findViewById(R.id.radiobtn_pin);
        Edit_pin = findViewById(R.id.PincodeEdit);
        layout = findViewById(R.id.spinnerLayout);
        locat = findViewById(R.id.radiobtn_location);

        distr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = distr.isChecked();
                if(ischecked)
                {
                    layout.setVisibility(View.GONE);
                    Edit_pin.setVisibility(View.VISIBLE);
                }
            }
        });
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = pin.isChecked();
                if(ischecked)
                {
                    layout.setVisibility(View.VISIBLE);
                    Edit_pin.setVisibility(View.GONE);
                }
            }
        });
        locat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = locat.isChecked();
                if(ischecked)
                {
                    layout.setVisibility(View.GONE);
                    Edit_pin.setVisibility(View.GONE);
                }
            }
        });

    }
}