package meitu.android.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

/**
 * 处理文件的工具类
 * Created by ye on 2017/4/12.
 */

public class CommonUtils {
    /**
     * 根据路径创建文件并返回这个文件
     * @return
     */
    public static File getTakePicFile(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        return new File(dir, System.currentTimeMillis() + ".jpg");
    }

    /**
     * 在对图片操作之前先判断图片是否被选择
     *
     * @param context
     * @param picAbsPath
     * @return
     */
    public static boolean checkPicAbsPath(Context context, String picAbsPath) {
        if (picAbsPath == null) {
            Toast.makeText(context, "请选择图片", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
