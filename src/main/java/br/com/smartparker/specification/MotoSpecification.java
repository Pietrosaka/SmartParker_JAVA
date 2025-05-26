package br.com.smartparker.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.smartparker.model.Moto;
import br.com.smartparker.model.MotoFilter;
import jakarta.persistence.criteria.Predicate;

public class MotoSpecification {

    public static Specification<Moto> withFilters(MotoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro de nome
            if (filter.nome() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            // Filtro de fabricante
            if (filter.fabricante() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("fabricante")), "%" + filter.fabricante().toLowerCase() + "%"));
            }

            // Filtro de placa
            if (filter.placa() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("placa")), "%" + filter.placa().toLowerCase() + "%"));
            }

            // Filtro de status
            if (filter.status() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("status")), "%" + filter.status().toLowerCase() + "%"));
            }

            var arrayPredicates = predicates.toArray(new Predicate[0]);
            return cb.and(arrayPredicates);
        };
    }
}
