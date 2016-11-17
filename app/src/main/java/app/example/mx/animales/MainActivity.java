package app.example.mx.animales;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

//    public final static String EXTRA_NOMBRE_ANIMAL = "Animales.NOMBRE_ANIMAL";
//    public final static String EXTRA_DESCRIPCION_ANIMAL = "Animales.DESCRIPCION_ANIMAL";

    public final static String EXTRA_ID_ANIMAL = "Animales.ID_ANIMAL";

//    String arregloDeNombres[];
//    String arregloDeDescripciones[];
    private SimpleCursorAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listaDeAnimales = (ListView) findViewById(R.id.activity_main);
        listaDeAnimales.setOnItemClickListener(this);

//        arregloDeNombres = getResources().getStringArray(R.array.nombres_animales);
//        arregloDeDescripciones = getResources().getStringArray(R.array.descripcion_animales);

//        ArrayAdapter adaptador =
//                new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1,
//                arregloDeNombres);

        adaptador = new SimpleCursorAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        null,
                        new String[]{AnimalsDatabase.COL_NAME},
                        new int[]{android.R.id.text1},
                        0);

        listaDeAnimales.setAdapter(adaptador);

        getSupportLoaderManager().initLoader(0,null,this);

        findViewById(R.id.boton_nuevo_animal).setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intento = new Intent(MainActivity.this, AgregarAnimal.class);
                startActivity(intento);

            }
        });



    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        String nombreAnimal = (String) parent.getItemAtPosition(position);
//        String descripcionAnimal = arregloDeDescripciones[position];

        Intent intencion = new Intent(this, DetalleAnimal.class);

        intencion.putExtra(EXTRA_ID_ANIMAL, adaptador.getItemId(position));
        startActivity(intencion);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AnimalsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adaptador.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adaptador.swapCursor(null);
    }
}
