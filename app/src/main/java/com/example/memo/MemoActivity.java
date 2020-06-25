package com.example.memo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MemoActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editContent;
    private LinearLayout layoutPhotoArea;
//    private SquareImageView imageAttaced;

    private InputMethodManager inputMethodManager;

    private ArrayList<String> attachedPhotoPathList = new ArrayList<>();  // 첨부된 사진들을 저장할 리스트
    private ArrayList<PhotoFrameLayout> attachedPhotoList = new ArrayList<>();
    private int memoListIndex;
    String tableName = null;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_memo);
//
//        getSupportActionBar().setElevation(0);
//
//        /* DB 구현 중 */
//        memoDB = new MemoDatabase(this);
//        memoListIndex = getIntent().getIntExtra("index", -1);
//        tableName = "ATTACHED_IMAGE_TABLE_" + memoListIndex;
//        memoDB.createAttachedImageTable(tableName);
//
//        inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        editTitle = (EditText) findViewById(R.id.edit_title);
//        editContent = (EditText) findViewById(R.id.edit_content);
//        layoutPhotoArea = (LinearLayout) findViewById(R.id.layout_photo_area);
//
//        editTitle.requestFocus();
//        showKeyboard(editTitle, true);  // View, 키보드 상태 (올리기: true / 내리기: false)
//
//        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View view, boolean hasFocus) {
//                invalidateOptionsMenu();
//
//                if(hasFocus) {
//                    showKeyboard(view, true);
//                }
//                else {
//                    showKeyboard(view, false);
//                }
//            }
//        });
//
//        editContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View view, boolean hasFocus) {
//                invalidateOptionsMenu();
//
//                if(hasFocus) {
//                    showKeyboard(view, true);
//                }
//                else {
//                    showKeyboard(view, false);
//                }
//            }
//        });
//
//        /* from memo list onClickItem */
//        if(getIntent().getIntExtra("reqCode", -2) == RequestCodes.ACTIVITY_REQUEST_CODE_MEMO_EDIT) {
//            String title = getIntent().getStringExtra("title");
//            String content = getIntent().getStringExtra("content");
//
//            attachedPhotoPathList = memoDB.selectAllImagePath(tableName);
//
//            for(String photoPath : attachedPhotoPathList) {
//                Drawable photoDrawable;
//                Bitmap croppedPhoto;
//
//                PhotoFrameLayout attachedPhoto = new PhotoFrameLayout(this);  // 이미지 뷰 객체 생성
//                attachedPhoto.setLayoutParams(photoLayoutParams());
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 8;
//
//                Bitmap photoBitmap = BitmapFactory.decodeFile(photoPath, options);
//                float imageWidth = photoBitmap.getWidth();
//                float imageHeight = photoBitmap.getHeight();
//                float widthHeightRate = imageWidth / imageHeight;
//
//                /* 불러온 이미지 자르기(center crop) */
//                if(widthHeightRate > 1) {  // 이미지의 가로가 세로보다 긴 경우
//                    croppedPhoto = Bitmap.createBitmap(photoBitmap,
//                            photoBitmap.getWidth()/2 - photoBitmap.getHeight()/2,
//                            0,
//                            photoBitmap.getHeight(),
//                            photoBitmap.getHeight());
//                }
//                else {  // 이미지의 세로가 가로보다 긴 경우
//                    croppedPhoto = Bitmap.createBitmap(photoBitmap,
//                            0,
//                            photoBitmap.getHeight()/2 - photoBitmap.getWidth()/2,
//                            photoBitmap.getWidth(),
//                            photoBitmap.getWidth());
//                }
//                if(croppedPhoto != photoBitmap) { photoBitmap.recycle(); }
//
//                photoDrawable = new BitmapDrawable(getResources(), croppedPhoto);
//                attachedPhoto.setBackground(photoDrawable);
//
//                attachedPhotoList.add(attachedPhoto);
//                layoutPhotoArea.addView(attachedPhoto);
//            }
//
//            editTitle.setText(title);
//            editContent.setText(content);
//        }
//
//        Button deletePhotoButton = (Button) findViewById(R.id.delete_photo_button);
//        deletePhotoButton.setOnClickListener(view -> {
//            ArrayList<Integer> deleteIndex = new ArrayList<>();
//
//            for(int i=layoutPhotoArea.getChildCount()-1; i>=0; i--) {
//                if(attachedPhotoList.get(i).isCheckBoxChecked()) {
//                    deleteIndex.add(i);
//                    layoutPhotoArea.removeView(attachedPhotoList.get(i));
//                    memoDB.deleteImage(tableName, i);
//                }
//            }
//
//            /* DB 재정렬 */
//            Collections.sort(deleteIndex);
//            for(int i=0; i<layoutPhotoArea.getChildCount(); i++) {
//                memoDB.updateImageIndex(tableName, deleteIndex.get(i), i);
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.actionbar_memo, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if(editTitle.isFocused() || editContent.isFocused()) {
//            menu.findItem(R.id.action_edit_memo).setVisible(false);
//            menu.findItem(R.id.action_delete_memo).setVisible(false);
//            menu.findItem(R.id.action_save_memo).setVisible(true);
//            menu.findItem(R.id.action_insert_photo).setVisible(true);
//            menu.findItem(R.id.action_edit_photo).setVisible(true);
//
//            /* 이미지 체크박스 설정 */
//            for(PhotoFrameLayout photo : attachedPhotoList) {
//                photo.setCheckBoxVisibility(true);
//            }
//        }
//        else {
//            menu.findItem(R.id.action_edit_memo).setVisible(true);
//            menu.findItem(R.id.action_delete_memo).setVisible(true);
//            menu.findItem(R.id.action_save_memo).setVisible(false);
//            menu.findItem(R.id.action_insert_photo).setVisible(false);
//            menu.findItem(R.id.action_edit_photo).setVisible(false);
//
//            /* 이미지 설정 */
//            for(PhotoFrameLayout photo : attachedPhotoList) {
//                photo.setCheckBoxVisibility(false);
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_delete_memo:
//                return true;
//            case R.id.action_edit_memo:
//                editTitle.requestFocus();
//                return true;
//            case R.id.action_save_memo:
//                editTitle.clearFocus();
//                editContent.clearFocus();
//                saveMemo();
//                return true;
//            case R.id.action_insert_photo:
//                checkPermission();
//
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                startActivityForResult(intent, RequestCodes.ACTIVITY_REQUEST_CODE_PHOTO_INSERT);
//                return true;
//            case R.id.action_edit_photo:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if(editTitle.isFocused() || editContent.isFocused()) {
//            editTitle.clearFocus();
//            editContent.clearFocus();
//        }
//        else {
//            finish();
//        }
//    }
//
//    public void showKeyboard(final View view, final boolean state) {
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(state) {
//                    inputMethodManager.showSoftInput(view, 0);
//                }
//                else {
//                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            }
//        }, 30);
//    }
//
//    public void saveMemo() {
//        Intent intent = new Intent();
//        intent.putExtra("index", memoListIndex);
//
//        /* 썸네일 저장 */
//        if(attachedPhotoList.size() > 0) {
//            intent.putExtra("thumbnail", attachedPhotoPathList.get(0));  // 첨부된 사진 중 첫 번째를 썸네일로 지정
//        }
//        else {
//            intent.putExtra("thumbnail", "");
//        }
//
//        /* 제목 저장 */
//        if(!editTitle.getText().toString().equals("")) { intent.putExtra("title", editTitle.getText().toString()); }
//        else { intent.putExtra("title", ""); }
//
//        /* 내용 저장 */
//        if(!editContent.getText().toString().equals("")) { intent.putExtra("content", editContent.getText().toString()); }
//        else { intent.putExtra("content", ""); }
//
//        setResult(Activity.RESULT_OK, intent);
//    }
//
//    private void checkPermission() {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                ActivityCompat.requestPermissions(
//                        this,
//                        new String[]{Manifest.permission.CAMERA},
//                        RequestCodes.PERMISSION_REQUEST_CODE_CAMERA
//                );
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int reqCode, int resCode, Intent data) {
//        switch(reqCode) {
//            case RequestCodes.ACTIVITY_REQUEST_CODE_PHOTO_INSERT:
//                if(resCode == Activity.RESULT_OK) {
//                    if(data.getData() != null) {
//                        try {
//                            Uri photoUri = data.getData();  // 앨범에서 선택한 사진
//                            UriConverter uriConverter = new UriConverter(this);
//                            String photoPath = uriConverter.getPathFromUri(photoUri);
//
//                            Drawable photoDrawable;
//                            Bitmap croppedPhoto;
//
//                            PhotoFrameLayout imageAttached = new PhotoFrameLayout(this);  // 이미지 뷰 객체 생성
//                            imageAttached.setLayoutParams(photoLayoutParams());
//
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 8;
//                            Bitmap photoBitmap = BitmapFactory.decodeFile(photoPath, options);
//                            float imgWidth = photoBitmap.getWidth();
//                            float imgHeight = photoBitmap.getHeight();
//                            float widthHeightRate = imgWidth / imgHeight;
//
//                            /* 불러온 이미지 자르기(center crop) */
//                            if(widthHeightRate > 1) {  // 이미지의 가로가 세로보다 긴 경우
//                                croppedPhoto = Bitmap.createBitmap(photoBitmap,
//                                        photoBitmap.getWidth()/2 - photoBitmap.getHeight()/2,
//                                        0,
//                                        photoBitmap.getHeight(),
//                                        photoBitmap.getHeight());
//                            }
//                            else {  // 이미지의 세로가 가로보다 긴 경우
//                                croppedPhoto = Bitmap.createBitmap(photoBitmap,
//                                        0,
//                                        photoBitmap.getHeight()/2 - photoBitmap.getWidth()/2,
//                                        photoBitmap.getWidth(),
//                                        photoBitmap.getWidth());
//                            }
//
//                            if(croppedPhoto != photoBitmap) { photoBitmap.recycle(); }
//
//                            photoDrawable = new BitmapDrawable(getResources(), croppedPhoto);
//                            imageAttached.setBackground(photoDrawable);
//
//                            int index = layoutPhotoArea.getChildCount();
//                            layoutPhotoArea.addView(imageAttached);
//
//                            attachedPhotoPathList.add(photoPath);
//
//                            memoDB.insertImagePath(tableName, index, photoPath);  // DB에 이미지 경로 저장
//
//                            // test
//                            attachedPhotoList.add(imageAttached);
//                        }
//                        catch (Exception e) {
//                            Log.e("TAKE", e.toString());
//                        }
//                    }
//                }
//                break;
//        }
//    }
//
//    private LinearLayout.LayoutParams photoLayoutParams() {
//        return new LinearLayout.LayoutParams(400, 400);
//    }
}
