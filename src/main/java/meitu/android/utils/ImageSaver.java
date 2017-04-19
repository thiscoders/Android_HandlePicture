package meitu.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by ye on 2017/4/19.
 */
public class ImageSaver extends AsyncTask<Void, Void, Void> {
    private final String TAG = ImageSaver.class.getSimpleName();

    private Context context;
    private Bitmap bitmap;
    private View view;

    public ImageSaver(Context context, Bitmap bitmap, View view) {
        this.context = context;
        this.bitmap = bitmap;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.view.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (isCancelled()) { //获取状态，完成异步任务
            return null;
        }
        // TODO: 2017/4/19 此处也将路径写死了，后续修改
        File dir = new File("/storage/emulated/0/meitu_pic/handle/");
        if (!dir.exists())
            dir.mkdirs();
        //将图片保存到sd卡
        File file = new File(dir, "handle_" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream stream = new FileOutputStream(file);
            this.bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        this.bitmap = null;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        view.setVisibility(View.INVISIBLE);
        Toast.makeText(context, "图片保存成功！", Toast.LENGTH_SHORT).show();
    }
}
