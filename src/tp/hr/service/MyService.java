package tp.hr.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements SensorEventListener {
	
	SensorManager sensorManager;
	Sensor proximity, accelerometer;
	AudioManager audioManager;
	float prox, xAccel, yAccel, zAccel, last_x, last_y, last_z;
	String flag = "";
	int Shak_Threshold = 800;
	long lastUpdate = 0;
	Boolean isShaked = false;
	Timer timer = new Timer();
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, proximity, sensorManager.SENSOR_DELAY_NORMAL);
		
		Toast toast = Toast.makeText(getApplicationContext(), "Service start", Toast.LENGTH_SHORT);
		toast.show();
		Log.d("Service", "Service Started");
		
		final Handler handler = new Handler();
		TimerTask timertask = new TimerTask() {
			
			
			@Override
			public void run() {
				handler.post(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						try {
        					if (last_z > 8 && !flag.equals("Home") && prox != 0)
        					{
        						Home ();
        					}
        					else if (isShaked == true && prox == 0 && !flag.equals("Pocket"))
        					{
        						Pocket ();
        					}
        					else if (last_z < -8 && prox == 0 && !flag.equals("Silent"))
        					{
        						Silent ();
        					}
        					else
        					{
        						
        					}
        				}
        				catch (Exception e)
        				{
        					
        				}
					}
						
					
			});
			}
		};
				
		timer.schedule(timertask, 0, 2000);
		return START_STICKY;
	}
	


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		timer.cancel();
		sensorManager.unregisterListener(this);
		Toast toast = Toast.makeText(getApplicationContext(), "Sercice stopped", Toast.LENGTH_SHORT);
		toast.show();
		
		
		super.onDestroy();
	}



	

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		try {		
			Sensor sens = event.sensor;
			
			if (sens.getType() == Sensor.TYPE_PROXIMITY)
			{
				prox = event.values[0];
			}
			
			if (sens.getType()== Sensor.TYPE_ACCELEROMETER)
			{
				long currentTime = System.currentTimeMillis();
				if ((currentTime - lastUpdate) > 100)
				{
					long diffTime = currentTime - lastUpdate;
					lastUpdate = currentTime;		
					
					xAccel = event.values[0];
					yAccel = event.values[1];
					zAccel = event.values[2];
				
					float speed = Math.abs(xAccel + yAccel + zAccel - last_x - last_y - last_z) / diffTime * 10000;
					
					if (speed > Shak_Threshold)
					{
						isShaked = true;
					}
				}
				last_x = event.values[0];
				last_y = event.values[1];
				last_z = event.values[2];
			}
		}
		catch (Exception e)
		{
			
		}
		
	}
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	private void Home() {
		// TODO Auto-generated method stub
		audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
		audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
		flag = "Home";
		Toast toast = Toast.makeText(getApplicationContext(), "Home mode", Toast.LENGTH_SHORT);
		toast.show();
		Log.d("Service :","Normal");
		
	}
	private void Silent() {
		// TODO Auto-generated method stub
		audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		flag = "Silent";
		Toast toast = Toast.makeText(getApplicationContext(), "Silent Mode Activated", Toast.LENGTH_SHORT);
		toast.show ();
		Log.d("Service", "Silent Mode");
		
		
	}

	private void Pocket() {
		// TODO Auto-generated method stub
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audioManager.setStreamVolume(AudioManager.STREAM_RING, 4, 0);
		audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
		flag = "Pocket";
		isShaked = false;
		Toast toast = Toast.makeText(getApplicationContext(), "Pocket Mode", Toast.LENGTH_SHORT);
		toast.show ();
		Log.d("Service", "Pocket Mood");
	}

	


	
	
}