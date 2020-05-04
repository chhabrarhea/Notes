package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class add extends AppCompatActivity {
SharedPreferences sp;
Button b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent in=getIntent();
        int t=in.getIntExtra("noteId",-1);
        sp=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        final String l=((Object)t).toString();
        final EditText et=(EditText) findViewById(R.id.editText);
        et.setText(sp.getString((l),""));
        b=(Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(l, String.valueOf(et.getText())).apply();
                back();
            }
        });

    }
    private void back()
    {

        Intent i=new Intent(add.this,MainActivity.class);
        startActivity(i);

    }

}
