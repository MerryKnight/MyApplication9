package com.example.myapplication9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_TEXT_VIEW = "TextValue";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    Button btnCreate;
    Button btnRead;
    Button btnDelete;
    Button btnAppend;
    Button btnExtC, btnExtD,btnExtR, btnNext;
    TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = findViewById(R.id.buttonCreateFile);
        btnRead = findViewById(R.id.buttonReadFile);
        btnDelete = findViewById(R.id.buttonDeleteFile);
        btnAppend = findViewById(R.id.buttonAppendToFile);

        btnNext = findViewById(R.id.buttonAct);

        textView = findViewById(R.id.textViewFileContent);
        btnExtR = findViewById(R.id.buttonReadExtFile);
        btnExtC = findViewById(R.id.buttonCreateExtFile);
        btnExtD = findViewById(R.id.buttonDeleteExtFile);
        EditText editText = findViewById(R.id.editTextFileName);
        btnExtC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExtFile(editText.getText().toString());
            }
        });
        btnExtD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteExtFile(editText.getText().toString());
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFile(MainActivity.this);
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               textView.setText(readFile(MainActivity.this,
                        editText.getText().toString()));
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteFileDialog(MainActivity.this,
                        editText.getText().toString());
            }
        });
        btnAppend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appendToFile(MainActivity.this,
                        editText.getText().toString());
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                startActivity(intent);
            }
        });
        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString(KEY_TEXT_VIEW);
            textView.setText(savedText);
        }
    }

    public void createFile(Context context) {
        EditText editText = findViewById(R.id.editTextFileName);
        EditText editText1 = findViewById(R.id.editTextFileContent);
        String filename = editText.getText().toString();
        String fileContents = editText1.getText().toString();
        try (FileOutputStream fos = context.openFileOutput(filename,
                Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public String readFile(Context context, String filename) {
        try (FileInputStream fis = context.openFileInput(filename)) {
            InputStreamReader inputStreamReader = new
                    InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new
                    BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String contents = stringBuilder.toString();
            return contents;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void deleteFile(Context context, String filename)
    {
        File dir = getFilesDir();
        File file = new File(dir, filename);
        boolean deleted = file.delete();
    }
    public void appendToFile(Context context, String filename) {
        EditText editText1 = findViewById(R.id.editTextFileContent);
        String fileContents = editText1.getText().toString();
        try (FileOutputStream fos = context.openFileOutput(filename,
                Context.MODE_APPEND)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDeleteFileDialog(final Context context, final String filename) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Удаление файла");
        builder.setMessage("Вы уверены, что хотите удалить файл \"" + filename + "\"?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFile(context, filename);
                Toast.makeText(context, "Файл \"" + filename + "\" удален", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void createExtFile(String filename)
    {
        EditText editText1 = findViewById(R.id.editTextFileContent);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "Files");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File file = new File(storageDir, filename);
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (created) {
                    FileWriter writer = new FileWriter(file);
                    writer.append(editText1.getText().toString());
                    writer.flush();
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public String readExtFile(String filename) {
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
        return "";*/

    public void deleteExtFile(String filename)
    {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Files");
        File file = new File(storageDir, filename);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                // Файл успешно удален
                Toast.makeText(MainActivity.this,"File deleted",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this,"File is not found",
                        Toast.LENGTH_LONG).show();
            }
        }
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