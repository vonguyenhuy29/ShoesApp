package com.example.shoesapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shoesapp.R;
import com.example.shoesapp.adapter.GiaycongsoAdapter;
import com.example.shoesapp.adapter.GiaythethaoAdapter;
import com.example.shoesapp.model.Sanpham;
import com.example.shoesapp.ultil.CheckConnection;
import com.example.shoesapp.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GiayCongSoActivity extends AppCompatActivity {


    Toolbar toolbargcs;
    ListView lvgcs;
    GiaycongsoAdapter giaycongsoAdapter;
    ArrayList<Sanpham> manggcs;
    int idgcs = 0;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    boolean limitdata = false;
    mHandler mHandler;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giay_cong_so);
        Anhxa();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {

            GetIdloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        }else{
            CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại internet");
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.example.shoesapp.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadMoreData() {
        lvgcs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",manggcs.get(i));
                startActivity(intent);
            }
        });
        lvgcs.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int FirstItem, int VisibleItem, int TotalItem) {
                if (FirstItem + VisibleItem == TotalItem && TotalItem != 0 && isLoading == false && limitdata == false){
                    isLoading = true ;
                    GiayCongSoActivity.ThreadData threadData = new GiayCongSoActivity.ThreadData();
                    threadData.start();

                }
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext()); //Phương thức kết nối gửi lên server
        String duongdan = Server.Duongdangiaythethao + String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tengcs = "";
                int Giagcs = 0;
                String Hinhanhgcs = "";
                String Mota = "";
                int Idspgcs = 0;
                if (response != null && response.length() != 2) {
                    lvgcs.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tengcs = jsonObject.getString("tensp");
                            Giagcs = jsonObject.getInt("giasp");
                            Hinhanhgcs = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("motasp");
                            Idspgcs = jsonObject.getInt("idsanpham");
                            manggcs.add(new Sanpham(id,Tengcs,Giagcs,Hinhanhgcs,Mota,Idspgcs));
                            giaycongsoAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    limitdata = true;
                    lvgcs.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String,String>();
                param.put("idsanpham",String.valueOf(idgcs));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbargcs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbargcs.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetIdloaisp() {
        idgcs = getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",idgcs+"");
    }

    private void Anhxa() {
        toolbargcs = (Toolbar) findViewById(R.id.toolbargiaycongso);
        lvgcs = (ListView) findViewById(R.id.listviewgiaycongso);
        manggcs = new ArrayList<>();
        giaycongsoAdapter = new GiaycongsoAdapter(getApplicationContext(),manggcs);
        lvgcs.setAdapter(giaycongsoAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler();


    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvgcs.addFooterView(footerview);
                    break;
                case 1:

                    GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        public  void run (){
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}