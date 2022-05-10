package br.com.tmax.projeto.teste.api.controller.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.tmax.projeto.teste.api.model.Perfil;
import br.com.tmax.projeto.teste.api.model.Usuario;

public class UsuarioDTO {

	private Long id;
	private String email;
	private List<String> perfis = new ArrayList<>();

	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.email = usuario.getEmail();
		for(Perfil perfil : usuario.getPerfis()) {
			perfis.add(perfil.getNome());
		}
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public List<String> getPerfis() {
		return perfis;
	}
	
	

}
