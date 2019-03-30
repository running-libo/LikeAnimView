package com.example.com.likeanimview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.Random;

public class LikeView extends RelativeLayout {
    private GestureDetector gestureDetector;
    /** 图片大小 */
    private int likeViewSize = 300;
    private int[] angles = new int[]{-30, 0, 30};

    public LikeView(Context context) {
        super(context);
        init();
    }

    public LikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //将事件传给GestureDetector处理双击事件
        gestureDetector = new GestureDetector(new MyGestureDetectorListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            addImageView(e);
            return true;
        }
    }

    /**
     * 在手势双击位置添加imageview
     *
     * @param e
     */
    private void addImageView(MotionEvent e) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_like));  //这里如果用background属性，添加在边缘图片会变形
        addView(imageView);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(likeViewSize, likeViewSize);
        layoutParams.leftMargin = (int) e.getX() - likeViewSize / 2;
        layoutParams.topMargin = (int) e.getY() - likeViewSize / 2;
        imageView.setLayoutParams(layoutParams);

        playAnim(imageView);
    }

    private void playAnim(final ImageView imageView) {
        AnimationSet animationSet = new AnimationSet(true);
        int degrees = angles[new Random().nextInt(3)];
        animationSet.addAnimation(AnimUtils.rotateAnim(0, 0, degrees));
        animationSet.addAnimation(AnimUtils.scaleAnim(100, 2f, 1f, 0));
        animationSet.addAnimation(AnimUtils.alphaAnim(0, 1, 100, 0));
        animationSet.addAnimation(AnimUtils.scaleAnim(500, 1f, 1.8f, 300));
        animationSet.addAnimation(AnimUtils.alphaAnim(1f, 0, 500, 300));
        animationSet.addAnimation(AnimUtils.translationAnim(500, 0, 0, 0, -400, 300));
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().post(new Runnable() {
                    public void run() {
                        removeView(imageView);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(animationSet);
    }
}
