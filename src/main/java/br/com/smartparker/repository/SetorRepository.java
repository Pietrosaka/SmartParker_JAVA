package br.com.smartparker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.smartparker.model.Setor;

public interface SetorRepository extends JpaRepository<Setor, Long>, JpaSpecificationExecutor<Setor> {
}