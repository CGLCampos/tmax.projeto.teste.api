package br.com.tmax.projeto.teste.api.controller;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tmax.projeto.teste.api.config.security.TokenService;
import br.com.tmax.projeto.teste.api.controller.dto.AlunoComReservasDTO;
import br.com.tmax.projeto.teste.api.controller.dto.AlunoDTO;
import br.com.tmax.projeto.teste.api.controller.dto.MensagemErroDTO;
import br.com.tmax.projeto.teste.api.controller.dto.MensagemSucessoDTO;
import br.com.tmax.projeto.teste.api.controller.form.AlunoAtualizaForm;
import br.com.tmax.projeto.teste.api.controller.form.AlunoCadastroForm;
import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.model.Perfil;
import br.com.tmax.projeto.teste.api.repository.AlunoRepository;
import br.com.tmax.projeto.teste.api.repository.PerfilRepository;
import br.com.tmax.projeto.teste.api.util.exceptions.ErroFormularioException;
import br.com.tmax.projeto.teste.api.util.exceptions.NotFoundException;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private TokenService tokenService;

	@GetMapping
	public ResponseEntity<?> listar(@PageableDefault(sort = "nome") Pageable paginacao) {
		try {

			Page<Aluno> alunos = alunoRepository.findAll(paginacao);
			return ResponseEntity.ok(AlunoDTO.converter(alunos));

		} catch (Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid AlunoCadastroForm form, UriComponentsBuilder uriBuilder) {
		try {

			Optional<Aluno> optional = alunoRepository.findByEmail(form.getEmail());
			if (optional.isPresent()) {
				throw new ErroFormularioException("email", "O e-mail informado ja está cadastrado.");
			}

			Perfil perfil = perfilRepository.findByNome("ROLE_USER");
			Aluno aluno = alunoRepository.save(form.converter(perfil));

			URI uri = uriBuilder.path("/alunos/{id}").buildAndExpand(aluno.getId()).toUri();
			return ResponseEntity.created(uri).body(new AlunoDTO(aluno));

		} catch (ErroFormularioException exception) {
			return ResponseEntity.badRequest().body(exception.getErroDTO());

		} catch (Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));

		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> mostrar(@PathVariable Long id) {
		try {

			Optional<Aluno> optional = alunoRepository.findById(id);
			if (!optional.isPresent()) {
				throw new NotFoundException("O aluno informado não existe");
			}

			return ResponseEntity.ok(new AlunoDTO(optional.get()));

		} catch (NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));

		} catch (Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));

		}
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid AlunoAtualizaForm form,
			UriComponentsBuilder uriBuilder) {
		try {

			Optional<Aluno> optional = alunoRepository.findById(id);
			if (!optional.isPresent()) {
				throw new NotFoundException("O aluno informado não existe");
			}
			Aluno aluno = form.converter(optional.get());

			URI uri = uriBuilder.path("/alunos/{id}").buildAndExpand(aluno.getId()).toUri();

			return ResponseEntity.created(uri).body(new AlunoDTO(aluno));

		} catch (NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));

		} catch (Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));

		}
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {

			Optional<Aluno> optional = alunoRepository.findById(id);
			if (!optional.isPresent()) {
				throw new NotFoundException("O aluno informado não existe");
			}
			Aluno aluno = optional.get();

			if (!optional.get().getReservas().isEmpty()) {
				throw new Exception("O aluno tem reservas pendentes e não pode ser removido");
			}
			alunoRepository.delete(aluno);

			return ResponseEntity.ok(new MensagemSucessoDTO("Aluno removido com sucesso"));

		} catch (NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));

		} catch (Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));

		}

	}

	@GetMapping("/perfil")
	public ResponseEntity<?> perfil(HttpServletRequest request) {
		try {

			String token = recuperarToken(request);
			Long idUsuario = tokenService.getIdUsuario(token);

			Optional<Aluno> optional = alunoRepository.findById(idUsuario);
			if (!optional.isPresent()) {
				throw new NotFoundException("O aluno informado não existe");
			}
			Aluno aluno = optional.get();

			return ResponseEntity.ok(new AlunoComReservasDTO(aluno));

		} catch (NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));

		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));

		}
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
	}

}
