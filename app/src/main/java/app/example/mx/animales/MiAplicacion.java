package app.example.mx.animales;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by richux on 05/11/16.
 */

public class MiAplicacion extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
