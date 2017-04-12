package meitu.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * 处理文件的工具类
 * Created by ye on 2017/4/12.
 */

public class CommonUtils {
    /**
     * 根据路径创建文件并返回这个文件
     *
     * @param path
     * @return
     */
    public static File getDownFile() {
        String path = "/storage/emulated/0/meitu_pic/";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        return new File(dir, System.currentTimeMillis() + ".jpg");
    }
}
