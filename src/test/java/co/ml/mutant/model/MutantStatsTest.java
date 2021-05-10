package co.ml.mutant.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MutantStatsTest {
	
	private MutantStats mutantStats;
	
	@Before
	public void setUp() {
		mutantStats = MutantStats.builder().count_mutant_dna(40).build();
	}
	
	@Test
	public void calculateRatioHumansGreaterThanZeroTest(){
		mutantStats.setCount_human_dna(100);
		assertEquals(mutantStats.calculateRatio(), (double) mutantStats.getCount_mutant_dna() / mutantStats.getCount_human_dna());
	}
	
	@Test
	public void calculateRatioHumansLessThanZeroTest(){
		mutantStats.setCount_human_dna(-1);
		assertEquals(mutantStats.calculateRatio(), 0);
	}
	
	@Test
	public void calculateRatioHumansZeroTest(){
		mutantStats.setCount_human_dna(0);
		assertEquals(mutantStats.calculateRatio(), 0);
	}

}
