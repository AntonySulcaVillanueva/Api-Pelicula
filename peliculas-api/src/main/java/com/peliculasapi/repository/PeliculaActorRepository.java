package com.peliculasapi.repository;

import com.peliculasapi.entity.PeliculaActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeliculaActorRepository extends JpaRepository<PeliculaActor, Long> {
    List<PeliculaActor> findByPeliculaId(Long peliculaId);
}
