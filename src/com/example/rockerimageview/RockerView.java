package com.example.rockerimageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class RockerView extends ImageView {

	private MyRocker rocker ;//define custom rocker
	private float current_X, current_Y;
	private boolean isRockerCreated = false;//when rocker is created, set true, else set false
	
	public RockerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RockerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(!isRockerCreated){
			rocker = new MyRocker(getWidth(), getHeight());
			isRockerCreated = true;
		}
		rocker.drawSelf(canvas);
		rocker.drawLine(canvas);
		super.onDraw(canvas);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		current_X = event.getX();
		current_Y = event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			rocker.begin(current_X, current_Y);
			postInvalidate();//refresh
			break;
		case MotionEvent.ACTION_MOVE:
			rocker.update(current_X, current_Y);
			postInvalidate();//refresh
			break;
		case MotionEvent.ACTION_UP:
			rocker.reset();
			postInvalidate();//refresh
			break;
		default:
			break;
		}
		return true;
	}
	
}
