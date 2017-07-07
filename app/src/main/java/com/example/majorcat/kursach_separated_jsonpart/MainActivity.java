package com.example.majorcat.kursach_separated_jsonpart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    ListView listView;
    Response responseObj;
    CustomAdapter adapter;
    String url = "http://kursachshop.000webhostapp.com/get_all_products.php";
    Gson gson;
    AsyncHttpClient client;
    boolean boolIsDelivered;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    Button btnRefresh;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        spinner = (ProgressBar)findViewById(R.id.progressBarMainActivity);

        fill();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                mSwipeRefreshLayout.setRefreshing(false);
                fill();
            }
        }, 4000);
    }

    public void fill(){

        listView = (ListView) findViewById(R.id.orederList);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        if (!listView.isShown())
            spinner.setVisibility(View.VISIBLE);
        if (btnRefresh.isShown()) btnRefresh.setVisibility(View.GONE);

        client = new AsyncHttpClient();
        client.get(MainActivity.this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                spinner.setVisibility(View.GONE);
                listView.setVisibility(listView.isShown() ? View.VISIBLE : View.VISIBLE);
                String responseStr = new String(responseBody);
                gson = new Gson();
                responseObj = gson.fromJson(responseStr, Response.class);
                adapter = new CustomAdapter(responseObj.getObjects(), MainActivity.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                spinner.setVisibility(View.GONE);
                listView.setVisibility(listView.isShown() ? View.GONE : View.GONE);

                Toast.makeText(MainActivity.this, "Проблемы с подключением к серверу", Toast.LENGTH_SHORT).show();
                btnRefresh.setVisibility(View.VISIBLE);
                btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fill();
                        btnRefresh.setVisibility(View.GONE);
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Создать новый activity с position внутри
                //Toast.makeText(MainActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), OrderInfo.class);
                intent.putExtra("id", responseObj.getObjects().get(position).getId());
                intent.putExtra("address", responseObj.getObjects().get(position).getAdres());
                intent.putExtra("appartament", responseObj.getObjects().get(position).getAppartament());
                intent.putExtra("bywhom", responseObj.getObjects().get(position).getBywhom());
                intent.putExtra("senderphone", responseObj.getObjects().get(position).getSenderphone());
                intent.putExtra("towhom", responseObj.getObjects().get(position).getTowhom());
                intent.putExtra("recipientphone", responseObj.getObjects().get(position).getRecipientphone());
                intent.putExtra("good", responseObj.getObjects().get(position).getGood());
                intent.putExtra("description", responseObj.getObjects().get(position).getDescription());
                intent.putExtra("price", responseObj.getObjects().get(position).getPrice());
                if (responseObj.getObjects().get(position).getIsdelivered().equals("1")) {
                    boolIsDelivered = true;
                } else boolIsDelivered = false;
                intent.putExtra("isedelivered", boolIsDelivered);
                startActivity(intent);
            }
        });
    }
}
