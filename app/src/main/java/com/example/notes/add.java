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
    SharedPreferences s;
     Button b;
    public static ArrayList<String> content;
    int t;
     
     
private void saveContent()
{
    try {
        s.edit().putString("add",ObjectSerializer.serialize(content)).apply();

        Log.i("add",ObjectSerializer.serialize(content));

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        s=getSharedPreferences("package com.example.notes;",Context.MODE_PRIVATE);
        content=MainActivity.editContent;
        saveContent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent in=getIntent();
        t=in.getIntExtra("noteId",-1);

        final String l=(content.get(t));
        final EditText et=(EditText) findViewById(R.id.editText);
        et.setText(l);

        b=(Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.set(t,et.getText().toString());
                back();
            }
        });

    }
     
     //return to main screen
    private void back()
    {

        saveContent();
        Intent i=new Intent(add.this,MainActivity.class);
        i.putExtra("changed",t);
        startActivity(i);

    }

}
