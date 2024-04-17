package com.example.speedreadingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView wordTextView;
    private SeekBar speedSeekBar;
    private TextView speedTextView;
    private EditText filePathEditText;
    private Button loadFileButton;
    private Button startButton;
    private Button stopButton;

    private ReadingTask readingTask;

    private static final int PICKFILE_RESULT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordTextView = findViewById(R.id.wordTextView);
        wordTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        speedSeekBar = findViewById(R.id.speedSeekBar);
        speedTextView = findViewById(R.id.speedTextView);
        loadFileButton = findViewById(R.id.loadFileButton);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        readingTask = new ReadingTask(this, wordTextView);

        loadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Выберите файл");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wordsPerMinute = speedSeekBar.getProgress();
                readingTask.start(wordsPerMinute);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readingTask.stop();
            }
        });

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedTextView.setText(progress + " слов/мин");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICKFILE_RESULT_CODE) {
            Uri uri = data.getData();
            readingTask.loadFile(uri.toString());
        }
    }

}
