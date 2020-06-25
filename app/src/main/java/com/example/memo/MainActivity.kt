package com.example.memo

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memo.adapter.MemoRecyclerAdapter
import com.example.memo.dto.Memo
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val memoAdapter = MemoRecyclerAdapter()
    private val memoRealm = Realm.getDefaultInstance()
    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        recycler_memo.adapter = memoAdapter
        recycler_memo.layoutManager = LinearLayoutManager(this)  // Default vertical

        memoRealm.addChangeListener { memoAdapter.notifyDataSetChanged() }

        memoAdapter.setOnItemClickListener(object : MemoRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (!editMode) {
                    val intent = Intent(this@MainActivity, MemoActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        delete_memo_button.setOnClickListener {
            val checkedMemoPositions: Set<Int> = memoAdapter.getCheckedMemoPositions()

            for (position in checkedMemoPositions) {
                Realm.getDefaultInstance().use {
                    val results = it.where(Memo::class.java).findAll()

                    it.executeTransaction {
                        val checkedMemo: Memo? = results[position]
                        checkedMemo?.deleteFromRealm()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create_memo -> {
                val intent = Intent(this, MemoActivity::class.java)
                intent.putExtra("reqCode", Constants.REQUEST_CREATE_MEMO)
                startActivity(intent)
                return true
            }

            R.id.action_edit_memo_list -> {
                editMode = true
                edit_navi.visibility = View.VISIBLE
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun checkPermissions() {
        val requiredPermissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    val permissionRequestDialog = AlertDialog.Builder(this)
                    permissionRequestDialog
                            .setTitle("권한 요청")
                            .setMessage("Memo 앱이 (카메라, 외부 저장소 쓰기/읽기)를 사용할 수 있게 허용하시겠습니까?")
                            .setPositiveButton("확인") { _, _ ->
                                ActivityCompat.requestPermissions(
                                        this,
                                        requiredPermissions,
                                        Constants.REQUEST_PERMISSIONS
                                )
                            }
                            .setNegativeButton("취소") { _, _ -> }
                            .show()
                } else {
                    ActivityCompat.requestPermissions(
                            this,
                            requiredPermissions,
                            Constants.REQUEST_PERMISSIONS
                    )
                }
            }
        }
    }

//    override fun onActivityResult(int reqCode, int resCode, Intent data) {
//        switch(reqCode) {
//            case RequestCodes . ACTIVITY_REQUEST_CODE_MEMO_CREATE :
//            if (resCode == Activity.RESULT_OK) {
//                int index = - 1;
//                String thumbnailPath = null;
//                String title = null;
//                String content = null;
//
//                if (data != null) {
//                    index = data.getIntExtra("index", -1);
//
//                    if (!data.getStringExtra("thumbnail").equals("")) {
//                        thumbnailPath = data.getStringExtra("thumbnail");
//                    }
//
//                    if (!data.getStringExtra("title").equals("")) {
//                        title = data.getStringExtra("title");
//                    }
//
//                    content = data.getStringExtra("content");
//                }
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 8;
//                Bitmap thumbnail = BitmapFactory . decodeFile (thumbnailPath, options);
//
//                memoAdapter.addMemo(index, thumbnail, title, content);
//                memoAdapter.notifyDataSetChanged();
//
//                memoDB.insertMemoData(index, thumbnailPath, title, content);
//                memoCount += 1;
//            }
//            break;
//            case RequestCodes . ACTIVITY_REQUEST_CODE_MEMO_EDIT :
//            if (resCode == Activity.RESULT_OK) {
//                int index = - 1;
//                String thumbnailPath = null;
//                String title = null;
//                String content = null;
//
//                if (data != null) {
//                    index = data.getIntExtra("index", -1);
//                    thumbnailPath = data.getStringExtra("thumbnail");
//                    title = data.getStringExtra("title");
//                    content = data.getStringExtra("content");
//                }
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 8;
//                Bitmap thumbnail = BitmapFactory . decodeFile (thumbnailPath, options);
//
//                memoAdapter.editMemo(index, thumbnail, title, content);
//                memoAdapter.notifyDataSetChanged();
//
//                memoDB.updateMemoItem(index, thumbnailPath, title, content);
//            }
//            break;
//        }
//    }
}
