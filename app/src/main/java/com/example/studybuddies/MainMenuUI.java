package com.example.studybuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuUI extends AppCompatActivity {

    Button createPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_ui);

        createPostButton = findViewById(R.id.createPostButton);

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePostUI();
            }
        });
    }

    public void openCreatePostUI(){
        Intent intent = new Intent(this, createPostUI.class);
        startActivity(intent);
    }
}