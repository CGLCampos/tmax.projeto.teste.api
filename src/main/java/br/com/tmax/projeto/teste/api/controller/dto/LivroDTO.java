package br.com.tmax.projeto.teste.api.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.tmax.projeto.teste.api.model.Livro;

public class LivroDTO {

	private Long id;
	private String titulo;
	private String autor;
	private String edicao;
	private String editora;
	private String dataPublicacao;
	private String nomeCategoria;
	private boolean reservado;
	
	
	public LivroDTO(Livro livro) {
		super();
		this.id = livro.getId();
		this.titulo = livro.getTitulo();
		this.autor = livro.getAutor();
		this.edicao = livro.getEdicao();
		this.editora = livro.getEditora();
		this.dataPublicacao = livro.getDataPublicacao();
		this.nomeCategoria = livro.getCategoria().getNome();
		if(livro.getReserva() == null) {
			this.reservado = false;
		} else {
			this.reservado = true;
		}
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getAutor() {
		return autor;
	}

	public String getEdicao() {
		return edicao;
	}

	public String getEditora() {
		return editora;
	}

	public String getDataPublicacao() {
		return dataPublicacao;
	}

	public String getCategoria() {
		return nomeCategoria;
	}

	public boolean isReservado() {
		return reservado;
	}
	
	public static Page<LivroDTO> converter(Page<Livro> livros) {
		return livros.map(LivroDTO::new);
	}

}
