package com.lucasteo.runningtracker.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class ComponentAnimator {

    public AlphaAnimation getFadeOut(int animationDuration, int offSet){
        final AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f );
        fadeOut.setStartOffset(offSet);
        fadeOut.setDuration(animationDuration);
        fadeOut.setFillAfter(true);
        return fadeOut;
    }

    public AlphaAnimation getFadeIn(int animationDuration, int offSet){
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
        fadeIn.setStartOffset(offSet);
        fadeIn.setDuration(animationDuration);
        fadeIn.setFillAfter(true);
        return fadeIn;
    }

    public void textViewFadeSetText(TextView textView, int animationDuration, int initialDelay, int resid){

        final AlphaAnimation fadeOut = getFadeOut(animationDuration, initialDelay);
        final AlphaAnimation fadeIn = getFadeIn(animationDuration, 0);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setText(resid);
                textView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textView.startAnimation(fadeOut);
    }

    public void imageViewFadeSetResource(ImageView imageView, int animationDuration, int initialDelay, int resid){

        final AlphaAnimation fadeOut = getFadeOut(animationDuration, initialDelay);
        final AlphaAnimation fadeIn = getFadeIn(animationDuration, 0);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageResource(resid);
                imageView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(fadeOut);
    }

}
