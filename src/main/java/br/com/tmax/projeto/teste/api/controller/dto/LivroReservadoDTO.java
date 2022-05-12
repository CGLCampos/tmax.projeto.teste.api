package br.com.tmax.projeto.teste.api.controller.dto;

import br.com.tmax.projeto.teste.api.model.Livro;
import br.com.tmax.projeto.teste.api.model.LivroReservado;
import br.com.tmax.projeto.teste.api.util.DateUtil;

public class LivroReservadoDTO {

	private Long id;
	private String titulo;
	private String autor;
	private String editora;
	private String dataDevolucao = "Em aberto";

	public LivroReservadoDTO(LivroReservado livroReservado) {
		Livro livro = livroReservado.getLivro();
		this.id = livro.getId();
		this.titulo = livro.getTitulo();
		this.autor = livro.getAutor();
		this.editora = livro.getEditora();
		if(livroReservado.getDataDevolucao() != null) {
			this.dataDevolucao = DateUtil.localDateToString(livroReservado.getDataDevolucao().toLocalDate());
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

	public String getEditora() {
		return editora;
	}

	public String getDataDevolucao() {
		return dataDevolucao;
	}

	

}
