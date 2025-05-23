package org.utl.elzarape;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import androidx.activity.result.ActivityResultLauncher;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.utl.elzarape.api.ClienteApiService;
import org.utl.elzarape.api.Globals;
import org.utl.elzarape.api.ProductoApiService;
import org.utl.elzarape.databinding.ActivityMainBinding;
import org.utl.elzarape.model.Ciudad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView txtCodigo, txtNombreProducto, txtDescripcionProducto, txtPrecio, txtNombreCategoria;
    ImageView imgProducto;

    FloatingActionButton btnScan;

    ProductoApiService apiServiceProducto;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.btnScan).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtDescripcionProducto = findViewById(R.id.txtDescripcionProducto);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtNombreCategoria = findViewById(R.id.txtNombreCategoria);
        imgProducto = findViewById(R.id.imgProducto);
        btnScan = findViewById(R.id.btnScan);

        btnScan.setOnClickListener(v ->{
            ScanBarCode();
        });

        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.BASE_URL) // Cambia esto por tu URL base
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiServiceProducto = retrofit.create(ProductoApiService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void ScanBarCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Escanea un código de barras");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        barcodeLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    txtCodigo.setText(result.getContents());
                    int idProducto = Integer.parseInt(result.getContents()); // Asume que el código de barras es el ID del producto
                    getProductosPorID(idProducto);
                }
            }
    );

    private void getProductosPorID(int idProducto) {
        apiServiceProducto.getProductosPorID(idProducto).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    if (jsonResponse.has("alimento")) {
                        JsonObject alimento = jsonResponse.getAsJsonObject("alimento");
                        JsonObject producto = alimento.getAsJsonObject("producto");
                        JsonObject categoria = alimento.getAsJsonObject("categoria");

                        // Extraer los datos
                        String nombreProducto = producto.get("nombre").getAsString();
                        String descripcionProducto = producto.get("descripcion").getAsString();
                        double precio = producto.get("precio").getAsDouble();
                        String nombreCategoria = categoria.get("descripcion").getAsString();

                        // Asignar los datos a los componentes
                        txtNombreProducto.setText(nombreProducto);
                        txtDescripcionProducto.setText(descripcionProducto);
                        txtPrecio.setText(String.format("$%.2f", precio));
                        txtNombreCategoria.setText(nombreCategoria);

                        // Extraer la cadena Base64 de la imagen
                        String fotoBase64 = producto.get("foto").getAsString();

                        // Eliminar el prefijo "data:image/jpeg;base64,"
                        if (fotoBase64 != null && fotoBase64.startsWith("data:image")) {
                            fotoBase64 = fotoBase64.split(",")[1]; // Obtener solo la parte Base64
                        }

                        // Decodificar y mostrar la imagen
                        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                            byte[] decodedString = Base64.decode(fotoBase64, Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgProducto.setImageBitmap(decodedBitmap);
                        }

                        // Mostrar un Toast de éxito
                        Toast.makeText(MainActivity.this, "Producto encontrado: " + nombreProducto, Toast.LENGTH_SHORT).show();
                    } else if (jsonResponse.has("bebida")) {
                        // Manejar el caso de bebida de manera similar
                        JsonObject bebida = jsonResponse.getAsJsonObject("bebida");
                        JsonObject producto = bebida.getAsJsonObject("producto");
                        JsonObject categoria = bebida.getAsJsonObject("categoria");

                        //Extraer los datos
                        String nombreProducto = producto.get("nombre").getAsString();
                        String descripcionProducto = producto.get("descripcion").getAsString();
                        double precio = producto.get("precio").getAsDouble();
                        String nombreCategoria = categoria.get("descripcion").getAsString();

                        //Asignar los datos a los componentes
                        txtNombreProducto.setText(nombreProducto);
                        txtDescripcionProducto.setText(descripcionProducto);
                        txtPrecio.setText(String.format("$%.2f", precio));
                        txtNombreCategoria.setText(nombreCategoria);

                        //Extraer la cadena Base64 de la imagen
                        String fotoBase64 = producto.get("foto").getAsString();

                        //Eliminar el prefijo "data:image/jpeg;base64,"
                        if (fotoBase64 != null && fotoBase64.startsWith("data:image")) {
                            fotoBase64 = fotoBase64.split(",")[1]; //Obtener solo la parte Base64
                        }

                        //Decodificar y mostrar la imagen
                        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                            byte[] decodedString = Base64.decode(fotoBase64, Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgProducto.setImageBitmap(decodedBitmap);
                        }

                        //Mostrar un Toast de éxito
                        Toast.makeText(MainActivity.this, "Producto encontrado: " + nombreProducto, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error al obtener el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_FAILURE", "Error en la llamada: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
