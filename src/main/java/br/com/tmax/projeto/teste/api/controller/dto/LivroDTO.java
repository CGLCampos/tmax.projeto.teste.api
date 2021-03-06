package br.com.tmax.projeto.teste.api.controller.dto;

import org.springframework.data.domain.Page;

import br.com.tmax.projeto.teste.api.model.Livro;

public class LivroDTO {

	private Long id;
	private String titulo;
	private String autor;
	private String idioma;
	private String editora;
	private String dataPublicacao;
	private String nomeCategoria;
	private boolean reservado;
	
	
	public LivroDTO(Livro livro) {
		super();
		this.id = livro.getId();
		this.titulo = livro.getTitulo();
		this.autor = livro.getAutor();
		this.idioma = livro.getIdioma();
		this.editora = livro.getEditora();
		this.dataPublicacao = livro.getDataPublicacao();
		this.nomeCategoria = livro.getCategoria().getNome();
		this.reservado = livro.estaReservado();
		
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

	public String getIdioma() {
		return idioma;
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
