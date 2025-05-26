package br.com.smartparker.controller;

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

import br.com.smartparker.dto.PatioDTO;
import br.com.smartparker.dto.SetorDTO;
import br.com.smartparker.model.Patio;
import br.com.smartparker.model.Setor;
import br.com.smartparker.model.SetorFilter;
import br.com.smartparker.repository.PatioRepository;
import br.com.smartparker.repository.SetorRepository;
import br.com.smartparker.specification.SetorSpecification;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/setores")
public class SetorController {

    @Autowired
    private SetorRepository repository;

    @Autowired
    private PatioRepository patioRepository;

    // 1. Read
    @GetMapping
    @Cacheable(value = "setores")
    @Operation(summary = "Listar todos os setores", description = "Lista todos os setores cadastrados com filtros", tags = "Setor")
    public Page<SetorDTO> index(SetorFilter filter,
            @PageableDefault(size = 5, sort = "nome") Pageable pageable) {
        return repository.findAll(SetorSpecification.withFilters(filter), pageable).map(this::toDTO);
    }

    // 1.1 Read {id}
    @GetMapping("{id}")
    @Cacheable(value = "setores")
    @Operation(summary = "Listar setor pelo ID", description = "Lista o setor com ID correspondente à requisição", tags = "Setor")
    public SetorDTO get(@PathVariable Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setor não encontrado."));
    }

    // 2. Create
    @PostMapping
    @CacheEvict(value = "setores", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo setor", description = "Criar setor de acordo com JSON enviado", tags = "Setor")
    public SetorDTO create(@RequestBody @Valid SetorDTO setorDTO) {
        Patio patio = patioRepository.findById(setorDTO.getPatio().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pátio não encontrado."));
        Setor setor = toEntity(setorDTO);
        setor.setPatio(patio);
        return toDTO(repository.save(setor));
    }

    // 3. Update
    @PutMapping("{id}")
    @CacheEvict(value = "setores", allEntries = true)
    @Operation(summary = "Atualizar setor", description = "Atualiza setor de acordo com ID e valores que precisam ser atualizados", tags = "Setor")
    public SetorDTO update(@PathVariable Long id, @RequestBody @Valid SetorDTO setorDTO) {
        return repository.findById(id).map(existing -> {
            existing.setNome(setorDTO.getNome());
            existing.setFileira(setorDTO.getFileira());
            existing.setVaga(setorDTO.getVaga());
            Patio patio = patioRepository.findById(setorDTO.getPatio().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pátio não encontrado."));
            existing.setPatio(patio);
            return toDTO(repository.save(existing));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setor não encontrado."));
    }

    // 4. Delete
    @DeleteMapping("{id}")
    @CacheEvict(value = "setores", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar setor", description = "Deleta setor com o ID escolhido", tags = "Setor")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private SetorDTO toDTO(Setor setor) {
        return new SetorDTO(
                setor.getId(),
                setor.getNome(),
                setor.getFileira(),
                setor.getVaga(),
                new PatioDTO(
                        setor.getPatio().getId(),
                        setor.getPatio().getNome(),
                        setor.getPatio().getLocalizacao()
                )
        );
    }

    private Setor toEntity(SetorDTO setorDTO) {
        return Setor.builder()
                .id(setorDTO.getId())
                .nome(setorDTO.getNome())
                .fileira(setorDTO.getFileira())
                .vaga(setorDTO.getVaga())
                .build();
    }
}