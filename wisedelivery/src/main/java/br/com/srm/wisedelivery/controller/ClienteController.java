package br.com.srm.wisedelivery.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.srm.wisedelivery.dominio.Cliente;
import br.com.srm.wisedelivery.dto.ClienteDTO;
import br.com.srm.wisedelivery.dto.ClienteIdDTO;
import br.com.srm.wisedelivery.dto.ClienteLoginDTO;
import br.com.srm.wisedelivery.dto.EnderecoDTO;
import br.com.srm.wisedelivery.dto.ItemCardapioTabelaDTO;
import br.com.srm.wisedelivery.dto.ItemCarrinhoDTO;
import br.com.srm.wisedelivery.dto.PedidoDTO;
import br.com.srm.wisedelivery.dto.RestauranteIdDTO;
import br.com.srm.wisedelivery.entidades.pagamento.DadosCartao;
import br.com.srm.wisedelivery.exception.RestauranteDiferenteExcpetion;
import br.com.srm.wisedelivery.exception.SenhaInvalidaException;
import br.com.srm.wisedelivery.service.pedidoService;
import br.com.srm.wisedelivery.validator.validator;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Controller
@RequestMapping("clientes")
public class ClienteController<carrinho, validator> {

    @Autowired
    @Getter private Carrinho carrinho;

    @Autowired
    @Getter private RestauranteService restauranteService;

    @Autowired
    @Getter private ClienteService clienteService;

    @Autowired
    @Getter private ItemCardapioService itemCardapioService;

    @Autowired
    @Getter private PedidoService pedidoService;

    @Autowired
    @Getter private Validator<ClienteDTO> validator;


    @GetMapping("form-cadastro")
    private String homeCadastroCliente(Model model){
        model.addAttribute("cliente", new Cliente());
        return "cliente-cadastro";
    }

    @PostMapping("/save")
    public String salvarCliente( @ModelAttribute("cliente") @Valid ClienteDTO cliente, BindingResult result ) {
        if(validator.validator(cliente)){
            log.error("Senha e confirmação não estão batendo", cliente);
            throw new SenhaInvalidaException("Senha e confirmação não estão batendo");
        }
        getClienteService().salvar(cliente);
        log.info(String.format("Cliente salvo. Nome: [%s]", cliente.getNome()));
        return "cliente-cadastro-ok";
    }

    private Object getClienteService() {
        return null;
    }

    @GetMapping("/login")
    public String telaLogin(Model model){
        model.addAttribute("cliente", new ClienteLoginDTO());
        return "login";
    }

    @PostMapping("/logar")
    public String login(@ModelAttribute("cliente") ClienteLoginDTO cliente, Model model){
        if(!getClienteService().login(cliente)) {
            return "login";
        }
        ClienteIdDTO clienteIdDto = getClienteService().procurarCliente(cliente.getEmail());
        model.addAttribute("clienteId", clienteIdDto);
        return home(model);
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("restaurantes", getRestauranteService().procurarTodos());
        return "cliente-home";
    }

    private Object getRestauranteService() {
        return null;
    }

    @GetMapping("restaurantes/{restauranteId}")
    public String clienteRestauranteCardapio(Model model, @PathVariable("restauranteId") Long restauranteId) {
        List<ItemCardapioTabelaDTO> itensCardapioRestaurante = getItemCardapioService().procurarTodosOsItensPeloIdDoRestaurante(restauranteId);
        //List<ItemCardapioTabelaDTO> itensEmDestaque = itensCardapioRestaurante.stream().filter( item -> item.isDestaque()).toList();
        List<ItemCardapioTabelaDTO> itensEmDestaque = getItemCardapioService().procurarTodosOsItensPeloIdDoRestauranteEQueEstejamEmDestaque(restauranteId);
        RestauranteIdDTO restaurante = getRestauranteService().procurarRestaurantePeloId(restauranteId);
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("itensEmDestaque", itensEmDestaque);
        model.addAttribute("itensCardapioRestaurante", itensCardapioRestaurante);
        return "cliente-restaurante-cardapio";
    }

    private Object getItemCardapioService() {
        return null;
    }

    @GetMapping("restaurantes/{restauranteId}/itens/{itemId}")
    public String clienteRestauranteCardapioItem(Model model, @PathVariable("itemId") Long itemId) {
        model.addAttribute("item", getItemCardapioService().procurarPeloId(itemId));
        model.addAttribute("itemCarrinho", new ItemCarrinhoDTO());
        return "cliente-restaurante-itemcardapio";
    }

    @PostMapping("carrinho/add/{itemId}")
    public String adicionaItemAoCarrinho(@ModelAttribute("itemCarrinho")ItemCarrinhoDTO itemCarrinhoDTO, @PathVariable("itemId") Long itemId, Model model) {
        try {
            carrinho.adicionarItem(itemCarrinhoDTO, itemId);
        } catch (RestauranteDiferenteExcpetion e) {
            model.addAttribute("erro", true);
            model.addAttribute("msgErroRestaurante", e.getMessage());
        }

        model.addAttribute("carrinho", carrinho);
        return "cliente-carrinho";
    }

    @GetMapping("carrinho/rem/{itemId}")
    public String removeItemCarrinho(@PathVariable("itemId") Long itemId, Model model) {
        carrinho.removerItemDCarrinho(itemId);
        model.addAttribute("carrinho", carrinho);
        return "cliente-carrinho";
    }

    @GetMapping("tela-finalizar-pedido")
    public String telaFinalizarPedido(Model model){
        PedidoDTO pedido = pedidoService.deCarrinhoParaPedidoDTO(carrinho);
        EnderecoDTO endereco = (EnderecoDTO) model.getAttribute("endereco");
        if(Objects.nonNull(endereco)) {
            pedido.setEndereco(endereco);
        }
        model.addAttribute("pedido", pedido);
        return "cliente-finalizar-pedido";
    }

    @GetMapping("pedido/inserir-endereco")
    public String telaInserirEndereco(Model model){
        model.addAttribute("endereco", new EnderecoDTO());
        return "tela-inserirEndereco";
    }

    @PostMapping("endereco/inserir")
    public String inserirEndereco(@ModelAttribute("endereco") EnderecoDTO endereco, Model model) {
        model.addAttribute("endereco", endereco);
        return telaFinalizarPedido(model);
    }

    @GetMapping("pedido/pagamento")
    public String telaRealizarPagamento(Model model){
        model.addAttribute("dadosCartao", new DadosCartao());
        return "tela-inserirDadosCartao";
    }

}
