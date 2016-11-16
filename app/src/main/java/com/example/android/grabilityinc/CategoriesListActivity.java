package com.example.android.grabilityinc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import datajson.AsyncDataJson;
import factory.FactoryAsyncDataJason;

/**
 * Intefaz para el detalle de las aplicacion con Collapsing Toolbar.
 */
public class CategoriesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int position = getIntent().getIntExtra("position", 0);

        recyclerView = (RecyclerView) findViewById(R.id.categories_list_recycler_view);

        ContentAdapter adapter;

        // se toma la ubicacion de la categoria seleccionada y se envia al contructor de la clase ContentAdapter
        if (position == -2) {
            if(MainActivity.Deseos.getNamesApps() == null)
                finish();
            else if(MainActivity.Deseos.getNamesApps().size() == 0)
                finish();

            Resources resources = this.getResources();
            adapter = new ContentAdapter(recyclerView.getContext(),"", position);
        }
        else {
            Resources resources = this.getResources();
            adapter = new ContentAdapter(recyclerView.getContext(), resources.getStringArray(R.array.categories)[position], position);
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

        private AsyncDataJson asyncDataJson;

        // Tamaño de la lista en el recyclerView
        private static int LENGTH;
        private String[] namesApps;
        private String[] descrApps;
        private Drawable[] imagesApps;


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageApp;
            public TextView nameApp;
            public TextView descriptionApp;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.item_list, parent, false));
                imageApp = (ImageView) itemView.findViewById(R.id.list_avatar);
                nameApp = (TextView) itemView.findViewById(R.id.list_title);
                descriptionApp = (TextView) itemView.findViewById(R.id.list_desc);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("position", -1);
                        intent.putExtra("nameApp", nameApp.getText());
                        context.startActivity(intent);
                    }
                });
            }
        }


        public ContentAdapter(Context context, String categoria, int flag)
        {
            // ingresa cuando la peticion viene del menu de deseos
            if(flag == -2)
            {
                if(!(MainActivity.Deseos.getNamesApps() ==  null))
                {
                    // se inicializan los array
                    namesApps = new String[MainActivity.Deseos.getNamesApps().size()];
                    descrApps = new String[MainActivity.Deseos.getDescrApps().size()];
                    imagesApps = new Drawable[MainActivity.Deseos.getImgAppsBig().size()];

                    for(int a=0; a<MainActivity.Deseos.getNamesApps().size(); a++)
                    {
                        namesApps[a] = MainActivity.Deseos.getNamesApps().get(a);
                        descrApps[a] = MainActivity.Deseos.getDescrApps().get(a);
                        imagesApps[a] = MainActivity.Deseos.getImgAppsBig().get(a);
                    }

                    LENGTH = namesApps.length;
                }

            }
            else
            {
                asyncDataJson = FactoryAsyncDataJason.getInstanceAsyncDataJson();

                Resources resources = context.getResources();

                // se filtran todas las aplicaciones que correspondan a la categoria
                ArrayList idApps = new ArrayList();

                for(int a=0; a < asyncDataJson.getCategoriesApps().length; a++)
                {
                    if(categoria.equals(asyncDataJson.getCategoriesApps()[a]))
                    {
                        idApps.add(a);
                    }
                }

                // se inicializan los array
                namesApps = new String[idApps.size()];
                descrApps = new String[idApps.size()];
                imagesApps = new Drawable[idApps.size()];

                // se obtiene la informacion de las aplicaciones que pertenecen a la categoria
                for(int b=0; b < idApps.size(); b++)
                {
                    //se toman los arrays que contienen la data de las aplicaciones
                    namesApps[b] = asyncDataJson.getNamesApps()[(Integer)idApps.get(b)];
                    descrApps[b] = asyncDataJson.getDescrApps()[(Integer)idApps.get(b)];
                    imagesApps[b] = asyncDataJson.getImgAppsBig()[(Integer)idApps.get(b)];
                }

                LENGTH = namesApps.length;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.imageApp.setImageDrawable(imagesApps[position % imagesApps.length]);
            holder.nameApp.setText(namesApps[position % namesApps.length]);
            holder.descriptionApp.setText(descrApps[position % descrApps.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
