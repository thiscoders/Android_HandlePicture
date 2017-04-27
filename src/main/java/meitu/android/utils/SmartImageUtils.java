package meitu.android.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
     * 从系统相册中选取数据
     *
     * @param context 上下文
     * @param data    意图
     * @return 图片的拷贝，这个图片就可以随意操作了
     */
    public static Object[] getImage(Context context, Intent data) {
        Object[] objs = new Object[2]; //定义返回数据，数据保存了bitmap图片和图片的路径

        Uri picUri = data.getData();
        String[] column = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(picUri, column, null, null, null);

        String picPath = null;
        while (cursor.moveToNext()) {
            picPath = cursor.getString(cursor.getColumnIndex(column[0]));
        }
        cursor.close();
        //将图片转化成bitmap
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
        //创建画布
        Canvas canvas = new Canvas(copyBitmap);
        //创建画笔
        Paint paint = new Paint();
        //图形矩阵
        Matrix matrix = new Matrix();
        //开始填充内容
        canvas.drawBitmap(srcBitmap, matrix, paint);
        return copyBitmap;
    }

    /**
     * 旋转图片
     *
     * @param srcPath
     * @param angle
     * @return
     */
    public static Bitmap rotateImage(String srcPath, float angle) {
        Bitmap srcBitmap = BitmapFactory.decodeFile(srcPath);
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
     * @param srcPath 原图
     * @param scaleX  x轴缩放率
     * @param scaleY  y轴缩放率
     * @return
     */
    public static Bitmap scaleImage(String srcPath, float scaleX, float scaleY) {
        Bitmap srcBitmap = BitmapFactory.decodeFile(srcPath);
        //创建原图的副本
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(copyBitmap);
        Paint paint = new Paint();
        Matrix matrix = new Matrix();
        //从图片的中心点开始放缩
        matrix.setScale(scaleX, scaleY, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);
        //缩放
        canvas.drawBitmap(srcBitmap, matrix, paint);
        return copyBitmap;
    }

    /**
     * 自动缩放
     *
     * @param srcPath 原图片路径
     * @param scales  缩放率
     * @return 缩放完成的图片
     */
    public static Bitmap autoScale(String srcPath, float scales) {
        return null;
    }

    /**
     * 翻转图片
     *
     * @param srcPath 图片的原地址
     * @param flag    翻转标记
     * @return 处理好的图片
     */
    public static Bitmap resersalImage(String srcPath, boolean flag) {
        Bitmap srcBitmap = BitmapFactory.decodeFile(srcPath);
        //创建原图的副本
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(copyBitmap);
        Paint paint = new Paint();
        Matrix matrix = new Matrix();
        if (flag) { //水平翻转
            matrix.setScale(-1, 1);
            matrix.postTranslate(copyBitmap.getWidth(), 0);
        } else { //竖直翻转
            matrix.setScale(1, -1);
            matrix.postTranslate(0, copyBitmap.getHeight());
        }
        canvas.drawBitmap(srcBitmap, matrix, paint);

        return copyBitmap;
    }

}
