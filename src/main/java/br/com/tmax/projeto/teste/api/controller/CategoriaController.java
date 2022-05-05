package br.com.tmax.projeto.teste.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

import br.com.tmax.projeto.teste.api.model.Categoria;
import br.com.tmax.projeto.teste.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	@Cacheable(value = "listaCategorias")
	public List<Categoria> listarCategoria() {
		List<Categoria> categorias = categoriaRepository.findAll();
		return categorias;
		
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value="listaCategorias", allEntries = true)
	public ResponseEntity<Categoria> cadastrarCategoria(@RequestBody Categoria categoria, UriComponentsBuilder uriBuilder) {
		categoriaRepository.save(categoria);

		URI uri = uriBuilder.path("/livros/categorias").buildAndExpand(categoria.getId()).toUri();
		return ResponseEntity.created(uri).body(categoria);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value="listaCategorias", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Categoria> optional = categoriaRepository.findById(id);
		if (optional.isPresent()) {
			categoriaRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

}
