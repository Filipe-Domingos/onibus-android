package br.com.caelum.ondeestaobusao.gps;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.com.caelum.ondeestaobusao.activity.ListPontosAndOnibusActivity;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.GetJsonAsyncTask;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;

public class GPSControl {

	private static final long TIME = 120000;
	private static final int DISTANCE = 5;
	private final ListPontosAndOnibusActivity activity;
	private LocationListener locationListener;

	public GPSControl(ListPontosAndOnibusActivity activity) {
		this.activity = activity;
		this.locationListener = createLocationListener();

	}

	private LocationListener createLocationListener() {
		return new LocationListener() {
			public void onLocationChanged(Location location) {
				Coordenada localizacao = makeUseLocation(location);
				activity.setAtual(localizacao);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
	}

	public void execute() {
		LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.i("Provider", "GPS");
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, locationListener);
		}

		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Log.i("Provider", "NETWORK");
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, locationListener);
		}
	}

	private Coordenada makeUseLocation(Location location) {
		Coordenada coordenada = null;
		if (location != null) {
			coordenada = new Coordenada(location.getLatitude(), location.getLongitude());
			new GetJsonAsyncTask<Coordenada, List<Ponto>>(new PontosEOnibusTask(activity.getDelegatePontos())).execute(coordenada);
		} else {
			Toast.makeText(activity, "Location not found", Toast.LENGTH_LONG).show();
			activity.onBackPressed();
		}
		return coordenada;
	}

}
