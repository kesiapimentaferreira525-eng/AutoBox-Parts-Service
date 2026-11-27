package AutoBox_PartsService.infrastructure.seed;

import AutoBox_PartsService.domain.entity.Cliente;
import AutoBox_PartsService.domain.entity.Peca;
import AutoBox_PartsService.domain.entity.Usuario;
import AutoBox_PartsService.domain.entity.Veiculo;
import AutoBox_PartsService.domain.enums.PerfilUsuario;
import AutoBox_PartsService.infrastructure.repository.ClienteRepository;
import AutoBox_PartsService.infrastructure.repository.PecaRepository;
import AutoBox_PartsService.infrastructure.repository.UsuarioRepository;
import AutoBox_PartsService.infrastructure.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DadosFicticiosConfig {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final PecaRepository pecaRepository;

    @Bean
    CommandLineRunner carregarDadosFicticios() {
        return args -> {
            if (usuarioRepository.findByEmail("admin@autobox.com").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setNome("Administrador");
                usuario.setEmail("admin@autobox.com");
                usuario.setSenha("123456");
                usuario.setPerfil(PerfilUsuario.ADMIN);
                usuario.setAtivo(true);
                usuarioRepository.save(usuario);
            }

            criarCliente("Carlos Silva", "(11) 98888-1001", "carlos@email.com", "11111111111");
            criarCliente("Marina Souza", "(11) 97777-2002", "marina@email.com", "22222222222");
            criarCliente("Oficina Central LTDA", "(11) 96666-3003", "contato@oficinacentral.com", "33333333000133");

            criarVeiculo("ABC1D23", "Toyota", "Corolla", 2022, 28500, "11111111111");
            criarVeiculo("EFG2H34", "Honda", "Civic", 2021, 42000, "22222222222");
            criarVeiculo("XYZ9K88", "Volkswagen", "Gol", 2019, 78000, "33333333000133");

            criarPeca("SKU-OLEO001", "Óleo Motor 5W30", "Castrol", "Motor a gasolina", "59.90", 25);
            criarPeca("SKU-FILT001", "Filtro de Óleo", "Mann", "Aplicação universal", "34.50", 18);
            criarPeca("SKU-FREI001", "Pastilha de Freio", "Bosch", "Palio/Civic/Corolla", "189.90", 12);
            criarPeca("SKU-BATE001", "Bateria 60Ah", "Moura", "Veículos nacionais", "499.00", 8);
            criarPeca("SKU-VELA001", "Jogo de Velas", "NGK", "Motores 1.0 a 2.0", "96.50", 30);
        };
    }

    private void criarCliente(String nome, String telefone, String email, String cpf) {
        if (clienteRepository.findAll().stream().noneMatch(c -> cpf.equals(c.getCpf()))) {
            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setEmail(email);
            cliente.setCpf(cpf);
            clienteRepository.save(cliente);
        }
    }

    private void criarVeiculo(String placa, String marca, String modelo, int ano, int km, String cpfCliente) {
        if (veiculoRepository.findAll().stream().anyMatch(v -> placa.equals(v.getPlaca()))) return;
        Cliente cliente = clienteRepository.findAll().stream()
                .filter(c -> cpfCliente.equals(c.getCpf()))
                .findFirst().orElse(null);
        if (cliente == null) return;

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(placa);
        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setAno(ano);
        veiculo.setQuilometragem(km);
        veiculo.setCliente(cliente);
        veiculoRepository.save(veiculo);
    }

    private void criarPeca(String sku, String nome, String fabricante, String aplicacao,
                           String preco, int quantidade) {
        if (pecaRepository.findAll().stream().anyMatch(p -> sku.equals(p.getSku()))) return;

        Peca peca = new Peca();
        peca.setSku(sku);
        peca.setNome(nome);
        peca.setFabricante(fabricante);
        peca.setDescricao(aplicacao);
        peca.setPreco(new BigDecimal(preco));
        peca.setQuantidadeEstoque(quantidade);
        peca.setEstoqueMinimo(3);
        peca.setVersao(0L);
        pecaRepository.save(peca);
    }
}