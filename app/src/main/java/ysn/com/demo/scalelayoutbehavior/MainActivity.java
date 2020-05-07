package ysn.com.demo.scalelayoutbehavior;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ysn.com.behavior.scalelayout.AppBarStateChangeListener;
import ysn.com.behavior.scalelayout.ScaleLayoutBehavior;
import ysn.com.behavior.scalelayout.support_25_3_1.AppBarLayout;
import ysn.com.statusbar.StatusBarUtils;

public class MainActivity extends AppCompatActivity {

    private View titleBarView;
    private AppBarLayout appBarLayout;
    private View titleBgView;
    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView editImageView;
    private ScaleLayoutBehavior scaleLayoutBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.setTransparentForWindow(this);

        titleBarView = findViewById(R.id.main_activity_title_bar);
        appBarLayout = findViewById(R.id.main_activity_app_bar_layout);
        titleBgView = findViewById(R.id.main_activity_title_bg);
        backImageView = findViewById(R.id.main_activity_back);
        titleTextView = findViewById(R.id.main_activity_title);
        editImageView = findViewById(R.id.main_activity_edit);

        scaleLayoutBehavior = (ScaleLayoutBehavior) ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();

        // 增加上边距, 值为状态栏高度
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleBarView.getLayoutParams();
        params.topMargin += StatusBarUtils.getStatusBarHeight(this);

        setListener();
    }

    private void setListener() {
        // 缩放回调
        scaleLayoutBehavior.setOnScaleChangeListener(new ScaleLayoutBehavior.OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scale, boolean isRelease) {
                Log.d("test", "scale: " + scale + "isRelease: " + isRelease);
            }
        });

        // title bar渐变处理
        final AppBarStateChangeListener appBarStateChangeListener = new AppBarStateChangeListener() {

            @Override
            public int getThreshold() {
                return titleBarView.getHeight() + StatusBarUtils.getStatusBarHeight(MainActivity.this);
            }

            @Override
            public void onExpanded() {
                StatusBarUtils.setLightMode(MainActivity.this);
            }

            @Override
            public void onCollapsed() {
                StatusBarUtils.setDarkMode(MainActivity.this);
            }

            @Override
            public void onAlphaChanged(float alpha) {
                titleBgView.setAlpha(alpha);
                if (alpha == 0) {
                    backImageView.setColorFilter(Color.WHITE);
                    editImageView.setColorFilter(Color.WHITE);
                    backImageView.setAlpha(1f);
                    editImageView.setAlpha(1f);
                } else {
                    backImageView.setColorFilter(Color.BLACK);
                    editImageView.setColorFilter(Color.BLACK);
                    backImageView.setAlpha(alpha);
                    editImageView.setAlpha(alpha);
                }
                titleTextView.setAlpha(alpha);
                StatusBarUtils.setColor(MainActivity.this, getColorWithAlpha(alpha, Color.WHITE));
            }
        };
        appBarLayout.addOnOffsetChangedListener(appBarStateChangeListener);
    }

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
