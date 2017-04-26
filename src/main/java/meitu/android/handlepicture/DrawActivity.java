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
public class DrawActivity extends AppCompatActivity {
    private ImageView iv_draw;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        iv_draw = (ImageView) findViewById(R.id.iv_draw);

        Intent intent = this.getIntent();
        String path = (String) intent.getExtras().get("path");
        iv_draw.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public void beginDraw(View view) {
        Toast.makeText(this, "涂鸦", Toast.LENGTH_SHORT).show();
    }
}
