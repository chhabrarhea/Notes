package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;



 public class MainActivity extends AppCompatActivity {
    ArrayList<String> n;
    ArrayAdapter arrayAdapter;
    SharedPreferences s;
    ListView lv;
    EditText ed;
    public static ArrayList<String> editContent;

    private void save()     //save data
    {
        try {
            s.edit().putString("name",ObjectSerializer.serialize(n)).apply();

            Log.i("name",ObjectSerializer.serialize(n));

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            s.edit().putString("content",ObjectSerializer.serialize(editContent)).apply();

            Log.i("content",ObjectSerializer.serialize(editContent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Note(final int operation)     //add or rename note
    {
        ed=(EditText)findViewById(R.id.editText2);
        lv.animate().alpha(0);
        ed.animate().alpha(1).translationY(-1000);
     
     //Pick user input from edit text view only when enter key is pressed
        ed.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode==event.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    lv.animate().alpha(1);
                    ed.animate().alpha(0).translationY(1000);
                 
                 
                 //Hide keyboard when enter key is pressed
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                 
                    String g = ed.getText().toString();
                    ed.setText("");
                 
                 
                 
                    if(operation==-1){
                        n.add(g);
                        editContent.add("");             //add new note
                    }                        
                    else{
                        n.remove(operation);
                        n.add(operation,g);       }        //rename existing note
                    arrayAdapter.notifyDataSetChanged();
                    save();

                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.add:
                Note(-1);
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        s=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        n = new ArrayList<>();
        try {       //Retrieve saved name of notes
            n = (ArrayList<String>) ObjectSerializer.deserialize(s.getString("name", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent i=getIntent();
        int t=i.getIntExtra("changed",-1);
         
        if(t==-1)
        {
        try {    //retrieve saved content of notes
            editContent = (ArrayList<String>) ObjectSerializer.deserialize(s.getString("content", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }}
        else{
            editContent=add.content;  //update content of notes
             save();
            }


        //Display names of notes
        lv=(ListView) findViewById(R.id.lv);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,n);
        lv.setAdapter(arrayAdapter);


        //open content when name is clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                save();
                Intent intent=new Intent(MainActivity.this,add.class);
                intent.putExtra("noteId",i);
                startActivity(intent);
            }
        });



        //open rename or delete option menu when name is long clicked
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Select:");
                b.setIcon(android.R.drawable.ic_dialog_alert);
                b.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder bb=new AlertDialog.Builder(MainActivity.this);
                        bb.setIcon(android.R.drawable.ic_dialog_alert);
                        bb.setTitle("Are You Sure?");
                        bb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                n.remove(position);
                                editContent.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                save();
                            }
                        });
                        bb.setNegativeButton("No",null);
                        bb.show();

                    }
                });
                b.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note(position);
                    }
                });
                b.show();
                return true;
            }
        });
    }
}
