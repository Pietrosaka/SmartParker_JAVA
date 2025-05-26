package br.com.smartparker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.smartparker.model.LocalizacaoMoto;

public interface LocalizacaoMotoRepository extends JpaRepository<LocalizacaoMoto, Long>, JpaSpecificationExecutor<LocalizacaoMoto> {
    Optional<LocalizacaoMoto> findByMotoId(Long motoId);
}
