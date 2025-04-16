package com.peliculasapi;

import com.peliculasapi.ServiceDTO.FichaTecnicaDetalladaServiceDTO;
import com.peliculasapi.ServiceDTO.PeliculaPublicaServiceDTO;
import com.peliculasapi.ServiceDTO.impl.FichaTecnicaDetalladaServiceDTOImpl;
import com.peliculasapi.ServiceDTO.impl.PeliculaPublicaServiceDTOImpl;
import com.peliculasapi.repository.PeliculaActorRepository;
import com.peliculasapi.repository.PeliculaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final PeliculaRepository peliculaRepository;
    private final PeliculaActorRepository peliculaActorRepository;

    public AppConfig(PeliculaRepository peliculaRepository, PeliculaActorRepository peliculaActorRepository) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaActorRepository = peliculaActorRepository;
    }

    @Bean
    public PeliculaPublicaServiceDTO peliculaPublicaServiceDTO() {
        return PeliculaPublicaServiceDTOImpl.getInstance(peliculaRepository);
    }

    @Bean
    public FichaTecnicaDetalladaServiceDTO fichaTecnicaDetalladaServiceDTO() {
        return FichaTecnicaDetalladaServiceDTOImpl.getInstance(peliculaRepository, peliculaActorRepository);
    }
}