package ysn.com.behavior.scalelayout;


import ysn.com.behavior.scalelayout.support_25_3_1.AppBarLayout;

/**
 * @Author yangsanning
 * @ClassName AppBarStateChangeListener
 * @Description AppBarLayout的状态监听
 * @Date 2018/12/4
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    /**
     * 状态
     */
    protected State state = State.EXPANDED;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float currentVerticalOffset = Math.abs(verticalOffset);
        int totalScrollRange = appBarLayout.getTotalScrollRange();
        float alpha = currentVerticalOffset / (float) totalScrollRange;
        float offset = totalScrollRange - currentVerticalOffset;

        if (offset >= getThreshold()) {
            if (state != State.EXPANDED) {
                // 修改为展开状态
                state = State.EXPANDED;
                onExpanded();
            }
        } else {
            if (state != State.COLLAPSED) {
                // 修改为折叠状态
                state = State.COLLAPSED;
                onCollapsed();
            }
        }
        onAlphaChanged(alpha);
        onVerticalOffsetChanged(appBarLayout, verticalOffset);
    }

    /**
     * 获取收起展开的临界值
     */
    public abstract int getThreshold();

    public abstract void onExpanded();

    public abstract void onCollapsed();

    public abstract void onAlphaChanged(float alpha);

    protected void onVerticalOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    /**
     * 状态
     */
    protected enum State {
        /**
         * 展开
         */
        EXPANDED,
        /**
         * 收起
         */
        COLLAPSED;
    }
}
