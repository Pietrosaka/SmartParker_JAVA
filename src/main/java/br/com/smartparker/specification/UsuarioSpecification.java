package br.com.smartparker.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;


import jakarta.persistence.criteria.Predicate;
import br.com.smartparker.model.Usuario;
import br.com.smartparker.model.UsuarioFilter;

public class UsuarioSpecification {
    
    public static Specification<Usuario> withFilters(UsuarioFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro de nome
            if (filter.nome() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            // Filtro de email
            if (filter.email() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("email")), "%" + filter.email().toLowerCase() + "%"));
            }

            // Filtro de CPF
            if (filter.cpf() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("cpf")), "%" + filter.cpf().toLowerCase() + "%"));
            }

            // Filtro de moto
            if (filter.moto() != null) {
                if (filter.moto().getId() != null) {
                    predicates.add(cb.equal(root.get("moto").get("id"), filter.moto().getId()));
                }
                if (filter.moto().getNome() != null) {
                    predicates.add(cb.like(cb.lower(
                            root.get("moto").get("nome")), "%" + filter.moto().getNome().toLowerCase() + "%"));
                }
                if (filter.moto().getPlaca() != null) {
                    predicates.add(cb.like(cb.lower(
                            root.get("moto").get("placa")), "%" + filter.moto().getPlaca().toLowerCase() + "%"));
                }
            }

            var arrayPredicates = predicates.toArray(new Predicate[0]);
            return cb.and(arrayPredicates);
        };
    }
}
