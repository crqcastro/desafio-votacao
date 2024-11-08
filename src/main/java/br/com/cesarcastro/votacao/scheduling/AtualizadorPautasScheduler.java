package br.com.cesarcastro.votacao.scheduling;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.repositories.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AtualizadorPautasScheduler {

    private final PautaRepository pautaRepository;


    @Scheduled(cron = "0 * * * * *")
    public void atualizaAberturaDePautas() {
        LocalDateTime dataHoraRefencia = LocalDateTime.now();
        log.info("INICIO - Atualizando abertura de pautas: {}", dataHoraRefencia);

        List<PautaEntity> pautas = pautaRepository.findByPautaAbertaFalseAndDataHoraInicioBetween(dataHoraRefencia);
        pautas.forEach(pauta -> {
            log.info("Abrindo pauta {}", pauta.getId());
            pauta.setPautaAberta(true);
            pautaRepository.save(pauta);
        });
        pautaRepository.saveAll(pautas);
        pautaRepository.flush();
        log.info("FIM - Atualizando abertura de pautas");
    }

    @Scheduled(cron = "0 * * * * *")
    public void atualizaEncerramentoDePautas() {
        LocalDateTime dataHoraRefencia = LocalDateTime.now();
        log.info("INICIO - Atualizando fechamento de pautas: {}", dataHoraRefencia);
        List<PautaEntity> pautas = pautaRepository.findByPautaAbertaTrueAndDataHoraFimBefore(dataHoraRefencia);
        pautas.forEach(pauta -> {
            log.info("Encerrando pauta {}", pauta.getId());
            pauta.setPautaAberta(false);
            pautaRepository.save(pauta);
        });
        pautaRepository.saveAll(pautas);
        pautaRepository.flush();
        log.info("FIM - Atualizando fechamento de pautas");
    }
}
