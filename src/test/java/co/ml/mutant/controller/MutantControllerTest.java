package co.ml.mutant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import co.ml.mutant.model.MutantRequest;
import co.ml.mutant.model.MutantStats;
import co.ml.mutant.service.MutantPersistenceService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MutantControllerTest {
	
	@Mock
	MutantPersistenceService mutantPersistenceService;
	
	@Autowired
	private MutantController mutantController;
	
	@InjectMocks
	private MutantController mutantControllerInjected;
	
	private MutantRequest mutantRequest;
	
	String[] mutantDNA = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
	
	String[] notMutantDNA = {"ATGCTA", "CAGTGC", "TTATGT", "AGAAGG", "ACCCTA", "TCACTG"};
	
	@Before
	public void setUp() {
		mutantRequest = new MutantRequest();
	}
	
	@Test
	public void isMutantOKTest() {
		mutantRequest.setDna(mutantDNA);
		assertEquals(mutantController.isMutant(mutantRequest).getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void isMutantForbiddenTest() {
		mutantRequest.setDna(notMutantDNA);
		assertEquals(mutantController.isMutant(mutantRequest).getStatusCode(), HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void getMutantStatsOkTest() {
		MutantStats expectedMutantStats = MutantStats.builder()
				.count_human_dna(100L)
				.count_mutant_dna(40L)
				.ratio(40L / 100L).build();
		Mockito.when(mutantPersistenceService.getMutantStats()).thenReturn(expectedMutantStats);
		MutantStats mutantStatsObtained = mutantControllerInjected.getMutantStats().getBody();
		assertEquals(expectedMutantStats.getCount_human_dna(), mutantStatsObtained.getCount_human_dna());
		assertEquals(expectedMutantStats.getCount_mutant_dna(), mutantStatsObtained.getCount_mutant_dna());
		assertEquals(expectedMutantStats.getRatio(), mutantStatsObtained.getRatio());
	}
	
	@Test
	public void getMutantStatsServerErrorTest() {
		Mockito.when(mutantPersistenceService.getMutantStats()).thenThrow(QueryTimeoutException.class);
		ResponseEntity<MutantStats> response  = mutantControllerInjected.getMutantStats();
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

}
