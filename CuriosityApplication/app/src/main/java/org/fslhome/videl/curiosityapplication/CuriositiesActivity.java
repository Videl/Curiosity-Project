package org.fslhome.videl.curiosityapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.graphics.AndroidResourceBitmap;
import org.mapsforge.map.android.layer.MyLocationOverlay;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;


public class CuriositiesActivity extends ActionBarActivity {

    private String currentCuriosity;

    // Mapsforge stuff

    // name of the map file in the external storage
    private final String MAPFILE = "germany.map";

    private MapView mapView;
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;

    // ActionBarActivity methods stuff

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(this.getApplication());
        //this.mapView = new MapView(this);

        setContentView(R.layout.activity_curiosities);
        this.mapView = (MapView) findViewById(R.id.mapView);


        //this.mapView = new MapView(this);
        //setContentView(this.mapView);

        // Get the data sent from the homepage
        Intent myIntent = getIntent();
        currentCuriosity = myIntent.getStringExtra("org.fslhome.curiosity.curiosities.button_clicked");

        setTitle(currentCuriosity);

        // Map

        this.mapView.setClickable(true);
        this.mapView.getMapScaleBar().setVisible(true);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
        this.mapView.getMapZoomControls().setZoomLevelMax((byte) 20);

        // create a tile cache of suitable size
        this.tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                this.mapView.getModel().frameBufferModel.getOverdrawFactor());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Mapsforge

    @Override
    protected void onStart() {
        super.onStart();

        //this.mapView.getModel().mapViewPosition.setCenter(new LatLong(52.517037, 13.38886));
        this.mapView.getModel().mapViewPosition.setCenter(new LatLong( 52.5170365, 13.3888599));

        this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 12);

        // tile renderer layer using internal render theme
        this.tileRendererLayer = new TileRendererLayer(tileCache,
                this.mapView.getModel().mapViewPosition, true, true, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setMapFile(getMapFile());
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);


        //Drawable drawable = getResources().getDrawable(R.drawable.marker_green);
        //Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        Bitmap bitmap;

        // only once a layer is associated with a mapView the rendering starts
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);

        TextView bubbleView = new TextView(this);
        Utils.setBackground(bubbleView, getResources().getDrawable(R.drawable.marker_red));
        bubbleView.setGravity(Gravity.CENTER);
        bubbleView.setMaxEms(20);
        bubbleView.setTextSize(15);
        bubbleView.setTextColor(Color.BLACK);
        bubbleView.setText("15");
        bubbleView.setPadding(0,0,0,70);
        bitmap = Utils.viewToBitmap(this, bubbleView);
        this.mapView.getLayerManager().getLayers().add(new Marker(new LatLong(52.5170365, 13.3888599), bitmap, 0, 0));

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mapView.getLayerManager().getLayers().remove(this.tileRendererLayer);
        this.tileRendererLayer.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.tileCache.destroy();
        this.mapView.getModel().mapViewPosition.destroy();
        this.mapView.destroy();
        AndroidResourceBitmap.clearResourceBitmaps();
    }

    public void onPause() {
        //myLocationOverlay.disableMyLocation();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        //this.myLocationOverlay.enableMyLocation(true);
    }

    private File getMapFile() {
        File file = new File(Environment.getExternalStorageDirectory(), MAPFILE);
        return file;
    }

}
