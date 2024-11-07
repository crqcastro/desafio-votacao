package br.com.cesarcastro.votacao.domain.service.pauta;

import br.com.cesarcastro.votacao.domain.model.entities.OpcaoEntity;
import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.repositories.PautaRepository;
import br.com.cesarcastro.votacao.mappers.PautaMapper;
import br.com.cesarcastro.votacao.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PautaServiceTest {
    @Mock
    private PautaRepository pautaRepository;
    private PautaMapper mapper;
    private PautaService pautaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mapper = PautaMapper.INSTANCE;
        this.pautaService = new PautaService(pautaRepository, mapper);
    }

    @Test
    public void testCadastrarPauta() {
        PautaRequest pautaRequest = TestUtils.generateRandom(PautaRequest.class, 2);
        PautaEntity pautaEntity = this.mapper.toPautaEntity(pautaRequest);
        pautaEntity.setId(1L);
        pautaEntity.setOpcoes(this.mapper.toOpcaoEntityList(pautaRequest.getOpcoes()));
        int i = 1;
        for(OpcaoEntity opcaoEntity : pautaEntity.getOpcoes()) {
            opcaoEntity.setId((long) i);
            i++;
        }

        when(pautaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        PautaResponse pautaResponse = this.pautaService.cadastrarPauta(pautaRequest);

        verify(pautaRepository).save(any());
        assertEquals(pautaEntity.getOpcoes().size(), pautaResponse.opcoes().size());
    }
}