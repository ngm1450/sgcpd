package br.uniesquina.backend.controller;

import br.uniesquina.backend.enums.StatusUsuarioEnum;
import br.uniesquina.backend.model.*;
import br.uniesquina.backend.repository.UsuarioRepository;
import br.uniesquina.backend.security.JwtUtil;
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
        String matricula = body.get("matricula");
        String senha = body.get("senha");

        if (matricula == null || senha == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Matrícula e senha são obrigatórios."));
        }

        return usuarioRepository.findByMatricula(matricula)
                .map(usuario -> {
                    if (!StatusUsuarioEnum.ATIVO.equals(usuario.getStatus())) {
                        return ResponseEntity.status(403).body(Map.of("erro", "Usuário inativo ou bloqueado."));
                    }

                    if (!passwordEncoder.matches(senha, usuario.getHashSenha())) {
                        return ResponseEntity.status(401).body(Map.of("erro", "Senha inválida."));
                    }

                    Pessoa pessoa = usuario.getPessoa();

                    String matriculaExtraida;

                    switch (pessoa) {
                        case Aluno aluno -> matriculaExtraida = aluno.getMatricula();
                        case Professor professor -> matriculaExtraida = professor.getMatricula();
                        case Funcionario funcionario -> matriculaExtraida = funcionario.getMatricula();
                        case null, default -> {
                            return ResponseEntity.status(500).body(Map.of("erro", "Tipo de pessoa desconhecido."));
                        }
                    }

                    String token = jwtUtil.generateToken(
                        matriculaExtraida,
                        Map.of(
                        "perfil", usuario.getTipoPessoa(),
                        "nome", pessoa.getNome(),
                            "sexo", pessoa.getSexo()

                        )
                    );

                    return ResponseEntity.ok(Map.of(
                        "token", token,
                        "perfil", usuario.getTipoPessoa(),
                        "nome", pessoa.getNome()
                    ));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("erro", "Usuário não encontrado.")));

    }


}
