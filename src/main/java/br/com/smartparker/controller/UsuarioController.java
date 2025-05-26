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
import br.com.smartparker.dto.UsuarioDTO;
import br.com.smartparker.model.Moto;
import br.com.smartparker.model.Usuario;
import br.com.smartparker.model.UsuarioFilter;
import br.com.smartparker.repository.MotoRepository;
import br.com.smartparker.repository.MotoRepository;
import br.com.smartparker.repository.MotoRepository;
import br.com.smartparker.repository.UsuarioRepository;
import br.com.smartparker.specification.UsuarioSpecification;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private MotoRepository motoRepository;

    // 1. Read
    @GetMapping
    @Cacheable(value = "usuarios")
    @Operation(summary = "Listar todos os usuários", description = "Lista todos os usuários cadastrados com filtros", tags = "Usuário")
    public Page<UsuarioDTO> index(UsuarioFilter filter,
            @PageableDefault(size = 5, sort = "nome") Pageable pageable) {
        return repository.findAll(UsuarioSpecification.withFilters(filter), pageable).map(this::toDTO);
    }

    // 1.1 Read {id}
    @GetMapping("{id}")
    @Cacheable(value = "usuarios")
    @Operation(summary = "Listar usuário pelo ID", description = "Lista o usuário com ID correspondente à requisição", tags = "Usuário")
    public UsuarioDTO get(@PathVariable Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }

    // 2. Create
    @PostMapping
    @CacheEvict(value = "usuarios", allEntries = true)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo usuário", description = "Criar usuário de acordo com JSON enviado", tags = "Usuário")
    public UsuarioDTO create(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        Usuario usuario = toEntity(usuarioDTO);

        if (usuarioDTO.getMoto() != null && usuarioDTO.getMoto().getId() != null) {
            Moto moto = motoRepository.findById(usuarioDTO.getMoto().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));
            usuario.setMoto(moto);
    }
    
        return toDTO(repository.save(usuario));
    }

    // 3. Update
    @PutMapping("{id}")
    @CacheEvict(value = "usuarios", allEntries = true)
    @Operation(summary = "Atualizar usuário", description = "Atualiza usuário de acordo com ID e valores que precisam ser atualizados", tags = "Usuário")
    public UsuarioDTO update(@PathVariable Long id, @RequestBody @Valid UsuarioDTO usuarioDTO) {
        return repository.findById(id).map(existing -> {
            existing.setNome(usuarioDTO.getNome());
            existing.setEmail(usuarioDTO.getEmail());
            existing.setCpf(usuarioDTO.getCpf());

            if (usuarioDTO.getMoto() != null && usuarioDTO.getMoto().getId() != null) {
                Moto moto = motoRepository.findById(usuarioDTO.getMoto().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));
                existing.setMoto(moto);
            } else {
                existing.setMoto(null);
            }

            return toDTO(repository.save(existing));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }

    // 4. Delete
    @DeleteMapping("{id}")
    @CacheEvict(value = "usuarios", allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar usuário", description = "Deleta usuário com o ID escolhido", tags = "Usuário")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getMoto() != null ? new MotoDTO(
                        usuario.getMoto().getId(),
                        usuario.getMoto().getNome(),
                        usuario.getMoto().getFabricante(),
                        usuario.getMoto().getCilindrada(),
                        usuario.getMoto().getPlaca(),
                        usuario.getMoto().getStatus(),
                        usuario.getMoto().getQrCode()
                ) : null
        );
    }

    private Usuario toEntity(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .id(usuarioDTO.getId())
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .cpf(usuarioDTO.getCpf())
                .build();
    }
}