package meitu.android.handlepicture;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by ye on 2017/4/19.
 */

public class ReversalActivity extends AppCompatActivity {
    private ImageView iv_reversal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversal);
        iv_reversal = (ImageView) findViewById(R.id.iv_reversal);
        Intent intent = this.getIntent();
        String path = (String) intent.getExtras().get("path");
        iv_reversal.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public void beginReversal(View view) {
        Toast.makeText(this, "翻转", Toast.LENGTH_SHORT).show();
    }
}
