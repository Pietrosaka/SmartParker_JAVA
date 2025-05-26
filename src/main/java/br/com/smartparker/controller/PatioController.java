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
import br.com.smartparker.model.Patio;
import br.com.smartparker.model.PatioFilter;
import br.com.smartparker.repository.PatioRepository;
import br.com.smartparker.specification.PatioSpecification;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patios")
public class PatioController {

    @Autowired
    private PatioRepository repository;

    // 1. Read
    @GetMapping
    @Cacheable(value = "patios")
    @Operation(summary = "Listar todos os pátios", description = "Lista todos os pátios cadastrados com filtros", tags = "Pátio")
    public Page<PatioDTO> index(PatioFilter filter,
            @PageableDefault(size = 5, sort = "nome") Pageable pageable) {
        return repository.findAll(PatioSpecification.withFilters(filter), pageable).map(this::toDTO);
    }

    // 1.1 Read {id}
    @GetMapping("{id}")
    @Cacheable(value = "patios")
    @Operation(summary = "Listar pátio pelo ID", description = "Lista o pátio com ID correspondente à requisição", tags = "Pátio")
    public PatioDTO get(@PathVariable Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pátio não encontrado."));
    }

    // 2. Create
    @PostMapping
    @CacheEvict(value = "patios", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo pátio", description = "Criar pátio de acordo com JSON enviado", tags = "Pátio")
    public PatioDTO create(@RequestBody @Valid PatioDTO patioDTO) {
        Patio patio = toEntity(patioDTO);
        return toDTO(repository.save(patio));
    }

    // 3. Update
    @PutMapping("{id}")
    @CacheEvict(value = "patios", allEntries = true)
    @Operation(summary = "Atualizar pátio", description = "Atualiza pátio de acordo com ID e valores que precisam ser atualizados", tags = "Pátio")
    public PatioDTO update(@PathVariable Long id, @RequestBody @Valid PatioDTO patioDTO) {
        return repository.findById(id).map(existing -> {
            existing.setNome(patioDTO.getNome());
            existing.setLocalizacao(patioDTO.getLocalizacao());
            return toDTO(repository.save(existing));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pátio não encontrado."));
    }

    // 4. Delete
    @DeleteMapping("{id}")
    @CacheEvict(value = "patios", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar pátio", description = "Deleta pátio com o ID escolhido", tags = "Pátio")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private PatioDTO toDTO(Patio patio) {
        return new PatioDTO(
                patio.getId(),
                patio.getNome(),
                patio.getLocalizacao()
        );
    }

    private Patio toEntity(PatioDTO patioDTO) {
        return Patio.builder()
                .id(patioDTO.getId())
                .nome(patioDTO.getNome())
                .localizacao(patioDTO.getLocalizacao())
                .build();
    }
}