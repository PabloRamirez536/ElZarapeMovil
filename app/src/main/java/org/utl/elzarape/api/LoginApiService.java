package org.utl.elzarape.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApiService {

    @POST("usuario/loginCliente1")
    @FormUrlEncoded
    Call<JsonObject>validarLogin(@Field("usuario") String usuario,
                                 @Field("contrasenia") String contrasenia);
}
