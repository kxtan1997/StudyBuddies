package com.example.studybuddies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPostUI extends AppCompatActivity {

    Button submitButton, upVote, downVote;
    int pos;
    String page, pid, userID;
    TextView pRating, pTitle, pDescription, pSubject;
    EditText commentInput;
    RecyclerView commentsRecyclerView;
    DatabaseReference current_post; //get firebase reference of current post to facilitate updating of it
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        pRating = findViewById(R.id.postRating);
        pTitle = findViewById(R.id.postTitle);
        pDescription = findViewById(R.id.postDescription);
        pSubject = findViewById(R.id.postSubject);
        submitButton = findViewById(R.id.submitButton);
        upVote = findViewById(R.id.upVote);
        downVote = findViewById(R.id.downVote);
        commentInput = findViewById(R.id.commentInput);

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.addItemDecoration(new DividerItemDecoration(commentsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        //get position of item, passed from MainMenuUI
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
            pos = extras.getInt("pos");
            page = extras.getString("from");
            pid = extras.getString("pid");
        }

        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upVote();
            }
        });

        downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downVote();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
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

                            //set firebase reference of current post
                            current_post = FirebaseDatabase.getInstance().getReference().child("posts").child(pid);

                            pRating.setText(String.valueOf(np.rating));
                            pTitle.setText(np.postTitle);
                            pDescription.setText(np.postDescription);
                            pSubject.setText(np.subject);

                            loadComments();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        } else if (page.equals("search")) {
            FirebaseDatabase.getInstance().getReference().child("posts")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Post p;
                            ArrayList<Post> posts = new ArrayList<>();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                p = ds.getValue(Post.class);
                                posts.add(p);
                            }

                            for (int i = 0; i < posts.size(); i++) {
                                String postid = (posts.get(i).getPostID());
                                if (postid.equals(pid)) {

                                    current_post = FirebaseDatabase.getInstance().getReference().child("posts").child(pid);

                                    pRating.setText(String.valueOf(posts.get(i).getRating()));
                                    pTitle.setText(posts.get(i).getPostTitle());
                                    pDescription.setText(posts.get(i).getPostDescription());
                                    pSubject.setText(posts.get(i).getSubject());
                                    break;
                                }
                            }

                            loadComments();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }
    }

    public void upVote() {
        current_post.child("rating").setValue(ServerValue.increment(1));
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(getApplicationContext(), "Upvoted!", Toast.LENGTH_SHORT);
        toast.show();

        int value = Integer.parseInt(pRating.getText().toString());
        value += 1;
        pRating.setText(String.valueOf(value));
    }

    public void downVote() {
        current_post.child("rating").setValue(ServerValue.increment(-1));
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(getApplicationContext(), "Downvoted!", Toast.LENGTH_SHORT);
        toast.show();

        int value = Integer.parseInt(pRating.getText().toString());
        value -= 1;
        pRating.setText(String.valueOf(value));
    }

    @SuppressLint("ShowToast")
    public void postComment() {
        String subject = commentInput.getText().toString().trim();
        commentInput.getText().clear();

        if (!TextUtils.isEmpty(subject)) {
            String id = current_post.child("comments").push().getKey();

            Comment comment = new Comment(id, subject, 0);

            assert id != null;
            current_post.child("comments").child(id).setValue(comment);

            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(this, "Comment posted", Toast.LENGTH_SHORT);
        } else {
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(this, "This field cannot be empty", Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public void loadComments() {
        Query commentsQuery = current_post.child("comments");
        FirebaseRecyclerOptions<Comment> options = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(commentsQuery, Comment.class).build();
        FirebaseRecyclerAdapter<Comment, CommentViewHolder> adapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final CommentViewHolder commentViewHolder, int i, final Comment comment) {
                commentViewHolder.commentUpVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentViewHolder.upVoteComment(comment);
                    }
                });
                commentViewHolder.commentDownVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentViewHolder.downVoteComment(comment);
                    }
                });
                commentViewHolder.commentSubject.setText(comment.getSubject());
                commentViewHolder.commentRating.setText(String.format("%s", comment.getRating()));
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view_holder, parent, false);
                return new CommentViewHolder(v);
            }
        };
        adapter.startListening();
        commentsRecyclerView.setAdapter(adapter);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentSubject, commentRating;
        Button commentUpVote, commentDownVote;
        View mView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            commentSubject = itemView.findViewById(R.id.commentSubject);
            commentRating = itemView.findViewById(R.id.commentRating);
            commentUpVote = itemView.findViewById(R.id.commentUpVote);
            commentDownVote = itemView.findViewById(R.id.commentDownVote);
        }

        public void upVoteComment(Comment comment) {
            current_post.child("comments").child(comment.getCommentID()).child("rating").setValue(ServerValue.increment(1));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Upvoted comment!", Toast.LENGTH_SHORT);
            toast.show();

            int value = Integer.parseInt(commentRating.getText().toString());
            value += 1;
            commentRating.setText(String.valueOf(value));
        }

        public void downVoteComment(Comment comment) {
            current_post.child("comments").child(comment.getCommentID()).child("rating").setValue(ServerValue.increment(-1));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Downvoted comment!", Toast.LENGTH_SHORT);
            toast.show();

            int value = Integer.parseInt(commentRating.getText().toString());
            value -= 1;
            commentRating.setText(String.valueOf(value));
        }
    }
}
