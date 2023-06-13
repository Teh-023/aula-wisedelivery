package br.com.srm.wisedelivery.controller;


RestauranteIdDTO procurarRestauranteIdPeloEmail(String email);

    RestauranteIdDTO procurarRestaurantePeloId(Long id);

    RestauranteSalvoDTO procurarPeloEmail(String email);

    RestauranteSalvoDTO salvar(RestauranteDTO dto);

    List<RestauranteCategoria> pegaTodasAsCategorias();

    boolean logar(RestauranteLoginDTO restaurante);

    List<RestauranteDTO> procurarTodos();
    
public interface RestauranteService {

    RestauranteIdDTO procurarRestauranteIdPeloEmail(String email);

    RestauranteIdDTO procurarRestaurantePeloId(Long id);

    RestauranteSalvoDTO procurarPeloEmail(String email);

    RestauranteSalvoDTO salvar(RestauranteDTO dto);

    List<RestauranteCategoria> pegaTodasAsCategorias();

    boolean logar(RestauranteLoginDTO restaurante);

    List<RestauranteDTO> procurarTodos();

}
