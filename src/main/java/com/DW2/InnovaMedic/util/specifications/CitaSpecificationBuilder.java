package com.DW2.InnovaMedic.util.specifications;

import com.DW2.InnovaMedic.entity.Cita;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CitaSpecificationBuilder {
    public static Specification<Cita> filterCitas(Integer id, Cita.Estado estado, TipoBusqueda tipoBusqueda) {
        String usuario = switch (tipoBusqueda) {
            case PACIENTE -> "paciente";
            case MEDICO -> "medico";
        };

        return (root, _, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(usuario).get("idUsuario"), id));

            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public enum TipoBusqueda {
        PACIENTE,
        MEDICO
    }
}
