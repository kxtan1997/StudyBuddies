package com.example.studybuddies;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class CreatePostUI extends AppCompatActivity {

    String userID, username;

    EditText postTitle;
    EditText postDescription;
    Button postValue;
    Spinner subjects;
    private ImageView image;
    private Uri imageUri;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    DatabaseReference databasePost;
    StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Post Images");

    Toast toast;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        databasePost = FirebaseDatabase.getInstance().getReference("posts");

        postTitle = findViewById(R.id.postTitle);
        postDescription = findViewById(R.id.postDescription);
        postValue = findViewById(R.id.postValue);
        subjects = findViewById(R.id.subjects);
        Button uploadButton = findViewById(R.id.upload_button);
        image = findViewById(R.id.iv_image);
        uploadButton.setOnClickListener(v -> {
            if (CreatePostUI.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                CreatePostUI.this.requestPermissions(permissions, PERMISSION_CODE);
            } else {
                CreatePostUI.this.pickImageFromGallery();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
        }

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

        postValue.setOnClickListener(v -> addPostToDB());
    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            assert data != null;
            image.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }

    void saveImage(Context context, Uri imageUri, String postId) {
        if (imageUri != null) {
            fileRef.child(postId + "." + getFileExtension(context, imageUri)).putFile(imageUri);
        }
    }

    private String getFileExtension(Context context, Uri imageUri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    private void addPostToDB() {
        String postDes = postDescription.getText().toString().trim();
        String title = postTitle.getText().toString().trim();
        String sub = subjects.getSelectedItem().toString();

        if (!TextUtils.isEmpty(postDes) && !TextUtils.isEmpty(title)) {
            if (sub.equals("-")){
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(this, "Have to fill subject field", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                String id = databasePost.push().getKey();

                System.out.println(username);
                Post post = new Post(username, id, title, postDes, sub, null, 0, null);
                assert id != null;
                databasePost.child(id).setValue(post);
                saveImage(this, imageUri, id);

                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(this, "Post created", Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(this, MainMenuUI.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        }
        else {
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(this, "This field cannot be empty", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}