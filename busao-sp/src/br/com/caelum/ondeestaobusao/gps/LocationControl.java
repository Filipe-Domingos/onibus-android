package br.com.caelum.ondeestaobusao.gps;

import java.util.Collection;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import br.com.caelum.ondeestaobusao.activity.PontosProximosActivity;
import br.com.caelum.ondeestaobusao.model.Coordenada;

public class LocationControl {
	private Collection<LocationObserver> observers = new LinkedList<LocationObserver>();
	private Coordenada atual;
	private final PontosProximosActivity activity;

	public LocationControl(PontosProximosActivity busaoActivity) {
		this.activity = busaoActivity;
	}

	public void makeUseLocation(Location location) {
		if (location != null) {
			this.atual = new Coordenada(location.getLatitude(), location.getLongitude());
			for (LocationObserver observer : observers) {
				observer.callback(atual);
			}
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
			dialog.setTitle("Ocorreu um erro :(");
			dialog.setMessage("Infelizmente não foi possível obter sua localização.");
			dialog.setCancelable(true);
			dialog.setPositiveButton("Quero habilitar o GPS",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        Intent gpsIntent = new Intent(
	                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                        activity.startActivity(gpsIntent);
	                    }

	                });
	        dialog.setNegativeButton("Agora não",
	                new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	activity.finish();
	                    }
	                });
	        dialog.show();
		}
	}

	public void registerObserver(LocationObserver observer) {
		if (atual != null) {
			observer.callback(atual);
		}
		this.observers.add(observer);
	}
	
	public void unRegisterObserver(LocationObserver observer) {
		this.observers.remove(observer);
	}
	
	public Coordenada getAtual() {
		return atual;
	}

}
