package br.com.smartparker.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.smartparker.model.Setor;
import br.com.smartparker.model.SetorFilter;
import jakarta.persistence.criteria.Predicate;

public class SetorSpecification {
    
    public static Specification<Setor> withFilters(SetorFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro de nome
            if (filter.nome() != null) {
                predicates.add(cb.like(cb.lower(
                        root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            // Filtro de fileira
            if (filter.fileira() != null) {
                predicates.add(cb.equal(
                        root.get("fileira"), filter.fileira()));
            }

            // Filtro de vaga
            if (filter.vaga() != null) {
                predicates.add(cb.equal(
                        root.get("vaga"), filter.vaga()));
            }

            var arrayPredicates = predicates.toArray(new Predicate[0]);
            return cb.and(arrayPredicates);
        };
    }
}
