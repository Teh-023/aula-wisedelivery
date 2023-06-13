package br.com.srm.wisedelivery.controller;

import java.util.List;

import br.com.srm.wisedelivery.dominio.dto.Restaurantedto.Carrinho;
import br.com.srm.wisedelivery.dto.PedidoDTO;

public interface PedidoService {
    PedidoDTO buscarPedidoPeloId(Long id);

    List<PedidoDTO> buscarTodosOsPedidosPorRestauranteId(Long restauranteId);

    List<PedidoDTO> buscarTodosOsPedidosPorClienteId(Long clienteId);

    PedidoDTO deCarrinhoParaPedidoDTO(Carrinho carrinho);

    PedidoDTO deCarrinhoParaPedidoFecharDto(Carrinho carrinho);

    PedidoDTO fecharPedido(PedidoDTO pedido);

    PedidoDTO salvar(Carrinho carrinho);
}