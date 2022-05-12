package br.com.tmax.projeto.teste.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Aluno aluno;

	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
	private List<LivroReservado> livrosReservados = new ArrayList<>();
	
	private LocalDateTime dataReserva;

	private boolean finalizado;
	private LocalDateTime dataDevolucaoEfetiva;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataReserva() {
		return dataReserva;
	}

	public void setDataReserva(LocalDateTime dataReserva) {
		this.dataReserva = dataReserva;
	}

	public LocalDateTime getDataDevolucaoEfetiva() {
		return dataDevolucaoEfetiva;
	}

	public void setDataDevolucaoEfetiva(LocalDateTime dataDevolucaoEfetiva) {
		this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<LivroReservado> getLivrosReservados() {
		return livrosReservados;
	}

	public void setLivrosReservados(List<LivroReservado> livrosReservados) {
		this.livrosReservados = livrosReservados;
	}

	public boolean estaFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

}
