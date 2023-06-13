package br.com.srm.wisedelivery.dto;

import java.util.List;

import br.com.srm.wisedelivery.entidades.restaurante.Restaurante;
import br.com.srm.wisedelivery.entidades.restaurante.RestauranteCategoria;

public interface RestauranteIdDTO {

    RestauranteIdDTO procurarRestauranteIdPeloEmail(String email);

    RestauranteIdDTO procurarRestaurantePeloId(Long id);

    Restaurante procurarPeloEmail(String email);

    Restaurante salvar(RestauranteCategoria dto);

    List<RestauranteCategoria> pegaTodasAsCategorias();

    boolean logar(Restaurante restaurante);

    List<RestauranteCategoria> procurarTodos();

}
