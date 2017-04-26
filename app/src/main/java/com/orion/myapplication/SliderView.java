package com.orion.myapplication;

/**
 * Created by orion on 25.4.17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderView extends RelativeLayout {

    private static final int DEFAULT_ANIM_DURATION = 500;

    // Header
    private final int mHeaderHeight;
    @ColorInt
    private final int mHeaderColor;
    @ColorInt
    private final int mHeaderTextColor;
    private final String mHeaderText;

    private ViewGroup mHandle;
    private TextView mTextViewHeaderText;
    private View mContentContainer;

    private boolean mExpanded = false;
    private boolean mFirstOpen = true;

    private int mCollapsedHeight;
    private int mContentHeight;
    private int mAnimationDuration = 0;

    @Nullable
    private OnExpandListener mListener;

    public SliderView(Context context) {
        this(context, null);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SliderView, 0, 0);

        mAnimationDuration = a.getInteger(R.styleable.SliderView_animationDuration, DEFAULT_ANIM_DURATION);

        mHeaderHeight = a.getDimensionPixelSize(R.styleable.SliderView_headerHeight, 0);
        mHeaderColor = a.getColor(R.styleable.SliderView_headerColor, ContextCompat.getColor(context, android.R.color.holo_blue_dark));
        mHeaderTextColor = a.getColor(R.styleable.SliderView_headerTextColor, ContextCompat.getColor(context, android.R.color.white));

        mHeaderText = a.getString(R.styleable.SliderView_headerText);
        if (TextUtils.isEmpty(mHeaderText)) {
            throw new IllegalArgumentException(
                    "The header text attribute is required.");
        }

        a.recycle();
    }

    public void setOnExpandListener(OnExpandListener listener) {
        mListener = listener;
    }

    public void setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setupHeader();

        if (getChildCount() > 2) {
            throw new IllegalArgumentException("Childes > 2");
        }

        mContentContainer = getChildAt(0);
        mContentContainer.setVisibility(View.INVISIBLE);
        mHandle.setOnClickListener(new PanelToggle());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mContentContainer.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mHandle.measure(MeasureSpec.UNSPECIFIED, heightMeasureSpec);
        mCollapsedHeight = mHandle.getMeasuredHeight();
        mContentHeight = mContentContainer.getMeasuredHeight();

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mFirstOpen) {
            mContentContainer.getLayoutParams().width = 0;
            mContentContainer.getLayoutParams().height = mCollapsedHeight;
            ((LayoutParams)mContentContainer.getLayoutParams()).topMargin = mCollapsedHeight;
            mFirstOpen = false;
        }

        int height = mContentContainer.getMeasuredHeight() + (mExpanded ? mCollapsedHeight : 0);
        setMeasuredDimension(widthMeasureSpec, height);
    }

    @NonNull
    public TextView getHeaderTextView() {
        return mTextViewHeaderText;
    }

    private void setupHeader() {
        mHandle = new RelativeLayout(getContext());
        mHandle.setBackgroundColor(mHeaderColor);
        mHandle.setLayoutParams(new ViewGroup.MarginLayoutParams(LayoutParams.MATCH_PARENT, mHeaderHeight));
        addView(mHandle);

        mTextViewHeaderText = new TextView(getContext());
        mTextViewHeaderText.setText(mHeaderText);
        mTextViewHeaderText.setPadding(10, 0, 10, 0);
        mTextViewHeaderText.setGravity(Gravity.CENTER_VERTICAL);
        mTextViewHeaderText.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mTextViewHeaderText.setLayoutParams(params);

        mTextViewHeaderText.setTextColor(mHeaderTextColor);
        mHandle.addView(mTextViewHeaderText);
    }

    private class PanelToggle implements OnClickListener {
        @Override
        public void onClick(View v) {
            Animation animation;

            if (mExpanded) {
                animation = new SpoilerAnimation(mContentHeight, mCollapsedHeight);
                if (mListener != null) {
                    mListener.onCollapse(mHandle, mContentContainer);
                }
            } else {
                SliderView.this.invalidate();
                mContentContainer.setVisibility(View.VISIBLE);
                animation = new SpoilerAnimation(mCollapsedHeight, mContentHeight);
                if (mListener != null) {
                    mListener.onExpand(mHandle, mContentContainer);
                }
            }

            animation.setDuration(mAnimationDuration);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mExpanded = !mExpanded;
                    if (!mExpanded) {
                        mContentContainer.setVisibility(View.INVISIBLE);
                    }

                    mContentContainer.clearAnimation();
                }
            });

            mContentContainer.startAnimation(animation);
        }
    }

    private class SpoilerAnimation extends Animation {

        private final int mFromHeight;
        private final int mToHeight;

        public SpoilerAnimation(int fromHeight, int toHeight) {
            this.mFromHeight = fromHeight;
            this.mToHeight = toHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation transformation) {
            if (mContentContainer.getHeight() != mToHeight) {
                mContentContainer.getLayoutParams().height = (int) (mFromHeight + ((mToHeight - mFromHeight) * interpolatedTime));
                mContentContainer.getLayoutParams().width = getMeasuredWidth();
                mContentContainer.requestLayout();
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public interface OnExpandListener {

        void onExpand(@NonNull View handle, @NonNull View content);
        void onCollapse(@NonNull View handle, @NonNull View content);
    }
}