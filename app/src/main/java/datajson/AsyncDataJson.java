package datajson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import http.HttpHandler;

/**
 * Created by CRISTIAN ANGEL on 9/11/2016.
 */

public class AsyncDataJson {

    // array para almacenar los nombres de las aplicaciones
    private static String[] namesApps;

    // array para almacenar las descripciones de las aplicaciones
    private static String[] descrApps;

    // array para almacenar las compañias creadoras de las aplicaciones
    private static String[] companiesApps;

    // array para almacenar los path de cada aplicacion
    private static String[] pathImageApps;

    // array para almacenar el array anterior
    private static ArrayList<String[]> imagesApps;

    // array para almacenar las imagenes
    private static Drawable[] imgAppsBig;

    // array para almacenar las categorias
    private static String[] categoriesApps;

    // direccion del json
    private static String url = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";


    public AsyncDataJson()
    {
        executeTask();
    }


    private void executeTask()
    {
        new asyncJson().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    /**
     * Clase AsyncTask para realizar peticion http para obtener Json
     */
    private class asyncJson extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject feed = jsonObj.getJSONObject("feed");

                    JSONArray applications = feed.getJSONArray("entry");

                    namesApps = new String[applications.length()];
                    descrApps = new String[applications.length()];
                    imagesApps = new ArrayList<>();
                    imgAppsBig = new Drawable[applications.length()];
                    companiesApps = new String[applications.length()];
                    categoriesApps = new String[applications.length()];

                    // recorrido de todas la aplicaciones
                    for (int i = 0; i < applications.length(); i++) {
                        JSONObject c = applications.getJSONObject(i);

                        // obtener nombre de la aplicacion
                        JSONObject name = c.getJSONObject("im:name");
                        namesApps[i] = name.getString("label");


                        // obtener imagen de la aplicacion
                        JSONArray image = c.getJSONArray("im:image");

                        // se inicializar el array de paths
                        pathImageApps = new String[image.length()];

                        // path de la imagen grande
                        JSONObject imageApp = image.getJSONObject(2);
                        pathImageApps[0] = imageApp.getString("label");

                        // se guarda la imagen de cada aplicacion dentro de un nodo del arrayList
                        imagesApps.add(pathImageApps);


                        // obtener descripcion de la aplicacion
                        JSONObject desc = c.getJSONObject("summary");
                        descrApps[i] = desc.getString("label");


                        // obtener compañia creadora de la aplicacion
                        JSONObject comp = c.getJSONObject("rights");
                        companiesApps[i] = comp.getString("label");;


                        // obtener categoria de la app
                        JSONObject cat = c.getJSONObject("category");
                        JSONObject attr = cat.getJSONObject("attributes");
                        categoriesApps[i] = attr.getString("label");

                    }
                } catch (final JSONException e) {
                    Log.e("Json parsing error: ", e.getMessage());
                }
            } else {
                Log.e("Error", "Couldn't get json from server.");
            }

            if (imagesApps != null) {

                for(int var = 0; var < imagesApps.size(); var++){

                    URL url = null;
                    Bitmap bmp = null;

                    try {
                        // proceso para descargar las imagenes grandes
                        url = new URL(imagesApps.get(var)[0]);
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        bmp.setDensity(Bitmap.DENSITY_NONE);
                        imgAppsBig[var] = new BitmapDrawable(bmp);

                    } catch (Exception e) {
                        Log.e("error: ", e.toString());
                    }
                }
            } else {
                Log.e("Error", "no existen path de imagenes para consultar");
            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {

            super.onPostExecute(result);
        }
    }


    public String[] getPathImageApps() {
        return pathImageApps;
    }

    public void setPathImageApps(String[] pathImageApps) {
        this.pathImageApps = pathImageApps;
    }

    public String[] getDescrApps() {
        return descrApps;
    }

    public void setDescrApps(String[] descrApps) {
        this.descrApps = descrApps;
    }

    public String[] getNamesApps(){
        return namesApps;
    }

    public void setNamesApps(String[] namesApps) {
        this.namesApps = namesApps;
    }

    public static ArrayList<String[]> getImagesApps() {
        return imagesApps;
    }

    public static void setImagesApps(ArrayList<String[]> imagesApps) {
        AsyncDataJson.imagesApps = imagesApps;
    }

    public static Drawable[] getImgAppsBig() {
        return imgAppsBig;
    }

    public static void setImgAppsBig(Drawable[] imgAppsBig) {
        AsyncDataJson.imgAppsBig = imgAppsBig;
    }

    public static String[] getCompaniesApps() {
        return companiesApps;
    }

    public static void setCompaniesApps(String[] companiesApps) {
        AsyncDataJson.companiesApps = companiesApps;
    }

    public Drawable getImgAppsBig(int position){
        if(imgAppsBig.length>0)
            return imgAppsBig[position];
        return null;
    }

    public static String[] getCategoriesApps() {
        return categoriesApps;
    }

    public static void setCategoriesApps(String[] categoriesApps) {
        AsyncDataJson.categoriesApps = categoriesApps;
    }

    /*public static Drawable[] getImgAppsMedium() {

        return imgAppsMedium;
    }

    public static void setImgAppsMedium(Drawable[] imgAppsMedium) {
        AsyncDataJson.imgAppsMedium = imgAppsMedium;
    }

    public static Drawable[] getImgAppsLittle() {

        return imgAppsLittle;
    }

    public static void setImgAppsLittle(Drawable[] imgAppsLittle) {
        AsyncDataJson.imgAppsLittle = imgAppsLittle;
    }*/
}




