package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPostUI extends AppCompatActivity {

    Button submitButton, backButton, upVote, downVote;
    int pos;
    String page;
    TextView pRating, pTitle, pDescription, pSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post);

        pRating = findViewById(R.id.postRating);
        pTitle = findViewById(R.id.postTitle);
        pDescription = findViewById(R.id.postDescription);
        pSubject = findViewById(R.id.postSubject);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        upVote = findViewById(R.id.upVote);
        downVote = findViewById(R.id.downVote);

        //get position of item, passed from MainMenuUI
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pos = extras.getInt("pos");
            page = extras.getString("from");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvote();
            }
        });

        downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downvote();
            }
        });

        if (page.equals("mainMenu")) {
            FirebaseDatabase.getInstance().getReference().child("posts")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Post p;
                            Post np;
                            ArrayList<Post> posts = new ArrayList<>();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                p = ds.getValue(Post.class);
                                posts.add(p);
                            }
                            np = posts.get(pos);

                            pRating.setText(String.valueOf(np.ratings));
                            pTitle.setText(np.postTitle);
                            pDescription.setText(np.postDescription);
                            pSubject.setText(np.subject);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        } else if (page.equals("search")) {
            //TODO set post details here
        } else {
            //TODO do something
        }
    }

    public void back() {
        Intent intent = new Intent(this, MainMenuUI.class);
        startActivity(intent);
    }

    public void upvote() {

    }

    public void downvote() {

    }

}