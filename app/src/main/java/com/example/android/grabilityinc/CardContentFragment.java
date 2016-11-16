package com.example.android.grabilityinc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import datajson.AsyncDataJson;
import factory.FactoryAsyncDataJason;


public class CardContentFragment extends Fragment {

    private ProgressDialog pDialog;
    public static AsyncDataJson asyncDataJson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        asyncDataJson = FactoryAsyncDataJason.getInstanceAsyncDataJson();

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;
        public TextView description;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("nameApp", "");
                    context.startActivity(intent);
                }
            });

            ImageButton favoriteImageButton = (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    MainActivity.Deseos.addWish(asyncDataJson.getNamesApps()[getAdapterPosition()],
                            asyncDataJson.getDescrApps()[getAdapterPosition()],
                            asyncDataJson.getImgAppsBig(getAdapterPosition()));
                    Snackbar.make(v, "Add to Wish List",
                            Snackbar.LENGTH_LONG).show();
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "shared application",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        // Tamaño de la lista en el recyclerView
        private static final int LENGTH = 5;
        private final String[] namesApps;
        private final String[] descrApps;
        private final Drawable[] imagesApps;

        public ContentAdapter(Context context) {

            asyncDataJson = FactoryAsyncDataJason.getInstanceAsyncDataJson();

            Resources resources = context.getResources();

            //se toman los arrays que contienen la data de las aplicaciones y solo se toma el top5
            namesApps = Arrays.copyOfRange(asyncDataJson.getNamesApps(),0 , LENGTH);
            descrApps = Arrays.copyOfRange(asyncDataJson.getDescrApps(),0 , LENGTH);
            imagesApps = Arrays.copyOfRange(asyncDataJson.getImgAppsBig(),0 , LENGTH);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.picture.setImageDrawable(imagesApps[position % imagesApps.length]);
            holder.name.setText(namesApps[position % namesApps.length]);
            holder.description.setText(descrApps[position % descrApps.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
