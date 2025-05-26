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

import br.com.smartparker.dto.MotoDTO;
import br.com.smartparker.model.Moto;
import br.com.smartparker.model.MotoFilter;
import br.com.smartparker.repository.MotoRepository;
import br.com.smartparker.specification.MotoSpecification;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/motos")
@Slf4j
public class MotoController {

    @Autowired
    private MotoRepository repository;

    // 1.Read
    @GetMapping
    @Cacheable(value = "motos")
    @Operation(summary = "Listar todas motos", description = "Lista todas as motos cadastradas", tags = "Moto")
    public Page<MotoDTO> index(MotoFilter filter,
            @PageableDefault(size = 5, sort = "nome") Pageable pageable) {
        return repository.findAll(MotoSpecification.withFilters(filter), pageable).map(this::toDTO);
    }

    // 1.1 Read {id}
    @GetMapping("{id}")
    @Cacheable(value = "motos")
    @Operation(summary = "Buscar moto por ID", description = "Busca uma moto específica pelo ID fornecido", tags = "Moto")
    public MotoDTO get(@PathVariable Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));
    }

    // 2.Create
    @PostMapping
    @CacheEvict(value = "motos", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova moto", description = "Cria uma nova moto com os dados fornecidos no corpo da requisição", tags = "Moto")
    public MotoDTO create(@RequestBody @Valid MotoDTO motoDTO) {
        Moto moto = toEntity(motoDTO);
        return toDTO(repository.save(moto));
    }

    // 3.Update
    @PutMapping("{id}")
    @CacheEvict(value = "motos", allEntries = true)
    @Operation(summary = "Atualizar moto", description = "Atualiza moto de acordo com ID e valores que precisam ser atualizados", tags = "Moto")
    public MotoDTO update(@PathVariable Long id, @RequestBody @Valid MotoDTO motoDTO) {
        return repository.findById(id).map(existing -> {
            existing.setNome(motoDTO.getNome());
            existing.setFabricante(motoDTO.getFabricante());
            existing.setCilindrada(motoDTO.getCilindrada());
            existing.setPlaca(motoDTO.getPlaca());
            existing.setStatus(motoDTO.getStatus());
            existing.setQrCode(motoDTO.getQrCode());
            return toDTO(repository.save(existing));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));
    }

    // 4.Delete
    @DeleteMapping("{id}")
    @CacheEvict(value = "motos", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir moto", description = "Exclui uma moto existente com base no ID fornecido", tags = "Moto")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private MotoDTO toDTO(Moto moto) {
        return new MotoDTO(
                moto.getId(),
                moto.getNome(),
                moto.getFabricante(),
                moto.getCilindrada(),
                moto.getPlaca(),
                moto.getStatus(),
                moto.getQrCode()
        );
    }

    private Moto toEntity(MotoDTO motoDTO) {
        return Moto.builder()
                .id(motoDTO.getId())
                .nome(motoDTO.getNome())
                .fabricante(motoDTO.getFabricante())
                .cilindrada(motoDTO.getCilindrada())
                .placa(motoDTO.getPlaca())
                .status(motoDTO.getStatus())
                .qrCode(motoDTO.getQrCode())
                .build();
    }
}
