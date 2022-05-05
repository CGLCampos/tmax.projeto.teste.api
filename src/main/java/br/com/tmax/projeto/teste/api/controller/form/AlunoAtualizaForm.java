package br.com.tmax.projeto.teste.api.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.util.DateUtil;

public class AlunoAtualizaForm {

	@NotEmpty
	@NotNull
	private String nome;

	@NotEmpty
	@NotNull
	@Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}")
	private String dataNascimento;

	@NotEmpty
	@NotNull
	@Pattern(regexp = "\\d{1}-[A-Z]{1}$")
	private String turma;

	public String getNome() {
		return nome;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public String getTurma() {
		return turma;
	}

	public Aluno converter(Aluno aluno) {
		aluno.setNome(this.nome);
		aluno.setDataNascimento(DateUtil.stringToLocalDate(this.dataNascimento));
		aluno.setTurma(this.turma);
		return aluno;
	}

}
