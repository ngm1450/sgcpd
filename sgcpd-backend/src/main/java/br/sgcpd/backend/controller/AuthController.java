package br.sgcpd.backend.controller;


import br.sgcpd.backend.repository.UsuarioRepository;
import br.sgcpd.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");

        if (email == null || senha == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Email e senha são obrigatórios."));
        }

        return usuarioRepository.findByEmail(email)
                .map(usuario -> {

                    if (!passwordEncoder.matches(senha, usuario.getHashSenha())) {
                        return ResponseEntity.status(401).body(Map.of("erro", "Senha inválida."));
                    }

                    String token = jwtUtil.generateToken(
                        email,
                        Map.of(
                            "email", usuario.getEmail(),
                            "nome", usuario.getNome()
                        )
                    );

                    return ResponseEntity.ok(Map.of(
                        "token", token
                    ));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("erro", "Usuário não encontrado.")));

    }


}
