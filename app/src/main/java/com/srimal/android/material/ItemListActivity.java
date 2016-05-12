package com.srimal.android.material;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.srimal.android.material.model.Material;
import com.srimal.android.material.model.Vendors;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Srimal on 5/4/2016.
 */
public class ItemListActivity extends Activity {

    public static String strResponse;

    //private RecyclerView mRecyclerView;
    private List<Material> mDatas;
    private HomeAdapter mAdapter;

    private List<Vendors> mData;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        progress = new ProgressDialog(ItemListActivity.this);

        Intent intent = getIntent();
        Bundle bd;
        if(intent.hasExtra("response")){
            bd = getIntent().getExtras();
            if(!bd.getString("response").equals(null)){
                strResponse = bd.getString("response").trim();
                Log.d("Response: ",strResponse);
                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Material>>() {
                }.getType();
                try {
                    mDatas = new Gson().fromJson(strResponse, listType);

                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
                    //initData();
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
                }catch(JsonSyntaxException e){
                    Log.d("JsonSyntaxException: ",e.getLocalizedMessage());
                }
            }
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return  new MyViewHolder(
                    LayoutInflater.from(ItemListActivity.this).inflate(R.layout.layout_item, parent, false));
        }

        @Override public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.TxtCompName.setText(mDatas.get(position).getMaterial_short_name());
            holder.TblRowItem.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(isOnline()) {
                        getVendors(mDatas.get(position).getMaterial_id(), mDatas.get(position));
                    }else{
                        Toast.makeText(ItemListActivity.this,R.string.no_network,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView TxtCompName;
            TableRow TblRowItem;

            public MyViewHolder(View view) {
                super(view);
                TxtCompName = (TextView) view.findViewById(R.id.txt_comp_name);
                TblRowItem = (TableRow) view.findViewById(R.id.tableRow);
            }
        }
    }

    private void getVendors(String strMaterialID, final Material material){

        //String url = "http://192.168.1.3/Material/getvendors.php?material_id="+strMaterialID;
        String url = "http://srimalfernando.netai.net/material/getvendors.php?material_id="+strMaterialID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ItemListActivity.this,url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
                progress.setMessage("Loading..");
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    String strResponse = new String(response,"UTF-8");
                    Log.d("Response: ", strResponse);
                    JSONArray resArray = new JSONArray(strResponse);
                    if(resArray.length()>0){
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Vendors>>() {

                        }.getType();
                        mData = new Gson().fromJson(strResponse, listType);

                        Intent i = new Intent(ItemListActivity.this,ItemDetailsActivity.class);
                        i.putExtra("details",material);
                        i.putExtra("vendors",strResponse);
                        ItemListActivity.this.startActivity(i);
                        //mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
                    }else{
                        Snackbar.make(null, R.string.alert_no_result,Snackbar.LENGTH_LONG).setAction(R.string.btn_go_back,null).show();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }catch (JSONException e){
                    Snackbar.make(null, R.string.error,Snackbar.LENGTH_LONG).setAction(R.string.btn_go_back,null).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Snackbar.make(null, R.string.error,Snackbar.LENGTH_LONG).setAction(R.string.btn_go_back,null).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progress.dismiss();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
