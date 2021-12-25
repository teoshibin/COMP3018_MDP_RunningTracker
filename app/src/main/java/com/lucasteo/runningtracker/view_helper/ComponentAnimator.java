package com.lucasteo.runningtracker.view_helper;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * a simple class wrapping up messy animation code into a multiple easy to call animation methods
 */
public class ComponentAnimator {

    /**
     * request an fade out AlphaAnimation object with specified animation duration and starting offset
     *
     * @param animationDuration integer animation duration in milli seconds
     * @param offSet integer starting offset in milli seconds
     * @return alpha animation object
     */
    public AlphaAnimation getFadeOut(int animationDuration, int offSet){
        final AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f );
        fadeOut.setStartOffset(offSet);
        fadeOut.setDuration(animationDuration);
        fadeOut.setFillAfter(true);
        return fadeOut;
    }

    /**
     * request an fade in AlphaAnimation object with specified animation duration and starting offset
     *
     * @param animationDuration integer animation duration in milli seconds
     * @param offSet integer starting offset in milli seconds
     * @return alpha animation object
     */
    public AlphaAnimation getFadeIn(int animationDuration, int offSet){
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
        fadeIn.setStartOffset(offSet);
        fadeIn.setDuration(animationDuration);
        fadeIn.setFillAfter(true);
        return fadeIn;
    }

    /**
     * fade out the text view, change the text, fade in the text view
     *
     * @param textView animated text view
     * @param animationDuration integer animation duration in milli seconds
     * @param initialDelay integer starting offset in milli seconds
     * @param resid id of the string resource to set during the animation
     */
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

    /**
     * fade out the text view, change the text, fade in the text view
     *
     * @param textView animated text view
     * @param animationDuration integer animation duration in milli seconds
     * @param initialDelay integer starting offset in milli seconds
     * @param text string text to set during the animation
     */
    public void textViewFadeSetText(TextView textView, int animationDuration, int initialDelay, String text){

        final AlphaAnimation fadeOut = getFadeOut(animationDuration, initialDelay);
        final AlphaAnimation fadeIn = getFadeIn(animationDuration, 0);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setText(text);
                textView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textView.startAnimation(fadeOut);
    }

    /**
     * fade out image view, change resource, fade in image view
     *
     * @param imageView animated image view
     * @param animationDuration integer animation duration in milli seconds
     * @param initialDelay integer starting offset in milli seconds
     * @param resid id of the string resource to set during the animation
     */
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
