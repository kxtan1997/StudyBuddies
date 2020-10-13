package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileUI extends AppCompatActivity {
    String userID;

    Button backButton, editButton;

    TextView username, rating, strongSub1, strongSub2, strongSub3, weakSub1, weakSub2, weakSub3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        backButton = findViewById(R.id.viewProfileBackButton);
        editButton = findViewById(R.id.viewProfileToEditButton);

        username = findViewById(R.id.viewProfileUsername);
        rating = findViewById(R.id.viewProfileRating);
        strongSub1 = findViewById(R.id.viewProfileStrong1);
        strongSub2 = findViewById(R.id.viewProfileStrong2);
        strongSub3 = findViewById(R.id.viewProfileStrong3);
        weakSub1 = findViewById(R.id.viewProfileWeak1);
        weakSub2 = findViewById(R.id.viewProfileWeak2);
        weakSub3 = findViewById(R.id.viewProfileWeak3);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
        }

        DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        current_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText((String) snapshot.child("username").getValue());
                rating.setText(String.format("%s", snapshot.child("rating").getValue()));
                strongSub1.setText((String) snapshot.child("strongSubs").child("0").getValue());
                strongSub2.setText((String) snapshot.child("strongSubs").child("1").getValue());
                strongSub3.setText((String) snapshot.child("strongSubs").child("2").getValue());
                weakSub1.setText((String) snapshot.child("weakSubs").child("0").getValue());
                weakSub2.setText((String) snapshot.child("weakSubs").child("1").getValue());
                weakSub3.setText((String) snapshot.child("weakSubs").child("2").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(v -> returnToMainMenu());

        editButton.setOnClickListener(v -> openEditProfileUI());
    }

    public void returnToMainMenu() {
        Intent intent = new Intent(this, MainMenuUI.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void openEditProfileUI() {
        Intent intent = new Intent(this, EditProfileUI.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}