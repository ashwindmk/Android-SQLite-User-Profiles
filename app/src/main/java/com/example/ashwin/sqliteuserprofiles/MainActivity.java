package com.example.ashwin.sqliteuserprofiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseHandler databaseHandler;
    private EditText mSearchUserIdEditText, mDeleteUserIdEditText;
    private long insertResult;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchUserIdEditText = (EditText) findViewById(R.id.searchUserIdEditText);
        mDeleteUserIdEditText = (EditText) findViewById(R.id.deleteUserIdEditText);

        databaseHandler = new DatabaseHandler(this);
    }


    public void viewUsersInDB(View view)
    {
        Intent intent = new Intent(MainActivity.this, ViewUsersActivity.class);
        startActivity(intent);
    }


    public void viewSingleUserInDB(View view)
    {
        if( !(mSearchUserIdEditText.getText().toString()).equals("") ) {
            try {
                int id = Integer.parseInt(mSearchUserIdEditText.getText().toString());
                Intent intent = new Intent(MainActivity.this, SingleUserProfile.class);
                intent.putExtra("id", id);
                startActivity(intent);
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Enter a number", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter a number", Toast.LENGTH_SHORT).show();
        }
    }


    public void clearDB(View view)
    {
        databaseHandler.clearAll();
        Toast.makeText(this, "Database Cleared", Toast.LENGTH_LONG).show();
    }


    public void addUser(View view)
    {
        Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
        startActivity(intent);
    }


    public void deleteUser(View view)
    {
        if( !(mDeleteUserIdEditText.getText().toString()).equals("") ) {
            try {
                int id = Integer.parseInt(mDeleteUserIdEditText.getText().toString());
                if( databaseHandler.checkIsDataAlreadyInDBorNot( "UserProfiles", "id", id ) ) {
                    databaseHandler.deleteRecord(id);
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
                    mDeleteUserIdEditText.setText("");
                } else {
                    Toast.makeText(this, "User id does not exist", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Enter a number", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter a number", Toast.LENGTH_SHORT).show();
        }
    }
}
