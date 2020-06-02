package com.example.crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.crud.model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//PROBANDO COMMIT
//----------------------------------------INICIO----------------------------------------------------
public class MainActivity extends AppCompatActivity {

    //**********************************************************************************************
    //DEFINO LOS ATRIBUTOS QUE VAN A INGRESAR POR EL ACTIVITY
    EditText nomP, appP,correoP,passwordP;
    ListView listV_personas;
    //**********************************************************************************************
    //DEFINO LA VARIABLES QUE VAN A SER UTILIZADAS PARA INICIALIZAR LA BASE DE DATOS
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //**********************************************************************************************
    //GENERO UNA LISTA DE PERSONAS Y UN ADAPTER DE TIPO PERSONA
    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;
    //**********************************************************************************************
    //AL CREAR EL ACTIVITY SE REALIZAN LAS SIGUIENTES ACCIONES:
    //-SE RECIBEN LOS DATOS INGRESADOS POR EL USUARIO EN LOS ATRIBUTOS ANTES DEFINIDOS
    //-SE LLAMA AL METODO inicializarFirebase(): PARA INICIALIZAR LA BASE DE DATOS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomP = findViewById(R.id.txt_nombrePersona);
        appP = findViewById(R.id.txt_appPersona);
        correoP = findViewById(R.id.txt_correoPersona);
        passwordP = findViewById(R.id.txt_passwordPersona);

        listV_personas = findViewById(R.id.lv_datosPersonas);
        inicializarFirebase();
        listarDatos();

    }

    //**********************************************************************************************
    //LISTA LOS DATOS
    private void listarDatos() {
        //REALIZA LA LECTURA SOBRE EL CHILD PERSONA
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //INICIALIZA LA LISTA
                listPerson.clear();
                //AGREGA A TODAS LAS PERSONAS A LA LISTA
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);
                    listPerson.add(p);

                    //AGREGO LA LISTA AL ADAPTADOR
                    arrayAdapterPersona = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listPerson);
                    //MUESTRO LA LISTA
                    listV_personas.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //**********************************************************************************************
    //SE INCLUYE LA VISTA DEL MENU AL INICIAR LA APLICACION
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_name,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //**********************************************************************************************
    //SE EVALUA QUE REALIZAR CUANDO ES ELEGIDO UNO DE LOS ELEMENTOS DEL MENU
    public boolean onOptionsItemSelected(MenuItem item) {

        String nombre = nomP.getText().toString();
        String correo = correoP.getText().toString();
        String password = passwordP.getText().toString();
        String app = appP.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if (nombre.equals("")||correo.equals("")||password.equals("")||app.equals("")){
                    validacion();
                }
                else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(app);
                    p.setCorreo(correo);
                    p.setPassword(password);
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save:{
                Toast.makeText(this,"Actualizado", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_delete:{

                Toast.makeText(this,"Eliminado", Toast.LENGTH_LONG).show();
                break;
            }
            default:break;
        }
        return true;
    }

    //**********************************************************************************************
    //LIMPA TODA LA PANTALLA UNA VEZ REALIZADA UNA ACCION
    private void limpiarCajas() {
        nomP.setText("");
        correoP.setText("");
        passwordP.setText("");
        appP.setText("");
    }

    //**********************************************************************************************
    //SI AL AGREGAR EL REGISTRO UNO DE LOS CAMPOS SE ENCUENTRA VACIO LANZA UN ERROR
    private void validacion() {
        String nombre = nomP.getText().toString();
        String correo = correoP.getText().toString();
        String password = passwordP.getText().toString();
        String app = appP.getText().toString();
        if (nombre.equals("")){
            nomP.setError("Required");
        }
        else if (app.equals("")){
            appP.setError("Required");
        }
        else if (correo.equals("")){
            correoP.setError("Required");
        }
        else if (password.equals("")){
            passwordP.setError("Required");
        }
    }

    //**********************************************************************************************
    //REALIZA LA CONEXION CON LA BASE DE DATOS
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

}
//----------------------------------------FIN----------------------------------------------------