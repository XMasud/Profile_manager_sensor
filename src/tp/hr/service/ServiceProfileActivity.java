package tp.hr.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ServiceProfileActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
    
    public void StartService(View v)
    {
    	try
    	{
    		Intent intent = new Intent(this, MyService.class);
			startService(intent);
    	}
    	catch (Exception e) {
			// TODO: handle exception
    		Toast toast = Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT);
			toast.show ();
			Log.d("Service", "Exception");
		}
    }
    public void StopService(View v)
    {
    
    	try
    	{
    		Intent intent = new Intent(this, MyService.class);
			stopService(intent);
    	}
    	catch (Exception e) {
			// TODO: handle exception
    		Toast toast = Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT);
			toast.show ();
			Log.d("Service", "Exception");
		}
    }
}