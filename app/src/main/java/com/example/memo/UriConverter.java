package com.example.memo;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class UriConverter {
    Context context;

    public UriConverter(Context context) {
        this.context = context;
    }
    /* Uri -> 경로 */
    public String getPathFromUri(Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        String convertedPath;

        if(cursor != null) {
            cursor.moveToNext();
            convertedPath = cursor.getString(cursor.getColumnIndex("_data"));

            cursor.close();
        }
        else {
            return null;
        }

        return convertedPath;
    }

    /* 경로 -> Uri */
    public Uri getUriFromPath(String contentPath) {
        Cursor cursor = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                "_data = '" + contentPath + "'",
                null,
                null);
        int columnIndex;
        Uri convertedUri;

        if(cursor != null) {
            cursor.moveToNext();
            columnIndex = cursor.getInt(cursor.getColumnIndex("_id"));
            convertedUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columnIndex);

            cursor.close();
        }
        else {
            return null;
        }

        return convertedUri;
    }
}
