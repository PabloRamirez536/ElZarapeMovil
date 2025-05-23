package org.utl.elzarape.api;

import com.google.gson.JsonObject;

import org.utl.elzarape.model.Ciudad;
import org.utl.elzarape.model.Estado;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClienteApiService {

    @POST("cliente/insertarCliente1")
    @FormUrlEncoded
    Call<JsonObject> registrarCliente(@Field("datosCliente")String datosCliente);

}
