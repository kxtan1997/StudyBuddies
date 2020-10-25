package com.example.studybuddies;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPostUI extends AppCompatActivity {

    Button submitButton, upVote, downVote;
    int pos;
    String page, pid, userID, username;
    TextView pRating, pTitle, pDescription, pSubject, pUsername;
    EditText commentInput;
    ImageView image;
    RecyclerView commentsRecyclerView;
    DatabaseReference current_post; //get firebase reference of current post to facilitate updating of it
    Toast toast;

    Integer postLocalRaterUIDValue = 0; //cos single listener so need this to update the app locally
    Integer commentLocalRaterUIDValue = 0;

    String postId = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        pRating = findViewById(R.id.postRating);
        pTitle = findViewById(R.id.postTitle);
        pDescription = findViewById(R.id.postDescription);
        pSubject = findViewById(R.id.postSubject);
        pUsername = findViewById(R.id.posterUsername);
        submitButton = findViewById(R.id.submitButton);
        upVote = findViewById(R.id.upVote);
        downVote = findViewById(R.id.downVote);
        commentInput = findViewById(R.id.commentInput);
        image = findViewById(R.id.iv_image);

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

        upVote.setOnClickListener(v -> upVote());

        downVote.setOnClickListener(v -> downVote());

        submitButton.setOnClickListener(v -> postComment());

        DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        current_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                            postId = pid;

                            pUsername.setText(np.posterUsername);
                            pRating.setText(String.valueOf(np.rating));
                            pTitle.setText(np.postTitle);
                            pDescription.setText(np.postDescription);
                            pSubject.setText(np.subject);
                            displayPostImage(pid); //display image of post if any
                            try {
                                postLocalRaterUIDValue = np.getRaterUID().get(userID);
                            } catch (Exception ignored) {
                            }

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

                                    pUsername.setText(posts.get(i).getPosterUsername());
                                    pRating.setText(String.valueOf(posts.get(i).getRating()));
                                    pTitle.setText(posts.get(i).getPostTitle());
                                    pDescription.setText(posts.get(i).getPostDescription());
                                    pSubject.setText(posts.get(i).getSubject());
                                    displayPostImage(pid);
                                    try {
                                        postLocalRaterUIDValue = posts.get(i).getRaterUID().get(userID);
                                    } catch (Exception ignored) {
                                    }
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

    public void displayPostImage(String postId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Post Images/" + postId + ".png");
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ViewGroup.LayoutParams params = image.getLayoutParams();
            if (bitmap.getWidth() < ((View) image.getParent()).getWidth())
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (bitmap.getHeight() > 400)
                params.height = 400;
            else
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            image.setLayoutParams(params);
            image.setImageBitmap(bitmap);
        });
        storageReference.getBytes(ONE_MEGABYTE).addOnFailureListener(e -> {
            ViewGroup.LayoutParams params = image.getLayoutParams();
            image.setLayoutParams(params);
            image.setVisibility(View.GONE);
        });
    }

    public void upVote() {
        if (postLocalRaterUIDValue == 0) { //allow upvote
            current_post.child("rating").setValue(ServerValue.increment(1));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Post Upvoted", Toast.LENGTH_SHORT);
            toast.show();

            int value = Integer.parseInt(pRating.getText().toString()); //update locally in same screen
            value += 1;
            pRating.setText(String.valueOf(value));

            current_post.child("raterUID").child(userID).setValue(ServerValue.increment(1)); //update firebase raterUID
            postLocalRaterUIDValue += 1;
        } else if (postLocalRaterUIDValue == 1) { //already upvoted, so remove current upvote
            current_post.child("rating").setValue(ServerValue.increment(-1));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Upvote Removed", Toast.LENGTH_SHORT);
            toast.show();

            int value = Integer.parseInt(pRating.getText().toString()); //update locally in same screen
            value -= 1;
            pRating.setText(String.valueOf(value));

            current_post.child("raterUID").child(userID).setValue(ServerValue.increment(-1)); //update firebase raterUID
            postLocalRaterUIDValue -= 1;
        } else { //value == -1, so remove downvote from post and add an upvote
            current_post.child("rating").setValue(ServerValue.increment(2));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Post Upvoted", Toast.LENGTH_SHORT);
            toast.show();

            int value = Integer.parseInt(pRating.getText().toString()); //update locally in same screen
            value += 2;
            pRating.setText(String.valueOf(value));

            current_post.child("raterUID").child(userID).setValue(ServerValue.increment(2)); //update firebase raterUID
            postLocalRaterUIDValue += 2;
        }
    }

    public void downVote() {
        if (postLocalRaterUIDValue == 0) { //allow downvote
            current_post.child("rating").setValue(ServerValue.increment(-1));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Post Downvoted", Toast.LENGTH_SHORT);
            toast.show();

            int value = Integer.parseInt(pRating.getText().toString());
            value -= 1;
            pRating.setText(String.valueOf(value));

            current_post.child("raterUID").child(userID).setValue(ServerValue.increment(-1)); //update firebase raterUID
            postLocalRaterUIDValue -= 1;
        } else if (postLocalRaterUIDValue == -1) { //already downvoted, so remove current downvote
            current_post.child("rating").setValue(ServerValue.increment(1));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Upvote Removed", Toast.LENGTH_SHORT);
            toast.show();

            int value = Integer.parseInt(pRating.getText().toString()); //update locally in same screen
            value += 1;
            pRating.setText(String.valueOf(value));

            current_post.child("raterUID").child(userID).setValue(ServerValue.increment(1)); //update firebase raterUID
            postLocalRaterUIDValue += 1;
        } else { //value == 1, so remove upvote from post and add an downvote
            current_post.child("rating").setValue(ServerValue.increment(-2));
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(getApplicationContext(), "Post Downvoted", Toast.LENGTH_SHORT);
            toast.show();

            int value = Integer.parseInt(pRating.getText().toString()); //update locally in same screen
            value -= 2;
            pRating.setText(String.valueOf(value));

            current_post.child("raterUID").child(userID).setValue(ServerValue.increment(-2)); //update firebase raterUID
            postLocalRaterUIDValue -= 2;
        }
    }

    @SuppressLint("ShowToast")
    public void postComment() {
        String subject = commentInput.getText().toString().trim();
        commentInput.getText().clear();

        if (!TextUtils.isEmpty(subject)) {
            String id = current_post.child("comments").push().getKey();

            Comment comment = new Comment(userID, username, id, subject, 0, null);

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
                commentViewHolder.commentUpVote.setOnClickListener(v -> commentViewHolder.upVoteComment(comment));
                commentViewHolder.commentDownVote.setOnClickListener(v -> commentViewHolder.downVoteComment(comment));
                commentViewHolder.commentUsername.setText(comment.getUsername());
                commentViewHolder.commentSubject.setText(comment.getSubject());
                commentViewHolder.commentRating.setText(String.format("%s", comment.getRating()));
                if (comment.getUserID().equals(userID)) {
                    commentViewHolder.deleteButton.setVisibility(View.VISIBLE);
                    commentViewHolder.deleteButton.setOnClickListener(v -> commentViewHolder.deleteComment(comment));
                }
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

        TextView commentUsername, commentSubject, commentRating;
        Button commentUpVote, commentDownVote, deleteButton;
        View mView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            commentUsername = itemView.findViewById(R.id.commentUsername);
            commentSubject = itemView.findViewById(R.id.commentSubject);
            commentRating = itemView.findViewById(R.id.commentRating);
            commentUpVote = itemView.findViewById(R.id.commentUpVote);
            commentDownVote = itemView.findViewById(R.id.commentDownVote);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void upVoteComment(Comment comment) {
            try {
                commentLocalRaterUIDValue = comment.getCommentUID().get(userID);
            } catch (Exception ignored) {
            }
            if (commentLocalRaterUIDValue == 0) { //allow upvote
                current_post.child("comments").child(comment.getCommentID()).child("rating").setValue(ServerValue.increment(1));
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(getApplicationContext(), "Upvoted Comment!", Toast.LENGTH_SHORT);
                toast.show();

                int value = Integer.parseInt(commentRating.getText().toString());
                value += 1;
                commentRating.setText(String.valueOf(value));

                current_post.child("comments").child(comment.getCommentID()).child("commentUID").child(userID).setValue(ServerValue.increment(1)); //update firebase raterUID
                commentLocalRaterUIDValue += 1;
            } else if (commentLocalRaterUIDValue == 1) { //already upvoted, so remove current upvote
                current_post.child("comments").child(comment.getCommentID()).child("rating").setValue(ServerValue.increment(-1));
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(getApplicationContext(), "Upvote Removed!", Toast.LENGTH_SHORT);
                toast.show();

                int value = Integer.parseInt(commentRating.getText().toString());
                value -= 1;
                commentRating.setText(String.valueOf(value));

                current_post.child("comments").child(comment.getCommentID()).child("commentUID").child(userID).setValue(ServerValue.increment(-1)); //update firebase raterUID
                commentLocalRaterUIDValue -= 1;
            } else { //value == -1, so remove downvote from post and add an upvote
                current_post.child("comments").child(comment.getCommentID()).child("rating").setValue(ServerValue.increment(2));
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(getApplicationContext(), "Upvoted Comment!", Toast.LENGTH_SHORT);
                toast.show();

                int value = Integer.parseInt(commentRating.getText().toString());
                value += 2;
                commentRating.setText(String.valueOf(value));

                current_post.child("comments").child(comment.getCommentID()).child("commentUID").child(userID).setValue(ServerValue.increment(2)); //update firebase raterUID
                commentLocalRaterUIDValue += 2;
            }
        }

        public void downVoteComment(Comment comment) {
            try {
                commentLocalRaterUIDValue = comment.getCommentUID().get(userID);
            } catch (Exception ignored) {
            }
            if (commentLocalRaterUIDValue == 0) {
                current_post.child("comments").child(comment.getCommentID()).child("rating").setValue(ServerValue.increment(-1));
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(getApplicationContext(), "Downvoted Comment!", Toast.LENGTH_SHORT);
                toast.show();

                int value = Integer.parseInt(commentRating.getText().toString());
                value += 1;
                commentRating.setText(String.valueOf(value));

                current_post.child("comments").child(comment.getCommentID()).child("commentUID").child(userID).setValue(ServerValue.increment(-1)); //update firebase raterUID
                commentLocalRaterUIDValue -= 1;
            } else if (commentLocalRaterUIDValue == -1) {
                current_post.child("comments").child(comment.getCommentID()).child("rating").setValue(ServerValue.increment(1));
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(getApplicationContext(), "Downvote Removed!", Toast.LENGTH_SHORT);
                toast.show();

                int value = Integer.parseInt(commentRating.getText().toString());
                value += 1;
                commentRating.setText(String.valueOf(value));

                current_post.child("comments").child(comment.getCommentID()).child("commentUID").child(userID).setValue(ServerValue.increment(1)); //update firebase raterUID
                commentLocalRaterUIDValue += 1;
            } else {
                current_post.child("comments").child(comment.getCommentID()).child("rating").setValue(ServerValue.increment(-2));
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(getApplicationContext(), "Downvoted Comment!", Toast.LENGTH_SHORT);
                toast.show();

                int value = Integer.parseInt(commentRating.getText().toString());
                value -= 2;
                commentRating.setText(String.valueOf(value));

                current_post.child("comments").child(comment.getCommentID()).child("commentUID").child(userID).setValue(ServerValue.increment(-2)); //update firebase raterUID
                commentLocalRaterUIDValue -= 2;
            }
        }

        public void deleteComment(Comment comment) {
            current_post.child("comments").child(comment.getCommentID()).removeValue();
        }
    }
}
