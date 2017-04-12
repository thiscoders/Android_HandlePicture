package meitu.android.handlepicture;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    // 菜单标识码
    private final int MENU_TAKE_PICTURE = 0;
    private final int MENU_OPEN_PICTURE = 1;
    private final int MENU_SAVE_PICTURE = 2;
    private final int MENU_HANDLE_PICTURE = 3;
    private final int MENU_ABOUT_APP = 4;
    //图片
    private Bitmap srcBitmap = null;
    private String tempPath = null;
    //寻找控件
    private ImageView iv_picture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_picture = (ImageView) this.findViewById(R.id.iv_picture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_TAKE_PICTURE, 0, "现拍一张");
        menu.add(1, MENU_OPEN_PICTURE, 1, "打开图片");
        menu.add(2, MENU_SAVE_PICTURE, 2, "保存图片");
        menu.add(3, MENU_HANDLE_PICTURE, 3, "处理图片");
        menu.add(4, MENU_ABOUT_APP, 4, "关于软件");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_TAKE_PICTURE:
                takePictureByCamera();
                break;
            case MENU_OPEN_PICTURE:
                pickPic();
                break;
            case MENU_SAVE_PICTURE:
                Toast.makeText(MainActivity.this, "保存图片", Toast.LENGTH_SHORT).show();
                break;
            case MENU_HANDLE_PICTURE:
                Toast.makeText(MainActivity.this, "处理图片", Toast.LENGTH_SHORT).show();
                break;
            case MENU_ABOUT_APP:
                Toast.makeText(MainActivity.this, "这是一个图片处理软件", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 选择图片
     */
    public void pickPic() {
        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(intent, this.MENU_OPEN_PICTURE);
    }

    /**
     * 相机拍照
     */
    public void takePictureByCamera() {
        //创建拍照照片保存位置
        String path = "/storage/emulated/0/meitu_pic/";
        File file = getDownFile(path);
        tempPath = file.getAbsolutePath();
        //解决Android N 的 Uri.fromFile(file)不兼容的问题
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        Uri fileUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //开启拍照意图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, this.MENU_TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.MENU_OPEN_PICTURE) {
            if (resultCode == RESULT_OK) {
                Uri picUri = data.getData();
                String[] column = new String[]{MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(picUri, column, null, null, null);
                String picPath = null;
                while (cursor.moveToNext()) {
                    picPath = cursor.getString(cursor.getColumnIndex(column[0]));
                }
                cursor.close();
                srcBitmap = BitmapFactory.decodeFile(picPath);
                Toast.makeText(MainActivity.this, picPath, Toast.LENGTH_LONG).show();
                Log.i(TAG, ",,,,," + picPath);
                iv_picture.setImageBitmap(srcBitmap);
            } else {
                Toast.makeText(MainActivity.this, "no picture!", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == MENU_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "拍照成功！", Toast.LENGTH_LONG).show();
                srcBitmap = BitmapFactory.decodeFile(tempPath);
                iv_picture.setImageBitmap(srcBitmap);
            } else {
                Toast.makeText(MainActivity.this, "拍照失败！", Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * 根据路径返回文件
     *
     * @param path
     * @return
     */
    private File getDownFile(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        return new File(dir, System.currentTimeMillis() + ".jpg");
    }
}
