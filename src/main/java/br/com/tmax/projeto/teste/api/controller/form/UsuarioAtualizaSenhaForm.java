package br.com.tmax.projeto.teste.api.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.tmax.projeto.teste.api.model.Usuario;

public class UsuarioAtualizaSenhaForm {

	@NotEmpty
	@NotNull
	private String senhaAnterior;

	@NotEmpty
	@NotNull
	private String novaSenha;

	public String getSenhaAnterior() {
		return senhaAnterior;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public Usuario converter(Usuario usuario) {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		if(bCrypt.matches(this.senhaAnterior, usuario.getSenha())) {
			usuario.setSenha(bCrypt.encode(this.novaSenha));
		}
		return usuario;
	}

}
