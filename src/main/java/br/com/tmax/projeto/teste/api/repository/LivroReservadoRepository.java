package br.com.tmax.projeto.teste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tmax.projeto.teste.api.model.LivroReservado;

public interface LivroReservadoRepository extends JpaRepository<LivroReservado, Long> {

}
