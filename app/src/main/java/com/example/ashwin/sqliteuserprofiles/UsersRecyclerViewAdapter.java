package com.example.ashwin.sqliteuserprofiles;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by ashwin on 2/11/16.
 */

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<User> usersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mNameTextView;

        public MyViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mNameTextView = (TextView) view.findViewById(R.id.name);

        }
    }

    public UsersRecyclerViewAdapter(ArrayList<User> usersList) {
        this.usersList = usersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = usersList.get(position);

        //convert bitmap to byte[]
        //Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.banner2);

        /*int bytes = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] imageBytes =  buffer.array();*/

        //byte[] imageBytes = getBitmapAsByteArray(bitmap);

        //convert byte[] to bitmap
        //Bitmap bitmapImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        Bitmap bitmap = user.getBitmapimage();
        Bitmap scaledBitmap = null;

        if( bitmap != null ) {
            //decompress bitmap if compressed
            int newHeight = (int) (bitmap.getHeight() );
            int newWidth = (int) (bitmap.getWidth() );
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }

        holder.mImageView.setImageBitmap(scaledBitmap);
        holder.mNameTextView.setText(user.getId() + " : " + user.getName());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
