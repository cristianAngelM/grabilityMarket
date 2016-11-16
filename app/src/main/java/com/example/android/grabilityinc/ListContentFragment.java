package com.example.android.grabilityinc;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import datajson.AsyncDataJson;
import factory.FactoryAsyncDataJason;


public class ListContentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
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
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private AsyncDataJson asyncDataJson;

        // Tamaño de la lista en el recyclerView
        private static int LENGTH;
        private final String[] namesApps;
        private final String[] descrApps;
        private final Drawable[] imagesApps;

        public ContentAdapter(Context context) {

            asyncDataJson = FactoryAsyncDataJason.getInstanceAsyncDataJson();

            Resources resources = context.getResources();

            //se toman los arrays que contienen la data de las aplicaciones
            namesApps = asyncDataJson.getNamesApps();
            descrApps = asyncDataJson.getDescrApps();
            imagesApps = asyncDataJson.getImgAppsBig();

            LENGTH = namesApps.length;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.avator.setImageDrawable(imagesApps[position % imagesApps.length]);
            holder.name.setText(namesApps[position % namesApps.length]);
            holder.description.setText(descrApps[position % descrApps.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
