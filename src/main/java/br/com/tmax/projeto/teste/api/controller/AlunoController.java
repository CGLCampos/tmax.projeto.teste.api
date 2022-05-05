package br.com.tmax.projeto.teste.api.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.servlet.tags.form.FormTag;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tmax.projeto.teste.api.config.validation.ErroFormularioDTO;
import br.com.tmax.projeto.teste.api.controller.dto.AlunoDTO;
import br.com.tmax.projeto.teste.api.controller.form.AlunoAtualizaForm;
import br.com.tmax.projeto.teste.api.controller.form.AlunoCadastroForm;
import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.model.Livro;
import br.com.tmax.projeto.teste.api.repository.AlunoRepository;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
	
	@Autowired	
	private AlunoRepository alunoRepository;
	
	@GetMapping
	public Page<AlunoDTO> listar(
			@PageableDefault(page=0, size=10, sort="id", direction=Direction.ASC) Pageable paginacao) {
		Page<Aluno> alunos = alunoRepository.findAll(paginacao);
		return AlunoDTO.converter(alunos);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid AlunoCadastroForm form, UriComponentsBuilder uriBuilder) {
		Optional<Aluno> optional = alunoRepository.findByEmail(form.getEmail());
		if(optional.isEmpty()) {
			Aluno aluno = alunoRepository.save(form.converter());
			
			URI uri = uriBuilder.path("/alunos/{id}").buildAndExpand(aluno.getId()).toUri();
			return ResponseEntity.created(uri).body(new AlunoDTO(aluno));
		}
		return ResponseEntity.badRequest()
			.body(new ErroFormularioDTO("email", "E-mail informado já está sendo utilizado"));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AlunoDTO> mostrar(@PathVariable Long id) {
		Optional<Aluno> optional = alunoRepository.findById(id);
		if(optional.isPresent()) {
			return ResponseEntity.ok(new AlunoDTO(optional.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<AlunoDTO> atualizar(
			@PathVariable Long id, 
			@RequestBody @Valid AlunoAtualizaForm form, 
			UriComponentsBuilder uriBuilder) {
		
		Optional<Aluno> optional = alunoRepository.findById(id);
		if(optional.isPresent()) {
			Aluno aluno = form.converter(optional.get());
			URI uri = uriBuilder.path("/alunos/{id}").buildAndExpand(aluno.getId()).toUri();
			return ResponseEntity.created(uri).body(new AlunoDTO(aluno));
		}
		return ResponseEntity.notFound().build();
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Aluno> optional = alunoRepository.findById(id);
		if (!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}	
		if (!optional.get().getReservas().isEmpty()) {
			String msg = "O aluno tem reservas pendentes e não pode ser removido";
			return ResponseEntity.badRequest().body(msg);
		}
		
		alunoRepository.deleteById(id);
		return ResponseEntity.ok("Aluno removido com sucesso");
		
	}

}