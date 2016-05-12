package com.srimal.android.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.srimal.android.material.model.Vendors;

/**
 * Created by Srimal on 5/4/2016.
 */
public class VendorDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vende_detail);

        Intent intent = getIntent();
        Bundle bd;

        if(intent.hasExtra("details")) {
            bd = getIntent().getExtras();
            if (!bd.getSerializable("details").equals(null)) {
                Vendors vendor = (Vendors) bd.getSerializable("details");
                setViews(vendor);
            }
        }
    }

    private void setViews(Vendors vendor) {
        ((TextView)findViewById(R.id.txt_vendor_name)).setText(vendor.getName());
        ((TextView)findViewById(R.id.txtContactInfo)).setText(vendor.getContact_data());
    }
}
