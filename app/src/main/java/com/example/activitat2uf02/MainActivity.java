package com.example.activitat2uf02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import android.text.Editable;
import android.text.TextWatcher;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickListener, ValueEventListener, ChildEventListener{

    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<Vehicle> elements;
    private RecyclerView rvMatriculas;

    private EditText editTextNom;
    private EditText editTextCognom;
    private EditText editTextTelefon;
    private AutoCompleteTextView editTextMatricula;
    private EditText editTextMarcaVehicle;
    private EditText editTextModelVehicle;

    private Vehicle vehicleConsulta;

    private Button buttonAfegir;
    private Button buttonConsultar;
    private Button buttonModificar;
    private ArrayAdapter<String> autoCompleteAdapter;



    private DatabaseReference dbParking ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inicialitzem la base de dades, del child "parking" que és on guardarem els vehicles
        dbParking = FirebaseDatabase.getInstance().getReference().child("parking");
        // Afegim els listeners
        dbParking.addChildEventListener(this);
        dbParking.addValueEventListener(this);

        // Inicialitzem els elements de la vista
        editTextNom = findViewById(R.id.editTextNom);
        editTextCognom = findViewById(R.id.editTextCognom);
        editTextTelefon = findViewById(R.id.editTextTelefon);
        editTextMatricula = findViewById(R.id.editTextMatricula);
        editTextMarcaVehicle = findViewById(R.id.editTextMarcaVehicle);
        editTextModelVehicle = findViewById(R.id.editTextModelVehicle);
        buttonAfegir = findViewById(R.id.buttonAfegir);
        buttonConsultar = findViewById(R.id.buttonConsultar);
        buttonModificar = findViewById(R.id.buttonModificar);

        // Configurar el AutoCompleteTextView con un ArrayAdapter
        autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        editTextMatricula.setAdapter(autoCompleteAdapter);


        // Afegim els listeners als botons
        buttonAfegir.setOnClickListener(this);
        buttonConsultar.setOnClickListener(this);
        buttonModificar.setOnClickListener(this);

        // Inicialitzem el RecyclerView
        rvMatriculas = findViewById(R.id.recyclerView);

        // Inicialitzem l'adaptador
        elements = new ArrayList<Vehicle>();
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, dbParking, elements, this);
        rvMatriculas.setAdapter(myRecyclerViewAdapter);

        // Añadir un TextWatcher para buscar matrículas mientras el usuario escribe
        editTextMatricula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Llamar a la función de búsqueda con la matrícula actual
                searchMatriculas(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }


    @Override
    public void onClick(View v) {

        boolean matriculaCorrecta = false;
        // Comprovem que la matrícula sigui correcta
        if (editTextMatricula.getText().toString().length() == 7) {
            String matricula = editTextMatricula.getText().toString();
            String numeros = matricula.substring(0, 4);
            String lletres = matricula.substring(4, 7);
            if (numeros.matches("[0-9]+") && lletres.matches("[A-Z]+")) {
                matriculaCorrecta = true;
            }
        }


        switch (v.getId()) {
            // Afegim un vehicle
            case R.id.buttonAfegir:

                if (editTextNom.getText().toString().isEmpty() || editTextCognom.getText().toString().isEmpty() || editTextTelefon.getText().toString().isEmpty() || editTextMatricula.getText().toString().isEmpty() || editTextMarcaVehicle.getText().toString().isEmpty() || editTextModelVehicle.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Has d'omplir tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matriculaCorrecta) {
                    Toast.makeText(MainActivity.this, "La matrícula no és correcta", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Creem un nou vehicle amb les dades dels editText
                String nom = editTextNom.getText().toString();
                String cognom = editTextCognom.getText().toString();
                String telefon = editTextTelefon.getText().toString();
                String matricula = editTextMatricula.getText().toString();
                String marca = editTextMarcaVehicle.getText().toString();
                String model = editTextModelVehicle.getText().toString();
                // Fem la consulta a la base de dades
                Vehicle vehicle = new Vehicle(matricula, nom, cognom, telefon, marca, model);
                vehicleConsulta = consultaBD(vehicle, "afegir");


                break;
            case R.id.buttonConsultar:
                if (editTextMatricula.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Has d'omplir el camp matrícula", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matriculaCorrecta) {
                    Toast.makeText(MainActivity.this, "La matrícula no és correcta", Toast.LENGTH_SHORT).show();
                    return;
                }
                String matriculaConsultar = editTextMatricula.getText().toString();
                DatabaseReference dbParkingConsultar = FirebaseDatabase.getInstance().getReference().child("parking").child(matriculaConsultar);
                dbParkingConsultar.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(MainActivity.this, "No existeix cap vehicle amb aquesta matrícula", Toast.LENGTH_SHORT).show();

                        } else {
                            Vehicle vehicle = snapshot.getValue(Vehicle.class);
                            editTextNom.setText(vehicle.getNom());
                            editTextCognom.setText(vehicle.getCognoms());
                            editTextTelefon.setText(vehicle.getTelefon());
                            editTextMarcaVehicle.setText(vehicle.getMarcaVehicle());
                            editTextModelVehicle.setText(vehicle.getModelVehicle());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.buttonModificar:
                if (!matriculaCorrecta) {
                    Toast.makeText(MainActivity.this, "La matrícula no és correcta", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextNom.getText().toString().isEmpty() || editTextCognom.getText().toString().isEmpty() || editTextTelefon.getText().toString().isEmpty() || editTextMatricula.getText().toString().isEmpty() || editTextMarcaVehicle.getText().toString().isEmpty() || editTextModelVehicle.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Has d'omplir tots els camps", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String nomModificar = editTextNom.getText().toString();
                    String cognomModificar = editTextCognom.getText().toString();
                    String telefonModificar = editTextTelefon.getText().toString();
                    String matriculaModificar = editTextMatricula.getText().toString();
                    String marcaModificar = editTextMarcaVehicle.getText().toString();
                    String modelModificar = editTextModelVehicle.getText().toString();

                    Vehicle vehicleModificar = new Vehicle(matriculaModificar, nomModificar, cognomModificar, telefonModificar, marcaModificar, modelModificar);
                    vehicleConsulta = consultaBD(vehicleModificar, "modificar");
                }
                break;
        }

    }

    private void searchMatriculas(String matricula) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("parking");

        Query query = database.orderByChild("matricula").startAt(matricula).endAt(matricula + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                autoCompleteAdapter.clear(); // Limpiar el adaptador antes de añadir nuevas opciones
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                    if (vehicle != null) {
                        autoCompleteAdapter.add(vehicle.getMatricula());
                    }
                }
                // No necesitas notificar cambios en este caso ya que AutoCompleteTextView se encargará automáticamente
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores si es necesario
            }
        });
    }




    /**
     * Mètode per netejar els camps del formulari
     */
    private void resetForm() {
        editTextNom.setText("");
        editTextCognom.setText("");
        editTextTelefon.setText("");
        editTextMatricula.setText("");
        editTextMarcaVehicle.setText("");
        editTextModelVehicle.setText("");
    }

    /**
     * Mètode per consultar a la base de dades, si existeix el vehicle o no
     * Segons el boto que s'hagi clicat, afegir o modificar
     */
    private Vehicle consultaBD(Vehicle vehicle, String boton) {

        vehicleConsulta = null;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query query = database.child("parking").orderByKey().equalTo(vehicle.getMatricula());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //
                if (snapshot.hasChildren()){

                    if (boton.equals("afegir")){
                        Toast.makeText(MainActivity.this, "Ja existeix un vehicle amb aquesta matrícula", Toast.LENGTH_SHORT).show();
                    } else if (boton.equals("modificar")) {
                        dbParking.child(vehicle.getMatricula()).setValue(vehicle);
                        resetForm();
                        Toast.makeText(MainActivity.this, "Vehicle modificat correctament", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    if(boton.equals("afegir")){
                        dbParking.child(vehicle.getMatricula()).setValue(vehicle);
                        resetForm();
                        Toast.makeText(MainActivity.this, "Vehicle afegit correctament", Toast.LENGTH_SHORT).show();
                    } else if (boton.equals("modificar")) {
                        Toast.makeText(MainActivity.this, "Vehicle no trobat per poder modificar", Toast.LENGTH_SHORT).show();
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return vehicleConsulta;

    }

    @Override
    public void onItemClick(Vehicle data) {
        editTextNom.setText(data.getNom().toString());
        editTextCognom.setText(data.getCognoms().toString());
        editTextTelefon.setText(data.getTelefon().toString());
        editTextMatricula.setText(data.getMatricula().toString());
        editTextMarcaVehicle.setText(data.getMarcaVehicle().toString());
        editTextModelVehicle.setText(data.getModelVehicle().toString());
        editTextMatricula.setText(data.getMatricula().toString());

    }

    @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            // Eliminem tot el contingut per no afegir cada cop que hi ha un canvi
            elements.removeAll(elements);
            // Recorrem tots els elements del DataSnapshot i els mostrem
            for (DataSnapshot element : snapshot.getChildren()) {
                Vehicle vehicle = new Vehicle(
                        element.getKey().toString(),
                        element.child("nom").getValue().toString(),
                        element.child("cognoms").getValue().toString(),
                        element.child("telefon").getValue().toString(),
                        element.child("marcaVehicle").getValue().toString(),
                        element.child("modelVehicle").getValue().toString());
                elements.add(vehicle);
            }

            // Per si hi ha canvis, que es refresqui l'adaptador
            myRecyclerViewAdapter.notifyDataSetChanged();
            rvMatriculas.scrollToPosition(elements.size() - 1);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


}