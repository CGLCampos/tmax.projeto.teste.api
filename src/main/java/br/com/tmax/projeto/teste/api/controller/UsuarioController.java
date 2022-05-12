package br.com.tmax.projeto.teste.api.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tmax.projeto.teste.api.config.validation.ErroFormularioDTO;
import br.com.tmax.projeto.teste.api.controller.dto.MensagemErroDTO;
import br.com.tmax.projeto.teste.api.controller.dto.UsuarioDTO;
import br.com.tmax.projeto.teste.api.controller.form.UsuarioAtualizaSenhaForm;
import br.com.tmax.projeto.teste.api.model.Usuario;
import br.com.tmax.projeto.teste.api.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("/{id}")
	public ResponseEntity<?> mostrarUsuario(@PathVariable Long id) {
		try {

			Optional<Usuario> optional = usuarioRepository.findById(id);
			if (!optional.isPresent()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(new UsuarioDTO(optional.get()));

		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MensagemErroDTO(e.getMessage()));
		}
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> atualizarSenha(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizaSenhaForm form,
			UriComponentsBuilder uriBuilder) {
		try {

			Optional<Usuario> optional = usuarioRepository.findById(id);
			if (!optional.isPresent()) {
				return ResponseEntity.notFound().build();
			}

			if (!new BCryptPasswordEncoder().matches(form.getSenhaAnterior(), optional.get().getSenha())) {
				return ResponseEntity.badRequest().body(new ErroFormularioDTO("senha", "Senha anterior não é a mesma"));
			}

			Usuario usuario = form.converter(optional.get());
			return ResponseEntity.ok(new UsuarioDTO(usuario));

		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MensagemErroDTO(e.getMessage()));
		}
	}
}
