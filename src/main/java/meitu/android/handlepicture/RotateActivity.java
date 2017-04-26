package meitu.android.handlepicture;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import meitu.android.interfaces.ActivityHelper;
import meitu.android.utils.DialogFactory;
import meitu.android.utils.ImageSaver;
import meitu.android.utils.SmartImageUtils;

/**
 * 旋转图片
 * Created by ye on 2017/4/19.
 */

public class RotateActivity extends AppCompatActivity {
    private final String TAG = RotateActivity.class.getSimpleName();
    private ImageView iv_rotate;
    private String srcPath; //待处理的图片地址
    private Bitmap resBitmap; //处理完成后返回的图片
    private ProgressBar pb_rotate; //保存修改进度条
    private AlertDialog dialog; //输入旋转角度的对话框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate);
        iv_rotate = (ImageView) findViewById(R.id.iv_rotate);
        pb_rotate = (ProgressBar) findViewById(R.id.pb_rotate);

        Intent intent = this.getIntent();
        srcPath = (String) intent.getExtras().get("path");
        iv_rotate.setImageBitmap(BitmapFactory.decodeFile(srcPath));
    }

    public void beginRotate(View view) {
        final EditText et = new EditText(RotateActivity.this);
        et.setHint("请输入旋转角度(建议0-360)");
        et.setSingleLine();
        dialog = DialogFactory.generateDialog(RotateActivity.this, this.getString(R.string.dialog_rotate_title), this.getString(R.string.dialog_rotate_content), et, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    float num = Float.parseFloat(et.getText().toString());
                    resBitmap = SmartImageUtils.rotateImage(srcPath, num);
                    iv_rotate.setImageBitmap(resBitmap);
                } catch (Exception e) {
                    Toast.makeText(RotateActivity.this, et.getText().toString() + ",请输入数字！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(RotateActivity.this, "取消旋转", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void resetRotate(View view) {
        iv_rotate.setImageBitmap(BitmapFactory.decodeFile(srcPath));
    }

    public void saveRotate(View view) {
        if (resBitmap == null) {
            Toast.makeText(RotateActivity.this, "请先处理图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        String imagePath = this.getString(R.string.global_path) + "temp/rotate.jpg";
        ImageSaver saver = new ImageSaver(RotateActivity.this, resBitmap, imagePath, pb_rotate);
        //设置延时关闭activity，保证图片保存完成
        saver.setMethod(new ActivityHelper() {
            @Override
            public void finishOK() {
                finish();
            }
        });
        saver.execute();
        Intent intent = new Intent();
        intent.putExtra("rotatePath", imagePath);
        setResult(200, intent);
        dialog = null;
    }

    public void backRotate(View view) {
        finish();
    }
}
