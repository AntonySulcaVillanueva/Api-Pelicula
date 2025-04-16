package com.peliculasapi.controllerDTO;

import com.peliculasapi.ServiceDTO.FichaTecnicaDetalladaServiceDTO;
import com.peliculasapi.dto.FichaTecnicaDetalladaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cinemax/fichas-tecnicas")
public class FichaTecnicaDetalladaControllerDTO {

    private final FichaTecnicaDetalladaServiceDTO fichaTecnicaDetalladaServiceDTO;

    // Inyección de dependencia mediante constructor
    public FichaTecnicaDetalladaControllerDTO(FichaTecnicaDetalladaServiceDTO fichaTecnicaDetalladaServiceDTO) {
        this.fichaTecnicaDetalladaServiceDTO = fichaTecnicaDetalladaServiceDTO;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichaTecnicaDetalladaDTO> getFichaTecnicaByPeliculaId(@PathVariable Long id) {
        FichaTecnicaDetalladaDTO fichaTecnica = fichaTecnicaDetalladaServiceDTO.getFichaTecnicaByPeliculaId(id);
        if (fichaTecnica != null) {
            return ResponseEntity.ok(fichaTecnica);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}