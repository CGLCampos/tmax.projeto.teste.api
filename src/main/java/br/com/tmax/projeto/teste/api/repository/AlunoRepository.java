package br.com.tmax.projeto.teste.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tmax.projeto.teste.api.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long>{

	Optional<Aluno> findByEmail(String email);

}
