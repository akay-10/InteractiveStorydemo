package com.example.interactivestories.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.interactivestories.R;
import com.example.interactivestories.model.Page;
import com.example.interactivestories.model.Story;

import java.util.Stack;

public class StoryActivity extends AppCompatActivity {

    public static final String TAG = StoryActivity.class.getSimpleName();
    private Story story;
    private String name;
    private ImageView storyImageView;
    private TextView storyTextView;
    private Button choice1Button;
    private Button choice2Button;
    private Stack<Integer> pageStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyImageView = findViewById(R.id.storyImageView);
        storyTextView = findViewById(R.id.storyTextView);
        choice1Button = findViewById(R.id.choice1Button);
        choice2Button = findViewById(R.id.choice2Button);

        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.key_name));
        //Exception Handling when button clicked without any name
        if(name == null || name.isEmpty()){
            name = "Friend";
        }
        Log.d(TAG, name);
        story = new Story();
        loadpage(0);
    }

    @SuppressLint("SetTextI18n")
    private void loadpage(int pageNumber) {
        pageStack.push(pageNumber);

        Page page = story.getPage(pageNumber);

        Drawable image = ContextCompat.getDrawable(this, page.getImageId());
        storyImageView.setImageDrawable(image);

        String pageText = getString(page.getTextId());
        pageText = String.format(pageText, name);
        storyTextView.setText(pageText);

        if(page.isFinalPage()){
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText("PLAY AGAIN");
            choice2Button.setOnClickListener(v -> {
//                    finish();
                loadpage(0);
            });
        }
        else {
            choice1Button.setVisibility(View.VISIBLE);
            choice1Button.setText(page.getChoice1().getTextId());
            choice1Button.setOnClickListener(v -> {
                int nextPage = page.getChoice1().getNextPage();
                loadpage(nextPage);
            });

            choice2Button.setVisibility(View.VISIBLE);
            choice2Button.setText(page.getChoice2().getTextId());
            choice2Button.setOnClickListener(v -> {
                int nextPage = page.getChoice2().getNextPage();
                loadpage(nextPage);
            });
        }

    }

    @Override
    public void onBackPressed() {
        pageStack.pop();
        if(pageStack.isEmpty()){
            super.onBackPressed();
        }
        else{
            loadpage(pageStack.pop());
        }
    }
}