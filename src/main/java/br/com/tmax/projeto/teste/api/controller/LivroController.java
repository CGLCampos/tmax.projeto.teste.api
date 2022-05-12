package br.com.tmax.projeto.teste.api.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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

import br.com.tmax.projeto.teste.api.controller.dto.LivroDTO;
import br.com.tmax.projeto.teste.api.controller.dto.MensagemErroDTO;
import br.com.tmax.projeto.teste.api.controller.dto.MensagemSucessoDTO;
import br.com.tmax.projeto.teste.api.controller.form.LivroAtualizaForm;
import br.com.tmax.projeto.teste.api.controller.form.LivroCadastroForm;
import br.com.tmax.projeto.teste.api.model.Categoria;
import br.com.tmax.projeto.teste.api.model.Livro;
import br.com.tmax.projeto.teste.api.repository.CategoriaRepository;
import br.com.tmax.projeto.teste.api.repository.LivroRepository;
import br.com.tmax.projeto.teste.api.util.exceptions.ErroFormularioException;
import br.com.tmax.projeto.teste.api.util.exceptions.NotFoundException;

@RestController
@RequestMapping("/livros")
public class LivroController {

	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<?> listar(
			@PageableDefault(page=0, size=10, sort="id", direction=Direction.ASC) Pageable paginacao) {
		try {
			
			Page<Livro> livros = livroRepository.findAll(paginacao);
	
			return ResponseEntity.ok(LivroDTO.converter(livros));
		
		} catch(Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(
			@RequestBody @Valid LivroCadastroForm form, 
			UriComponentsBuilder uriBuilder) {
		try {
			
			Optional<Categoria> optional = categoriaRepository.findById(form.getIdCategoria());
			if (!optional.isPresent()) {
				throw new ErroFormularioException("idCategoria", "A categoria informado não existe");
			}
			Categoria categoria = optional.get();
			
			Livro livro = form.converter(categoria);
			livroRepository.save(livro);
			
			URI uri = uriBuilder.path("/livros/{id}").buildAndExpand(livro.getId()).toUri();
			
			return ResponseEntity.created(uri).body(new LivroDTO(livro));
			
		} catch(Throwable e) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(e.getMessage()));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> mostrar(@PathVariable Long id) {
		try {
			
			Optional<Livro> optional = livroRepository.findById(id);
			if (!optional.isPresent()) {
				throw new NotFoundException("O livro informado não existe");
			}
		
			return ResponseEntity.ok(new LivroDTO(optional.get()));
			
		} catch(NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));
		} catch(Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> atualizar(
			@PathVariable Long id, 
			@RequestBody @Valid LivroAtualizaForm form,
			UriComponentsBuilder uriBuilder) {
		try {

			Optional<Livro> optionalLivro = livroRepository.findById(id);
			if (!optionalLivro.isPresent()) {
				throw new NotFoundException("O livro informado não existe");
			} 
			Livro livro = optionalLivro.get();
			
			Optional<Categoria> optionalCategoria = categoriaRepository.findById(form.getIdCategoria());
			if (!optionalCategoria.isPresent()) {
				throw new NotFoundException("A categoria informado não existe");
			}
			Categoria categoria = optionalCategoria.get();
			
			return ResponseEntity.ok(new LivroDTO(form.converter(livro, categoria)));
			
		}catch(NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));
		} catch(Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			
			Optional<Livro> optional = livroRepository.findById(id);
			if (!optional.isPresent()) {
				throw new NotFoundException("O livro informado não existe");
			}
			Livro livro = optional.get();
					
			if (livro.estaReservado()) {
				String msg = "O livro está reservado e não pode ser removido.";
				return ResponseEntity.badRequest().body(new MensagemErroDTO(msg));
			}
			
			livroRepository.delete(livro);
			return ResponseEntity.ok(new MensagemSucessoDTO("Livro removido com sucesso."));
			
		}catch(NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));
		} catch(Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}
}
