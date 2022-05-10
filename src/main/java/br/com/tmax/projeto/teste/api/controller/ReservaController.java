package br.com.tmax.projeto.teste.api.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import br.com.tmax.projeto.teste.api.controller.dto.ReservaDTO;
import br.com.tmax.projeto.teste.api.controller.form.ReservaCadastroForm;
import br.com.tmax.projeto.teste.api.model.Reserva;
import br.com.tmax.projeto.teste.api.repository.AlunoRepository;
import br.com.tmax.projeto.teste.api.repository.LivroRepository;
import br.com.tmax.projeto.teste.api.repository.ReservaRepository;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	@GetMapping
	public Page<ReservaDTO> listar(
			@PageableDefault(sort="id") Pageable paginacao) {
		
		Page<Reserva> reservas = reservaRepository.findAll(paginacao);
		return ReservaDTO.converter(reservas);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid ReservaCadastroForm form, UriComponentsBuilder uriBuilder) {
		Reserva reserva = form.converter(alunoRepository, livroRepository);
		
		reservaRepository.save(reserva);
		URI uri = uriBuilder.path("/reservas/{1}").buildAndExpand(reserva.getId()).toUri();
		return ResponseEntity.created(uri).body(new ReservaDTO(reserva));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReservaDTO> mostrar(@PathVariable Long id) {
		Optional<Reserva> optional = reservaRepository.findById(id);
		if(!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(new ReservaDTO(optional.get()));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Reserva> optional = reservaRepository.findById(id);
		if (!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		optional.get().getLivros().forEach(livro->livro.devolver());
		
		
		reservaRepository.deleteById(id);
		return ResponseEntity.ok("Reserva removida com sucesso");
		
	}
	
	
}
