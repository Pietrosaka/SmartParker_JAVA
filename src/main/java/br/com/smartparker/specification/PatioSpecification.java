package br.com.smartparker.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.smartparker.model.Patio;
import br.com.smartparker.model.PatioFilter;
import jakarta.persistence.criteria.Predicate;

public class PatioSpecification {

    public static Specification<Patio> withFilters(PatioFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro de nome
            if (filter.nome() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            // Filtro de localizacao
            if (filter.localizacao() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("localizacao")), "%" + filter.localizacao().toLowerCase() + "%"));
            }

            var arrayPredicates = predicates.toArray(new Predicate[0]);
            return cb.and(arrayPredicates);
        };
    }
}