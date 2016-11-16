package com.example.android.grabilityinc;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import datajson.AsyncDataJson;
import factory.FactoryAsyncDataJason;

/**
 * Intefaz para el detalle de las aplicacion con Collapsing Toolbar.
 */
public class DetailActivity extends AppCompatActivity {

    //public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // se capturan los objetos de la interfaz
        TextView detalle = (TextView) findViewById(R.id.place_detail);
        TextView compañia =  (TextView) findViewById(R.id.place_location);
        ImageView imagen = (ImageView) findViewById(R.id.image);


        // se crear un objeto de la clase AsyncDataJson
        AsyncDataJson asyncDataJson = FactoryAsyncDataJason.getInstanceAsyncDataJson();

        // collapsing toolbar
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("position", 0);
        String nameApp = extras.getString("nameApp");

        if(!nameApp.equals("") && position == -1) {
            for (int a = 0; a < asyncDataJson.getNamesApps().length; a++) {
                if (asyncDataJson.getNamesApps()[a].equals(nameApp)) {
                    // titulo del bar
                    collapsingToolbar.setTitle(asyncDataJson.getNamesApps()[a % asyncDataJson.getNamesApps().length]);
                    //collapsingToolbar.setBackgroundColor(9);

                    // detalle de la aplicacion
                    detalle.setText(asyncDataJson.getDescrApps()[a % asyncDataJson.getDescrApps().length]);

                    // compañia creadora
                    compañia.setText(asyncDataJson.getCompaniesApps()[a % asyncDataJson.getCompaniesApps().length]);

                    // imagen de la aplicacion
                    imagen.setImageDrawable(asyncDataJson.getImgAppsBig()[a % asyncDataJson.getImgAppsBig().length]);
                }
            }
        }else
        {
            // titulo del bar
            collapsingToolbar.setTitle(asyncDataJson.getNamesApps()[position % asyncDataJson.getNamesApps().length]);

            // detalle de la aplicacion
            detalle.setText(asyncDataJson.getDescrApps()[position % asyncDataJson.getDescrApps().length]);

            // compañia creadora
            compañia.setText(asyncDataJson.getCompaniesApps()[position % asyncDataJson.getCompaniesApps().length]);

            // imagen de la aplicacion
            imagen.setImageDrawable(asyncDataJson.getImgAppsBig()[position % asyncDataJson.getImgAppsBig().length]);
        }
    }
}
