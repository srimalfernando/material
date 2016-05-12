package com.srimal.android.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srimal.android.material.model.Material;
import com.srimal.android.material.model.Vendors;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srimal on 5/4/2016.
 */
public class ItemDetailsActivity extends Activity{

    private List<Vendors> mData;
    private VendorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();
        Bundle bd;

        if(intent.hasExtra("vendors")) {
            bd = getIntent().getExtras();
            if (!bd.getString("vendors").equals(null)) {
                String strVendors = bd.getString("vendors");

                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Vendors>>() {
                }.getType();
                mData = new Gson().fromJson(strVendors, listType);

                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_vendors);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(mAdapter = new VendorAdapter());
            }
        }

        if(intent.hasExtra("details")) {
            bd = getIntent().getExtras();
            if (!bd.getSerializable("details").equals(null)) {
                Material m = (Material)bd.getSerializable("details");
                setViews(m);
            }
        }

    }

    private void setViews(Material material){
        ((TextView)findViewById(R.id.txtItemType)).setText(material.getType());
        ((TextView)findViewById(R.id.txtItemDescription)).setText(material.getMaterial_description());
        ((TextView)findViewById(R.id.txtDensity)).setText(material.getMaterial_short_name());

        ((TextView)findViewById(R.id.txtMeltingPoint)).setText(material.getProperty()+":"+material.getValue1()+","+material.getValue2());
    }

    class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorViewHolder> {

        @Override public VendorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return  new VendorViewHolder(
                    LayoutInflater.from(ItemDetailsActivity.this).inflate(R.layout.layout_item_vendor, parent, false));
        }

        @Override public void onBindViewHolder(VendorViewHolder holder, final int position) {
            holder.TxtCompName.setText(mData.get(position).getName());
            holder.TblRowItem.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Vendors vendor = mData.get(position);
                    Intent i = new Intent(ItemDetailsActivity.this,VendorDetailsActivity.class);
                    i.putExtra("details", vendor);
                    ItemDetailsActivity.this.startActivity(i);
                }
            });
        }

        @Override public int getItemCount() {
            return mData.size();
        }

        class VendorViewHolder extends RecyclerView.ViewHolder {

            TextView TxtCompName;
            TableRow TblRowItem;

            public VendorViewHolder(View view) {
                super(view);
                TxtCompName = (TextView) view.findViewById(R.id.txt_vendor_name);
                TblRowItem = (TableRow) view.findViewById(R.id.tableRow);
            }
        }
    }

}
