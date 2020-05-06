package ysn.com.behavior.scalelayout.utils;

import android.content.Context;

/**
 * @Author yangsanning
 * @ClassName ConvertUtils
 * @Description 单位换算工具
 * @Date 2020/5/5
 */
public class ConvertUtils {

    /**
     * dp值转换成px值
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
