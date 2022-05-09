package br.com.tmax.projeto.teste.api.controller.dto;

import org.springframework.data.domain.Page;

import br.com.tmax.projeto.teste.api.model.Livro;

public class LivroReservadoDTO {

	private Long id;
	private String titulo;
	private String autor;
	private String editora;
	private String nomeCategoria;
	
	
	public LivroReservadoDTO(Livro livro) {
		super();
		this.id = livro.getId();
		this.titulo = livro.getTitulo();
		this.autor = livro.getAutor();
		this.editora = livro.getEditora();
		this.nomeCategoria = livro.getCategoria().getNome();
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

	public String getEditora() {
		return editora;
	}

	public String getCategoria() {
		return nomeCategoria;
	}
	
	public static Page<LivroReservadoDTO> converter(Page<Livro> livros) {
		return livros.map(LivroReservadoDTO::new);
	}

}
