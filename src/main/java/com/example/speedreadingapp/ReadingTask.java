package com.example.speedreadingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadingTask {
    private Context context;
    private TextView wordTextView;
    private List<String> words;
    private int currentIndex;
    private Handler handler;
    private Runnable updateRunnable;
    private int delay;
    private static final String DEFAULT_TEXT = "Приветствуем Приветствуем в нашем приложении для быстрого чтения, загрузите свой файл в формате txt и читайте быстрее";
    private void setDefaultText() {
        words = new ArrayList<>(); // добавляем инициализацию списка слов
        String[] defaultWords = DEFAULT_TEXT.split("\\s+");
        for (String word : defaultWords) {
            words.add(word);
        }
    }

    public ReadingTask(Context context, TextView wordTextView) {
        this.context = context;
        this.wordTextView = wordTextView;
        this.words = new ArrayList<>();
        setDefaultText();
        this.currentIndex = 0;
        this.handler = new Handler();
        start(100); //запускаем чтение дефолтного текста при создании объекта
    }

    public void loadFile(String uriString) {
        if (uriString == null) {
            setDefaultText();
        } else {
            try {
                words.clear(); //очищаем список слов перед загрузкой нового файла
                Uri uri = Uri.parse(uriString);
                BufferedReader reader = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(uri)));
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] lineWords = line.split("\\s+");
                    for (String word : lineWords) {
                        words.add(word);
                    }
                }

                reader.close();
                currentIndex = 0; //обнуляем индекс после загрузки нового файла
            } catch (Exception e) {
                e.printStackTrace();
                setDefaultText();
            }
        }
    }


    public void start(int wordsPerMinute) {
        delay = 60000 / wordsPerMinute;
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentIndex < words.size()) {
                    displayWord(words.get(currentIndex));
                    animateWord(wordTextView);
                    currentIndex++;
                    handler.postDelayed(this, delay);
                }
            }
        };
        handler.post(updateRunnable);
    }

    public void stop() {
        handler.removeCallbacks(updateRunnable);
    }

    private void displayWord(String word) {
        wordTextView.setText(word);
    }

    private void animateWord(TextView textView) {
        textView.setAlpha(1.0f);
        textView.animate()
                .alpha(0.0f)
                .setDuration(delay / 2)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        textView.setTranslationX(0);
                    }
                })
                .start();
    }
}