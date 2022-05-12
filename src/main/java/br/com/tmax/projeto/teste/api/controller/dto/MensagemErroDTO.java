package br.com.tmax.projeto.teste.api.controller.dto;

public class MensagemErroDTO {
	private String erro;

	public MensagemErroDTO(String erro) {
		this.erro = erro;
	}

	public String getErro() {
		return erro;
	}

}
