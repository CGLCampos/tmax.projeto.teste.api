package br.com.tmax.projeto.teste.api.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.tmax.projeto.teste.api.controller.CategoriaRepository;
import br.com.tmax.projeto.teste.api.model.Categoria;
import br.com.tmax.projeto.teste.api.model.Livro;

public class LivroCadastroForm {

	@NotNull @NotEmpty
	private String titulo;
	private String autor = "Não informado";
	private String edicao = "Não informado";
	private String editora = "Não informado";
	private String dataPublicacao = "Não informado";
	@NotNull @NotEmpty
	private String nomeCategoria;

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

	public String getEdicao() {
		return edicao;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
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

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public Livro converter(CategoriaRepository categoriaRepository) {
		Categoria categoria = categoriaRepository.findByNome(nomeCategoria);
		Livro livro = new Livro();
		livro.setTitulo(this.titulo);
		livro.setAutor(this.autor);
		livro.setEdicao(this.edicao);
		livro.setEditora(this.editora);
		livro.setDataPublicacao(this.dataPublicacao);
		livro.setCategoria(categoria);
		
		return livro;
	}
}
