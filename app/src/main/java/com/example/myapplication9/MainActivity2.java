package com.example.myapplication9;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;

import java.io.IOException;


public class MainActivity2 extends AppCompatActivity {

    private static final String KEY_TEXT_VIEW = "TextValue";

    TextView textView;
    Button btnExtR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView = findViewById(R.id.textViewFileContent);
        btnExtR = findViewById(R.id.buttonReadExtFile);

        EditText editText = findViewById(R.id.editTextFileName);

        btnExtR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(readExtFile(editText.getText().toString()));
            }
        });

        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString(KEY_TEXT_VIEW);
            textView.setText(savedText);
        }
    }

    public String readExtFile(String filename) {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Files");
        File file = new File(storageDir, filename);
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            Log.d("RRR", "Here");
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            } catch (IOException e) {
                Log.d("RRR", e.toString());
            }
            String fileContent = text.toString();
            return fileContent;
        }
        return "";
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String textToSave = textView.getText().toString();
        outState.putString(KEY_TEXT_VIEW,textToSave);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String state = savedInstanceState.getString(KEY_TEXT_VIEW);

    }


}