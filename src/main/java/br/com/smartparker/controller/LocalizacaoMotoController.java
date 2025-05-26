package br.com.smartparker.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.smartparker.dto.LocalizacaoMotoDTO;
import br.com.smartparker.dto.MotoDTO;
import br.com.smartparker.dto.SetorDTO;
import br.com.smartparker.model.LocalizacaoMoto;
import br.com.smartparker.model.LocalizacaoMotoFilter;
import br.com.smartparker.model.Moto;
import br.com.smartparker.model.Setor;
import br.com.smartparker.repository.LocalizacaoMotoRepository;
import br.com.smartparker.repository.MotoRepository;
import br.com.smartparker.repository.SetorRepository;
import br.com.smartparker.specification.LocalizacaoMotoSpecification;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/localizacoes")
public class LocalizacaoMotoController {

    @Autowired
    private LocalizacaoMotoRepository repository;
    
    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private SetorRepository setorRepository;

    // 1. Read
    @GetMapping
    @Cacheable(value = "localizacoes")
    @Operation(summary = "Listar todas as localizações", description = "Lista todas as localizações cadastradas com filtros", tags = "LocalizacaoMoto")
    public Page<LocalizacaoMotoDTO> index(LocalizacaoMotoFilter filter,
            @PageableDefault(size = 5, sort = "dataAtualizada") Pageable pageable) {
        return repository.findAll(LocalizacaoMotoSpecification.withFilters(filter), pageable).map(this::toDTO);
    }

    // 1.1 Read {id}
    @GetMapping("{id}")
    @Cacheable(value = "localizacoes")
    @Operation(summary = "Listar localização pelo ID", description = "Lista a localização com ID correspondente à requisição", tags = "LocalizacaoMoto")
    public LocalizacaoMotoDTO get(@PathVariable Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada."));
    }

    // 1.2 Read detalhes/{motoId}
    @GetMapping("detalhes/{motoId}")
    @Cacheable(value = "localizacoes")
    @Operation(summary = "Detalhes da localização", description = "Exibe detalhes da localização, incluindo moto, setor e horário", tags = "LocalizacaoMoto")
    public String detalhesPorMotoIdString(@PathVariable Long motoId) {
        return repository.findByMotoId(motoId)
                .map(localizacao -> String.format(
                        "Moto: %s (Placa: %s), Setor: %s (Fileira: %d, Vaga: %d), Atualizado em: %s",
                        localizacao.getMoto().getNome(),
                        localizacao.getMoto().getPlaca(),
                        localizacao.getSetor().getNome(),
                        localizacao.getSetor().getFileira(),
                        localizacao.getSetor().getVaga(),
                        localizacao.getDataAtualizada()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada para a moto com ID " + motoId));
    }

    // 2. Create
    @PostMapping
    @CacheEvict(value = "localizacoes", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova localização", description = "Criar localização de acordo com JSON enviado", tags = "LocalizacaoMoto")
    public LocalizacaoMotoDTO create(@RequestBody @Valid LocalizacaoMotoDTO localizacaoMotoDTO) {
        Moto moto = motoRepository.findById(localizacaoMotoDTO.getMoto().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));
        Setor setor = setorRepository.findById(localizacaoMotoDTO.getSetor().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setor não encontrado."));

        LocalizacaoMoto localizacaoMoto = toEntity(localizacaoMotoDTO);
        localizacaoMoto.setMoto(moto);
        localizacaoMoto.setSetor(setor);
        localizacaoMoto.setDataAtualizada(LocalDateTime.now());
        return toDTO(repository.save(localizacaoMoto));
    }

    // 3. Update
    @PutMapping("{id}")
    @CacheEvict(value = "localizacoes", allEntries = true)
    @Operation(summary = "Atualizar localização", description = "Atualiza localização de acordo com ID e valores que precisam ser atualizados", tags = "LocalizacaoMoto")
    public LocalizacaoMotoDTO update(@PathVariable Long id, @RequestBody @Valid LocalizacaoMotoDTO localizacaoMotoDTO) {
        return repository.findById(id).map(existing -> {
            Moto moto = motoRepository.findById(localizacaoMotoDTO.getMoto().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));
            Setor setor = setorRepository.findById(localizacaoMotoDTO.getSetor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setor não encontrado."));

            existing.setMoto(moto);
            existing.setSetor(setor);
            existing.setDataAtualizada(LocalDateTime.now());
            return toDTO(repository.save(existing));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada."));
    }

    // 4. Delete
    @DeleteMapping("{id}")
    @CacheEvict(value = "localizacoes", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar localização", description = "Deleta localização com o ID escolhido", tags = "LocalizacaoMoto")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private LocalizacaoMotoDTO toDTO(LocalizacaoMoto localizacaoMoto) {
        return new LocalizacaoMotoDTO(
                localizacaoMoto.getId(),
                localizacaoMoto.getDataAtualizada(),
                new MotoDTO(
                        localizacaoMoto.getMoto().getId(),
                        localizacaoMoto.getMoto().getNome(),
                        localizacaoMoto.getMoto().getFabricante(),
                        localizacaoMoto.getMoto().getCilindrada(),
                        localizacaoMoto.getMoto().getPlaca(),
                        localizacaoMoto.getMoto().getStatus(),
                        localizacaoMoto.getMoto().getQrCode()
                ),
                new SetorDTO(
                        localizacaoMoto.getSetor().getId(),
                        localizacaoMoto.getSetor().getNome(),
                        localizacaoMoto.getSetor().getFileira(),
                        localizacaoMoto.getSetor().getVaga(),
                        null
                )
        );
    }

    private LocalizacaoMoto toEntity(LocalizacaoMotoDTO localizacaoMotoDTO) {
        return LocalizacaoMoto.builder()
                .id(localizacaoMotoDTO.getId())
                .dataAtualizada(localizacaoMotoDTO.getDataAtualizada())
                .build();
    }
}