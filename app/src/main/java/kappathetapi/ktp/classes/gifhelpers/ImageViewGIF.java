package kappathetapi.ktp.classes.gifhelpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * This is a View class that wraps Android {@link Movie} object and displays it.
 * You can set GIF as a Movie object or as a resource id from XML or by calling
 * {@link #setMovie(Movie)} or {@link #setMovieResource(int)}.
 * <p>
 * You can pause and resume GIF animation by calling {@link #setPaused(boolean)}.
 * <p>
 * The animation is drawn in the center inside of the measured view bounds.
 *
 * @author Sergey Bakhtiarov
 * @author sjdallst
 */

public class ImageViewGIF extends ImageView {

    private static final int DEFAULT_MOVIEW_DURATION = 1000;

    private int mMovieResourceId;
    private Movie mMovie;

    private long mMovieStart = 0;
    private int mCurrentAnimationTime = 0;

    /**
     * Position for drawing animation frames in the center of the view.
     */
    private float mLeft;
    private float mTop;

    /**
     * Scaling factor to fit the animation within view bounds.
     */
    private float mScale;

    /**
     * Scaled movie frames width and height.
     */
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;

    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;
    private Canvas mCanvas;

    private volatile boolean mPaused = false;
    private boolean mVisible = true;

    public ImageViewGIF(Context context) {
        this(context, null);
    }

    public ImageViewGIF(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewGIF(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setMovieResource(int movieResId) {
        this.mMovieResourceId = movieResId;
        mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        calculateMovieLayout(mWidthMeasureSpec, mHeightMeasureSpec);
        calculateLeftAndTop();
        requestLayout();
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
        calculateMovieLayout(mWidthMeasureSpec, mHeightMeasureSpec);
        calculateLeftAndTop();
        invalidateView();
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
        invalidate();
    }

    public void setPaused(boolean paused) {
        this.mPaused = paused;

        /**
         * Calculate new movie start time, so that it resumes from the same
         * frame.
         */
        if (!paused) {
            mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;
        }

        invalidate();
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    @Override
    public void setImageBitmap(Bitmap bmp) {
        super.setImageBitmap(bmp);
        mMovie = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;

        if (mMovie != null) {
            calculateMovieLayout(widthMeasureSpec, heightMeasureSpec);
        } else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void calculateMovieLayout(int widthMeasureSpec, int heightMeasureSpec) {
        int movieWidth = mMovie.width();
        int movieHeight = mMovie.height();

			/*
			 * Calculate horizontal scaling
			 */
        float scaleH = 1f;
        int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);

        if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
            int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
            if (movieWidth > maximumWidth) {
                scaleH = (float) movieWidth / (float) maximumWidth;
            }
        }

			/*
			 * calculate vertical scaling
			 */
        float scaleW = 1f;
        int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);

        if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
            int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
            if (movieHeight > maximumHeight) {
                scaleW = (float) movieHeight / (float) maximumHeight;
            }
        }

			/*
			 * calculate overall scale
			 */
        mScale = 1f / Math.max(scaleH, scaleW);

        mMeasuredMovieWidth = (int) (movieWidth * mScale);
        mMeasuredMovieHeight = (int) (movieHeight * mScale);

        System.out.println("HEIGHT " + movieHeight);
        System.out.println("WIDTH " + movieWidth);
        System.out.println("SCALE " + mScale);

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(mMovie != null) {
		    calculateLeftAndTop();
        }
    }

    private void calculateLeftAndTop() {
        /*
		 * Calculate left / top for drawing in center
		 */
        mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
        mTop = (getHeight() - mMeasuredMovieHeight) / 2f;

        System.out.println("LEFT " + mLeft);
        System.out.println("TOP " + mTop);

        mVisible = getVisibility() == View.VISIBLE;
    }

    //TODO: GET THE GIF TO ACTUALLY DRAW
    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;

        if (mMovie != null) {
            drawMovie();
        } else {
            super.onDraw(canvas);
        }
    }

    private void drawMovie() {
        if (!mPaused) {
            updateAnimationTime();
            drawMovieFrame(mCanvas);
            invalidateView();
        } else {
            drawMovieFrame(mCanvas);
        }
    }

    /**
     * Invalidates view only if it is visible.
     * <br>
     * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
     *
     */
    @SuppressLint("NewApi")
    private void invalidateView() {
        if(mVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        int dur = mMovie.duration();

        if (dur == 0) {
            dur = DEFAULT_MOVIEW_DURATION;
        }

        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    /**
     * Draw current GIF frame
     */
    private void drawMovieFrame(Canvas canvas) {

        mMovie.setTime(mCurrentAnimationTime);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(mScale, mScale);
        mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
        canvas.restore();
    }

    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        mVisible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }
}
