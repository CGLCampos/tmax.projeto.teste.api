package br.com.tmax.projeto.teste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tmax.projeto.teste.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	
}
