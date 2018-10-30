package com.helionmusic.invertoverlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Provides a layout that will apply a simple color inversion filter on the views drawn below it,
 * after being attached to a given view. The attached view will drive updates to the inversion
 * layout when necessary. If no view is attached, InvertOverlay will subscribe to updates dispatched
 * by the android root layout
 * <p>
 * Created by HelionMusic on 23,October,2018. Contact at contact@helionmusic.com
 */
public class InvertOverlay extends View {

    private static final String key_ioAttachToView = "ioAttachToView";


    ColorMatrixColorFilter colorFilter;
    Paint                  paint;
    View                   attachedView;

    private boolean shouldInvalidate = false;
    Bitmap sourceBitmap = null;
    Bitmap resultBitmap = null;

    private boolean isInverted = true;

    public InvertOverlay(Context context) {
        super(context);
        setAttributes(context, null);
        init();
    }

    public InvertOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        init();
    }

    public InvertOverlay(Context context, AttributeSet attrs,
                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        init();
    }


    private void setAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        String packageName    = "http://schemas.android.com/apk/res-auto";
        int    attachedViewId = attrs.getAttributeResourceValue(packageName, key_ioAttachToView, 0);

        View view = findViewById(attachedViewId);
        if (view != null) {
            attachedView = view;
        }
    }


    private void init() {
        float[] mat = new float[]
                {
                        -1, 0, 0, 0, 255,
                        0, -1, 0, 0, 255,
                        0, 0, -1, 0, 255,
                        0, 0, 0, 1, 0
                };

        colorFilter = new ColorMatrixColorFilter(new ColorMatrix(mat));
        paint = new Paint();
        paint.setColorFilter(colorFilter);
    }

    public void attachToView(final View view) {
        if (this.attachedView != view) {
            this.attachedView = view;

            setupAttachedViewListener();
        } else {

            return;
        }

    }

    public void toggleInversion() {
        isInverted = !isInverted;

        float fromAlpha = isInverted ? 0f : 1f;
        float toAlpha   = isInverted ? 1f : 0f;

        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(700);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (isInverted)
                    setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isInverted) {
                    setVisibility(View.GONE);
                    releaseBitmaps();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(alphaAnimation);
    }

    public boolean isInverted() {
        return isInverted;
    }

    private void releaseBitmaps() {
        if (sourceBitmap != null) {
            sourceBitmap.recycle();
            sourceBitmap = null;
        }
        if (resultBitmap != null) {
            resultBitmap.recycle();
            resultBitmap = null;
        }
    }

    private void setupAttachedViewListener() {
        this.attachedView.getViewTreeObserver().addOnDrawListener(
                new ViewTreeObserver.OnDrawListener() {
                    @Override
                    public void onDraw() {
                        if (attachedView.getWidth() < 1 && attachedView.getHeight() < 1)
                            return;


                        if (sourceBitmap == null)
                            sourceBitmap = Bitmap.createBitmap(attachedView.getWidth(),
                                    attachedView.getHeight(),
                                    Bitmap.Config.ARGB_8888);
                        if (resultBitmap == null)
                            resultBitmap = Bitmap.createBitmap(attachedView.getWidth(),
                                    attachedView.getHeight(),
                                    Bitmap.Config.ARGB_8888);

                        updateInvertSource();

                        if (shouldInvalidate) {
                            invalidate();
                            shouldInvalidate = false;
                        }
                    }
                });
    }

    private void loadBitmapFromView(View v, int width, int height) {
        if (width == 0 || height == 0) {
            sourceBitmap = null;
            return;
        }

        Bitmap oldSource = sourceBitmap;

        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, width, height);
        //Get the view’s background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(c);
        else
            //does not have background drawable, then draw white background on the canvas
            c.drawColor(Color.WHITE);
        v.draw(c);
        sourceBitmap = b;

        shouldInvalidate = sourceBitmap != oldSource;
        oldSource = null;
    }

    private void updateInvertSource() {

        if (attachedView == null)
            return;

        loadBitmapFromView(attachedView, attachedView.getWidth(), attachedView.getHeight());
    }

    private void drawInvertedView(Canvas canvas, Bitmap bitmap) {
        if (bitmap == null) return;
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (resultBitmap != null)
            drawInvertedView(canvas, sourceBitmap);
    }

    private static class StopException extends RuntimeException {
    }

    private static StopException STOP_EXCEPTION = new StopException();

}
