package com.example.studybuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileUI extends AppCompatActivity {

    String strong1, strong2, strong3, weak1, weak2, weak3;

    Button backButton, saveButton;
    TextView username, rating;
    Spinner strongSub1, strongSub2, strongSub3, weakSub1, weakSub2, weakSub3;

    DatabaseReference current_user;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_u_i);

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

        current_user = FirebaseDatabase.getInstance().getReference().child("users").child("MItaHYgH6qwertyuiop");
        current_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("username").getValue().toString());
                rating.setText(snapshot.child("rating").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TODO set spinner values by firebase
        /*for (int i = 0; i < strongSub1.getCount(); i++) {
            if (strongSub1.getItemAtPosition(i).toString().trim().equals(strong1)) {
                strongSub1.setSelection(i);
            }
        }*/
        strongSub1.setSelection(1);
        strongSub2.setSelection(6);
        weakSub1.setSelection(2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToViewProfile();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    public void returnToViewProfile(){
        Intent intent = new Intent(this, ViewProfileUI.class);
        startActivity(intent);
    }

    public void saveSettings(){
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
        startActivity(intent);
    }
}