package br.com.cesarcastro.votacao.scheduling;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.repositories.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AtualizadorPautasSchedulerTest {
    @Mock
    PautaRepository pautaRepository;
    @InjectMocks
    AtualizadorPautasScheduler atualizadorPautasScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAtualizaAberturaDePautas() {
        when(pautaRepository.findByPautaAbertaFalseAndDataHoraInicioBetween(any(LocalDateTime.class)))
                .thenReturn(List.of(PautaEntity.builder()
                        .id(1L)
                        .pautaAberta(false)
                        .dataHoraInicio(LocalDateTime.now().minusMinutes(1))
                        .build()));
        when(pautaRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(pautaRepository).flush();
        atualizadorPautasScheduler.atualizaAberturaDePautas();
        verify(pautaRepository, times(1))
                .findByPautaAbertaFalseAndDataHoraInicioBetween(any(LocalDateTime.class));
        verify(pautaRepository).saveAll(anyList());
        verify(pautaRepository).flush();
    }

    @Test
    void testAtualizaEncerramentoDePautas() {
        when(pautaRepository.findByPautaAbertaTrueAndDataHoraFimBefore(any(LocalDateTime.class)))
                .thenReturn(List.of(PautaEntity.builder()
                        .id(1L)
                        .pautaAberta(true)
                        .dataHoraInicio(LocalDateTime.now().minusSeconds(1))
                        .build()));
        when(pautaRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(pautaRepository).flush();
        atualizadorPautasScheduler.atualizaEncerramentoDePautas();
        verify(pautaRepository, times(1))
                .findByPautaAbertaTrueAndDataHoraFimBefore(any(LocalDateTime.class));
        verify(pautaRepository).saveAll(anyList());
        verify(pautaRepository).flush();
    }
}