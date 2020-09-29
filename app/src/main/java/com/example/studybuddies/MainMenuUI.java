package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainMenuUI extends AppCompatActivity {

    Button createPostButton;
    String postID;

    private FirebaseRecyclerOptions<Post> options;
    private FirebaseRecyclerAdapter<Post, MainMenuViewHolder> adapter;
    private RecyclerView mainMenuRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_ui);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");

        createPostButton = findViewById(R.id.createPostButton);
        mainMenuRecyclerView = findViewById(R.id.mainMenuRecyclerView);
        mainMenuRecyclerView.setHasFixedSize(true);
        mainMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePostUI();
            }
        });

        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(reference,Post.class).build();
        adapter = new FirebaseRecyclerAdapter<Post, MainMenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MainMenuViewHolder mainMenuViewHolder, int i, @NonNull Post post) {
                mainMenuViewHolder.postTitle.setText(""+post.getPostTitle());
                mainMenuViewHolder.postSubject.setText(""+post.getSubject());
                mainMenuViewHolder.postRating.setText(""+post.getRatings());

                MainMenuViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = mainMenuViewHolder.getAdapterPosition();
                        Intent intent = new Intent(view.getContext(), ViewPostUI.class);
                        intent.putExtra("pos", pos);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MainMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_mainmenu_layout,parent,false);

                return new MainMenuViewHolder(v);
            }
        };

        adapter.startListening();
        mainMenuRecyclerView.setAdapter(adapter);

    }

    public void openCreatePostUI(){
        Intent intent = new Intent(this, createPostUI.class);
        startActivity(intent);
    }
}