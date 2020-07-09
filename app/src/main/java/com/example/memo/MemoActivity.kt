package com.example.memo;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*

import androidx.appcompat.app.AppCompatActivity;
import com.example.memo.dto.Memo
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_memo.*
import kotlinx.android.synthetic.main.activity_memo.app_toolbar

import kotlin.math.max

class MemoActivity : AppCompatActivity() {

    private var requestCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
        setSupportActionBar(app_toolbar)

        requestCode = intent.getIntExtra("reqCode", Constants.INVALID_CODE)

        when (requestCode) {
            Constants.REQUEST_CREATE_MEMO -> {
                Log.d("Mode", "Create")
            }

            Constants.REQUEST_EDIT_MEMO -> {
                Log.d("Mode", "Edit")

                val position = intent.getIntExtra("position", Constants.INVALID_POSITION)

                Realm.getDefaultInstance().use {
                    val memo: Memo? = it
                            .where(Memo::class.java)
                            .equalTo("position", position)
                            .findFirst()
                    val title: String? = memo?.title
                    val content: String? = memo?.content

                    if (title == null || title.isEmpty()) edit_title.setText("")
                    else edit_title.setText(title)

                    if (content == null || content.isEmpty()) edit_content.setText("")
                    else edit_content.setText(content)
                }
            }

            else -> {
                Log.d("Error", "Invalid code")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_memo, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (edit_title.isFocused || edit_content.isFocused) setActionbarToEditMode(menu)
        else setActionbarToReadMode(menu)
        return true
    }

    private fun setActionbarToEditMode(menu: Menu?) {
        menu?.let {
            it.findItem(R.id.action_edit_memo).isVisible = false
            it.findItem(R.id.action_delete_memo).isVisible = false
            it.findItem(R.id.action_save_memo).isVisible = true
            it.findItem(R.id.action_insert_photo).isVisible = true
            it.findItem(R.id.action_edit_photo).isVisible = true
        }
    }

    private fun setActionbarToReadMode(menu: Menu?) {
        menu?.let {
            it.findItem(R.id.action_edit_memo).isVisible = true
            it.findItem(R.id.action_delete_memo).isVisible = true
            it.findItem(R.id.action_save_memo).isVisible = false
            it.findItem(R.id.action_insert_photo).isVisible = false
            it.findItem(R.id.action_edit_photo).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_memo -> {
                return true
            }

            R.id.action_edit_memo -> {
                edit_title.requestFocus()
                return true
            }

            R.id.action_save_memo -> {
                when (requestCode) {
                    Constants.REQUEST_CREATE_MEMO -> {
                        val memo = Memo()

                        memo.id = getValidId()
                        memo.title = edit_title.text.toString()
                        memo.content = edit_content.text.toString()
                        memo.imageAddress
                        memo.position = getValidPosition()

                        saveMemo(memo)
                    }

                    Constants.REQUEST_EDIT_MEMO -> {
                        val position = intent.getIntExtra("position", Constants.INVALID_POSITION)
                        var memo: Memo? = null

                        Realm.getDefaultInstance().use {
                            it.executeTransaction { realm ->
                                memo = realm
                                        .where(Memo::class.java)
                                        .equalTo("position", position)
                                        .findFirst()?.apply {
                                            this.title = edit_title.text.toString()
                                            this.content = edit_content.text.toString()
                                            this.imageAddress
                                            this.position = position
                                        }
                            }
                        }
                        saveMemo(memo)
                    }

                    else -> {

                    }
                }

                edit_title.clearFocus()
                edit_content.clearFocus()
                return true
            }

            R.id.action_insert_photo -> {
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getValidId(): Long {
        Realm.getDefaultInstance().use {
            val maxId = it.where(Memo::class.java).max("id")
            return maxId?.toLong()?.plus(1) ?: 0
        }
    }

    private fun getValidPosition(): Int {
        Realm.getDefaultInstance().use {
            val maxPosition = it.where(Memo::class.java).max("position")
            return maxPosition?.toInt()?.plus(1) ?: 0
        }
    }

    private fun saveMemo(memo: Memo?) {
        if (memo != null) {
            Realm.getDefaultInstance().use { realm ->
                realm.executeTransaction {
                    realm.copyToRealm(memo)
                }
            }
        } else {
            Toast.makeText(this, "메모장 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

        this.finish()
    }
}

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R . id . action_insert_photo :
//                checkPermission();
//
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                startActivityForResult(intent, RequestCodes.ACTIVITY_REQUEST_CODE_PHOTO_INSERT);
//                return true;
//                case R . id . action_edit_photo :
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
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
