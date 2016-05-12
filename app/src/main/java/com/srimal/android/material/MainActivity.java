package com.srimal.android.material;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.srimal.android.material.model.Material;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    String strDensityMin = null;
    String strDensityMax = null;
    String strMeltingPointMin = null;
    String strMeltingPointMax = null;

    ProgressDialog progress;

    //String url = "http://192.168.1.3/Material/dosearch.php?";
    String url = "http://srimalfernando.netai.net/material/dosearch.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Add Custom Property for search", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        progress = new ProgressDialog(MainActivity.this);

        Button btnSearch = (Button)findViewById(R.id.btn_search);
        if (btnSearch != null) {
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isEmpty(((EditText)findViewById(R.id.et_density_min)))){
                        strDensityMin = ((EditText)findViewById(R.id.et_density_min)).getText().toString().trim();
                    }

                    if(!isEmpty(((EditText)findViewById(R.id.et_density_max)))){
                        strDensityMax = ((EditText)findViewById(R.id.et_density_max)).getText().toString().trim();
                    }

                    if(!isEmpty(((EditText)findViewById(R.id.et_melting_point_min)))){
                        strMeltingPointMin = ((EditText)findViewById(R.id.et_melting_point_min)).getText().toString().trim();
                    }

                    if(!isEmpty(((EditText)findViewById(R.id.et_melting_point_max)))){
                        strMeltingPointMax = ((EditText)findViewById(R.id.et_melting_point_max)).getText().toString().trim();
                    }

                    if(isOnline()) {
                        search_clicked(view);
                    }else{
                        Snackbar.make(view, R.string.no_network,Snackbar.LENGTH_LONG).setAction(R.string.btn_go_back,null).show();
                    }
                }
            });
        }
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //method for search button event
    private void search_clicked(final View view){

        if(strDensityMin!=null){
            url = url+"density_min="+strDensityMin+"&";
        }
        if(strDensityMax!=null){
            url = url+"density_max="+strDensityMax+"&";
        }
        if(strMeltingPointMin!=null){
            url = url+"melting_min="+strMeltingPointMin+"&";
        }
        if(strMeltingPointMax!=null){
            url = url+"melting_max="+strMeltingPointMax+"&";
        }

        Log.d("SearchURL: ",url);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {

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

                    //Gson gson = new Gson();
                    //gson.fromJson(strResponse, Material.class);

                    /*Type listType = new TypeToken<ArrayList<Material>>() {
                    }.getType();
                      List<Material> mDatas = new Gson().fromJson(strResponse, listType);*/
                    JSONArray resArray = new JSONArray(strResponse);
                    if(resArray.length()>0){
                        Intent listIntent = new Intent(MainActivity.this,ItemListActivity.class);
                        listIntent.putExtra("response",strResponse);
                        startActivity(listIntent);
                    }else{
                        Snackbar.make(view, R.string.alert_no_result,Snackbar.LENGTH_LONG).setAction(R.string.btn_go_back,null).show();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }catch (JSONException e){
                    Snackbar.make(view, R.string.error,Snackbar.LENGTH_LONG).setAction(R.string.btn_go_back,null).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Snackbar.make(view, R.string.error,Snackbar.LENGTH_LONG).setAction(R.string.btn_go_back,null).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progress.dismiss();
                url = "http://srimalfernando.netai.net/material/dosearch.php?";
                strDensityMin = null;
                strDensityMax = null;
                strMeltingPointMin = null;
                strMeltingPointMax = null;
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
