package com.example.ashwin.sqliteuserprofiles;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SingleUserProfile extends AppCompatActivity {

    private DatabaseHandler databaseHandler;
    private TextView mSingleUserTextView;
    private ImageView mImageView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user_profile);

        int id = getIntent().getIntExtra("id", 0);

        mSingleUserTextView = (TextView) findViewById(R.id.singleUserText);
        mImageView = (ImageView) findViewById(R.id.singleUserImage);

        databaseHandler = new DatabaseHandler(this);

        user = databaseHandler.getUser(id);

        if( user != null ) {
            mSingleUserTextView.setText( user.getId() + " : " +user.getName() );

            Bitmap bitmap = user.getBitmapimage();
            int newWidth = (int) (bitmap.getWidth());
            int newHeight = (int) (bitmap.getHeight());
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            mImageView.setImageBitmap(scaledBitmap);
        } else {
            Toast.makeText(this, id + " does not exist in database", Toast.LENGTH_SHORT).show();
        }

    }
}
