package com.fastfoodapp.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.fastfoodapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class MySwipeHepler extends ItemTouchHelper.SimpleCallback {

    int buttonWidth;
    private RecyclerView recyclerView;
    private List<MyBtn> btnList;
    private GestureDetector gestureDetector;
    private int swipePosition = -1;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<MyBtn>> btnBuffer;
    private Queue<Integer> removerQueue;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (MyBtn btn : btnList) {
                if (btn.onClick(e.getX(), e.getY()))
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (swipePosition < 0)
                return false;
            Point point = new Point((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
            RecyclerView.ViewHolder swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition);
            System.out.println("position : " + swipePosition);
            View swipeItem = swipeViewHolder.itemView;
            Rect rect = new Rect();
            swipeItem.getGlobalVisibleRect(rect);
            if (motionEvent.getAction() == motionEvent.ACTION_DOWN ||
                    motionEvent.getAction() == MotionEvent.ACTION_UP ||
                    motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y)
                    gestureDetector.onTouchEvent(motionEvent);
                else {
                    removerQueue.add(swipePosition);
                    swipePosition = -1;
                    recoverSwipedItem();
                }
            }
            return false;
        }
    };

    private synchronized void recoverSwipedItem() {
        while (!removerQueue.isEmpty()) {
            int pos = removerQueue.poll();
            if (pos > -1)
                recyclerView.getAdapter().notifyItemChanged(pos);
        }
    }

    public MySwipeHepler(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public MySwipeHepler(Context context, RecyclerView recyclerView, int buttonWidth) {
        super(0, ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.btnList = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.btnBuffer = new HashMap<>();
        this.buttonWidth = buttonWidth;

        removerQueue = new LinkedList<Integer>() {
            @Override
            public boolean add(Integer integer) {
                if (contains(integer))
                    return false;
                else
                    return super.add(integer);
            }
        };

        attachSwipe();
    }

    private void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        if (swipePosition != pos)
            removerQueue.add(swipePosition);
        swipePosition = pos;
        if (btnBuffer.containsKey(swipePosition))
            btnList = btnBuffer.get(swipePosition);
        else
            btnList.clear();
        btnBuffer.clear();
        swipeThreshold = 0.5f * btnList.size() * buttonWidth;
        recoverSwipedItem();
    }

    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;
        if (pos < 0) {
            swipePosition = pos;
            return;
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                List<MyBtn> buffer = new ArrayList<>();
                if (!btnBuffer.containsKey(pos)) {
                    instantiateMyBtn(viewHolder, buffer);
                    btnBuffer.put(pos, buffer);
                } else {
                    buffer = btnBuffer.get(pos);
                }
                translationX = dX * buffer.size() * buttonWidth / itemView.getWidth();
                drawBtn(c, itemView, buffer, pos, translationX);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private void drawBtn(Canvas c, View itemView, List<MyBtn> buffer, int pos, float translationX) {
        float right = itemView.getRight();
        float dButnWidth = -1 * translationX / buffer.size();
        for (MyBtn btn : buffer) {
            float left = right - dButnWidth;
            btn.onDraw(c, new RectF(left, itemView.getTop(), right, itemView.getBottom()), pos);
            right = left;
        }
    }

    public abstract void instantiateMyBtn(RecyclerView.ViewHolder viewHolder, List<MyBtn> buffer);

    public class MyBtn {
        private String text;
        private int imageResID, color, textSize, pos;
        private RectF clickRegion;
        private MyBtnClickListner listner;
        private Context context;
        private Resources resources;
        Bitmap icon;

        public MyBtn(Context context, String text, int imageResID, int color, int textSize, MyBtnClickListner listner) {
            this.text = text;
            this.imageResID = imageResID;
            this.color = color;
            this.textSize = textSize;
            this.listner = listner;
            this.context = context;
            this.resources = context.getResources();
        }

        public boolean onClick(float x, float y) {
            if (clickRegion != null && clickRegion.contains(x, y)) {
                listner.onClick(pos);
                return true;
            }
            return false;
        }

        public Bitmap drawableToBitmap(Drawable drawable) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            int width = drawable.getIntrinsicWidth();
            width = width > 0 ? width : 1;
            int height = drawable.getIntrinsicHeight();
            height = height > 0 ? height : 1;

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }

        public void onDraw(Canvas canvas, RectF rectF, int pos) {
            //this.icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.item_orange);
            Drawable drawable = context.getDrawable(R.drawable.item_orange);
            Bitmap icon = drawableToBitmap(drawable);
            Paint paint = new Paint();
            //paint.setColor(color);
            //canvas.drawRect(rectF, paint);
            System.out.println("icon : " + icon);
            canvas.drawBitmap(icon, 0, 0, paint);
            //paint.setColor(Color.WHITE);
            //paint.setTextSize(textSize);

            Rect rect = new Rect();
            float cHeight = rectF.height();
            float cWidth = rectF.width();
            //paint.setTextAlign(Paint.Align.LEFT);
            //paint.getTextBounds(text, 0, text.length(), rect);
            float x = 0, y = 0;
            if (imageResID == 0) {
                x = cWidth / 2f - rect.width() / 2f - rect.left;
                y = cHeight / 2f - rect.height() / 2f - rect.bottom;
                canvas.drawText(text, rectF.left + x, rectF.top + y, paint);
            } else {
//                x = cWidth / 2f - rect.width() / 2f;
//                y = cHeight / 2f - rect.height() / 2f;
                //Drawable d = ContextCompat.getDrawable(context, imageResID);
                Drawable d = ContextCompat.getDrawable(context, R.drawable.ic_delete);
                Bitmap bitmap = drawableToBitmap(d);
                System.out.println("rectF.right and left : " + (rectF.left + rectF.right) / 2);
                System.out.println("rectF.top and bottom : " + (rectF.top + rectF.bottom) / 2);
                System.out.println("rectF.right and left : " + (rectF.left + rectF.right));
                System.out.println("rectF.top and bottom : " + (rectF.top + rectF.bottom));
                System.out.println("rectF.right : " + rectF.right);
                System.out.println("rectF.left : " + rectF.left);
                System.out.println("rectF.top : " + rectF.top);
                System.out.println("rectF.bottom " + rectF.bottom);
                double left = ((rectF.left + rectF.right)) * 0.47;
                //double top = ((rectF.top + rectF.bottom)) * 0.46;
                double top = rectF.centerY();
                System.out.println("left : " + left);
                System.out.println("top : " + top);
                canvas.drawBitmap(bitmap, (float) left, (float) top, paint);
                //canvas.drawBitmap(bitmap, 880, 190, paint);
//                x = cWidth / 2f - rect.width() / 2f - rect.left;
//                y = cHeight / 2f - rect.height() / 2f - rect.bottom;
//                canvas.drawBitmap(bitmap, rectF.left + x, rectF.top + y, paint);
            }
            clickRegion = rectF;
            this.pos = pos;
        }
    }

    protected Bitmap drawableToBitmap(Drawable d) {
        if (d instanceof BitmapDrawable)
            return ((BitmapDrawable) d).getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return bitmap;
    }


}
