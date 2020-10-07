package com.example.studybuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileUI extends AppCompatActivity {

    Button backButton, editButton;

    TextView username, rating, strongSub1, strongSub2, strongSub3, weakSub1, weakSub2, weakSub3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_u_i);

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

        DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("users").child("MItaHYgH6qwertyuiop");
        current_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("username").getValue().toString());
                rating.setText(snapshot.child("rating").getValue().toString());
                strongSub1.setText(snapshot.child("strongSubs").child("0").getValue().toString());
                strongSub2.setText(snapshot.child("strongSubs").child("1").getValue().toString());
                strongSub3.setText(snapshot.child("strongSubs").child("2").getValue().toString());
                weakSub1.setText(snapshot.child("weakSubs").child("0").getValue().toString());
                weakSub2.setText(snapshot.child("weakSubs").child("1").getValue().toString());
                weakSub3.setText(snapshot.child("weakSubs").child("2").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainMenu();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileUI();
            }
        });
    }

    public void returnToMainMenu(){
        Intent intent = new Intent(this, MainMenuUI.class);
        startActivity(intent);
    }

    public void openEditProfileUI(){
        Intent intent = new Intent(this, EditProfileUI.class);
        startActivity(intent);
    }
}