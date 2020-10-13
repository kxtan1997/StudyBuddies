package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchPostUI extends AppCompatActivity {

    private String userID;

    private EditText mSearchField;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;
    private FirebaseRecyclerOptions<Post> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("posts");

        mSearchField = findViewById(R.id.search_field);
        ImageButton mSearchBtn = findViewById(R.id.search_btn);

        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
        }

        mSearchBtn.setOnClickListener(view -> {
            String searchText = mSearchField.getText().toString();
            firebaseUserSearch(searchText);
        });
    }

    private void firebaseUserSearch(String searchText) {

        Query firebaseSearchQuery = mUserDatabase.orderByChild("postTitle").startAt(searchText).endAt(searchText + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(firebaseSearchQuery, Post.class).build();
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(PostViewHolder viewHolder, final int position, final Post post) {
                viewHolder.setDetails(post.getPostTitle(), post.getPostDescription(), post.getSubject());
                viewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(SearchPostUI.this, ViewPostUI.class);
                    intent.putExtra("pos", 0);
                    intent.putExtra("from", "search");
                    intent.putExtra("pid", post.getPostID());
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_search_view_holder, parent, false);
                return new PostViewHolder(v);
            }
        };

        firebaseRecyclerAdapter.startListening();
        mResultList.setAdapter(firebaseRecyclerAdapter);
    }


    // View Holder Class

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String postTitle, String postDescription, String subject) {

            TextView post_title = mView.findViewById(R.id.postTitle_text);
            TextView post_description = mView.findViewById(R.id.postDescription_text);
            TextView post_subject = mView.findViewById(R.id.subject_text);

            post_title.setText(postTitle);
            post_description.setText(postDescription);
            post_subject.setText(subject);
        }
    }
}
