package com.example.android.grabilityinc;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import datajson.AsyncDataJson;
import factory.FactoryAsyncDataJason;


public class TileContentFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // padding
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView name;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_tile, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CategoriesListActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("nameApp", "");
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private AsyncDataJson asyncDataJson;

        // Tamaño de la lista en el recyclerView
        private static int LENGTH;

        private final String[] categorias;
        private final Drawable[] imagenes;

        ArrayList listaComp;

        public ContentAdapter(Context context) {

            asyncDataJson = FactoryAsyncDataJason.getInstanceAsyncDataJson();

            Resources resources = context.getResources();
            categorias = resources.getStringArray(R.array.categories);
            imagenes = new Drawable[categorias.length];

            listaComp = new ArrayList();

            LENGTH = categorias.length;

            if(asyncDataJson.getNamesApps().length > 0)
            {
                for(int a=0; a<categorias.length; a++)
                {
                    for(int b=0; b<asyncDataJson.getCategoriesApps().length; b++)
                    {
                        if( categorias[a].equals(asyncDataJson.getCategoriesApps()[b]))
                        {
                            // se obtiene la primera imagen que corresponda a la categoria
                            imagenes[a] = asyncDataJson.getImgAppsBig(b);
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.picture.setImageDrawable(imagenes[position % imagenes.length]);
            holder.name.setText(categorias[position % categorias.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}