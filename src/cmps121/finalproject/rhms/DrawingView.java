// ColorCoder DrawingView
// 
// Class that deals with the canvas object and display of the app.
//
// base code from drawing app by Sue Smith
// http://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-interface-creation--mobile-19021

package cmps121.finalproject.rhms;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DrawingView extends View {
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFFFF0000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	// RGB color values
	private int rVal = 255, gVal = 0, bVal = 0;
	// convert RGB to hex
	private String hexText = String.format("#%02X%02X%02X", rVal, gVal, bVal);
	// color of text displayed on canvas
	private Paint textPaint;
	// CMYK color values
	private float cVal = 0, mVal = 1, yVal = 1, kVal = 0;
	// HSV color values
	private int hVal = 0;
	private float sVal = 100, vVal = 100;
	// circle radius
	private final int rSmall = 50, rMed = 100, rLarge = 200, rXL = 300;
	// default circle size
	private int circleSize = rLarge;
	
	public DrawingView(Context context, AttributeSet attrs){ // class constructor
	    super(context, attrs);
	    setupDrawing();
	}
	
	private void setupDrawing(){
		// initial settings
		drawPaint = new Paint();
		// text settings for canvas display
		textPaint = new Paint();
		textPaint.setColor(0xFF000000);
		textPaint.setTextSize(textPaint.getTextSize()*2);
		textPaint.setTextAlign(Paint.Align.LEFT);
		// circle paint settings
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(20);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	// view given size
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	
	
	/* 
	 * Main onDraw() method for the canvas.
	 * 
	 * Displays colored circle and associated color values.
	 */
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawARGB(255,255,255,255); // canvas default color is white
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint); 
		// draw the circle
	    drawPaint.setStyle(Paint.Style.FILL); 
		canvas.drawCircle(canvasBitmap.getWidth()/2, canvasBitmap.getHeight()/2, circleSize, drawPaint);
		// display RGB values in bottom left of canvas 
		canvas.drawText("R,G,B: " + ("" + rVal) + "," + ("" + gVal) + "," + ("" + bVal), 10, canvasBitmap.getHeight() - 10, textPaint);
		// display hex value in bottom right of canvas
	    canvas.drawText("Hex: " + hexText, canvasBitmap.getWidth() -  textPaint.getTextSize()*2 - 113, canvasBitmap.getHeight() - 10, textPaint);
		// display CMYK in top left of canvas
		canvas.drawText("C: " + String.format("%.3f", cVal), 10, 30, textPaint);
		canvas.drawText("M: " + String.format("%.3f", mVal), 10, 50, textPaint);
		canvas.drawText("Y: " + String.format("%.3f", yVal), 10, 70, textPaint);
		canvas.drawText("K: " + String.format("%.3f", kVal), 10, 90, textPaint);
		// display HSV in top right of canvas
		canvas.drawText("H: " + ("" + hVal), canvasBitmap.getWidth() - textPaint.getTextSize()*2 - 30, 30, textPaint);
		canvas.drawText("S: " + String.format("%.0f", sVal), canvasBitmap.getWidth() -  textPaint.getTextSize()*2 - 30, 50, textPaint);
		canvas.drawText("V: " + String.format("%.0f", vVal), canvasBitmap.getWidth() -  textPaint.getTextSize()*2 - 30, 70, textPaint);
	}
	
	
	/* 
	 * Set the color for the circle.
	 */
	
	public void setColor(String newColor){
		//set color   
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
		}
	
	
	/* 
	 * Change the background color of the canvas.
	 * Takes in an int.  If int is...
	 *     1, canvas is white
	 *     2, canvas is gray
	 *     3, canvas is black
	 */
	
	public void changeCanvasColor(int c){
		if (c == 1){ // set bg to white 
			drawCanvas.drawARGB(255,255,255,255); // background white
			textPaint.setColor(0xFF000000); // color of text is black
			invalidate();
		}
		else if (c == 2){ // set bg to gray
			drawCanvas.drawARGB(255,128,128,128); // background gray
			textPaint.setColor(0xFFFFFFFF); // text is white
			invalidate();
		}
		else if (c == 3){ // set bg to black 
			drawCanvas.drawARGB(255,0,0,0); // background black
			textPaint.setColor(0xFFFFFFFF); // color is white
			invalidate();
		}
	}
	
	
	/* 
	 * Change the color of the circle displayed.
	 * Parameters r, g, and b correspond to the new RGB values of the circle.
	 */
	
	public void changePaintColor(int r, int g, int b){
		// set RGB
		rVal = r;
		gVal = g;
		bVal = b;
		// format into hex
		String newColor = String.format( "#FF%02x%02x%02x", rVal, gVal, bVal);
		// turn into color object
		paintColor = Color.parseColor(newColor);
		// change the circle color
		drawPaint.setColor(paintColor);
		// update the hex string displayed
		hexText = String.format( "#%02X%02X%02X", rVal, gVal, bVal);
        // calculate the values for the other color spaces
		calculateCMYK(rVal, gVal, bVal);
		calculateHSV(rVal, gVal, bVal);
		// force redraw
		invalidate();
	}
	
	
	/* 
	 * Takes in RGB values and calculates corresponding CMYK values.
	 * Algorithm from rapidtables.com
	 */
	
	public void calculateCMYK(int r, int g, int b){
		// convert rgb values to be 0 - 1
		float rConv = (float) r/255;
		float gConv = (float) g/255;
		float bConv = (float) b/255;
		
		// black is 1-max(r, g, b);
		kVal = 1 - Math.max(rConv,Math.max(gConv, bConv));
		
		// if k is 1, everything else is 0
		if(kVal == 1){
			cVal = 0;
			mVal = 0;
			yVal = 0;
		}
		else{
		cVal = (1-rConv-kVal)/(1-kVal); // c = (1 - r -k)/(1-k)
		mVal = (1-gConv-kVal)/(1-kVal); // m = (1-g-k)/(1-k)
		yVal = (1-bConv-kVal)/(1-kVal); // y = (1-b-k)/(1-k)
		}
	}
	
	
	/* 
	 * Takes in RGB values and calculates corresponding HSV values.
	 * Algorithm from rapidtables.com
	 */
	
	public void calculateHSV(int r, int g, int b){
		// defaults
		float s = 100, v = 100;
		int h = 0;
		
		// convert RGB to be 0-1
		float rConv = (float) r/255;
		float gConv = (float) g/255;
		float bConv = (float) b/255;
		
		float colorMax = Math.max(rConv, Math.max(gConv, bConv)); // max of r,g,b
		float colorMin = Math.min(rConv, Math.min(gConv, bConv)); // min of r,g,b
		float delta = colorMax - colorMin; // difference between max and min
		
		v = colorMax; //  value = max(r,g,b)
		
		if(delta == 0){ // max and min are the same
			h = 0; // hue is 0
			s = 0; // saturation is also 0
		}
		else{
			s = delta/colorMax; // saturation = (mex-min)/max
			
			if(colorMax == rConv){ // red is max
				h = (int)(60*(((gConv-bConv)/delta)%6)); // h = (60deg*(g-b)/delta)%6
			}
			else if(colorMax == gConv){ // green is max
				h = (int)(60*(((bConv-rConv)/delta) + 2)); // h = (60deg*(b-r)/delta)+2
			}
			else if(colorMax == bConv){ // blue is max
				h = (int)(60*(((rConv-gConv)/delta) + 4)); // h = (60deg*(r-g)/delta)+4
			}
		}
		
		if (h < 0){
			h += 360; // h between 0 and 360 degrees
		}
		
		// set HSV 
		hVal = h; // degrees
		sVal = s*100; // percent
		vVal = v*100; // percent
	}
	
	
	/* 
	 * Change the circle size to one selected by user.
	 * Default is large.
	 * Takes in integer size.  If size is...
	 *     1, radius is small
	 *     2, radius is medium
	 *     3, radius is large
	 *     4, radius is extra large
	 */
	
	public void changeCircleSize(int size){
		if(size == 1){
			circleSize = rSmall;
		}
		else if(size == 2){
			circleSize = rMed;
		}
		else if(size == 3){
			circleSize = rLarge;
		}
		else if(size == 4){
			circleSize = rXL;
		}
		// force redraw
		invalidate();
	}
	
} // end of class
