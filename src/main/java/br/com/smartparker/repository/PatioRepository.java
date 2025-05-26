package br.com.smartparker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.smartparker.model.Patio;

public interface PatioRepository extends JpaRepository<Patio, Long>, JpaSpecificationExecutor<Patio> {
}
