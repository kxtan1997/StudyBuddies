package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainMenuUI extends AppCompatActivity {

    Button createPostButton, searchButton;

    private FirebaseRecyclerOptions<Post> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_ui);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");

        searchButton = findViewById(R.id.mainMenuSearchButton);
        createPostButton = findViewById(R.id.createPostButton);
        RecyclerView mainMenuRecyclerView = findViewById(R.id.mainMenuRecyclerView);
        mainMenuRecyclerView.setHasFixedSize(true);
        mainMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePostUI();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchPostUI();
            }
        });

        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(reference, Post.class).build();
        FirebaseRecyclerAdapter<Post, MainMenuViewHolder> adapter = new FirebaseRecyclerAdapter<Post, MainMenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MainMenuViewHolder mainMenuViewHolder, int i, @NonNull final Post post) {
                mainMenuViewHolder.postTitle.setText(post.getPostTitle());
                mainMenuViewHolder.postSubject.setText(post.getSubject());
                mainMenuViewHolder.postRating.setText(String.format(Locale.ENGLISH, "%d", post.getRating()));

                final String pid = post.getPostID();

                mainMenuViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = mainMenuViewHolder.getAdapterPosition();
                        String page = "mainMenu";
                        Intent intent = new Intent(view.getContext(), ViewPostUI.class);
                        intent.putExtra("pos", pos);
                        intent.putExtra("from", page);
                        intent.putExtra("pid", pid);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MainMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_view_holder, parent, false);

                return new MainMenuViewHolder(v);
            }
        };

        adapter.startListening();
        mainMenuRecyclerView.setAdapter(adapter);
    }

    public void openSearchPostUI() {
        Intent intent = new Intent(this, SearchPostUI.class);
        startActivity(intent);
    }

    public void openCreatePostUI() {
        Intent intent = new Intent(this, CreatePostUI.class);
        startActivity(intent);
    }

    public static class MainMenuViewHolder extends RecyclerView.ViewHolder {

        TextView postTitle, postSubject, postRating;
        View mView;

        public MainMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            postTitle = itemView.findViewById(R.id.singlePostTitle);
            postSubject = itemView.findViewById(R.id.singlePostSubject);
            postRating = itemView.findViewById(R.id.singleRating);
        }
    }
}