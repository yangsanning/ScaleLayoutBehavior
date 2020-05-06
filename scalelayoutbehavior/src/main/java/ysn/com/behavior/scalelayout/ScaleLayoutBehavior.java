package ysn.com.behavior.scalelayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import ysn.com.behavior.scalelayout.support_25_3_1.AppBarLayout;
import ysn.com.behavior.scalelayout.utils.AnimUtils;

/**
 * @Author yangsanning
 * @ClassName ScaleLayoutBehavior
 * @Description 缩放layout的Behavior
 * @Date 2020/5/5
 */
public class ScaleLayoutBehavior extends AppBarLayout.Behavior {

    /**
     * TAG_SCALE_LAYOUT; 缩放布局tag
     * TAG_SCALE_LAYOUT_INFO_LAYOUT; 缩放布局的信息布局tag
     * TAG_SCALE_LAYOUT_BOTTOM_LAYOUT; 缩放布局下方的布局tag
     */
    private static final String TAG_SCALE_LAYOUT = "scaleLayout";
    private static final String TAG_SCALE_LAYOUT_INFO_LAYOUT = "scaleLayoutInfoLayout";
    private static final String TAG_SCALE_LAYOUT_BOTTOM_LAYOUT = "scaleLayoutBottomLayout";

    /**
     * maxScale: 最大缩放
     */
    private float maxScale = 1.5f;

    /**
     * scaleLayout: 缩放布局
     * scaleLayoutHeight: 缩放布局的高
     * maxScaleLayoutHeight: 缩放布局的最大高
     */
    private View scaleLayout;
    private int scaleLayoutHeight;
    private float maxScaleLayoutHeight;

    /**
     * scaleLayoutInfoLayout: 缩放布局的信息布局
     * scaleLayoutInfoLayoutHeight: 缩放布局的信息布局的高
     */
    private ViewGroup scaleLayoutInfoLayout;
    private int scaleLayoutInfoLayoutHeight;

    /**
     * scaleLayoutBottomLayout: 缩放布局下方的布局tag
     * scaleLayoutBottomLayoutHeight: 缩放布局下方的布局的高
     */
    private ViewGroup scaleLayoutBottomLayout;
    private int scaleLayoutBottomLayoutHeight;

    /**
     * AppBarLayout 的高
     */
    private int appBarLayoutHeight;

    /**
     * totalDy: 总的Y轴滑动距离
     * lastScale: 最后一次的缩放
     * lastBottom: 最后一次的bottom
     */
    private float totalDy;
    private float lastScale;
    private int lastBottom;

    /**
     * isStartAnim: 是否执行动画
     * isRecovering: 是否正在自动回弹中
     */
    private boolean isStartAnim;
    private boolean isRecovering = false;

    private OnScaleChangeListener onScaleChangeListener;

    public ScaleLayoutBehavior() {
        super();
    }

    public ScaleLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * AppBarLayout布局时调用
     *
     * @param parent          父布局CoordinatorLayout
     * @param child           使用此Behavior的AppBarLayout
     * @param layoutDirection 布局方向
     * @return 返回true表示子View重新布局, 返回false表示请求默认布局
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        initView(parent, child);
        return handled;
    }

    /**
     * 根据tag获取View, 以及获取相应高度
     * 注意:需要在调用过super.onLayoutChild()方法之后获取
     */
    private void initView(View view, AppBarLayout appBarLayout) {
        if (scaleLayout == null) {
            scaleLayout = view.findViewWithTag(TAG_SCALE_LAYOUT);
        }

        if (scaleLayoutInfoLayout == null) {
            scaleLayoutInfoLayout = view.findViewWithTag(TAG_SCALE_LAYOUT_INFO_LAYOUT);
        }

        if (scaleLayoutBottomLayout == null) {
            scaleLayoutBottomLayout = view.findViewWithTag(TAG_SCALE_LAYOUT_BOTTOM_LAYOUT);
        }

        if (scaleLayout != null) {
            appBarLayout.setClipChildren(false);
            appBarLayoutHeight = appBarLayout.getHeight();
            scaleLayoutHeight = scaleLayout.getHeight();
            maxScaleLayoutHeight = maxScale * scaleLayoutHeight;
            scaleLayoutBottomLayoutHeight = scaleLayoutBottomLayout.getHeight();
            scaleLayoutInfoLayoutHeight = scaleLayoutInfoLayout.getHeight();
        }
    }

    /**
     * 当coordinatorLayout的子View试图开始嵌套滑动的时候被调用.
     * 当返回值为true的时候表明coordinatorLayout充当nested scroll parent处理这次滑动,
     * 需要注意的是只有当返回值为true的时候，Behavior才能收到后面的一些nested scroll 事件回调（如：onNestedPreScroll、onNestedScroll等）
     * 这个方法有个重要的参数nestedScrollAxes，表明处理的滑动的方向。
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child             使用此Behavior的AppBarLayout
     * @param directTargetChild
     * @param target            触发滑动嵌套的View(实现NestedScrollingChild接口)
     * @param nestedScrollAxes  嵌套滑动 应用的滑动方向，看 {@link ViewCompat#SCROLL_AXIS_HORIZONTAL},{@link ViewCompat#SCROLL_AXIS_VERTICAL}
     * @return 只有当返回值为true的时候, Behavior才能收到后面的一些nested scroll 事件回调（如：onNestedPreScroll、onNestedScroll等）
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        isStartAnim = true;
        if (target instanceof ScaleLayoutScrollView) {
            return true;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    /**
     * 触发滑动嵌套滚动之前调用的方法
     *
     * @param coordinatorLayout coordinatorLayout父布局
     * @param child             使用Behavior的子View
     * @param target            触发滑动嵌套的View(实现NestedScrollingChild接口)
     * @param dx                滑动的X轴距离
     * @param dy                滑动的Y轴距离
     * @param consumed          父布局消费的滑动距离，consumed[0]和consumed[1]代表X和Y方向父布局消费的距离，默认为0
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        if (!isRecovering) {
            if ((isPullDown(child, dy) || isPullUp(child, dy))) {
                // 缩放
                scale(child, target, dy);
                return;
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    /**
     * 是否上拉
     */
    private boolean isPullDown(AppBarLayout child, int dy) {
        return dy < 0 && child.getBottom() >= appBarLayoutHeight;
    }

    /**
     * 是否下拉
     */
    private boolean isPullUp(AppBarLayout child, int dy) {
        return dy > 0 && child.getBottom() > appBarLayoutHeight;
    }

    /**
     * 缩放
     */
    private void scale(AppBarLayout appBarLayout, View target, int dy) {
        totalDy -= dy;

        // 限制最大拉伸
        lastScale = Math.min(maxScale, 1f + totalDy / maxScaleLayoutHeight);
        lastBottom = (int) (appBarLayoutHeight - scaleLayoutHeight / 2 + scaleLayoutHeight / 2 * lastScale);
        appBarLayout.setBottom(lastBottom);
        target.setScrollY(0);
        if (scaleLayout != null) {
            scaleLayout.setScaleX(lastScale);
            scaleLayout.setScaleY(lastScale);
        }

        if (scaleLayoutInfoLayout != null) {
            scaleLayoutInfoLayout.setBottom(lastBottom - scaleLayoutBottomLayoutHeight);
            scaleLayoutInfoLayout.setTop(lastBottom - scaleLayoutBottomLayoutHeight - scaleLayoutInfoLayoutHeight);
        }

        if (scaleLayoutBottomLayout != null) {
            scaleLayoutBottomLayout.setTop(lastBottom - scaleLayoutBottomLayoutHeight);
            scaleLayoutBottomLayout.setBottom(lastBottom);
        }

        if (onScaleChangeListener != null) {
            onScaleChangeListener.onScaleChange(lastScale, false);
        }
    }

    /**
     * 用户松开手指并且会发生惯性动作之前调用, 参数提供了速度信息, 可以根据这些速度信息决定最终状态
     * (比如滚动Header, 是让Header处于展开状态还是折叠状态)
     *
     * @param coordinatorLayout coordinatorLayout父布局
     * @param child             使用Behavior的子View
     * @param target            触发滑动嵌套的View(实现NestedScrollingChild接口)
     * @param velocityX         x 方向的速度
     * @param velocityY         y 方向的速度
     * @return 返回true 表示消费了fling.
     */
    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child,
                                    @NonNull View target, float velocityX, float velocityY) {
        if (velocityY > 100) {
            // 当y速度>100,就秒弹回
            isStartAnim = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    /**
     * 嵌套滚动结束时被调用(这是一个清除滚动状态等的好时机)
     *
     * @param coordinatorLayout coordinatorLayout父布局
     * @param child             使用Behavior的子View
     * @param target            触发滑动嵌套的View(实现NestedScrollingChild接口)
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target) {
        reset(child);
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    /**
     * 重置状态
     */
    private void reset(final AppBarLayout appBarLayout) {
        if (isRecovering) {
            return;
        }
        if (totalDy > 0) {
            isRecovering = true;
            totalDy = 0;
            if (isStartAnim) {
                AnimUtils.ofFloat(200, new AnimUtils.OnAnimUpdateAndEndListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // 动画执行对应的值
                        float value = (float) animation.getAnimatedValue();
                        // 动画执行的百分比
                        float animatedFraction = animation.getAnimatedFraction();

                        int bottom = (int) (lastBottom - (lastBottom - appBarLayoutHeight) * animatedFraction);
                        appBarLayout.setBottom(bottom);

                        if (scaleLayout != null) {
                            scaleLayout.setScaleX(value);
                            scaleLayout.setScaleY(value);
                        }

                        if (scaleLayoutInfoLayout != null) {
                            scaleLayoutInfoLayout.setTop(bottom - scaleLayoutBottomLayoutHeight - scaleLayoutInfoLayoutHeight);
                        }

                        if (scaleLayoutBottomLayout != null) {
                            scaleLayoutBottomLayout.setTop(bottom - scaleLayoutBottomLayoutHeight);
                        }

                        if (onScaleChangeListener != null) {
                            onScaleChangeListener.onScaleChange(value, true);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRecovering = false;
                    }
                }, lastScale, 1f);
            } else {
                appBarLayout.setBottom(appBarLayoutHeight);

                if (scaleLayout != null) {
                    scaleLayout.setScaleX(1f);
                    scaleLayout.setScaleY(1f);
                }

                if (scaleLayoutInfoLayout != null) {
                    scaleLayoutInfoLayout.setTop(appBarLayoutHeight - scaleLayoutBottomLayoutHeight - scaleLayoutInfoLayoutHeight);
                }

                if (scaleLayoutBottomLayout != null) {
                    scaleLayoutBottomLayout.setTop(appBarLayoutHeight - scaleLayoutBottomLayoutHeight);
                }

                isRecovering = false;

                if (onScaleChangeListener != null) {
                    onScaleChangeListener.onScaleChange(1, true);
                }
            }
        }
    }

    /**
     * 缩放倍数的监听
     */
    public interface OnScaleChangeListener {

        /**
         * @param scale     缩放倍数
         * @param isRelease 是否是释放状态
         */
        void onScaleChange(float scale, boolean isRelease);
    }

    /**
     * 设置缩放倍数监听
     */
    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.onScaleChangeListener = onScaleChangeListener;
    }
}