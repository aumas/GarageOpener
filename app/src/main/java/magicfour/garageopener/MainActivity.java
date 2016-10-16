package magicfour.garageopener;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private ProgressBar progressBar1;
    private ImageView status;
    private WebService ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        status = (ImageView) findViewById(R.id.Status_ImageView);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckGarageDoorAsync().execute();
            }
        });

        ImageButton openButton = (ImageButton) findViewById(R.id.OpenButton);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new ActivateGarageDoorAsync().execute();
            }
        });
        ImageButton closeButton = (ImageButton) findViewById(R.id.CloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new ActivateGarageDoorAsync().execute();
            }
        });
        final Handler handler = new Handler();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.d("z", "check garage door");
                        setUI(ws.IsGarageDoorOpen());
                    }
                });
            }
        }, 0, 900000);
        new CheckGarageDoorAsync().execute();
    }

    /*
	 * Set UI components Garage Open Open button is disabled Close button is
	 * enabled Status graphic - OPEN Garage Closed Open button is enabled Close
	 * button is disabled Status graphic - Closed Garage Unknown Open button is
	 * disabled Close button is disabled Status graphic - black
	 */
    private void setUI(String mode) {
        if (mode.equals("Open")) {
            ImageButton o = (ImageButton) findViewById(R.id.OpenButton);
            o.setImageResource(R.drawable.open_button_alt); // Open button
            // depressed
            o.setEnabled(false); 							// Disable open button
            ImageButton c = (ImageButton) findViewById(R.id.CloseButton);
            c.setImageResource(R.drawable.close_button); 	// Closed button up
            c.setEnabled(true); 							// Enable close button
            ImageView status = (ImageView) findViewById(R.id.Status_ImageView);
            status.setImageResource(R.drawable.status_open); // Set status
            // graphic
        } else if (mode.equals("Closed")) {
            ImageButton o = (ImageButton) findViewById(R.id.OpenButton);
            o.setImageResource(R.drawable.open_button); 	// Open button up
            o.setEnabled(true); 							// Enable open button
            ImageButton c = (ImageButton) findViewById(R.id.CloseButton);
            c.setImageResource(R.drawable.close_button_alt); // Close button
            // depressed
            c.setEnabled(false); 							 // Disable close button
            ImageView status = (ImageView) findViewById(R.id.Status_ImageView);
            status.setImageResource(R.drawable.status_closed);// Set status
            // graphic
        } else {
            ImageButton o = (ImageButton) findViewById(R.id.OpenButton);
            o.setImageResource(R.drawable.open_button_alt);  // Open button
            // depressed
            o.setEnabled(false); 							 // Disable open button
            ImageButton c = (ImageButton) findViewById(R.id.CloseButton);
            c.setImageResource(R.drawable.close_button_alt); // Close button
            // depressed
            c.setEnabled(false); 							 // Disable close button
            ImageView status = (ImageView) findViewById(R.id.Status_ImageView);
            status.setImageResource(R.drawable.status_unknown);// Set status
            // graphic
        }
    }

    /*
	 * Check the garage door on background thread with an AsyncTask
	 */
    private class CheckGarageDoorAsync extends AsyncTask<Void, Void, String[]> {
        @Override
        protected void onPreExecute() {
            setUI("Unkown"); 					      // While we check disable all buttons and clear status display
            progressBar1.setVisibility(View.VISIBLE); // Display progress bar
        }

        @Override
        protected String[] doInBackground(Void... arg0) {
            //String r = ws.IsGarageDoorOpen(); 		 // Make webservice call to check
            // garage door
            return new String[] { r }; 				 // Return result
        }

        @Override
        protected void onPostExecute(String[] result) {
            progressBar1.setVisibility(View.INVISIBLE); // Hide progress bar
            setUI(result[0]); 							// Pass result to setUI method to sync the UI
        }

    }

    /*
	 * Activate the garage door on a background thread with an AsyncTask
	 */
    private class ActivateGarageDoorAsync extends
            AsyncTask<String, String, String[]> {
        @Override
        protected void onPreExecute() {
            setUI("Unkown"); 							// While we check disable all buttons and clear
            // display graphic
            progressBar1.setVisibility(View.VISIBLE);	// Display progress bar
        }

        @Override
        protected String[] doInBackground(String... arg0) {
            String start = ws.IsGarageDoorOpen();

            String r = ws.ActivateGarageDoor(); 		// Activate garage door
            for (int i = 0; i < 60; i++) { 				// Give the door up to 1 minute to
                // close/open
                try {
                    Thread.sleep(1000); 				// Pause for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r = ws.IsGarageDoorOpen(); 				// Check the door status
                if (r.equals(start) == false && r.equals("failed") == false)
                    break; 								// The door is done opening/closing
            }
            return new String[] { r }; 					// Return the status of garage
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(String[] result) {
            progressBar1.setVisibility(View.INVISIBLE);
            setUI(result[0]);
        }

    }

}
