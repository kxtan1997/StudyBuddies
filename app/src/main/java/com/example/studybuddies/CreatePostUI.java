package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePostUI extends AppCompatActivity {

    EditText postTitle;
    EditText postDescription;
    Button postValue;
    Spinner subjects;

    DatabaseReference databasePost;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        databasePost = FirebaseDatabase.getInstance().getReference("posts");

        postTitle = findViewById(R.id.postTitle);
        postDescription = findViewById(R.id.postDescription);
        postValue = findViewById(R.id.postValue);
        subjects = findViewById(R.id.subjects);

        postValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPostToDB();
            }
        });
    }

    private void addPostToDB() {
        String postDes = postDescription.getText().toString().trim();
        String title = postTitle.getText().toString().trim();
        String sub = subjects.getSelectedItem().toString();

        if (!TextUtils.isEmpty(postDes) && !TextUtils.isEmpty(title)) {
            String id = databasePost.push().getKey();

            Post post = new Post(id, title, postDes, sub, null, 0);
            assert id != null;
            databasePost.child(id).setValue(post);

            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(this, "Post created", Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent(this, MainMenuUI.class);
            startActivity(intent);
        } else {
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(this, "This field cannot be empty", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}