package com.example.unitarias.Service;

import com.example.unitarias.Model.Mascota;
import com.example.unitarias.Repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /* Test para guardar mascota en la capa servicio */
    @Test
    void testGuardarMascota() {
        Mascota mascota = new Mascota(null, "Rex", "Perro", 5);
        Mascota mascotaGuardada = new Mascota(1L, "Rex", "Perro", 5);
        when(mascotaRepository.save(mascota)).thenReturn(mascotaGuardada);

        Mascota resultado = mascotaService.guardarMascota(mascota);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(mascotaRepository).save(mascota);
    }

    @Test
    void testListarMascotas() {
        Mascota m1 = new Mascota(1L, "Rex", "Perro", 5);
        Mascota m2 = new Mascota(2L, "Michi", "Gato", 2);
        when(mascotaRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Mascota> resultado = mascotaService.listarMascotas();
        assertThat(resultado).hasSize(2).contains(m1, m2);
        verify(mascotaRepository).findAll();
    }

    @Test
    void testEliminarMascota() {
        Long idMascota = 1L;
        
        // Configurar el mock para que no haga nada cuando se llame a deleteById
        doNothing().when(mascotaRepository).deleteById(idMascota);
        
        // Ejecutar el método a probar
        mascotaService.eliminarMascota(idMascota);
        
        // Verificar que se llamó al método del repositorio con el ID correcto
        verify(mascotaRepository).deleteById(idMascota);
    }

    @Test
    void testObtenerMascotaxId() {
        // 1. Preparación (Arrange)
        Long idExistente = 1L;
        Mascota mascotaSimulada = new Mascota(idExistente, "Rex", "Perro", 5);
        
        // Configurar el mock para devolver un Optional con la mascota
        when(mascotaRepository.findById(idExistente))
            .thenReturn(Optional.of(mascotaSimulada)); // ✅ Devuelve Optional<Mascota>
    
        // 2. Ejecución (Act)
        Optional<Mascota> resultado = mascotaService.obtenerMascotaPorId(idExistente); // ✅ Recibe Optional
    
        // 3. Verificación (Assert)
        assertThat(resultado)
            .isPresent() // Verifica que el Optional no está vacío
            .hasValueSatisfying(mascota -> {
                assertThat(mascota.getId()).isEqualTo(idExistente);
                assertThat(mascota.getNombre()).isEqualTo("Rex");
                assertThat(mascota.getTipo()).isEqualTo("Perro");
                assertThat(mascota.getEdad()).isEqualTo(5);
            });
    
        verify(mascotaRepository).findById(idExistente);
    }
}