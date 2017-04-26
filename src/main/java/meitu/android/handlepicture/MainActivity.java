package meitu.android.handlepicture;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import meitu.android.utils.CommonUtils;
import meitu.android.utils.ImageSaver;
import meitu.android.utils.SmartImageUtils;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    // 菜单标识码
    private final int MENU_TAKE_PICTURE = 0;
    private final int MENU_OPEN_PICTURE = 1;
    private final int MENU_SAVE_PICTURE = 2;
    private final int MENU_ABOUT_APP = 3;

    //按钮开启Activity识别码
    private final int BTN_CUT_APP = 10;
    private final int BTN_ROTATE_APP = 20;
    private final int BTN_SCALE_APP = 30;
    private final int BTN_REVERSAL_APP = 40;
    private final int BTN_DRAW_APP = 50;

    //处理过的最新图片
    private Bitmap handleBitmap;
    //图片显示控件
    private ImageView iv_picture;
    //保存图片进度条
    private ProgressBar pb_save;
    //图片绝对路径
    private String picAbsPath;

    private ImageSaver imageSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_picture = (ImageView) this.findViewById(R.id.iv_picture);
        pb_save = (ProgressBar) this.findViewById(R.id.pb_save);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_TAKE_PICTURE, 0, "现拍一张");
        menu.add(1, MENU_OPEN_PICTURE, 1, "打开图片");
        menu.add(2, MENU_SAVE_PICTURE, 2, "保存图片");
        menu.add(3, MENU_ABOUT_APP, 3, "关于软件");
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
                Bitmap bitmap = BitmapFactory.decodeFile(picAbsPath);
                imageSaver = new ImageSaver(MainActivity.this, bitmap, "", pb_save);
                imageSaver.execute();
                break;
            case MENU_ABOUT_APP:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Activity返回结果处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*设置图片的拷贝显示到imageview上 */
        if (requestCode == this.MENU_OPEN_PICTURE) { // 选择图片
            if (resultCode == RESULT_OK) {
                Object[] objs = SmartImageUtils.getImage(MainActivity.this, data);//这个图片是可以直接操作的,同时记录绝对路径
                Bitmap bitmap = (Bitmap) objs[0]; //可修改的图片bitmap对象
                picAbsPath = (String) objs[1];  //图片绝对路径
                iv_picture.setImageBitmap(bitmap);
            } else {
                Toast.makeText(MainActivity.this, "no picture!", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == MENU_TAKE_PICTURE) { //现拍照片
            if (resultCode == RESULT_OK) {
                Bitmap srcBitmap = BitmapFactory.decodeFile(picAbsPath);
                Bitmap copyBitmap = SmartImageUtils.getCopyImage(srcBitmap);// 拷贝的图片，这个图片可以直接修改
                iv_picture.setImageBitmap(srcBitmap);
            } else {
                Toast.makeText(MainActivity.this, "取消拍照", Toast.LENGTH_LONG).show();
            }
        } else if (resultCode == 200) {
            String tempPath = data.getStringExtra("rotatePath");
            Log.i(TAG, "旋转回来..." + tempPath);
            if (tempPath != null)
                picAbsPath = tempPath;
            handleBitmap = BitmapFactory.decodeFile(picAbsPath);
            iv_picture.setImageBitmap(handleBitmap);
        }
    }

    //开启选择图片界面
    private void pickPic() {
        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(intent, this.MENU_OPEN_PICTURE);
    }

    //开启相机拍照界面
    private void takePictureByCamera() {
        //创建拍照照片保存位置
        String path = "/storage/emulated/0/meitu_pic/take";
        File file = CommonUtils.getTakePicFile(path);
        picAbsPath = file.getAbsolutePath();
        //解决Android N 的 Uri.fromFile(file)不兼容的问题
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        Uri fileUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //开启拍照意图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, this.MENU_TAKE_PICTURE);
    }

    //裁切
    public void cutPic(View view) {
        if (!CommonUtils.checkPicAbsPath(MainActivity.this, picAbsPath))
            return;
        Intent intent = new Intent(MainActivity.this, CutActivity.class);
        intent.putExtra("path", picAbsPath);
        this.startActivityForResult(intent, BTN_CUT_APP);
    }

    //旋转
    public void rotatePic(View view) {
        if (!CommonUtils.checkPicAbsPath(MainActivity.this, picAbsPath))
            return;
        Log.i(TAG, "进入旋转..." + picAbsPath);
        Intent intent = new Intent(MainActivity.this, RotateActivity.class);
        intent.putExtra("path", picAbsPath);
        this.startActivityForResult(intent, BTN_ROTATE_APP);
    }

    //缩放
    public void scalePic(View view) {
        if (!CommonUtils.checkPicAbsPath(MainActivity.this, picAbsPath))
            return;
        Intent intent = new Intent(MainActivity.this, ScaleActivity.class);
        intent.putExtra("path", picAbsPath);
        this.startActivityForResult(intent, BTN_SCALE_APP);
    }

    //翻转
    public void reversalPic(View view) {
        if (!CommonUtils.checkPicAbsPath(MainActivity.this, picAbsPath))
            return;
        Intent intent = new Intent(MainActivity.this, ReversalActivity.class);
        intent.putExtra("path", picAbsPath);
        this.startActivityForResult(intent, BTN_REVERSAL_APP);
    }

    //涂鸦
    public void drawPic(View view) {
        if (!CommonUtils.checkPicAbsPath(MainActivity.this, picAbsPath))
            return;
        Intent intent = new Intent(MainActivity.this, DrawActivity.class);
        intent.putExtra("path", picAbsPath);
        this.startActivityForResult(intent, BTN_DRAW_APP);
    }

    //重置图片
    public void resetPic(View view) {
        if (!CommonUtils.checkPicAbsPath(MainActivity.this, picAbsPath))
            return;
        Toast.makeText(MainActivity.this, "重置图片", Toast.LENGTH_SHORT).show();
    }

}