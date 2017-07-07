package com.example.majorcat.kursach_separated_jsonpart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class OrderInfo extends AppCompatActivity {
    TextView idText;
    TextView addressText;
    TextView appartamentText;
    TextView byWhomText;
    TextView toWhomText;
    TextView goodText;
    TextView descriptionText;
    TextView isdeliveredText;
    Button makeRouteBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_info);


        makeRouteBtn = (Button) findViewById(R.id.makeRouteBtn);
        idText = (TextView) findViewById(R.id.id);
        addressText = (TextView) findViewById(R.id.address);
        appartamentText = (TextView) findViewById(R.id.appartament);
        toWhomText = (TextView) findViewById(R.id.towhom);
        byWhomText = (TextView) findViewById(R.id.bywhom);
        goodText = (TextView) findViewById(R.id.good);
        descriptionText = (TextView) findViewById(R.id.description);
        isdeliveredText = (TextView) findViewById(R.id.isdelivered);

        intent = getIntent();

        int id = intent.getIntExtra("id", 0);
        final String address = intent.getStringExtra("address");
        int appartament = intent.getIntExtra("appartament", 0);
        String byWhom = intent.getStringExtra("bywhom") + " (" +
                intent.getStringExtra("senderphone") + ")";
        String toWhom = intent.getStringExtra("towhom") + " (" +
                intent.getStringExtra("recipientphone") + ")";
        String good = intent.getStringExtra("good") + " " +
                intent.getIntExtra("price", 0);;
        String description = intent.getStringExtra("description");
        Boolean isdelivered = intent.getBooleanExtra("isedelivered", false);

        idText.setText(Integer.toString(id));
        addressText.setText(address);
        appartamentText.setText(Integer.toString(appartament));
        byWhomText.setText(byWhom);
        toWhomText.setText(toWhom);
        goodText.setText(good);
        descriptionText.setText(description);
        if (isdelivered) isdeliveredText.setText("доставлено");
        else isdeliveredText.setText("не доставлено");

        makeRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
                intentMap.putExtra("address", address);
                intentMap.putExtra("senderphone", intent.getStringExtra("senderphone"));
                intentMap.putExtra("recipientphone", intent.getStringExtra("recipientphone"));
                startActivity(intentMap);
            }
        });

    }
}
