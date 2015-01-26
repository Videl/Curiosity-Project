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
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class CuriositiesActivity extends ActionBarActivity {

    private String currentCuriosity;

    // Mapsforge stuff
    private String MAPFILE;

    private MapView mapView;
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;

    // DB stuff
    private CuriosityDBAdapter dbAdapter;

    // ListView data
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(this.getApplication());

        setContentView(R.layout.activity_curiosities);
        this.mapView = (MapView) findViewById(R.id.mapView);
        this.dbAdapter = new CuriosityDBAdapter(this);


        // Get the data sent from the homepage
        Intent myIntent = getIntent();
        currentCuriosity = myIntent.getStringExtra("org.fslhome.curiosity.curiosities.button_clicked");
        this.MAPFILE = currentCuriosity.toLowerCase() + ".map";

        setTitle(currentCuriosity);

        // Map

        this.mapView.setClickable(true);
        this.mapView.getMapScaleBar().setVisible(true);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.getMapZoomControls().setZoomLevelMin((byte) 12);
        this.mapView.getMapZoomControls().setZoomLevelMax((byte) 20);

        // create a tile cache of suitable size
        this.tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                this.mapView.getModel().frameBufferModel.getOverdrawFactor());

        // ListView info

        this.listView = (ListView) findViewById(R.id.listView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
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

        this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 14);

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

        this.dbAdapter.open_read();
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
            /*
            Log.i("Videl", "Data 0: " + dataString[i][0]);
            Log.i("Videl", "Data 1: " + dataString[i][1]);
            Log.i("Videl", "Data 2: " + dataGPS[i][0]);
            Log.i("Videl", "Data 3: " + dataGPS[i][1]);
            */
            i++;
            cursor.moveToNext();
        }
        this.dbAdapter.close();

        /*
        Log.i("Videl", "Number of rows for this Curiosity: " + cursor.getCount());
        */

        String[] textForListView = new String[i];
        for(int j = 0; j < i; j++) {
            LatLong coordinates = new LatLong(dataGPS[j][0], dataGPS[j][1]);
            if(j == 0)
            {
                this.mapView.getModel().mapViewPosition.setCenter(coordinates);
            }
            TextView bubbleView = new TextView(this);
            Utils.setBackground(bubbleView, getResources().getDrawable(R.drawable.marker_red));
            // The following numbers are only for aesthetic of the Markers
            bubbleView.setText("" + (j + 1)); // This number will be the same as displayed in the
                                              // ListView, below the map
            bubbleView.setGravity(Gravity.CENTER);
            bubbleView.setMaxEms(20);
            bubbleView.setTextSize(15);
            bubbleView.setTextColor(Color.BLACK);
            bubbleView.setPadding(0, 0, 0, 70);
            // Creation of the image with the number inside
            bitmap = Utils.viewToBitmap(this, bubbleView);
            temp = new Marker(coordinates, bitmap, 0, 0);
            this.mapView.getLayerManager().getLayers().add(temp);
            // Creating the String that will be displayed
            textForListView[j] = (j+1) + ": [" + dataString[j][0] + "] " + dataString[j][1];
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        arrayAdapter.addAll(textForListView);
        this.listView.setAdapter(arrayAdapter);

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
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            // read this file into InputStream
            switch(this.MAPFILE)
            {
                case "maynooth.map":
                    inputStream = getResources().openRawResource(R.raw.maynooth);
                    break;
                case "dublin.map":
                    inputStream = getResources().openRawResource(R.raw.dublin);
                    break;
                case "montpellier.map":
                    inputStream = getResources().openRawResource(R.raw.montpellier);
                    break;
                case "versailles.map":
                    inputStream = getResources().openRawResource(R.raw.versailles);
                    break;
            }

            // write the inputStream to a FileOutputStream
            outputStream =
                    new FileOutputStream(new File(Environment.getExternalStorageDirectory(), this.MAPFILE));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            Log.i("Videl_fi", "File creation completed.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        File file = new File(Environment.getExternalStorageDirectory(), this.MAPFILE);
        return file;
    }

}
