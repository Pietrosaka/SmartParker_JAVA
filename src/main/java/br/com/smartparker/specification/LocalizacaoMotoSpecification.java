package br.com.smartparker.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.smartparker.model.LocalizacaoMoto;
import br.com.smartparker.model.LocalizacaoMotoFilter;
import jakarta.persistence.criteria.Predicate;

public class LocalizacaoMotoSpecification {

    public static Specification<LocalizacaoMoto> withFilters(LocalizacaoMotoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por data (intervalo de datas)
            if (filter.dataInicio() != null && filter.dataFim() != null) {
                predicates.add(cb.between(root.get("dataAtualizada"), filter.dataInicio(), filter.dataFim()));
            } else if (filter.dataInicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataAtualizada"), filter.dataInicio()));
            } else if (filter.dataFim() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataAtualizada"), filter.dataFim()));
            }

            // Filtro por Moto
            if (filter.moto() != null && filter.moto().getId() != null) {
                predicates.add(cb.equal(root.get("moto").get("id"), filter.moto().getId()));
            }

            // Filtro por Setor
            if (filter.setor() != null && filter.setor().getId() != null) {
                predicates.add(cb.equal(root.get("setor").get("id"), filter.setor().getId()));
            }

            var arrayPredicates = predicates.toArray(new Predicate[0]);
            return cb.and(arrayPredicates);
        };
    }

}
