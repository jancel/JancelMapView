package net.jancel;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class JancelMapView extends MapActivity implements LocationListener {
	private LinearLayout linearLayout;
	private MapView mapView;
	private MapController mapController;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private JancelItemizedOverlay itemizedOverlay;
	private LocationManager locationManager;
	private String provider;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setStreetView(true);
        mapController = mapView.getController();
        mapController.setZoom(17);
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        itemizedOverlay = new JancelItemizedOverlay(drawable);    
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		
	    itemizedOverlay.addOverlay(getPoint());

        mapOverlays.add(itemizedOverlay);
        mapController.animateTo(here());
 
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		itemizedOverlay.removeItem(0);
		itemizedOverlay.addOverlay(getPoint());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		this.provider = null;
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		this.provider = provider;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private OverlayItem getPoint(){
		OverlayItem overlayItem = new OverlayItem(here(), "", "");
		return overlayItem;
	}
	
	private GeoPoint here(){
		return new GeoPoint(getLatitude(),getLongitude());
	}
	
	private int getLongitude(){
		int lon = -99120000;
		try {
			if (provider != null) lon = (int)(this.locationManager
				.getLastKnownLocation(provider).getLongitude() * 1e6);
			System.out.println("The current longitude will be set to: " + lon);
		} catch (NullPointerException npe){
			npe.printStackTrace();
		}
		return lon;
	}
	
	private int getLatitude(){
		int lat = 19240000;
		try {
			if (provider != null) lat = (int)(this.locationManager
				.getLastKnownLocation(provider).getLatitude()*1e6);
			System.out.println("The current latitude will be set to: " + lat);
		} catch (NullPointerException npe){
			npe.printStackTrace();
		}
		return lat;
	}
}