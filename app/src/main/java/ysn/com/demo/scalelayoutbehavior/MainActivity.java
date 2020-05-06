package ysn.com.demo.scalelayoutbehavior;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ysn.com.behavior.scalelayout.ScaleLayoutBehavior;
import ysn.com.behavior.scalelayout.support_25_3_1.AppBarLayout;

public class MainActivity extends AppCompatActivity {

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

        appBarLayout = findViewById(R.id.main_activity_appbarLayout);
        titleBgView = findViewById(R.id.main_activity_title_bg);
        backImageView = findViewById(R.id.main_activity_back);
        titleTextView = findViewById(R.id.main_activity_title);
        editImageView = findViewById(R.id.main_activity_edit);

        scaleLayoutBehavior = (ScaleLayoutBehavior) ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();

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

        // title渐变处理
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                float alpha = (float) Math.abs(i) / (float) appBarLayout.getTotalScrollRange();
                titleBgView.setAlpha(alpha);
                backImageView.setColorFilter(alpha == 0 ? Color.WHITE : Color.parseColor("#000000"));
                editImageView.setColorFilter(alpha == 0 ? Color.WHITE : Color.parseColor("#000000"));
                if (alpha == 0) {
                    backImageView.setAlpha(1f);
                    editImageView.setAlpha(1f);
                } else {
                    backImageView.setAlpha(alpha);
                    editImageView.setAlpha(alpha);
                }
                titleTextView.setAlpha(alpha);
            }
        });
    }
}
