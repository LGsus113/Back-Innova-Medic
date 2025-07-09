package com.DW2.InnovaMedic.util.specifications;

import com.DW2.InnovaMedic.entity.Cita;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class CitaSpecificationBuilder {
    public static Specification<Cita> filterCitas(
            Integer id,
            Cita.Estado estado,
            String nombreUsuario,
            TipoBusqueda tipoBusqueda
    ) {
        String usuario = switch (tipoBusqueda) {
            case PACIENTE -> "paciente";
            case MEDICO -> "medico";
        };

        String nombreBuscarSegunUsuario = switch (tipoBusqueda) {
            case PACIENTE -> "medico";
            case MEDICO -> "paciente";
        };

        return (root, _, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Path<Object> userPath = root.get(usuario);
            Path<Object> userBusquedaSegunUsuarioPath = root.get(nombreBuscarSegunUsuario);

            predicates.add(criteriaBuilder.equal(userPath.get("idUsuario"), id));

            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }

            if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
                String patronBusqueda = "%" + nombreUsuario.toLowerCase() + "%";

                Predicate nombre = criteriaBuilder.like(criteriaBuilder.lower(userBusquedaSegunUsuarioPath.get("nombre")), patronBusqueda);
                Predicate apellido = criteriaBuilder.like(criteriaBuilder.lower(userBusquedaSegunUsuarioPath.get("apellido")), patronBusqueda);

                predicates.add(criteriaBuilder.or(nombre, apellido));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public enum TipoBusqueda {
        PACIENTE,
        MEDICO
    }
}
