package org.utl.elzarape;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonObject;

import org.utl.elzarape.api.Globals;
import org.utl.elzarape.api.LoginApiService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    Button btnLogin;
    TextView txtRegistrar, txtUser, txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnLogin = findViewById(R.id.btnLogin);
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        txtRegistrar =findViewById(R.id.txtRegistrar);
        btnLogin.setOnClickListener(v -> {
            ValidarLogin(txtUser.getText().toString(), txtPass.getText().toString());
        });
        txtRegistrar.setOnClickListener(v -> {
            SendToRegistrar();
        });
    }

    public void ValidarLogin(String user, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginApiService service = retrofit.create(LoginApiService.class);
        Call<JsonObject> call = service.validarLogin(user, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    String status = responseBody.get("status").getAsString();
                    if ("success".equals(status)) {
                        Toast.makeText(Login.this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                        SendToLogin();
                    } else {
                        String message = responseBody.get("message").getAsString();
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Login.this, "Error al hacer la llamada", Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void SendToLogin() {
        Intent loginView = new Intent(this, MainActivity.class);
        startActivity(loginView);
    }
    private void SendToRegistrar() {
        Intent RegistrarView = new Intent(this, RegistroCliente.class);
        startActivity(RegistrarView);
    }
}