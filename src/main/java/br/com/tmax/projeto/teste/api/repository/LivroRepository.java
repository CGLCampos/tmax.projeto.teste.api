package br.com.tmax.projeto.teste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tmax.projeto.teste.api.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {

}
