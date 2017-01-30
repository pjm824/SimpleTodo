package com.pjm284.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import static com.pjm284.simpletodo.R.id.etNewItem;
import static com.pjm284.simpletodo.R.id.lvItems;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        String taskString = getIntent().getStringExtra("itemBody");
        etEditItem.setText(taskString);
        etEditItem.setSelection(etEditItem.getText().length());
        Intent i = getIntent();
        pos = getIntent().getIntExtra("itemPos", 0);
    }

    public void onSaveItem(View v) {
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("itemBody", etEditItem.getText().toString());
        data.putExtra("itemPos", pos);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
