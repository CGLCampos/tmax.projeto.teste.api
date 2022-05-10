package br.com.tmax.projeto.teste.api.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.model.Perfil;
import br.com.tmax.projeto.teste.api.util.DateUtil;

public class AlunoCadastroForm {

	@NotEmpty 
	@NotNull
	private String nome;
	
	@NotEmpty 
	@NotNull 
	@Pattern(regexp="\\d{2}/\\d{2}/\\d{4}")
	private String dataNascimento;
	
	@NotEmpty
	@NotNull 
	@Pattern(regexp="\\d{1}-[A-Z]{1}$")
	private String turma;
	
	@NotEmpty 
	@NotNull
    @Email(regexp=".+@.+\\..+", message="Por favor, forneça um endereço de e-mail válido")
	private String email;
	
	@NotEmpty 
	@NotNull 
	@Size(min=6)
	private String senha;
	
	public String getNome() {
		return nome;
	}
	public String getDataNascimento() {
		return dataNascimento;
	}
	public String getTurma() {
		return turma;
	}
	public String getEmail() {
		return email;
	}
	public String getSenha() {
		return senha;
	}
	public Aluno converter(Perfil perfil) {
		Aluno aluno = new Aluno();
		aluno.setNome(this.nome);
		aluno.setDataNascimento(DateUtil.stringToLocalDate(this.dataNascimento));
		aluno.setTurma(this.turma);
		aluno.setEmail(this.email);
		aluno.setSenha(new BCryptPasswordEncoder().encode(this.senha));
		aluno.getPerfis().add(perfil);
		return aluno;
	}
	
	
}
