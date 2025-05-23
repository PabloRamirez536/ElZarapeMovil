package org.utl.elzarape.api;

import com.google.gson.JsonObject;

import org.utl.elzarape.model.Ciudad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductoApiService {
    @GET("alimento/getProducto")
    Call<JsonObject> getProductosPorID(@Query("id") int idProducto);
}
