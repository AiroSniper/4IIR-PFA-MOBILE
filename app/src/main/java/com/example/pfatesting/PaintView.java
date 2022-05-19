package com.example.pfatesting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.SAXResult;

public class PaintView extends View implements View.OnTouchListener {
    Paint mPaint;
    float mX,mY;
    public String annotaion = "";
    private final float NEAR_TO = 100;
    public boolean isClosed = false;
    public int index = -1;
    public int points_counter = -1;
    public List<Polygone> polygones;
    public List<Point> pointes;
    public List<Line> lines;
    public PaintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mPaint = new Paint();
        mX = mY = -100;
        pointes = new ArrayList<>();
        lines = new ArrayList<>();
        polygones = new ArrayList<>();
        annotaion = null;


    }

    @Override
    protected void onDraw(Canvas canvas) {


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return true;
    }
}
