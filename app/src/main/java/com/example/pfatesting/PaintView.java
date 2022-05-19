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
        super.onDraw(canvas);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(6);

        for(Point point : pointes){
            canvas.drawCircle(point.getX(),point.getY(),12,mPaint);

        }
        for (Line line : lines){
            canvas.drawLine(line.getX1(), line.getY1(),
                    line.getX2(), line.getY2(), mPaint);
        }
        if(index >=0 && index < polygones.size()){
            Polygone polygone = polygones.get(index);

            for(Point point : polygone.getPoints()){

                canvas.drawCircle(point.getX(),point.getY(),12,mPaint);

            }

            for (Line line : polygone.getLines()){

                canvas.drawLine(line.getX1(), line.getY1(),
                        line.getX2(), line.getY2(), mPaint);
            }
        }

        invalidate();

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                points_counter++;
                mX = motionEvent.getX();
                mY = motionEvent.getY();
                Point point = new Point(mX,mY);
                pointes.add(point);
                if(points_counter > 0)
                    lines.add(new Line(pointes.get(points_counter-1).getX(),pointes.get(points_counter-1).getY(),pointes.get(points_counter).getX(),pointes.get(points_counter).getY()));
                //verfy(point);
                float distance = (float)Math.sqrt( Math.pow(point.getX() - pointes.get(0).getX() ,2) + Math.pow(point.getY() - pointes.get(0).getY(), 2) );
                if(distance <= NEAR_TO && pointes.size() >= 3){
                    lines.add(new Line(pointes.get(0).getX(),pointes.get(0).getY(),point.getX(),point.getY()));
                    isClosed = true;
                    polygones.add(new Polygone(pointes, lines,annotaion));
                    annotaion = "";
                    MainActivity.annotation.setText("");
                    index=polygones.size() - 1;
                    pointes.clear();
                    lines.clear();
                    points_counter = -1;

                    for(Polygone polygone : polygones){
                        Log.d("Draw", polygone.toString());}


                }




        }
        return true;
    }
}
