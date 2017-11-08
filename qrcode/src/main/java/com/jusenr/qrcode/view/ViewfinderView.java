/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jusenr.qrcode.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.jusenr.qrcode.R;
import com.jusenr.qrcode.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * 自定义组件实现,扫描功能
 */
public final class ViewfinderView extends View {

    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG |
            Canvas.CLIP_SAVE_FLAG |
            Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
            Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
            Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    private static final long ANIMATION_DELAY = 100L;
    private static final int OPAQUE = 0xFF;

    private final Paint paint;
    private final int resultColor;
    private Bitmap resultBitmap;

    private int maskColor;
    private int viewWidth;
    private int viewHeight;
    private boolean isRoundMode;
    private boolean isShowScanLine;

    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    public ViewfinderView(Context context) {
        this(context, null);
    }

    public ViewfinderView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ViewfinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        Resources resources = getResources();
        resultColor = resources.getColor(R.color.result_view);
        possibleResultPoints = new HashSet<>(5);
        initInnerRect(context, attrs);
    }

    /**
     * 初始化内部框的大小
     *
     * @param context
     * @param attrs
     */
    private void initInnerRect(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);
        // 扫描框距离顶部
        float innerMarginTop = ta.getDimension(R.styleable.ViewfinderView_inner_margintop, -1);
        if (innerMarginTop != -1) {
            CameraManager.FRAME_MARGINTOP = (int) innerMarginTop;
        }
        // 扫描框的宽度
        viewWidth = (int) ta.getDimension(R.styleable.ViewfinderView_inner_width, -1);
        // 扫描框的高度
        viewHeight = (int) ta.getDimension(R.styleable.ViewfinderView_inner_height, -1);
        //扫描框外部背景色
        maskColor = getResources().getColor(ta.getResourceId(R.styleable.ViewfinderView_inner_maskColor, R.color.viewfinder_mask));

        // 扫描框边角颜色
        innercornercolor = getResources().getColor(ta.getResourceId(R.styleable.ViewfinderView_inn_corner_color, R.color.color_45DDDD));
        // 扫描框边角长度 default(65px)
        innercornerlength = (int) ta.getDimension(R.styleable.ViewfinderView_inn_corner_length, 65);
        // 扫描框边角宽度 default(15px)
        innercornerwidth = (int) ta.getDimension(R.styleable.ViewfinderView_inn_corner_width, 15);

        // 扫描控件
        scanLight = BitmapFactory.decodeResource(getResources(), ta.getResourceId(R.styleable.ViewfinderView_inner_scan_bitmap, R.drawable.scan_light));
        // 扫描速度 default(10)
        SCAN_VELOCITY = ta.getInt(R.styleable.ViewfinderView_inner_scan_speed, 10);
        //是否显示扫描线动画 default(显示扫描线动画true)
        isShowScanLine = ta.getBoolean(R.styleable.ViewfinderView_inner_isShowScanLine, true);

        //是否展示小圆点 default(展示小圆点true)
        isCircle = ta.getBoolean(R.styleable.ViewfinderView_inner_scan_iscircle, true);
        //数据像素点颜色
        resultPointColor = getResources().getColor(ta.getResourceId(R.styleable.ViewfinderView_inner_result_points, R.color.possible_result_points));

        //显示圆角扫描框(true)还是拐角扫描框(false) default(拐角扫描框false)
        isRoundMode = ta.getBoolean(R.styleable.ViewfinderView_inner_isRoundMode, false);

        //圆角矩形圆角半径
        radius = (int) ta.getDimension(R.styleable.ViewfinderView_in_Radius, 20f);
        radius = dip2px(context, radius);

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidth = -1;
        int myHeight = -1;

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.UNSPECIFIED) {
            myWidth = screenWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED) {
            myHeight = screenHeight;
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            screenWidth = myWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            screenHeight = myHeight;
        }
        if (isInEditMode()) {
            return;
        }

        // CameraManager.FRAME_WIDTH和CameraManager.FRAME_HEIGHT相等
        CameraManager.FRAME_WIDTH = viewWidth != -1 ? viewWidth : screenWidth / 2;
        CameraManager.FRAME_HEIGHT = viewHeight != -1 ? viewHeight : screenWidth / 2;

        // 需要调用下面的方法才会执行onDraw方法
        setWillNotDraw(false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }

        if (isRoundMode) {
            drawRoundBackground(canvas, frame);
            drawRoundRectForeground(canvas, frame);
        } else {
            drawBackground(canvas, frame);
            drawRectForeground(canvas, frame);
        }

        if (isShowScanLine) {
            drawScanLight(canvas, frame);
        }

        if (isCircle) {
            drawCircle(canvas, frame);
        }
        postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
    }

    // 是否展示小圆点
    private boolean isCircle;
    //数据像素点颜色
    private int resultPointColor;

    /**
     * @param canvas
     * @param frame
     */
    private void drawCircle(Canvas canvas, Rect frame) {
        Collection<ResultPoint> currentPossible = possibleResultPoints;
        Collection<ResultPoint> currentLast = lastPossibleResultPoints;
        if (currentPossible.isEmpty()) {
            lastPossibleResultPoints = null;
        } else {
            possibleResultPoints = new HashSet<ResultPoint>(5);
            lastPossibleResultPoints = currentPossible;
            paint.setAlpha(OPAQUE);
            paint.setColor(resultPointColor);

            if (isCircle) {
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
                }
            }
        }
        if (currentLast != null) {
            paint.setAlpha(OPAQUE / 2);
            paint.setColor(resultPointColor);

            if (isCircle) {
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
                }
            }
        }
    }

    // 扫描线移动的y
    private int scanLineTop;
    // 扫描线移动速度
    private int SCAN_VELOCITY;
    // 扫描线
    private Bitmap scanLight;

    /**
     * 绘制移动扫描线
     *
     * @param canvas
     * @param frame
     */
    private void drawScanLight(Canvas canvas, Rect frame) {
        paint.setAlpha(OPAQUE);

        if (scanLineTop == 0) {
            scanLineTop = frame.top;
        }

        if (scanLineTop >= frame.bottom - 30) {
            scanLineTop = frame.top;
        } else {
            scanLineTop += SCAN_VELOCITY;
        }
        Rect scanRect = new Rect(frame.left, scanLineTop, frame.right,
                scanLineTop + 30);
        canvas.drawBitmap(scanLight, null, scanRect, paint);
    }

    //圆角矩形圆角半径
    private int radius;

    /**
     * 绘制有四个圆角的取景框边框
     *
     * @return
     */
    private void drawRoundRectForeground(Canvas canvas, Rect frame) {
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(60);

        //set mode为clear
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        canvas.drawRoundRect(new RectF(frame.left, frame.top, frame.right, frame.bottom), radius, radius, paint);
        paint.setXfermode(null);
    }

    private void drawRoundBackground(Canvas canvas, Rect frame) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setColor(resultBitmap != null ? resultColor : maskColor);

        canvas.drawRect(0, 0, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        }
    }

    // 扫描框边角颜色
    private int innercornercolor;
    // 扫描框边角长度
    private int innercornerlength;
    // 扫描框边角宽度
    private int innercornerwidth;

    /**
     * 绘制拐角取景框边框
     *
     * @param canvas
     * @param frame
     */
    private void drawRectForeground(Canvas canvas, Rect frame) {
        paint.setColor(innercornercolor);
        paint.setStyle(Paint.Style.FILL);

        int corWidth = innercornerwidth;
        int corLength = innercornerlength;

        // 左上角
        canvas.drawRect(frame.left, frame.top, frame.left + corWidth, frame.top
                + corLength, paint);
        canvas.drawRect(frame.left, frame.top, frame.left
                + corLength, frame.top + corWidth, paint);
        // 右上角
        canvas.drawRect(frame.right - corWidth, frame.top, frame.right,
                frame.top + corLength, paint);
        canvas.drawRect(frame.right - corLength, frame.top,
                frame.right, frame.top + corWidth, paint);
        // 左下角
        canvas.drawRect(frame.left, frame.bottom - corLength,
                frame.left + corWidth, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - corWidth, frame.left
                + corLength, frame.bottom, paint);
        // 右下角
        canvas.drawRect(frame.right - corWidth, frame.bottom - corLength,
                frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - corLength, frame.bottom - corWidth,
                frame.right, frame.bottom, paint);
    }

    private void drawBackground(Canvas canvas, Rect frame) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
        requestFocus();
        requestLayout();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
