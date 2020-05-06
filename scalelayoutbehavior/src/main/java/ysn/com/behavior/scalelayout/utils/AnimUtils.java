package ysn.com.behavior.scalelayout.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;

/**
 * @Author yangsanning
 * @ClassName AnimUtils
 * @Description 动画工具
 * @Date 2020/5/1
 */
public class AnimUtils {

    public static void ofFloat(long duration, final OnAnimUpdateAndEndListener onAnimUpdateAndEndListener, float... values) {
        ValueAnimator anim = ValueAnimator.ofFloat(values).setDuration(duration);
        anim.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (onAnimUpdateAndEndListener != null) {
                            onAnimUpdateAndEndListener.onAnimationUpdate(animation);
                        }
                    }
                }
        );
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimUpdateAndEndListener != null) {
                    onAnimUpdateAndEndListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.start();
    }

    public interface OnAnimUpdateAndEndListener {

        void onAnimationUpdate(ValueAnimator animation);

        void onAnimationEnd(Animator animation);
    }
}
