package meitu.android.handlepicture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import meitu.android.interfaces.ActivityHelper;
import meitu.android.utils.ImageSaver;
import meitu.android.utils.SmartImageUtils;

/**
 * 翻转图片
 * Created by ye on 2017/4/19.
 */

public class ReversalActivity extends AppCompatActivity {
    private ImageView iv_reversal;
    private ProgressBar pb_resersal;

    private String srcPath; //待处理的图片地址
    private Bitmap srcBitmap; //源图片
    private Bitmap resBitmap; //处理完成后返回的图片

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversal);
        iv_reversal = (ImageView) findViewById(R.id.iv_reversal);
        pb_resersal = (ProgressBar) findViewById(R.id.pb_resersal);

        Intent intent = this.getIntent();
        srcPath = (String) intent.getExtras().get("path");
        srcBitmap = BitmapFactory.decodeFile(srcPath);
        iv_reversal.setImageBitmap(srcBitmap);
    }

    //返回
    public void backReversal(View view) {
        finish();
    }

    //水平翻转
    public void horizontalReversal(View view) {
        srcBitmap = SmartImageUtils.resersalImage(srcBitmap, true, -1);
        iv_reversal.setImageBitmap(srcBitmap);
        resBitmap = srcBitmap;
    }

    //竖直翻转
    public void verticalReversal(View view) {
        srcBitmap = SmartImageUtils.resersalImage(srcBitmap, false, -1);
        iv_reversal.setImageBitmap(srcBitmap);
        resBitmap = srcBitmap;
    }

    //重置
    public void resetReversal(View view) {
        iv_reversal.setImageBitmap(BitmapFactory.decodeFile(srcPath));
        resBitmap = null;
    }

    //保存
    public void saveReversal(View view) {
        if (resBitmap == null) {
            Toast.makeText(ReversalActivity.this, "请先处理图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        String imagePath = this.getString(R.string.global_path) + "temp/reversal.jpg";
        ImageSaver saver = new ImageSaver(ReversalActivity.this, resBitmap, imagePath, pb_resersal);
        //设置延时关闭activity，保证图片保存完成
        saver.setMethod(new ActivityHelper() {
            @Override
            public void finishOK() {
                finish();
            }
        });
        saver.execute();
        Intent intent = new Intent();
        intent.putExtra("resersalPath", imagePath);
        setResult(400, intent);
    }

}
