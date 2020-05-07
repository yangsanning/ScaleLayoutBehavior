package ysn.com.demo.scalelayoutbehavior.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ysn.com.demo.scalelayoutbehavior.R;

/**
 * @Author yangsanning
 * @ClassName EmptyFragment
 * @Description 一句话概括作用
 * @Date 2020/5/7
 */
public class EmptyFragment extends Fragment {

    public static final String ARGS_TEXT = "ARGS_TEXT";

    /**
     * 当前显示的内容
     **/
    protected View mRootView;

    public static EmptyFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARGS_TEXT, text);
        EmptyFragment fragment = new EmptyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_empty, container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            TextView emptyTextView = mRootView.findViewById(R.id.empty_fragment_text);
            emptyTextView.setText(getArguments().getString(ARGS_TEXT));
        }
    }
}