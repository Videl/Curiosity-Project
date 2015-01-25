package org.fslhome.videl.curiosityapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.fslhome.videl.curiosityapplication.model.CuriosityDBAdapter;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class CuriositiesActivity extends ActionBarActivity {

    private String currentCuriosity;

    // Mapsforge stuff
    private String MAPFILE;

    private MapView mapView;
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;

    // DB stuff
    private CuriosityDBAdapter dbAdapter;


    // ActionBarActivity methods stuff

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(this.getApplication());

        setContentView(R.layout.activity_curiosities);
        this.mapView = (MapView) findViewById(R.id.mapView);
        this.dbAdapter = new CuriosityDBAdapter(this);

        this.dbAdapter.open_read();


        // Get the data sent from the homepage
        Intent myIntent = getIntent();
        currentCuriosity = myIntent.getStringExtra("org.fslhome.curiosity.curiosities.button_clicked");
        this.MAPFILE = currentCuriosity.toLowerCase() + ".map";

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

        this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 12);

        // tile renderer layer using internal render theme
        this.tileRendererLayer = new TileRendererLayer(tileCache,
                this.mapView.getModel().mapViewPosition, true, true, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setMapFile(getMapFile());
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        // only once a layer is associated with a mapView the rendering starts
        // this is the base layer, with the map.
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);

        // generation of all the dots from the database.

        Bitmap bitmap;
        Marker temp;
        Cursor cursor = this.dbAdapter.fetchAllDataAboutOneCuriosity(currentCuriosity);

        final String dataString[][] = new String[cursor.getCount()][2];
        Double dataGPS[][] = new Double[cursor.getCount()][2];
        int i = 0;

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            dataString[i][0] = cursor.getString(0);
            dataString[i][1] = cursor.getString(1);
            dataGPS[i][0] = cursor.getDouble(2);
            dataGPS[i][1] = cursor.getDouble(3);
            Log.i("Videl", "Data 0: " + dataString[i][0]);
            Log.i("Videl", "Data 1: " + dataString[i][1]);
            Log.i("Videl", "Data 2: " + dataGPS[i][0]);
            Log.i("Videl", "Data 3: " + dataGPS[i][1]);
            i++;
            cursor.moveToNext();
        }

        Log.i("Videl", "Number of rows for this Curiosity: " + cursor.getCount());

        for(int j = 0; j < i; j++) {
            LatLong coordinates = new LatLong(dataGPS[j][0], dataGPS[j][1]);
            if(j == 0)
            {
                this.mapView.getModel().mapViewPosition.setCenter(coordinates);
            }
            TextView bubbleView = new TextView(this);
            Utils.setBackground(bubbleView, getResources().getDrawable(R.drawable.marker_red));
            bubbleView.setText("" + (j + 1));
            bubbleView.setGravity(Gravity.CENTER);
            bubbleView.setMaxEms(20);
            bubbleView.setTextSize(15);
            bubbleView.setTextColor(Color.BLACK);
            bubbleView.setPadding(0, 0, 0, 70);
            bitmap = Utils.viewToBitmap(this, bubbleView);
            temp = new Marker(coordinates, bitmap, 0, 0);
            this.mapView.getLayerManager().getLayers().add(temp);
        }

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
        /*
       FileInputStream enva = null;
        try {
            enva = openFileInput("dublin.map");
            Log.i("Videl_fi", "GOT IT");

        } catch (FileNotFoundException e) {
            Log.i("Videl_fi", "Désolé...");
            e.printStackTrace();
        }
        File env = this.getFilesDir();
        Log.i("Videl_fi", "Je vais vous montrer où sont tous les fichiers (" + env.list().length + ")");
        for(String fi: env.list())
        {
            Log.i("Videl_fi", fi);
        }*/

        File file = new File(Environment.getExternalStorageDirectory(), this.MAPFILE);
        return file;
    }

}
