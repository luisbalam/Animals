package app.example.mx.animales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class DetalleAnimal extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView nombreTextView;
    private TextView descripcionTextView;
    private long animalId;
    public static final String EXTRA_ANIMAL_ID = "animal.id.extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_animal);

        nombreTextView = (TextView) findViewById(R.id.nombre_text_view);
        descripcionTextView = (TextView) findViewById(R.id.descripcion_text_view);

        Intent intencion = getIntent();
        animalId = intencion.getLongExtra(MainActivity.EXTRA_ID_ANIMAL, -1L);

        getSupportLoaderManager().initLoader(0, null, this);

        findViewById(R.id.boton_eliminar).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new DeleteAnimalTask(DetalleAnimal.this, animalId).execute();
                    }
                }
        );

        findViewById(R.id.boton_actualizar).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(DetalleAnimal.this, AgregarAnimal.class);
                        intent.putExtra(EXTRA_ANIMAL_ID, animalId);
                        startActivity(intent);

                    }
                }
        );

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

            nombreTextView.setText(nombreAnimal);
            descripcionTextView.setText(descripcionAnimal);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static class DeleteAnimalTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<Activity> weakActivity;
        private long animalId;

        public DeleteAnimalTask(Activity activity, long id){
            weakActivity = new WeakReference<Activity>(activity);
            animalId = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Context context = weakActivity.get();
            if (context == null){
                return false;
            }

            Context appContext = context.getApplicationContext();

            int filasAfectadas = AnimalsDatabase.eliminaConId(appContext, animalId);
            return  (filasAfectadas != 0);
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
