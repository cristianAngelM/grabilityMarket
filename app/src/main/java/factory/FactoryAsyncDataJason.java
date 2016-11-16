package factory;

import android.util.Log;

import datajson.AsyncDataJson;

/**
 * Created by CRISTIAN ANGEL on 9/11/2016.
 */

public class FactoryAsyncDataJason {

    private static AsyncDataJson asyncDataJson;


    static
    {
        try
        {
            asyncDataJson = new AsyncDataJson();
        }
        catch (Throwable ex) {
            Log.e("Error: ", ex.toString());
        }
    }

    public static AsyncDataJson getInstanceAsyncDataJson()
    {
        if(asyncDataJson == null)
            return new AsyncDataJson();
        else
            return asyncDataJson;
    }


}
