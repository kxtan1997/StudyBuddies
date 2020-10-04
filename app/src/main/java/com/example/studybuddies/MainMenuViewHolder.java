package com.example.studybuddies;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MainMenuViewHolder extends RecyclerView.ViewHolder {

    TextView postTitle, postSubject, postRating;
    static ConstraintLayout parentLayout;

    public MainMenuViewHolder(@NonNull View itemView) {
        super(itemView);

        postTitle = itemView.findViewById(R.id.singlePostTitle);
        postSubject = itemView.findViewById(R.id.singlePostSubject);
        postRating = itemView.findViewById(R.id.singleRating);
        parentLayout = itemView.findViewById(R.id.singleCl);
    }
}
