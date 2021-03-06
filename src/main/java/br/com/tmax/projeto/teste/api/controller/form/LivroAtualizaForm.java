package br.com.tmax.projeto.teste.api.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.tmax.projeto.teste.api.model.Categoria;
import br.com.tmax.projeto.teste.api.model.Livro;

public class LivroAtualizaForm {

	@NotEmpty
	private String titulo;

	@NotEmpty
	private String autor;

	@NotEmpty
	private String idioma;

	@NotEmpty
	private String editora;

	@NotEmpty
	private String dataPublicacao;
	
	@NotNull
	private Long idCategoria;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public String getEditora() {
		return editora;
	}

	public void setEditora(String editora) {
		this.editora = editora;
	}

	public String getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public Long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Livro converter(Livro livro, Categoria categoria) {

		livro.setTitulo(this.titulo);
		livro.setAutor(this.autor);
		livro.setIdioma(this.idioma);
		livro.setEditora(this.editora);
		livro.setDataPublicacao(this.dataPublicacao);
		livro.setCategoria(categoria);

		return livro;
	}

}
