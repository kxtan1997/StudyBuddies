package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;

public class FilterPostUI extends AppCompatActivity {
    String userID;
    Button filterButton;
    LinearLayout checkBoxView;

    ArrayList<String> filters = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_post);

        filterButton = findViewById(R.id.filter);
        checkBoxView = findViewById(R.id.checkBoxView);
        final String[] subjects = getResources().getStringArray(R.array.subjects);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
        }

        for (int i = 1; i < subjects.length; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(ViewCompat.generateViewId());
            checkBox.setText(subjects[i]);
            final int finalI = i;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filters.add(subjects[finalI]);
                    } else {
                        filters.remove(subjects[finalI]);
                    }
                }
            });
            checkBoxView.addView(checkBox);
        }

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilteredMainMenuUI();
            }
        });
    }

    public void openFilteredMainMenuUI() {
        Intent intent = new Intent(this, MainMenuUI.class);
        intent.putExtra("userID", userID);
        intent.putExtra("filters", filters);
        startActivity(intent);
    }
}
