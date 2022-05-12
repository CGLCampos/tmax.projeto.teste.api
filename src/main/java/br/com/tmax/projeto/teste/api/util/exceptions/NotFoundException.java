package br.com.tmax.projeto.teste.api.util.exceptions;

import br.com.tmax.projeto.teste.api.config.validation.ErroFormularioDTO;

public class NotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	
	private ErroFormularioDTO erroDTO;
	
	public NotFoundException(String message) {
		super(message);
	}
	
	public NotFoundException(String campo, String erro) {
		super(erro);
		this.erroDTO = new ErroFormularioDTO(campo, erro);
	}

	public ErroFormularioDTO getErroDTO() {
		return erroDTO;
	}

	public String getCampo() {
		return erroDTO.getCampo();
	}
}
