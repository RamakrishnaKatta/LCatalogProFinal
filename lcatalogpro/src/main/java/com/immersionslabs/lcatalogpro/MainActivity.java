package com.immersionslabs.lcatalogpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    AppCompatButton Augmentbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Augmentbutton=findViewById(R.id.button_augment);

        Augmentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AugmentActivity.class);
                startActivity(intent);
            }
        });
    }
}
