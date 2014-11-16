// ColorCoder MainActivity
// 
// Main activity of the app.

package cmps121.finalproject.rhms;

import java.io.OutputStream;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private DrawingView drawView; // used to communicate with drawView class, which displays canvas
	private RadioGroup radioColors; // used for selected canvas background
    private SeekBar seekerRed, seekerGreen, seekerBlue; // used for values set by RGB sliders
    private int rVal = 255, gVal = 0, bVal = 0; // used to keep track of color values
    private TextView redDisplay, greenDisplay, blueDisplay; // used to display RGB values
    private String hexCode; // for naming the image when saved to Gallery
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnRadio(); // user can select from different bg colors
		addSeekListenerRed(); // when user changes R slider
		addSeekListenerGreen(); // when user changes G slider
		addSeekListenerBlue(); // when user changes B slider
		drawView = (DrawingView)findViewById(R.id.drawing);
		// set the different color values
		redDisplay = (TextView)findViewById(R.id.redEdit);
		redDisplay.setText("" + rVal);
		greenDisplay = (TextView)findViewById(R.id.greenEdit);
		greenDisplay.setText("" + gVal);
		blueDisplay = (TextView)findViewById(R.id.blueEdit);
		blueDisplay.setText("" + bVal);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}

	
	/* 
	 * Displays custom options menu opened 
	 * by the settings/menu button built into the device.
	 * 
	 * Options to select from are: 
	 *     different color presets 
	 *     different circle sizes for different display sizes
	 *     save canvas as an image to the Gallery on device
	 */
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		// bold colors selected
		  case R.id.preRed:
		      presetSelected(255,0,0);
			  break;
		  case R.id.preGreen:
			  presetSelected(0,255,0);
		   break;
		  case R.id.preBlue:
			  presetSelected(0,0,255);
		   break;
		  case R.id.preCyan:
			  presetSelected(0,255,255);
		   break;
		  case R.id.preMagenta:
			  presetSelected(255,0,255);
		   break;
		  case R.id.preYellow:
			  presetSelected(255,255,0);
		   break;
		  case R.id.prePurple:
			  presetSelected(128,0,128);
			  break;
		  case R.id.preOrange:
			  presetSelected(255,128,0);
			  break;
			  
		   //pastels selected
		   
		  case R.id.prePastPink:
		      presetSelected(255,204,255);
			  break;
		  case R.id.prePastYellow:
			  presetSelected(255,255,180);
		   break;
		  case R.id.prePastGreen:
			  presetSelected(204,255,204);
		   break;
		  case R.id.prePastBlue:
			  presetSelected(192,204,255);
		   break;
		  case R.id.prePastPurple:
			  presetSelected(229,192,255);
		   break;
		   
		   // metals selected
		   
		  case R.id.preGold:
		      presetSelected(204,182,75);
			  break;
		  case R.id.preSilver:
			  presetSelected(192,192,192);
		   break;
		  case R.id.preCopper:
			  presetSelected(192,108,52);
		   break;
		   
		   // grayscale selected
		   
		  case R.id.preBlack:
		      presetSelected(0,0,0);
			  break;
		  case R.id.preDarkGray:
			  presetSelected(96,96,96);
		   break;
		  case R.id.preMidGray:
			  presetSelected(128,128,128);
		   break;
		  case R.id.preLightGray:
			  presetSelected(160,160,160);
		   break;
		  case R.id.preWhite:
			  presetSelected(255,255,255);
		   break;
		   
		   // circles sizes
		  case R.id.circleSmall:
			  drawView.changeCircleSize(1);
		   break;
		  case R.id.circleMedium:
			  drawView.changeCircleSize(2);
		   break;
		  case R.id.circleLarge:
			  drawView.changeCircleSize(3);
		   break;
		  case R.id.circleXL:
			  drawView.changeCircleSize(4);
		   break;
		   
		  case R.id.menuSave:
			  saveImage();
		   break;   
	}
		return super.onOptionsItemSelected(item);
	}
	
	
	/* 
	 * Listener for the radio buttons.
	 * Changes the background color of the canvas based on what button the user selects.
	 * When one is selected, all others are deselected.
	 */
	
	public void addListenerOnRadio() {
		radioColors = (RadioGroup)findViewById(R.id.radioBG);
		radioColors.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(checkedId == R.id.radioBlack){
				drawView.changeCanvasColor(3); // set to black
			}else if(checkedId == R.id.radioWhite){
				drawView.changeCanvasColor(1); // set to white
			}else if(checkedId == R.id.radioGray){
				drawView.changeCanvasColor(2); // set to gray
			}
		}
		});	
	}
	
	
	/* 
	 * Listener for the red value slider.
	 * Changes the red color value according to the seekBar's progress.
	 */
	
	public void addSeekListenerRed() {
		seekerRed = (SeekBar)findViewById(R.id.redSeeker);
		seekerRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
		    public void onProgressChanged(SeekBar seekBar, int progress,
		    		boolean fromUser) {
		    	// TODO Auto-generated method stub
		    	rVal = progress;
		    	redDisplay.setText("" + rVal);
		    	drawView.changePaintColor(rVal, gVal, bVal);
		    }
		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub
		    }
		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub  	
		    }
		});	
	}
	
	
	/* 
	 * Listener for the green value slider.
	 * Changes the green color value according to the seekBar's progress.
	 */
	
	public void addSeekListenerGreen() {
		seekerGreen = (SeekBar)findViewById(R.id.greenSeeker);
		seekerGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
		    public void onProgressChanged(SeekBar seekBar, int progress,
		    		boolean fromUser) {
		    	gVal = progress;
		    	greenDisplay.setText("" + gVal);
		    	drawView.changePaintColor(rVal, gVal, bVal);
		    }
		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub
		    }
		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub  	
		    }
		});	
	}
	
	
	/* 
	 * Listener for the blue value slider.
	 * Changes the blue color value according to the seekBar's progress.
	 */
	
	public void addSeekListenerBlue() {
		seekerBlue = (SeekBar)findViewById(R.id.blueSeeker);
		seekerBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
		    public void onProgressChanged(SeekBar seekBar, int progress,
		    		boolean fromUser) {
		    	// TODO Auto-generated method stub
		    	bVal = progress;
		    	blueDisplay.setText("" + bVal);
		    	drawView.changePaintColor(rVal, gVal, bVal);
		    }
		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub
		    }
		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
		    	// TODO Auto-generated method stub  	
		    }
		});	
	}
	
	
	/* 
	 * If the user selects a preset,
	 * set the values of the color displayed and update everything.
	 */
	
	public void presetSelected(int r, int g, int b){ 
		rVal = r;
		gVal = g;
		bVal = b;
		seekerRed.setProgress(rVal);
		seekerGreen.setProgress(gVal);
		seekerBlue.setProgress(bVal);
		drawView.changePaintColor(rVal, gVal, bVal);
		
	}
	
	
	/* 
	 * When "save" is clicked in options menu, dialogue box appears.
	 * If the user wants to save, an image of the canvas is saved to the Gallery on the device
	 */ 
	
	public void saveImage(){
		hexCode = String.format( "#%02X%02X%02X", rVal, gVal, bVal); // format hexCode (used for naming)
		// create alert dialogue
		AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
		saveDialog.setTitle("Save Canvas");
		  saveDialog.setMessage("Save canvas to device's Gallery?");
		  // user wants to save
		  saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
		      public void onClick(DialogInterface dialog, int which){
		    	  drawView.setDrawingCacheEnabled(true);
		    	  String imgSaved = insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
		    			    hexCode +".jpg", ("Color: " + "" + rVal + "," + "" + gVal +  "," + "" + gVal));
		    	  if(imgSaved!=null){ // success
		    		    Toast savedToast = Toast.makeText(getApplicationContext(), 
		    		        "Image saved to Gallery", Toast.LENGTH_SHORT);
		    		    savedToast.show();
		    		}
		    		else{ // image was not saved
		    		    Toast unsavedToast = Toast.makeText(getApplicationContext(), 
		    		        "Image could not be saved.", Toast.LENGTH_SHORT);
		    		    unsavedToast.show();
		    		}
		    	  drawView.destroyDrawingCache();
		      }
		  });
		  // user does not want to save
		  saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
		      public void onClick(DialogInterface dialog, int which){
		          dialog.cancel();
		      }
		  });
		  saveDialog.show();
	}
	
	
	/*
	 * method to replace default insert image from MediaStore.
	 * needed mostly to get correct date on image
	 * from The Android Open Source Project:
	 *     https://github.com/android/platform_frameworks_base/blob/master/core/java
	 *         /android/provider/MediaStore.java#L888
	 * 
	 * @param cr The content resolver to use
	 * @param imagePath The path to the image to insert
	 * @param name The name of the image
	 * @param description The description of the image
	 * @return The URL to the newly created image
	 * @throws FileNotFoundException
	 */
	
	private static final String insertImage(ContentResolver cr, Bitmap source,
	        String title, String description) {
	    ContentValues values = new ContentValues();
	    values.put(Media.TITLE, title);
	    values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
	    values.put(Images.Media.MIME_TYPE, "image/jpeg");
	    Uri uri = null;
	    String stringUrl = null; /* value to be returned */
	    try {
	        uri = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);

	        if (source != null) {
	            OutputStream imageOut = cr.openOutputStream(uri);

	            try {
	                source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
	            } finally {                 
	                imageOut.close();
	            }

	            long id = ContentUris.parseId(uri);
	            // Wait until MINI_KIND thumbnail is generated.
	            Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id,
	                    Images.Thumbnails.MINI_KIND, null);
	        } else {
	            cr.delete(uri, null, null);
	            uri = null;
	        }
	    } catch (Exception e) {
	        if (uri != null) {
	            cr.delete(uri, null, null);
	            uri = null;
	        }
	    }
	    if (uri != null) {
	        stringUrl = uri.toString();
	    }
	    return stringUrl;
	}

} // end of MainActivity 
