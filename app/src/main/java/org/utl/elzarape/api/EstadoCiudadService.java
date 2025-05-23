package org.utl.elzarape.api;

import org.utl.elzarape.model.Ciudad;
import org.utl.elzarape.model.Estado;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EstadoCiudadService {

    @GET("estado/getAllEstados")
    Call<List<Estado>> getAllEstados();

    @GET("ciudad/getCiudadesPorEstado")
    Call<List<Ciudad>> getCiudadesPorEstado(@Query("idEstado") int idEstado);
}
