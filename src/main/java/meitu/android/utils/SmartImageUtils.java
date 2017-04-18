package meitu.android.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * ImageView相关的设置工具
 * Created by ye on 2017/4/12.
 */

public class SmartImageUtils {

    /**
     * 获取图片的拷贝
     *
     * @param context 上下文
     * @param data    意图
     * @return 图片的拷贝，这个图片就可以随意操作了
     */
    public static Object[] getImage(Context context, Intent data) {
        Object[] objs = new Object[2];
        Uri picUri = data.getData();
        String[] column = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(picUri, column, null, null, null);
        String picPath = null;
        while (cursor.moveToNext()) {
            picPath = cursor.getString(cursor.getColumnIndex(column[0]));
        }
        cursor.close();
        Bitmap srcBitmap = BitmapFactory.decodeFile(picPath);
        objs[0] = getCopyImage(srcBitmap);
        objs[1] = picPath;
        return objs;//返回了2个信息，需要拆分
    }

    /**
     * 获取文件副本
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap getCopyImage(Bitmap srcBitmap) {
        //获取图片的空白副本
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(copyBitmap);
        Paint paint = new Paint();
        Matrix matrix = new Matrix();
        canvas.drawBitmap(srcBitmap, matrix, paint);
        return copyBitmap;
    }

    /**
     * 旋转图片
     *
     * @param srcBitmap
     * @param angle
     * @return
     */
    public static Bitmap rotateImage(Bitmap srcBitmap, int angle) {

        //创建原图的副本
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        Canvas canvas = new Canvas(copyBitmap);

        Paint paint = new Paint();

        Matrix matrix = new Matrix();

        matrix.setRotate(angle, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);

        canvas.drawBitmap(srcBitmap, matrix, paint);

        return copyBitmap;
    }

    /**
     * 缩放图片
     *
     * @param srcBitmap 原图
     * @param scaleX    x轴缩放率
     * @param scaleY    y轴缩放率
     * @return
     */
    public static Bitmap scaleImage(Bitmap srcBitmap, float scaleX, float scaleY) {
        //创建原图的副本
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(copyBitmap);
        Paint paint = new Paint();
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        //缩放
        canvas.drawBitmap(srcBitmap, matrix, paint);
        return copyBitmap;
    }
}
