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

import meitu.android.interfaces.ActivityHelper;

/**
 * Created by ye on 2017/4/19.
 */
public class ImageSaver extends AsyncTask<Void, Void, Void> {
    private final String TAG = ImageSaver.class.getSimpleName();

    private Context context;
    private Bitmap bitmap;
    private String path;
    private View view;

    private ActivityHelper activityHelper;

    public ImageSaver(Context context, Bitmap bitmap, String path, View view) {
        this.context = context;
        this.bitmap = bitmap;
        this.path = path;
        this.view = view;
    }

    public void setMethod(ActivityHelper activityHelper) {
        this.activityHelper = activityHelper;
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
        String ppath = this.path.substring(0, this.path.lastIndexOf('/'));
        File dir = new File(ppath);
        if (!dir.exists())
            dir.mkdirs();
        //将图片保存到sd卡
        File file = new File(this.path);
        try {
            FileOutputStream stream = new FileOutputStream(file);
            this.bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        view.setVisibility(View.INVISIBLE);
        Toast.makeText(context, "图片保存成功！", Toast.LENGTH_SHORT).show();
        this.activityHelper.finishOK();
    }
}
