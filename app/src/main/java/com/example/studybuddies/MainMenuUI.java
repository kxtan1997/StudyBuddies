package com.example.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainMenuUI extends AppCompatActivity {

    String userID = "MItaHYgH6qwertyuiop";
    ArrayList<String> userSubs = new ArrayList<>();
    ArrayList<String> filters = new ArrayList<>();
    boolean filterFlag = false;

    Button createPostButton, viewProfileButton;
    ImageButton filterButton, searchButton;
    RecyclerView mainMenuRecyclerView;

    FirebaseRecyclerOptions<Post> options;
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference().child("posts");
    FirebaseRecyclerAdapter<Post, MainMenuViewHolder> adapter;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        filterButton = findViewById(R.id.mainMenuFilterButton);
        searchButton = findViewById(R.id.mainMenuSearchButton);
        createPostButton = findViewById(R.id.createPostButton);
        viewProfileButton = findViewById(R.id.viewProfileButton);
        mainMenuRecyclerView = findViewById(R.id.mainMenuRecyclerView);
        mainMenuRecyclerView.setHasFixedSize(true);
        mainMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
            filters = extras.getStringArrayList("filters");
        }

        filterFlag = filters != null && !filters.isEmpty();

        if (!filterFlag) {
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (int i = 0; i < 3; i++) {
                        userSubs.add((String) snapshot.child("strongSubs").child(Integer.toString(i)).getValue());
                        userSubs.add((String) snapshot.child("weakSubs").child(Integer.toString(i)).getValue());
                    }
                    initialisePosts();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            initialisePosts();
        }

        createPostButton.setOnClickListener(v -> openCreatePostUI());

        filterButton.setOnClickListener(v -> openFilterPostUI());

        searchButton.setOnClickListener(v -> openSearchPostUI());

        viewProfileButton.setOnClickListener(v -> openViewProfileUI());
    }

    public void initialisePosts() {
        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(postReference, Post.class).build();
        adapter = new FirebaseRecyclerAdapter<Post, MainMenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MainMenuViewHolder mainMenuViewHolder, int i, @NonNull final Post post) {
                if (filterFlag) {
                    if (filters.contains(post.subject)) {
                        mainMenuViewHolder.postTitle.setText(post.getPostTitle());
                        mainMenuViewHolder.postSubject.setText(post.getSubject());
                        mainMenuViewHolder.postRating.setText(String.format(Locale.ENGLISH, "%d", post.getRating()));
                        if (post.getPosterID().equals(userID)) {
                            mainMenuViewHolder.deleteButton.setVisibility(View.VISIBLE);
                            mainMenuViewHolder.deleteButton.setOnClickListener(v -> mainMenuViewHolder.deletePost(post));
                        }

                        final String pid = post.getPostID();

                        mainMenuViewHolder.itemView.setOnClickListener(view -> {
                            int pos = mainMenuViewHolder.getAdapterPosition();
                            String page = "mainMenu";
                            Intent intent = new Intent(view.getContext(), ViewPostUI.class);
                            intent.putExtra("userID", userID);
                            intent.putExtra("pos", pos);
                            intent.putExtra("from", page);
                            intent.putExtra("pid", pid);
                            startActivity(intent);
                        });
                    } else {
                        mainMenuViewHolder.itemView.setVisibility(View.GONE);
                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mainMenuViewHolder.itemView.getLayoutParams();
                        params.height = 0;
                        mainMenuViewHolder.itemView.setLayoutParams(params);
                    }
                } else {
                    if (userSubs.contains(post.subject)) {
                        mainMenuViewHolder.postTitle.setText(post.getPostTitle());
                        mainMenuViewHolder.postSubject.setText(post.getSubject());
                        mainMenuViewHolder.postRating.setText(String.format(Locale.ENGLISH, "%d", post.getRating()));
                        if (post.getPosterID().equals(userID)) {
                            mainMenuViewHolder.deleteButton.setVisibility(View.VISIBLE);
                            mainMenuViewHolder.deleteButton.setOnClickListener(v -> mainMenuViewHolder.deletePost(post));
                        }

                        final String pid = post.getPostID();

                        mainMenuViewHolder.itemView.setOnClickListener(view -> {
                            int pos = mainMenuViewHolder.getAdapterPosition();
                            String page = "mainMenu";
                            Intent intent = new Intent(view.getContext(), ViewPostUI.class);
                            intent.putExtra("userID", userID);
                            intent.putExtra("pos", pos);
                            intent.putExtra("from", page);
                            intent.putExtra("pid", pid);
                            startActivity(intent);
                        });
                    } else {
                        mainMenuViewHolder.itemView.setVisibility(View.GONE);
                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mainMenuViewHolder.itemView.getLayoutParams();
                        params.height = 0;
                        mainMenuViewHolder.itemView.setLayoutParams(params);
                    }
                }
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

    public void openFilterPostUI() {
        Intent intent = new Intent(this, FilterPostUI.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void openSearchPostUI() {
        Intent intent = new Intent(this, SearchPostUI.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void openCreatePostUI() {
        Intent intent = new Intent(this, CreatePostUI.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void openViewProfileUI() {
        Intent intent = new Intent(this, ViewProfileUI.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public class MainMenuViewHolder extends RecyclerView.ViewHolder {

        TextView postTitle, postSubject, postRating;
        View mView;
        ImageButton deleteButton;

        public MainMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            postTitle = itemView.findViewById(R.id.singlePostTitle);
            postSubject = itemView.findViewById(R.id.singlePostSubject);
            postRating = itemView.findViewById(R.id.singleRating);
            deleteButton = itemView.findViewById(R.id.deletePostButton);
        }

        public void deletePost(Post post) {
            postReference.child(post.getPostID()).removeValue();
            adapter.notifyDataSetChanged();
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Post deleted", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}