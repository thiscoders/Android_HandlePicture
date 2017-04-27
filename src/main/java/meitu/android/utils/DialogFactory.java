package meitu.android.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by ye on 2017/4/25.
 */

public class DialogFactory {
    /**
     * 生成对话框
     *
     * @param title    标题
     * @param content  内容
     * @param view     处理过程显示的视图
     * @param positive 确定按钮触发的事件
     * @param negative 取消按钮触发的事件
     * @return 设置完成的对话框
     */
    public static AlertDialog generateDialog(Context context, String title, String content, View view, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        if (view != null)
            builder.setView(view);
        //添加响应事件
        builder.setPositiveButton("确定", positive);
        builder.setNegativeButton("取消", negative);
        return builder.create();
    }
}


