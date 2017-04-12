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
    public static Bitmap getImage(Context context, Intent data) {
        Uri picUri = data.getData();
        String[] column = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(picUri, column, null, null, null);
        String picPath = null;
        while (cursor.moveToNext()) {
            picPath = cursor.getString(cursor.getColumnIndex(column[0]));
        }
        cursor.close();
        Bitmap srcBitmap = BitmapFactory.decodeFile(picPath); //获取原图
        return getCopyImage(srcBitmap);
    }

    public static Bitmap getCopyImage(Bitmap srcBitmap) {
        //获取图片的空白副本
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(copyBitmap);
        Paint paint = new Paint();
        Matrix matrix = new Matrix();
        canvas.drawBitmap(srcBitmap, matrix, paint);
        return copyBitmap;
    }
}
