package br.com.tmax.projeto.teste.api.controller.dto;

public class MensagemSucessoDTO {
	private String mensagem;

	public MensagemSucessoDTO(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}
}
