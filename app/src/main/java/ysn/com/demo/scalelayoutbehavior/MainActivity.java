package ysn.com.demo.scalelayoutbehavior;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ysn.com.behavior.scalelayout.AppBarStateChangeListener;
import ysn.com.behavior.scalelayout.ScaleLayoutBehavior;
import ysn.com.behavior.scalelayout.support_25_3_1.AppBarLayout;
import ysn.com.demo.scalelayoutbehavior.adapter.FragmentPagerAdapter;
import ysn.com.demo.scalelayoutbehavior.page.EmptyFragment;
import ysn.com.demo.scalelayoutbehavior.utils.ColorUtils;
import ysn.com.statusbar.StatusBarUtils;

public class MainActivity extends AppCompatActivity {

    private ScaleLayoutBehavior scaleLayoutBehavior;
    private String[] titles = {"推荐", "分类", "排行", "管理", "我的"};

    private View titleBarView;
    private AppBarLayout appBarLayout;
    private View titleBgView;
    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView editImageView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        tabLayout = findViewById(R.id.main_activity_tab_layout);
        viewPager = findViewById(R.id.main_activity_view_pager);

        scaleLayoutBehavior = (ScaleLayoutBehavior) ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();

        // 增加上边距, 值为状态栏高度
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleBarView.getLayoutParams();
        params.topMargin += StatusBarUtils.getStatusBarHeight(this);

        StatusBarUtils.setPaddingTop(MainActivity.this, findViewById(R.id.main_activity_tool_bar));

        initViewPager();
        setListener();
    }

    private void initViewPager() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager());
        for (String title : titles) {
            adapter.addFragment(title, EmptyFragment.newInstance(title));
        }
        viewPager.setAdapter(adapter);
        //将ViewPager与TabLayout绑定
        tabLayout.setupWithViewPager(viewPager);
        //显示样式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
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
                if (alpha == 0) {
                    backImageView.setColorFilter(Color.WHITE);
                    editImageView.setColorFilter(Color.WHITE);
                    backImageView.setAlpha(1f);
                    editImageView.setAlpha(1f);
                } else if (alpha < 0.02f) {
                    return;
                } else {
                    backImageView.setColorFilter(Color.BLACK);
                    editImageView.setColorFilter(Color.BLACK);
                    backImageView.setAlpha(alpha);
                    editImageView.setAlpha(alpha);
                }
                titleBgView.setAlpha(alpha);
                titleTextView.setAlpha(alpha);
                StatusBarUtils.setColor(MainActivity.this, ColorUtils.getColorWithAlpha(alpha, Color.WHITE));
            }
        };
        appBarLayout.addOnOffsetChangedListener(appBarStateChangeListener);
    }
}
