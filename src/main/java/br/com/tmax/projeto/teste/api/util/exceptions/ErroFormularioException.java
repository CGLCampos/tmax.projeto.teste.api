package br.com.tmax.projeto.teste.api.util.exceptions;

import br.com.tmax.projeto.teste.api.config.validation.ErroFormularioDTO;

public class ErroFormularioException extends Exception {
	private static final long serialVersionUID = 1L;

	private ErroFormularioDTO erroDTO;
	
	public ErroFormularioException(String campo, String erro) {
		super(erro);
		this.erroDTO = new ErroFormularioDTO(campo, erro);
	}
	
	public ErroFormularioDTO getErroDTO() {
		return erroDTO;
	}
	
	public void getCampo() {
		this.erroDTO.getCampo();
	}
	
}
