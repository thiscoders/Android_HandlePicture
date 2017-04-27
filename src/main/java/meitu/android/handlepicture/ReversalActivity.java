package meitu.android.handlepicture;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 翻转图片
 * Created by ye on 2017/4/19.
 */

public class ReversalActivity extends AppCompatActivity {
    private ImageView iv_reversal;
    private ProgressBar pb_resersal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversal);
        iv_reversal = (ImageView) findViewById(R.id.iv_reversal);
        pb_resersal = (ProgressBar) findViewById(R.id.pb_resersal);

        Intent intent = this.getIntent();
        String path = (String) intent.getExtras().get("path");
        iv_reversal.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    //返回
    public void backReversal(View view) {
        finish();
    }

    //翻转
    public void beginReversal(View view) {
        Toast.makeText(this, "翻转", Toast.LENGTH_SHORT).show();
    }

    //重置
    public void resetReversal(View view) {

    }

    //保存
    public void saveReversal(View view) {

    }

}
