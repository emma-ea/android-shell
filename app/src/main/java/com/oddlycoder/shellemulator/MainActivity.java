package com.oddlycoder.shellemulator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mTextViewResults;
    private EditText mEditTextShell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextShell = findViewById(R.id.edit_text_shell);
        mTextViewResults = findViewById(R.id.text_view_results);

        mTextViewResults.setMovementMethod(new ScrollingMovementMethod());

        findViewById(R.id.button_exec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEditTextShell.getText().toString().trim();
                if (s.isEmpty()) {
                    return;
                }
                execCommands(s);
            }
        });
    }

    private String[] getCommands(String commands) {
        return commands.split(" ");
    }

    private void execCommands(String c) {
        try {
            Process process = Runtime.getRuntime().exec(getCommands(c));
            InputStream iStream = process.getInputStream();
            tryWriteProcessOutput(iStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(19)
    private void tryWriteProcessOutput(InputStream iStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(iStream))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append("\n");
            }
            mTextViewResults.setText(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
