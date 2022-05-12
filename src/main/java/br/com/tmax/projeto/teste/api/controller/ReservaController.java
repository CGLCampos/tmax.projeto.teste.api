package br.com.tmax.projeto.teste.api.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tmax.projeto.teste.api.controller.dto.MensagemErroDTO;
import br.com.tmax.projeto.teste.api.controller.dto.MensagemSucessoDTO;
import br.com.tmax.projeto.teste.api.controller.dto.ReservaDTO;
import br.com.tmax.projeto.teste.api.controller.form.ReservaCadastroForm;
import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.model.Livro;
import br.com.tmax.projeto.teste.api.model.Reserva;
import br.com.tmax.projeto.teste.api.repository.AlunoRepository;
import br.com.tmax.projeto.teste.api.repository.LivroRepository;
import br.com.tmax.projeto.teste.api.repository.ReservaRepository;
import br.com.tmax.projeto.teste.api.util.exceptions.ErroFormularioException;
import br.com.tmax.projeto.teste.api.util.exceptions.NotFoundException;

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
	public ResponseEntity<?> listar(@PageableDefault(sort = "id") Pageable paginacao) {
		try {

			Page<Reserva> reservas = reservaRepository.findAll(paginacao);
			return ResponseEntity.ok(ReservaDTO.converter(reservas));

		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrar(@RequestBody @Valid ReservaCadastroForm form, UriComponentsBuilder uriBuilder) {
		try {

			Optional<Aluno> optionalAluno = alunoRepository.findById(form.getIdAluno());
			if (!optionalAluno.isPresent()) {
				throw new NotFoundException("aluno", "O aluno ("+form.getIdAluno()+") não foi encontrado.");
			}
			Aluno aluno = optionalAluno.get();
						
			List<Livro> livros = new ArrayList<>();
			for (Long idLivro : form.getIdLivros()) {
				Optional<Livro> optionalLivro = livroRepository.findById(idLivro);
				if (!optionalLivro.isPresent()) {
					throw new NotFoundException("livros", "O informado livro("+idLivro+") não foi encontrado.");
				}

				Livro livro = optionalLivro.get();
				if (livro.estaReservado()) {
					throw new ErroFormularioException("livros", "O livro " + idLivro + " já está reservado.");
				}
				
				livros.add(livro);
			}

			Reserva reserva = form.converter(aluno, livros);
			reservaRepository.save(reserva);

			URI uri = uriBuilder.path("/reservas/{1}").buildAndExpand(reserva.getId()).toUri();
			return ResponseEntity.created(uri).body(new ReservaDTO(reserva));

		} catch (NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getErroDTO());

		} catch (ErroFormularioException exception) {
			return ResponseEntity.badRequest().body(exception.getErroDTO());

		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.badRequest().body(exception.getMessage());

		} catch (Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> mostrar(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(new ReservaDTO(this.buscarReservaPorId(id)));

		} catch (NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));

		} catch (Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}
	}
	
//	@PutMapping("/devolver")
//	public void devolverLivro() {
//		
//	}
//
//	@PutMapping("/{id}")
//	public void finalizar(@PathVariable Long id) {
//		Optional<Reserva> findById = reservaRepository.findById(id);
//		if (!optionalLivro.isPresent()) {
//			throw new ErroFormularioException("livros", "O informado livro não foi encontrado.");
//		}
//		
//	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			Reserva reserva = this.buscarReservaPorId(id);
			
			reserva.getLivrosReservados().forEach(livro -> livro.getLivro().devolver());

			reservaRepository.delete(reserva);
			return ResponseEntity.ok(new MensagemSucessoDTO("Reserva removida com sucesso"));

		} catch (NotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErroDTO(exception.getMessage()));

		} catch (Throwable throwable) {
			return ResponseEntity.badRequest().body(new MensagemErroDTO(throwable.getMessage()));
		}

	}
	
	private Reserva buscarReservaPorId(Long id) throws NotFoundException{
		Optional<Reserva> optional = reservaRepository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("A reserva informada não existe");
		}
		return optional.get();
	}

}
