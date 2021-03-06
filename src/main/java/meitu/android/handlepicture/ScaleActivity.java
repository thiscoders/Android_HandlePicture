package meitu.android.handlepicture;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
 * Created by ye on 2017/4/19.
 */

public class ScaleActivity extends AppCompatActivity {
    private ImageView iv_scale;
    private ProgressBar pb_scale; //保存修改进度条

    private String srcPath; //待处理的图片地址
    private Bitmap resBitmap; //处理完成后返回的图片
    private AlertDialog dialog; //输入旋转角度的对话框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);
        iv_scale = (ImageView) findViewById(R.id.iv_scale);
        pb_scale = (ProgressBar) findViewById(R.id.pb_scale);

        Intent intent = this.getIntent();
        srcPath = (String) intent.getExtras().get("path");
        iv_scale.setImageBitmap(BitmapFactory.decodeFile(srcPath));
    }

    public void beginScale(View view) {
        final EditText et_angle = new EditText(ScaleActivity.this);
        et_angle.setHint("请输入放缩率(建议0.1-2)");
        et_angle.setSingleLine();
        dialog = DialogFactory.generateDialog(ScaleActivity.this, this.getString(R.string.dialog_scale_title), this.getString(R.string.dialog_scale_content), et_angle,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            float scales = Float.parseFloat(et_angle.getText().toString());
                            resBitmap = SmartImageUtils.scaleImage(srcPath, scales, scales);
                            iv_scale.setImageBitmap(resBitmap);
                        } catch (Exception e) {
                            Toast.makeText(ScaleActivity.this, et_angle.getText().toString() + ",请输入数字！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ScaleActivity.this, "取消" + et_angle.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.show();
    }

    public void backMain(View view) {
        finish();
    }

    public void resetScale(View view) {
        iv_scale.setImageBitmap(BitmapFactory.decodeFile(srcPath));
        resBitmap = null;
    }

    /**
     * 保存修改
     *
     * @param view
     */
    public void saveScale(View view) {
        if (resBitmap == null) {
            Toast.makeText(ScaleActivity.this, "请先处理图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        String imagePath = this.getString(R.string.global_path) + "temp/scale.jpg";
        ImageSaver saver = new ImageSaver(ScaleActivity.this, resBitmap, imagePath, pb_scale);
        //设置延时关闭activity，保证图片保存完成
        saver.setMethod(new ActivityHelper() {
            @Override
            public void finishOK() {
                finish();
            }
        });
        saver.execute();
        Intent intent = new Intent();
        intent.putExtra("scalePath", imagePath);
        setResult(300, intent);
        dialog = null;
    }
}
