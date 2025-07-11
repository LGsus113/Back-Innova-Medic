package com.DW2.InnovaMedic.service.session;

import com.DW2.InnovaMedic.entity.Usuario;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findOneByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("El usuario no se ha encontrado con el email: " + email) );

        return new UsuarioDetailImpl(usuario);
    }
}