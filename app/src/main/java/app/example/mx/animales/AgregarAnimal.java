package app.example.mx.animales;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class AgregarAnimal extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private long animalId;
    private EditText animalEditText;
    private EditText descripcionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_animal);

        animalEditText = (EditText) findViewById(R.id.nombre_edit_text);
        descripcionEditText = (EditText) findViewById(R.id.descripcion_edit_text);

        animalId = getIntent().getLongExtra(DetalleAnimal.EXTRA_ANIMAL_ID, -1L);

        if (animalId != -1L){
            getSupportLoaderManager().initLoader(0, null, this);
        }

        findViewById(R.id.boton_agregar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String nombreAnimal = animalEditText.getText().toString();
        String descripcionAnimal = descripcionEditText.getText().toString();

        new CreateAnimalTask(this, nombreAnimal, descripcionAnimal, animalId).execute();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AnimalLoader(this, animalId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data != null && data.moveToFirst()){

            int nameIndex = data.getColumnIndexOrThrow(AnimalsDatabase.COL_NAME);
            String nombreAnimal = data.getString(nameIndex);

            int descriptionIndex = data.getColumnIndexOrThrow(AnimalsDatabase.COL_DESCRIPTION);
            String descripcionAnimal = data.getString(descriptionIndex);

            animalEditText.setText(nombreAnimal);
            descripcionEditText.setText(descripcionAnimal);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public static class CreateAnimalTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<Activity> weakActivity;
        private String animalName;
        private String animalDesc;
        private long animalId;


       /* public CreateAnimalTask(Activity activity, String name, String desc){
            weakActivity = new WeakReference<Activity>(activity);
            animalName = name;
            animalDesc = desc;
            animalId = -1L;
        }*/

        public CreateAnimalTask(Activity activity, String name, String desc, long animalId){
            weakActivity = new WeakReference<Activity>(activity);
            animalName = name;
            animalDesc = desc;
            this.animalId = animalId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Context context = weakActivity.get();
            if (context == null){
                return false;
            }

            Context appContext = context.getApplicationContext();

            Boolean success = false;

            if (animalId != -1L){
                int filasAfectadas = AnimalsDatabase.actualizaAnimal(appContext, animalName, animalDesc, animalId);
                success = (filasAfectadas != 0);
            } else {
                long id = AnimalsDatabase.insertaAnimal(appContext, animalName, animalDesc);
                success = (id != -1L);
            }
            return  success;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Activity context = weakActivity.get();
            if(context == null){
                return;
            }
            if (aBoolean){
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
            }
            context.finish();
        }
    }
}
