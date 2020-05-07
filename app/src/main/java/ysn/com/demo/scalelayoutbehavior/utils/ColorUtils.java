package ysn.com.demo.scalelayoutbehavior.utils;

/**
 * @Author yangsanning
 * @ClassName ColorUtils
 * @Description 一句话概括作用
 * @Date 2020/5/7
 */
public class ColorUtils {

    /**
     * 给color添加透明度
     *
     * @param alpha     透明度 0f～1f
     * @param baseColor 基本颜色
     * @return
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
