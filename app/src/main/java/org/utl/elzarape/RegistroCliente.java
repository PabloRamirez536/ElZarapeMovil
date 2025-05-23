package org.utl.elzarape;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.utl.elzarape.api.ClienteApiService;
import org.utl.elzarape.api.EstadoCiudadService;
import org.utl.elzarape.api.Globals;
import org.utl.elzarape.model.Ciudad;
import org.utl.elzarape.model.Cliente;
import org.utl.elzarape.model.Estado;
import org.utl.elzarape.model.Persona;
import org.utl.elzarape.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroCliente extends AppCompatActivity {

    Button btnRegistrar;

    TextView txtNombre, txtApellidos, txtTelefono, txtUsuario, txtContrasenia;

    Spinner spinnerEstado, spinnerCiudad;
    ClienteApiService apiService;
    EstadoCiudadService serviceCiudadEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_cliente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        spinnerCiudad = findViewById(R.id.spinnerCiudad);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.BASE_URL) // Cambia esto por tu URL base
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ClienteApiService.class);
        serviceCiudadEstado = retrofit.create(EstadoCiudadService.class);

        // Cargar estados al iniciar
        cargarEstados();

        // Asignar listener para cargar ciudades al seleccionar un estado
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Estado estadoSeleccionado = (Estado) parent.getItemAtPosition(position);
                cargarCiudadesPorEstado(estadoSeleccionado.getIdEstado());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        btnRegistrar.setOnClickListener(v -> registrarUsuario());

    }

    private void cargarEstados() {
        serviceCiudadEstado.getAllEstados().enqueue(new Callback<List<Estado>>() {
            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Estado> estados = response.body();
                    ArrayAdapter<Estado> adapter = new ArrayAdapter<>(RegistroCliente.this,
                            android.R.layout.simple_spinner_item, estados);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEstado.setAdapter(adapter);
                } else {
                    Toast.makeText(RegistroCliente.this, "Error al cargar estados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Toast.makeText(RegistroCliente.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarCiudadesPorEstado(int idEstado) {
        serviceCiudadEstado.getCiudadesPorEstado(idEstado).enqueue(new Callback<List<Ciudad>>() {
            @Override
            public void onResponse(Call<List<Ciudad>> call, Response<List<Ciudad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ciudad> ciudades = response.body();
                    ArrayAdapter<Ciudad> adapter = new ArrayAdapter<>(RegistroCliente.this,
                            android.R.layout.simple_spinner_item, ciudades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCiudad.setAdapter(adapter);
                } else {
                    Toast.makeText(RegistroCliente.this, "Error al cargar ciudades", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ciudad>> call, Throwable t) {
                Toast.makeText(RegistroCliente.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText().toString().trim();
        String apellido = txtApellidos.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String usuario = txtUsuario.getText().toString().trim();
        String contrasena = txtContrasenia.getText().toString().trim();
        Estado estado = (Estado) spinnerEstado.getSelectedItem();
        Ciudad ciudad = (Ciudad) spinnerCiudad.getSelectedItem();

        // Input validation
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || estado == null || ciudad == null) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        Persona persona = new Persona(nombre, apellido, telefono);
        Usuario usuario1 = new Usuario(usuario, contrasena);

        // Crear cliente y asignar valores
        Cliente cliente = new Cliente();
        //cliente.setIdCliente();  // Asegúrate de que esto se envíe como null
        cliente.setPersona(persona);
        cliente.setUsuario(usuario1);

        // Crear objetos Ciudad y Estado
        Ciudad ciudad1 = new Ciudad();
        ciudad1.setIdCiudad(ciudad.getIdCiudad()); // Asegúrate de que sea String
        cliente.setCiudad(ciudad1);

        Estado estado1 = new Estado();
        estado1.setIdEstado(estado.getIdEstado()); // Asegúrate de que sea String
        cliente.setEstado(estado1);

        cliente.setActivo(true);  // Asignar el valor de activo

        // Convertir Cliente a JSON
        Gson gson = new Gson();
        String clienteJson = gson.toJson(cliente);

        // Imprimir el JSON que se enviará a la API
        Log.d("RegistrarUsuario", "Cliente JSON: " + clienteJson);

        // Retrofit setup for registration
        Call<JsonObject> call = apiService.registrarCliente(clienteJson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegistroCliente.this, "Usuario registrado exitosamente", Toast.LENGTH_LONG).show();
                    sendToLogin();
                } else {
                    Toast.makeText(RegistroCliente.this, "Error en la respuesta: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegistroCliente.this, "Error al hacer la llamada: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendToLogin() {
        Intent loginView = new Intent(this, Login.class);
        startActivity(loginView);
    }
}