package com.example.ashwin.sqliteuserprofiles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewUsersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<User> usersList = new ArrayList<>();
    private DatabaseHandler databaseHandler;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiles);

        mRecyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        databaseHandler = new DatabaseHandler(this);
        usersList = databaseHandler.getAllUsers();

        if( usersList.size() == 0 )
        {
            Toast.makeText(this, "No users in database", Toast.LENGTH_LONG).show();
        }

        mAdapter = new UsersRecyclerViewAdapter(usersList);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = usersList.get(position);
                Toast.makeText(getApplicationContext(), user.getName() + " is selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                User user = usersList.get(position);
                Toast.makeText(getApplicationContext(), user.getName() + " is long pressed", Toast.LENGTH_LONG).show();
            }
        }));

        mAdapter.notifyDataSetChanged();
    }
}
