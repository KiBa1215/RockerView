package com.example.rockerimageview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Custom Rocker Class 
 * @author KiBa
 */
public class MyRocker {

	float width;//screen width
	float height;//screen height
	private Paint paint;
	private float x_position, y_position;//position percentage of the screen value:0.0~1.0
	private final float OuterCenter_X, OuterCenter_Y;//position coordinates of outer circle center
	private float InnerCenter_X, InnerCenter_Y;//position coordinates of inner circle center
	private float Outer_R, Inner_R;//radius of outer and inner circle
	private boolean isRockerTouched = false;//when the rocker is touched at ACTION_DOWN, set true
	private float degree = 0;//the degree between current position and center position
	
	/**
	 * <b>This constructor is for SufaceView!</b>
	 * @param ScreenWidth  screen width
	 * @param ScreenHeight screen height
	 * @param x_position   position percentage of the screen value:0.0~1.0f defualt:0.5f
	 * @param y_position   position percentage of the screen value:0.0~1.0f default:0.5f
	 * @param R radius
	 */
	public MyRocker(int ScreenWidth, int ScreenHeight, float x_position, float y_position, float R) {
		this.width = ScreenWidth;
		this.height = ScreenHeight;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setAlpha(0x88);
		//if the x_position and y_position is between 0.0~1.0f
		if(x_position > 0 && x_position < 1 && y_position > 0 && y_position < 1){
			this.x_position = x_position;
			this.y_position = y_position;	
		}else{//default 0.5f
			this.x_position = 0.5f;
			this.y_position = 0.5f;
		}
		//radius
		this.Outer_R = R;
		this.Inner_R = R*0.618f;
		//calculate the positions of outer and inner circle center
		this.OuterCenter_X = this.width * this.x_position;
		this.OuterCenter_Y = this.height * this.y_position;
		this.InnerCenter_X = OuterCenter_X;
		this.InnerCenter_Y = OuterCenter_Y;
	}
	
	/**
	 * This constructor is for ImageView!<br/>
	 * When this View is added in the xml, 
	 * the width and height <b>CANNOT</b> be set as <b>"WARP_CONTENT"</b>
	 * <b>Caution： width == height</b>
	 * @param width the width of ImageView
	 * @param height the height of ImageView
	 */
	public MyRocker(int width, int height) {
		super();
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setAlpha(0x88);
		//set width and height
		this.width = width;
		this.height = height;
		//set radius
		this.Outer_R = width / (2 * (1 + 0.618f));
		this.Inner_R = this.Outer_R * 0.618f;
		System.out.println("Outer_R == " + Outer_R);
		//calculate the positions of outer and inner circle center
		this.OuterCenter_X = this.width / 2;
		this.OuterCenter_Y = this.height / 2;
		this.InnerCenter_X = OuterCenter_X;
		this.InnerCenter_Y = OuterCenter_Y;
	}


	protected void drawSelf(Canvas canvas){
		paint.setColor(0xFFE3E3E3);
		canvas.drawCircle(OuterCenter_X, OuterCenter_Y, Outer_R, paint);
		paint.setColor(Color.GRAY);
		canvas.drawCircle(InnerCenter_X, InnerCenter_Y, Inner_R, paint);
	}
	
	protected void drawLine(Canvas canvas){
		if(OuterCenter_X != InnerCenter_X || OuterCenter_Y != InnerCenter_Y){
			paint.setColor(Color.RED);
			paint.setStrokeWidth(3f);
			canvas.drawLine(OuterCenter_X, OuterCenter_Y, InnerCenter_X, InnerCenter_Y, paint);
		}
	}
	
	/**
	 * Call this method when ACTION_DOWN 
	 * @param x
	 * @param y
	 */
	protected void begin(float x, float y){
		//if touch position is inside the outer circle area
		float distance = distance(x, y, OuterCenter_X, OuterCenter_Y);
		if(distance < Outer_R){
			InnerCenter_X = x;
			InnerCenter_Y = y;
			isRockerTouched = true;
		}
		//calculate the degree
		this.degree = (float) Math.atan((x - OuterCenter_X) / (y - OuterCenter_Y));
	}
	
	/**
	 * Call this method when ACTION_MOVE 
	 * @param x
	 * @param y
	 */
	protected void update(float x, float y){
		if(isRockerTouched){//the rocker can be moved only when it is touched 
			float distance = distance(x, y, OuterCenter_X, OuterCenter_Y);
			//if the touch position is inside the outer circle area
			if(distance < Outer_R){
				InnerCenter_X = x;
				InnerCenter_Y = y;
				isRockerTouched = true;
			}else{//outside the outer circle area
				//calculate the degree
				this.degree = (float) Math.atan((x - OuterCenter_X) / (y - OuterCenter_Y));
				if(y < OuterCenter_Y){
					InnerCenter_X = OuterCenter_X + (float) (Outer_R * -Math.sin(this.degree));
					InnerCenter_Y = OuterCenter_Y + (float) (Outer_R * -Math.cos(this.degree));
				}else{
					InnerCenter_X = OuterCenter_X + (float) (Outer_R * Math.sin(this.degree));
					InnerCenter_Y = OuterCenter_Y + (float) (Outer_R * Math.cos(this.degree));
				}
			}
		}
	}
	
	/**
	 * Call this method when ACTION_UP 
	 */
	protected void reset(){
		InnerCenter_X = OuterCenter_X;
		InnerCenter_Y = OuterCenter_Y;
		isRockerTouched = false;
		this.degree = 0;
	}
	
	/**
	 * Calculate the distance between two dots
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return distance
	 */
	private float distance(float x1, float y1, float x2, float y2){
		float distance = (float) Math.sqrt((float) Math.pow(x1-x2, 2) + (float) Math.pow(y1-y2, 2));
		return distance;
	}

	/**
	 * get the degree between current position and center position
	 * @return
	 */
	public float getDegree() {
		return degree;
	}
	
}

