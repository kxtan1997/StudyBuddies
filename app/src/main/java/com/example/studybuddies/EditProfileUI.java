package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileUI extends AppCompatActivity {

    String userID, strong1, strong2, strong3, weak1, weak2, weak3;

    Button backButton, saveButton;
    TextView username, rating;
    Spinner strongSub1, strongSub2, strongSub3, weakSub1, weakSub2, weakSub3;

    DatabaseReference current_user;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        backButton = findViewById(R.id.editProfileBackButton);
        saveButton = findViewById(R.id.editProfileSaveButton);
        username = findViewById(R.id.editProfileUsername);
        rating = findViewById(R.id.editProfileRating);
        strongSub1 = findViewById(R.id.editProfileStrongSub1);
        strongSub2 = findViewById(R.id.editProfileStrongSub2);
        strongSub3 = findViewById(R.id.editProfileStrongSub3);
        weakSub1 = findViewById(R.id.editProfileWeakSub1);
        weakSub2 = findViewById(R.id.editProfileWeakSub2);
        weakSub3 = findViewById(R.id.editProfileWeakSub3);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
        }

        current_user = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        current_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText((String) snapshot.child("username").getValue());
                rating.setText(String.format("%s", snapshot.child("rating").getValue()));
                for (int i = 0; i < strongSub1.getCount(); i++) {
                    if (strongSub1.getItemAtPosition(i).equals(snapshot.child("strongSubs").child("0").getValue())) {
                        strongSub1.setSelection(i);
                    }
                    if (strongSub2.getItemAtPosition(i).equals(snapshot.child("strongSubs").child("1").getValue())) {
                        strongSub2.setSelection(i);
                    }
                    if (strongSub3.getItemAtPosition(i).equals(snapshot.child("strongSubs").child("2").getValue())) {
                        strongSub3.setSelection(i);
                    }
                    if (weakSub1.getItemAtPosition(i).equals(snapshot.child("weakSubs").child("0").getValue())) {
                        weakSub1.setSelection(i);
                    }
                    if (weakSub2.getItemAtPosition(i).equals(snapshot.child("weakSubs").child("1").getValue())) {
                        weakSub2.setSelection(i);
                    }
                    if (weakSub3.getItemAtPosition(i).equals(snapshot.child("weakSubs").child("2").getValue())) {
                        weakSub3.setSelection(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(v -> returnToViewProfile());

        saveButton.setOnClickListener(v -> saveSettings());
    }

    public void returnToViewProfile() {
        Intent intent = new Intent(this, ViewProfileUI.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void saveSettings() {
        //TODO save settings
        strong1 = strongSub1.getSelectedItem().toString();
        strong2 = strongSub2.getSelectedItem().toString();
        strong3 = strongSub3.getSelectedItem().toString();
        weak1 = weakSub1.getSelectedItem().toString();
        weak2 = weakSub2.getSelectedItem().toString();
        weak3 = weakSub3.getSelectedItem().toString();
        current_user.child("strongSubs").child("0").setValue(strong1);
        current_user.child("strongSubs").child("1").setValue(strong2);
        current_user.child("strongSubs").child("2").setValue(strong3);
        current_user.child("weakSubs").child("0").setValue(weak1);
        current_user.child("weakSubs").child("1").setValue(weak2);
        current_user.child("weakSubs").child("2").setValue(weak3);

        toast = Toast.makeText(this, "Settings saved", Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this, ViewProfileUI.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}