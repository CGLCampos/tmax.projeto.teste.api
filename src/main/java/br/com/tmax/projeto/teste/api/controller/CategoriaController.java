package br.com.tmax.projeto.teste.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tmax.projeto.teste.api.controller.dto.MensagemErroDTO;
import br.com.tmax.projeto.teste.api.controller.dto.MensagemSucessoDTO;
import br.com.tmax.projeto.teste.api.model.Categoria;
import br.com.tmax.projeto.teste.api.repository.CategoriaRepository;
import br.com.tmax.projeto.teste.api.util.exceptions.NotFoundException;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	@Cacheable(value = "listaCategorias")
	public ResponseEntity<?> listarCategoria() {
		try {
			
			List<Categoria> categorias = categoriaRepository.findAll();
			return ResponseEntity.ok(categorias);

		} catch(Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
		
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value="listaCategorias", allEntries = true)
	public ResponseEntity<?> cadastrarCategoria(@RequestBody Categoria categoria, UriComponentsBuilder uriBuilder) {
		try {
			
			categoriaRepository.save(categoria);
	
			URI uri = uriBuilder.path("/livros/categorias").buildAndExpand(categoria.getId()).toUri();
			return ResponseEntity.created(uri).body(categoria);
		
		} catch(Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value="listaCategorias", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			
			Optional<Categoria> optional = categoriaRepository.findById(id);
			if (!optional.isPresent()) {
				throw new NotFoundException("A categoria informado não existe");
			}
			Categoria categoria = optional.get();
			
			categoriaRepository.delete(categoria);
			
			return ResponseEntity.ok(new MensagemSucessoDTO("Categoria removida com sucesso."));
		
			
			}catch(NotFoundException exception) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));
			} catch(Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}

}
