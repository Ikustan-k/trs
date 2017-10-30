package com.example.user.tdbs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button_g = (Button)findViewById(R.id.goal);
        final Button button_c1 = (Button)findViewById(R.id.check1);
        final Button button_c2 = (Button)findViewById(R.id.check2);
        final Button button_c3 = (Button)findViewById(R.id.check3);
        final Button button_dk = (Button)findViewById(R.id.check4);

        button_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_g = new Intent(getApplicationContext(),Goal.class);
                startActivity(intent_g);
            }
        });
        button_c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_c1 = new Intent(getApplicationContext(),CheckPoint1.class);
                startActivity(intent_c1);
            }
        });
        button_c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_c2 = new Intent(getApplicationContext(),CheckPoint2.class);
                startActivity(intent_c2);
            }
        });
        button_c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_c3 = new Intent(getApplicationContext(),CheckPoint3.class);
                startActivity(intent_c3);
            }
        });
        button_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_dk = new Intent(getApplicationContext(),DK.class);
                startActivity(intent_dk);
            }
        });
    }

}
