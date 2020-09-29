package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewPost extends AppCompatActivity {

    Button submitButton,backButton,upVote,downVote;
    String title,description,pID,subject;
    int rating;
    TextView prating,ptitle,pdescription,psubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        prating = findViewById(R.id.postRating);
        ptitle = findViewById(R.id.postTitle);
        pdescription = findViewById(R.id.postDescription);
        psubject = findViewById(R.id.postSubject);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        upVote = findViewById(R.id.upVote);
        downVote = findViewById(R.id.downVote);


        //get postID, passed from mainmenu
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pID = extras.getString("postid");
            System.out.println("pid" + pID);
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

        FirebaseDatabase.getInstance().getReference().child("posts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post p = snapshot.getValue(Post.class);
                            System.out.println("pid inside loop" + p.postID);
                            if(p.postID.equals(pID)){
                                rating = p.ratings;
                                //System.out.println("rating inside loop " + p.ratings);
                                title = p.postTitle;
                                //System.out.println("title inside loop " + p.postTitle);
                                description = p.postDescription;
                                //System.out.println("Desc inside loop " + p.postDescription);
                                subject = p.subject;
                                break;
                            }

                        }
                        prating.setText(String.valueOf(rating));
                        ptitle.setText(title);
                        pdescription.setText(description);
                        psubject.setText(subject);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void back(){
        /*prating.setText(String.valueOf(""));
        ptitle.setText("");
        pdescription.setText("");
        psubject.setText("");
        title = "";
        description ="";
        subject = "";
        this.finish();*/
        Intent intent = new Intent(this, MainMenuUI.class);
        startActivity(intent);

    }

    public void upvote(){

    }

    public void downvote(){

    }

}
