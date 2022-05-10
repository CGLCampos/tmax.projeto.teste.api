package br.com.tmax.projeto.teste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tmax.projeto.teste.api.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long>{

	Perfil findByNome(String string);

}
