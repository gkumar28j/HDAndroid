package com.mcn.honeydew.utils.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * Created by gkumar on 8/16/2016.
 */
public class SideBar extends View {
    private Character[] l;
    private SectionIndexer sectionIndexter = null;
    private ListView list;
    private int m_nItemHeight = 40;
    private int textSize = 40;

    public SideBar(Context context) {
        super(context);
        init();
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        l = new Character[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        setBackgroundColor(0x44FFFFFF);
    }

    public void init(Character[] array) {
        l = array;
        setBackgroundColor(0x44FFFFFF);
        invalidate();
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListView(ListView _list) {
        list = _list;
        sectionIndexter = (SectionIndexer) _list.getAdapter();
    }

    public void setSize(int textSize, int letterheight) {
        this.textSize = textSize;
        m_nItemHeight = letterheight;

    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int i = (int) event.getY();
        int idx = i / m_nItemHeight;
        if (idx >= l.length) {
            idx = l.length - 1;
        } else if (idx < 0) {
            idx = 0;
        }


        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (sectionIndexter == null) {
                if (list != null) {
                    sectionIndexter = (SectionIndexer) list.getAdapter();
                }

            }
            int position = sectionIndexter.getPositionForSection(l[idx]);
            if (position == -1) {
                return true;
            }
            if (list != null) {
                list.setSelection(position);
            }

        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        // paint.setColor(0xFFA6A9AA);
        paint.setColor(Color.parseColor("#0394e0"));
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        float widthCenter = getMeasuredWidth() / 2;
        for (int i = 0; i < l.length; i++) {
            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight + (i * m_nItemHeight), paint);
        }
        super.onDraw(canvas);
    }
}
