package com.example.ashwin.sqliteuserprofiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.attr.id;

public class AddUserActivity extends AppCompatActivity
    implements ImageDownloadAsync.ImageDownloader
{
    private static final String TAG = AddUserActivity.class.getSimpleName();
    private EditText mUserIdEditText, mUserNameEditText, mImageUrlEditText;
    private String mUserName = "", mImageUrl = "";
    private int mUserId;
    private ImageDownloadAsync.ImageDownloader mImageDownloader;
    private ImageDownloadAsync mImageDownloadAsync;
    private Context mContext;
    private User user;
    private DatabaseHandler databaseHandler;
    private long insertResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        mContext = this;

        mUserIdEditText = (EditText) findViewById(R.id.userIdEditText);
        mUserNameEditText = (EditText) findViewById(R.id.userNameEditText);
        mImageUrlEditText = (EditText) findViewById(R.id.imageUrlEditText);

        databaseHandler = new DatabaseHandler(this);
    }


    public void saveUser(View view)
    {
        try {
            mUserId = Integer.parseInt(mUserIdEditText.getText().toString());
            if ( !databaseHandler.checkIsDataAlreadyInDBorNot("UserProfiles", "id", id) ) {
                mUserName = mUserNameEditText.getText().toString();
                mImageUrl = mImageUrlEditText.getText().toString();
                startImageDownload();
            } else {
                Toast.makeText(this, "Id already exists", Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Enter a number", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void startImageDownload() {
        Log.i(TAG, "startImageDownload");
        String url = mImageUrlEditText.getText().toString();
        mImageDownloadAsync = new ImageDownloadAsync(mContext, url, this);
        mImageDownloadAsync.execute();
    }

    @Override
    public void onDownloadSuccess(Bitmap bitmap) {
        Log.i(TAG, "onDownloadSuccess");
        //compress bitmap
        int newHeight = (int) ( bitmap.getHeight() );
        int newWidth = (int) ( bitmap.getWidth() );
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        user = new User();
        user.setId( mUserId );
        user.setName( mUserName );
        user.setImageurl(mImageUrl);
        user.setBitmapimage(scaledBitmap);
        insertResult = databaseHandler.addUser(user);
        if(insertResult==-1)
        {
            Toast.makeText(this, "User Not Saved", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "User Saved", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onDownloadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
