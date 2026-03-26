package com.yindula.api.controller;

import com.yindula.api.dtoEenum.AgendamentoDTO;
import com.yindula.api.dtoEenum.ListarAgendamentos;
import com.yindula.api.dtoEenum.Posto;
import com.yindula.api.entity.AgendamentoEntity;
import com.yindula.api.repository.AgendamentoRepository;
import com.yindula.api.service.AgendamentoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {

    //Regra de negocio e gerenciador do Repositorio
    private final AgendamentoService agendamentoService;
    //Gerenciador do Banco de Dados
    private final AgendamentoRepository agendamentoRepository;

    // O Spring injeta ambos via Construtor (Melhor prática!)
    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoRepository agendamentoRepository) {
        this.agendamentoService = agendamentoService;
        this.agendamentoRepository = agendamentoRepository;
    }


    @CrossOrigin(origins = "*") // Importante para o navegador não bloquear
    @GetMapping("/ocupados")
    public ResponseEntity<List<String>> getOcupados(
            @RequestParam("data") String data,
            @RequestParam("posto") Posto posto) {

        List<String> horas = agendamentoService.buscarHorasOcupadas(data, posto);
        return ResponseEntity.ok(horas);
    }


    // ====================== CREATE (HÍBRIDO: UM OU VÁRIOS) ======================
    @PostMapping("/salvar")
    public ResponseEntity<List<ListarAgendamentos>> salvar(@RequestBody @Valid List<AgendamentoDTO> listaDados) {
        List<ListarAgendamentos> resposta = listaDados.stream()
                .map(dados -> {
                    // Chama a lógica de negócio que criámos no Service
                    var salvo = agendamentoService.salvarComValidacao(dados);
                    return new ListarAgendamentos(salvo);
                })
                .toList();

        return ResponseEntity.status(201).body(resposta);
    }

    // ====================== READ (LISTAR) ======================
    @GetMapping("/listar")
    public ResponseEntity<List<ListarAgendamentos>> listarAgendamento() {
        var lista = agendamentoRepository.findAll().stream()
                .map(ListarAgendamentos::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // ====================== READ (BUSCAR POR ID) ======================
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarAgendamento(@PathVariable Long id) {
        return agendamentoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    // ====================== READ (BUSCAR POR NOME) ======================
    @GetMapping("/buscar/cidadao/{cidadao}")
    public ResponseEntity<List<AgendamentoEntity>> buscarAgendamentoPorCidadao(@PathVariable String cidadao) {
        List<AgendamentoEntity> lista = agendamentoRepository.findByCidadaoContainingIgnoreCase(cidadao);
        return lista.isEmpty() ? ResponseEntity.status(404).build() : ResponseEntity.ok(lista);
    }

    // ====================== DELETE ======================
    @DeleteMapping("/excluir/{id}")
    @Transactional
    public ResponseEntity<String> excluirAgendamento(@PathVariable Long id) {
        if (!agendamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        agendamentoRepository.deleteById(id);
        return ResponseEntity.status(204).body("Agendamento excluido com sucesso");
    }
}

    /*
    @PostMapping("salvar")
    @Transactional
    public ResponseEntity<AgendamentoEntity> salvarAgendamento(@RequestBody @Valid AgendamentoDTO agendamentoDTO,
                                                    UriComponentsBuilder uriBilder) {
        var agendamento = new AgendamentoEntity(agendamentoDTO);
        agendamentoRepository.save(agendamento);
        var uri = uriBilder.path("/api/agendamento/{id}").buildAndExpand(agendamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new AgendamentoEntity(agendamentoDTO));
    }

    @PostMapping("/salvar")
    @Transactional
    public ResponseEntity<AgendamentoEntity> salvarAgendamento( @RequestBody @Valid AgendamentoDTO agendamentoDTO,
            UriComponentsBuilder uriBuilder) {   // ← corrigido o nome

        // Converte DTO → Entity e salva
        AgendamentoEntity agendamento = new AgendamentoEntity(agendamentoDTO);
        agendamento = agendamentoRepository.save(agendamento);   // ← aqui o ID é gerado

        // Monta a URI com o ID real
        var uri = uriBuilder.path("/api/agendamento/{id}").buildAndExpand(agendamento.getId()).toUri();

        // Devolve o objeto SALVO (com ID preenchido)
        return ResponseEntity.created(uri).body(agendamento);
    }

    // ====================== CREATE - VÁRIOS AGENDAMENTOS ======================
    @PostMapping("/salvar")
    @Transactional
    public ResponseEntity<?> salvarAgendamentos(
            @RequestBody @Valid List<AgendamentoDTO> listaDTO,
            UriComponentsBuilder uriBuilder) {

        // Lista para guardar os agendamentos criados (com ID preenchido)
        List<AgendamentoEntity> agendamentosSalvos = new ArrayList<>();

        for (AgendamentoDTO dto : listaDTO) {
            AgendamentoEntity agendamento = new AgendamentoEntity(dto);
            agendamento = agendamentoRepository.save(agendamento);   // salva um por um
            agendamentosSalvos.add(agendamento);
        }
        // Monta a resposta com todos os IDs criados
        return ResponseEntity.created(
                uriBuilder.path("/api/agendamento/listar").build().toUri()
        ).body(agendamentosSalvos);   // devolve a lista completa com IDs
    }







    // 2. Construtor para o Spring colocar o Service aqui dentro
    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<List<ListarAgendamentos>> salvar(@RequestBody @Valid List<AgendamentoDTO> listaDados) {

        List<ListarAgendamentos> resposta = listaDados.stream()
                .map(dados -> {
                    // 3. CHAMADA CORRETA: usamos a variável da instância, não a Classe!
                    var salvo = agendamentoService.salvarComValidacao(dados);
                    return new ListarAgendamentos(salvo);
                })
                .toList();

        return ResponseEntity.ok(resposta);
    }
}


    @GetMapping("/listar")
    @Transactional
    public ResponseEntity<List<ListarAgendamentos>> listarAgenadamento(){
        var lista = agendamentoRepository.findAll().stream().map(ListarAgendamentos::new).toList();
        return ResponseEntity.status(200).body(lista);
    }



    @GetMapping("/buscar/{id}")
    @Transactional
    public ResponseEntity<?> buscarAgendamento(@PathVariable Long id) {
        return agendamentoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/buscar/cidadao/{cidadao}")
    @Transactional
    public ResponseEntity<List<AgendamentoEntity>> buscarAgendamentoPorCidadao(@PathVariable String cidadao) {

        List<AgendamentoEntity> agendamentoEntities = agendamentoRepository.findByCidadaoContainingIgnoreCase(cidadao);
        if (agendamentoEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(agendamentoEntities);
    }

    @DeleteMapping("/excluir/{id}")
    @Transactional
    public ResponseEntity<String> excluirAgendamento(@PathVariable Long id){
        agendamentoRepository.deleteAllById(Collections.singleton(id));
        return ResponseEntity.ok().body("Agendamento excluído com sucesso");
    }
}*/

