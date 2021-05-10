package co.ml.mutant.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import co.ml.mutant.model.DNAMatrix;
import co.ml.mutant.model.MutantStats;
import co.ml.mutant.repository.DNAMatrixRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MutantPersistenceServiceImplTest {
	
	@Mock
	private DNAMatrixRepository dnaMatrixRepository;
	
	@InjectMocks
	MutantPersistenceServiceImpl mutantPersistenceServiceImpl;
	
	private DNAMatrix dnaMatrix;
	
	String[] mutantDNA = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
	
	@Before
	public void setUp() {
		this.dnaMatrix = new DNAMatrix(mutantDNA);
	}
	
	@Test
	public void saveDNAMatrixTest() {
		Mockito.when(dnaMatrixRepository.save(dnaMatrix)).thenReturn(dnaMatrix);
		mutantPersistenceServiceImpl.saveDNAMatrix(dnaMatrix);
		Mockito.verify(dnaMatrixRepository).save(dnaMatrix);
	}
	
	@Test
	public void saveDNAMatrixWithErrorTest(){
		Mockito.when(dnaMatrixRepository.save(dnaMatrix)).thenThrow(org.springframework.dao.DuplicateKeyException.class);
		mutantPersistenceServiceImpl.saveDNAMatrix(dnaMatrix);
		Mockito.verify(dnaMatrixRepository).save(dnaMatrix);
	}
	
	@Test
	public void getMutantStatsTest() {
		Mockito.when(dnaMatrixRepository.countByIsMutant(true)).thenReturn(40L);
		Mockito.when(dnaMatrixRepository.countByIsMutant(false)).thenReturn(100L);
		MutantStats mutantStats = mutantPersistenceServiceImpl.getMutantStats();
		assertEquals(mutantStats.getCount_human_dna(), 100L);
		assertEquals(mutantStats.getCount_mutant_dna(), 40L);
		assertEquals(mutantStats.getRatio(), 40L / 100L);
	}

}
