package br.com.tmax.projeto.teste.api.controller.dto;

import org.springframework.data.domain.Page;

import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.util.DateUtil;

public class AlunoDTO {

	private Long id;
	private String nome;
	private String dataNascimento;
	private Integer idade;
	private String turma;
	private String email;

	public AlunoDTO(Aluno aluno) {
		this.id = aluno.getId();
		this.nome = aluno.getNome();
		this.dataNascimento = DateUtil.localDateToString(aluno.getDataNascimento());
		this.idade = DateUtil.idade(aluno.getDataNascimento());
		this.turma = aluno.getTurma();
		this.email = aluno.getEmail();
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}
	
	public Integer getIdade() {
		return idade;
	}

	public String getTurma() {
		return turma;
	}

	public String getEmail() {
		return email;
	}

	public static Page<AlunoDTO> converter(Page<Aluno> alunos) {
		return alunos.map(AlunoDTO::new);
	}

}
