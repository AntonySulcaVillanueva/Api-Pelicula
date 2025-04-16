package com.peliculasapi.ServiceDTO;

import com.peliculasapi.dto.FichaTecnicaDetalladaDTO;

public interface FichaTecnicaDetalladaServiceDTO {
    FichaTecnicaDetalladaDTO getFichaTecnicaByPeliculaId(Long id);
}