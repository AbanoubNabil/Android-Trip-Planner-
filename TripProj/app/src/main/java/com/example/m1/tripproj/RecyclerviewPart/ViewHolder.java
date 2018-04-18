package com.example.m1.tripproj.RecyclerviewPart;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m1.tripproj.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    protected TextView tripNameTxtView, timeTxtView;
    protected Button startBtn, deleteBtn;
    protected ImageView tripIcon;
    protected Button reverseDirection;

    public ViewHolder(View itemView) {
        super(itemView);
        tripNameTxtView = (TextView) itemView.findViewById(R.id.tripName);
        startBtn = (Button) itemView.findViewById(R.id.startBtn);
        deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn);
        tripIcon = (ImageView) itemView.findViewById(R.id.tripIcon);
        timeTxtView = (TextView) itemView.findViewById(R.id.time);
        reverseDirection = (Button) itemView.findViewById(R.id.reverseDirBtn);
    }
}
