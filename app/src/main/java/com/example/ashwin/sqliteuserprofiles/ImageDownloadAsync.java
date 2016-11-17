package com.example.ashwin.sqliteuserprofiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by ashwin on 17/11/16.
 */

public class ImageDownloadAsync extends AsyncTask<Void, Void, Bitmap> {

    private static final String TAG = "ImageDownloadAsync";
    private DatabaseHandler mDatabaseHandler;
    private String imageUrl;

    public interface ImageDownloader
    {
        void startImageDownload();

        void onDownloadSuccess(Bitmap bitmap);

        void onDownloadFailed(String message);
    }

    private Context mContext;
    private ImageDownloadAsync.ImageDownloader mImageDownloader;

    public ImageDownloadAsync(Context context, String imageUrl, ImageDownloadAsync.ImageDownloader imageDownloader)
    {
        this.mContext = context;
        this.imageUrl = imageUrl;
        this.mImageDownloader = imageDownloader;
        this.mDatabaseHandler = new DatabaseHandler(context);
    }

    private String message = "";
    private Bitmap bitmap;

    @Override
    protected Bitmap doInBackground(Void... params)
    {
        bitmap = null;
        Log.i(TAG, "do in background");

        if( !mDatabaseHandler.checkIsStringAlreadyInDBorNot( "UserProfiles", "imageurl", imageUrl ) ) {

            ImageLoader imageLoader = ImageLoader.getInstance();

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    mContext.getApplicationContext())
                    .threadPoolSize(5)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .build();

            ImageLoader.getInstance().init(config);

            bitmap = imageLoader.loadImageSync(imageUrl);

            imageLoader.destroy();

            if (bitmap != null) {
                message = "Download success";
            } else {
                message = "Download failed";
            }
        }
        else
        {
            message = "Image already exists";
            User user = mDatabaseHandler.getUserFromAnyField("UserProfiles", "imageurl", imageUrl);
            if( user != null ) {
                bitmap = user.getBitmapimage();
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmamp)
    {
        super.onPostExecute(bitmamp);
        Log.i(TAG, "on post execute");
        if( message.equals("Download success") && mImageDownloader != null )
        {
            Log.i(TAG, "on post execute download success");
            mImageDownloader.onDownloadSuccess(bitmap);
        }
        else if( message.equals("Image already exists") && mImageDownloader != null )
        {
            Log.i(TAG, "on post execute image already exists");
            mImageDownloader.onDownloadSuccess(bitmap);
        }
        else if( bitmap == null && mImageDownloader != null )
        {
            Log.i(TAG, "on post execute download failed");
            mImageDownloader.onDownloadFailed(message);
        }
    }
}
