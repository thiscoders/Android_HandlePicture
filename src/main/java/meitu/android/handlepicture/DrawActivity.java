package meitu.android.handlepicture;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import meitu.android.interfaces.ActivityHelper;
import meitu.android.utils.ColorPickerDialog;
import meitu.android.utils.DialogFactory;
import meitu.android.utils.ImageSaver;
import meitu.android.utils.SmartImageUtils;

/**
 * Created by ye on 2017/4/19.
 */
public class DrawActivity extends AppCompatActivity {
    private final String TAG = DrawActivity.class.getSimpleName();

    private ColorPickerDialog colorDialog;
    private AlertDialog sizeDialog;

    private ImageView iv_draw;
    private ProgressBar pb_draw;

    private Canvas canvas;
    private Paint paint;

    private String srcPath;
    private Bitmap srcBitmap;
    private Bitmap copyBitmap;
    private Boolean isOperate = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        iv_draw = (ImageView) findViewById(R.id.iv_draw);
        pb_draw = (ProgressBar) findViewById(R.id.pb_draw);

        Intent intent = this.getIntent();
        srcPath = (String) intent.getExtras().get("path");
        srcBitmap = BitmapFactory.decodeFile(srcPath);
        //获取图片的副本开始操作
        copyBitmap = SmartImageUtils.getCopyImage(srcBitmap);
        //创建画布
        canvas = new Canvas(copyBitmap);
        //定义画笔
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(6);

        iv_draw.setImageBitmap(copyBitmap);

        iv_draw.setOnTouchListener(listener); //设置触摸监听

        Toast.makeText(this, "你随时可以开始涂鸦！", Toast.LENGTH_SHORT).show();
    }

    //退出涂鸦
    public void backDraw(View view) {
        finish();
    }

    //设置画笔颜色
    public void setPaintColor(View view) {
        colorDialog = new ColorPickerDialog(DrawActivity.this, paint.getColor(), "选择颜色", new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                paint.setColor(color);
            }
        });
        colorDialog.show();
    }

    //设置画笔尺寸
    public void setPaintSize(View view) {
        final NumberPicker np_size = new NumberPicker(DrawActivity.this);
        np_size.setMinValue(2);
        np_size.setMaxValue(50);
        np_size.setValue((int) paint.getStrokeWidth());
        sizeDialog = DialogFactory.generateDialog(DrawActivity.this, this.getString(R.string.dialog_paint_size_title), this.getString(R.string.dialog_paint_size_content), np_size,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int size = np_size.getValue();
                            if (size <= 0)
                                throw new Exception();
                            paint.setStrokeWidth(size);
                        } catch (Exception e) {
                            Toast.makeText(DrawActivity.this, "你输入的数字有误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        sizeDialog.show();
    }

    //重置
    public void resetDraw(View view) {
        copyBitmap = SmartImageUtils.getCopyImage(srcBitmap);
        canvas = new Canvas(copyBitmap);
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(6);
        iv_draw.setImageBitmap(copyBitmap);
        isOperate = false;
        Toast.makeText(DrawActivity.this, "重置", Toast.LENGTH_SHORT).show();
    }

    //保存
    public void saveDraw(View view) {
        if (!isOperate) {
            Toast.makeText(DrawActivity.this, "请先涂鸦！", Toast.LENGTH_SHORT).show();
            return;
        }
        String imagePath = this.getString(R.string.global_path) + "temp/draw.jpg";
        ImageSaver saver = new ImageSaver(DrawActivity.this, copyBitmap, imagePath, pb_draw);
        //设置延时关闭activity，保证图片保存完成
        saver.setMethod(new ActivityHelper() {
            @Override
            public void finishOK() {
                finish();
            }
        });
        saver.execute();
        Intent intent = new Intent();
        intent.putExtra("drawPath", imagePath);
        setResult(500, intent);
    }

    // 触摸监听事件
    private View.OnTouchListener listener = new View.OnTouchListener() {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;

        private float limit;

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            limit = paint.getStrokeWidth() / 2;

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    stopX = motionEvent.getX();
                    stopY = motionEvent.getY();

                    for (float i = -limit; i < limit; i++) {
                        for (float j = -limit; j < limit; j++) {
                            if (Math.sqrt(i * i + j * j) < limit) {//加上这个判断的话，触摸笔迹是一个圆，不加则为矩形
                                Log.i(TAG, "lala==>>" + startX + "," + startY + "|||||" + stopX + "," + stopY);
                                copyBitmap.setPixel((int) (motionEvent.getX() + i), (int) (motionEvent.getY() + j), paint.getColor());
                                canvas.drawLine(startX, startY, stopX, stopY, paint);
                            }
                        }
                    }


                    iv_draw.setImageBitmap(copyBitmap);

                    startX = stopX;
                    startY = stopY;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            isOperate = true;
            return true;
        }
    };
}
