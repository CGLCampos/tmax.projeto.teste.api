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
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tmax.projeto.teste.api.controller.dto.LivroDTO;
import br.com.tmax.projeto.teste.api.controller.form.LivroAtualizaForm;
import br.com.tmax.projeto.teste.api.controller.form.LivroCadastroForm;
import br.com.tmax.projeto.teste.api.model.Livro;
import br.com.tmax.projeto.teste.api.repository.CategoriaRepository;
import br.com.tmax.projeto.teste.api.repository.LivroRepository;

@RestController
@RequestMapping("/livros")
public class LivroController {

	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public Page<LivroDTO> listar(
			@PageableDefault(page=0, size=10, sort="id", direction=Direction.ASC) Pageable paginacao) {
		
		Page<Livro> livros = livroRepository.findAll(paginacao);

		return LivroDTO.converter(livros);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<LivroDTO> cadastrar(
			@RequestBody @Valid LivroCadastroForm form, 
			UriComponentsBuilder uriBuilder) {

		Livro livro = form.converter(categoriaRepository);
		livroRepository.save(livro);

		URI uri = uriBuilder.path("/livros/{id}").buildAndExpand(livro.getId()).toUri();
		return ResponseEntity.created(uri).body(new LivroDTO(livro));
	}

	@GetMapping("/{id}")
	public ResponseEntity<LivroDTO> mostrar(@PathVariable Long id) {
		Optional<Livro> optional = livroRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(new LivroDTO(optional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<LivroDTO> atualizar(
			@PathVariable Long id, 
			@RequestBody @Valid LivroAtualizaForm form,
			UriComponentsBuilder uriBuilder) {

		Optional<Livro> optional = livroRepository.findById(id);
		if (optional.isPresent()) {
			Livro livro = form.converter(optional.get(), categoriaRepository);
			return ResponseEntity.ok(new LivroDTO(livro));
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		
		Optional<Livro> optional = livroRepository.findById(id);
		if (!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}	
		if (optional.get().getReserva() != null) {
			String msg = "O livro está reservado e não pode ser removido";
			return ResponseEntity.badRequest().body(msg);
		}
		
		livroRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
