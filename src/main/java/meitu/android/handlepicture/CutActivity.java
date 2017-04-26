package meitu.android.handlepicture;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by ye on 2017/4/19.
 */

public class CutActivity extends AppCompatActivity {
    private ImageView iv_cut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut);

        iv_cut = (ImageView) findViewById(R.id.iv_cut);
        Intent intent = this.getIntent();
        String path = (String) intent.getExtras().get("path");
        iv_cut.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public void beginCut(View view) {
        Toast.makeText(this, "裁切", Toast.LENGTH_SHORT).show();
    }
}
