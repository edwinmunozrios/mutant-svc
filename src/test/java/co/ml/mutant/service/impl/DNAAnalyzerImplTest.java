package co.ml.mutant.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DNAAnalyzerImplTest {
	
	@Autowired
	private DNAAnalyzerImpl dnaAnalizer;
	
	char[][] inputMatrix = {{'A', 'C', 'T'}, 
						    {'T', 'A', 'C'}, 
						    {'T', 'C', 'A'}};
	
	String[] mutantDNA = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
	
	String[] notMutantDNA = {"ATGCTA", "CAGTGC", "TTATGT", "AGAAGG", "ACCCTA", "TCACTG"};
	
	@Before
	public void setUp() {
		dnaAnalizer.getDnaMatrix().setDna(null);
		dnaAnalizer.getDnaMatrix().setMutant(false);
		dnaAnalizer.getDnaMatrix().setRows(new ArrayList<String>());
		dnaAnalizer.getDnaMatrix().setColumns(new ArrayList<String>());
		dnaAnalizer.getDnaMatrix().setLeftToRighDiagonals(new ArrayList<String>());
		dnaAnalizer.getDnaMatrix().setRightToLeftDiagonals(new ArrayList<String>());
	}
	
	@Test
	public void getMutantSequenceEmptyDnaTest() {
		int result = dnaAnalizer.getMutantSequence("");
		assertEquals(0, result);
	}
	
	@Test
	public void getMutantSequenceShortDnaTest() {
		int result = dnaAnalizer.getMutantSequence("ACT");
		assertEquals(0, result);
	}
	
	@Test
	public void getMutantSequenceExactMatchDnaTest() {
		int result = dnaAnalizer.getMutantSequence("AAAA");
		assertEquals(1, result);
	}
	
	@Test
	public void getMutantSequenceMultipleMatchDnaTest() {
		int result = dnaAnalizer.getMutantSequence("CTAAAATCAAAATTCC");
		assertEquals(2, result);
	}
	
	@Test
	public void getMutantSequenceMultipleSubsequentMatchDnaTest() {
		int result = dnaAnalizer.getMutantSequence("CTAAAAAAAATC");
		assertEquals(2, result);
	}
	
	@Test
	public void getMutantSequenceOverlapedMatchDnaTest() {
		int result = dnaAnalizer.getMutantSequence("CTAAAAAACTTTCC");
		assertEquals(1, result);
	}
	
	@Test
	public void getMutantSequenceTwoDifferentBaseTest() {
		int result = dnaAnalizer.getMutantSequence("CTAAAACTTTTCC");
		assertEquals(2, result);
	}
	
	@Test
	public void getMutantSequenceTwoDifferentBaseExactTest() {
		int result = dnaAnalizer.getMutantSequence("AAAATTTT");
		assertEquals(2, result);
	}
	
	@Test
	public void getMutantSequenceThreeDifferentBaseExactTest() {
		int result = dnaAnalizer.getMutantSequence("AAAATTTTCCCC");
		assertEquals(3, result);
	}
	
	@Test
	public void getMutantSequenceTwoDifferentBaseOverlapedTest() {
		int result = dnaAnalizer.getMutantSequence("CAAAAATTTTTTCCC");
		assertEquals(2, result);
	}
	
	@Test
	public void getMatrixfromArrayBasicTest() {
		char expectedMatrix[][] = inputMatrix;
		char result [][] = dnaAnalizer.getMatrixfromArray(new String[]{"ACT", "TAC", "TCA"});
		assertTrue(Arrays.equals(expectedMatrix[0], result[0]));
		
	}
	@Test
	public void getMatrixfromArrayEmptyTest() {
		char expectedMatrix [][] = new char[0][0];
		char result [][] = dnaAnalizer.getMatrixfromArray(new String[]{});
		assertTrue(Arrays.equals(expectedMatrix, result));
		
	}
	
	@Test
	public void getColumnsAsStringsTest() {
		List<String> expectedList = Arrays.asList("ATT", "CAC", "TCA");
		dnaAnalizer.getColumnsAsStrings(inputMatrix);
		assertTrue(expectedList.equals(dnaAnalizer.getDnaMatrix().getColumns()));
	}
	
	@Test
	public void getColumnsAsStringsEmptyMatrixTest() {
		char inputMatrix [][] = new char[0][0];
		List<String> expectedList = new ArrayList<String>();
		dnaAnalizer.getColumnsAsStrings(inputMatrix);
		assertTrue(expectedList.equals(dnaAnalizer.getDnaMatrix().getColumns()));
	}
	
	@Test
	public void getColumnsAsStringsOneItemMatrixTest() {
		char inputMatrix [][] = {{'A'}};
		List<String> expectedList = Arrays.asList("A");
		dnaAnalizer.getColumnsAsStrings(inputMatrix);
		assertTrue(expectedList.equals(dnaAnalizer.getDnaMatrix().getColumns()));
	}
	
	@Test
	public void getDiagonalsAsStringsTest() {
		List<String> expectedRightToLeftDiagsList = Arrays.asList("A", "CT", "TAT", "CC", "A");
		List<String> expectedLeftToRightDiagsList = Arrays.asList("T", "CC", "AAA", "TC", "T");
		dnaAnalizer.getDiagonalsAsStrings(inputMatrix);
		assertTrue(expectedRightToLeftDiagsList.equals(dnaAnalizer.getDnaMatrix().getRightToLeftDiagonals()));
		assertTrue(expectedLeftToRightDiagsList.equals(dnaAnalizer.getDnaMatrix().getLeftToRighDiagonals()));
	}
	
	@Test
	public void getDiagonalsAsStringsEmptyMatrixTest() {
		char inputMatrix [][] = new char[0][0];
		List<String> expectedRightToLeftDiagsList = new ArrayList<String>();
		List<String> expectedLeftToRightDiagsList = new ArrayList<String>();
		dnaAnalizer.getDiagonalsAsStrings(inputMatrix);
		assertTrue(expectedRightToLeftDiagsList.equals(dnaAnalizer.getDnaMatrix().getRightToLeftDiagonals()));
		assertTrue(expectedLeftToRightDiagsList.equals(dnaAnalizer.getDnaMatrix().getLeftToRighDiagonals()));
	}
	
	@Test
	public void getDiagonalsAsStringsOneItemMatrixTest() {
		char inputMatrix [][] = {{'A'}};
		List<String> expectedRightToLeftDiagsList = Arrays.asList("A");
		List<String> expectedLeftToRightDiagsList = Arrays.asList("A");
		dnaAnalizer.getDiagonalsAsStrings(inputMatrix);
		assertTrue(expectedRightToLeftDiagsList.equals(dnaAnalizer.getDnaMatrix().getRightToLeftDiagonals()));
		assertTrue(expectedLeftToRightDiagsList.equals(dnaAnalizer.getDnaMatrix().getLeftToRighDiagonals()));
	}
	
	@Test
	public void analyzeDnaRowCompliantTest() {
		String[] mutantDNARowCompliant = {"ATGCGA",
										  "CTGTTC",
										  "TTTTGT",
										  "AGAAGG",
										  "CCCCTA",
										  "TCACTG"};
		dnaAnalizer.analyzeDna(mutantDNARowCompliant);
		assertTrue(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void analyzeDnaColCompliantTest() {
		String[] mutantDNAColCompliant = {"ATGCGA",
										  "CAGTGC",
										  "TTATGT",
										  "AGAAGG",
										  "CCCCTA",
										  "TCACTG"};
		dnaAnalizer.analyzeDna(mutantDNAColCompliant);
		assertTrue(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void analyzeDnaLtoRDiagCompliantTest() {
		String[] mutantDNALtoRDiagCompliant = {"ATGCGA",
											   "CAGTGC",
											   "TTATGT",
											   "AGAAGG",
											   "CCCATA",
											   "TCACTG"};
		dnaAnalizer.analyzeDna(mutantDNALtoRDiagCompliant);
		assertTrue(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void analyzeDnaRtoLDiagCompliantTest() {
		String[] mutantDNALtoRDiagCompliant = {"ATGCCA",
											   "CAGTAC",
											   "TTGAGT",
											   "ATAGGG",
											   "CCCAGA",
											   "TCACTG"};
		dnaAnalizer.analyzeDna(mutantDNALtoRDiagCompliant);
		assertTrue(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void analyzeDnaNoComplianceCompliantTest() {
		String[] mutantDNANoCompliant = {"ATGCCA",
										 "CAGTGC",
										 "TTGTGT",
										 "AGGGCG",
										 "CCCAGA",
										 "TCACTG"};
		dnaAnalizer.analyzeDna(mutantDNANoCompliant);
		assertTrue(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void analyzeDnaEmptyMatrixTest() {
		String[] mutantDNALtoRDiagCompliant = {};
		dnaAnalizer.analyzeDna(mutantDNALtoRDiagCompliant);
		assertFalse(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void analyzeDnaOneItemMatrixTest() {
		String[] mutantDNALtoRDiagCompliant = {"A"};
		dnaAnalizer.analyzeDna(mutantDNALtoRDiagCompliant);
		assertFalse(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void analyzeDnaNullMatrixTest() {
		String[] mutantDNALtoRDiagCompliant = null;
		dnaAnalizer.analyzeDna(mutantDNALtoRDiagCompliant);
		assertFalse(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void sumMutantSequencesNullListTest() {
		assertEquals(dnaAnalizer.sumMutantSequences(null), 0);
	}
	
	@Test
	public void sumMutantSequencesEmtpyListTest() {
		assertEquals(dnaAnalizer.sumMutantSequences(new ArrayList<String>()), 0);
	}
	
	@Test
	public void sumMutantSequencesMutantListTest() {
		List<String> stringList = Arrays.asList(mutantDNA);
		assertTrue(dnaAnalizer.sumMutantSequences(stringList) > 0);
	}
	
	@Test
	public void sumMutantSequencesNotMutantListTest() {
		List<String> stringList = Arrays.asList(notMutantDNA);
		assertEquals(dnaAnalizer.sumMutantSequences(stringList), 0);
	}
	
	@Test
	public void isMutantCompliantZeroSequenceTest() {
		assertFalse(dnaAnalizer.isMutantCompliant(0));
		assertFalse(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void isMutantCompliantOneSequenceTest() {
		assertFalse(dnaAnalizer.isMutantCompliant(1));
		assertFalse(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void isMutantCompliantMoreThanOneSequenceTest() {
		assertTrue(dnaAnalizer.isMutantCompliant(2));
		assertTrue(dnaAnalizer.getDnaMatrix().isMutant());
	}
	
	@Test
	public void isValidMatrixDnaNullTest() {
		assertFalse(dnaAnalizer.isValidMatrix(null));
	}
	
	@Test
	public void isValidMatrixSquareButGreaterThanMaxLengthTest() {
		int lengthGreaterThanMaxLength = 1001;
		String[] dna = new String[lengthGreaterThanMaxLength];
		DNAAnalyzerImpl dnaAnalyzerSpy = Mockito.spy(dnaAnalizer);
		doReturn(true).when(dnaAnalyzerSpy).isSquareMatrix(dna, lengthGreaterThanMaxLength);
		assertFalse(dnaAnalyzerSpy.isValidMatrix(dna));
	}
	
	@Test
	public void isValidMatrixSquareAndLessThanMaxLengthTest() {
		String[] dna = new String[10];
		DNAAnalyzerImpl dnaAnalyzerSpy = Mockito.spy(dnaAnalizer);
		doReturn(true).when(dnaAnalyzerSpy).isSquareMatrix(dna, 10);
		assertTrue(dnaAnalyzerSpy.isValidMatrix(dna));
	}
	
	@Test
	public void isValidMatrixNotSquareAndLessThanMaxLengthTest() {
		String[] dna = new String[10];
		DNAAnalyzerImpl dnaAnalyzerSpy = Mockito.spy(dnaAnalizer);
		doReturn(false).when(dnaAnalyzerSpy).isSquareMatrix(dna, 10);
		assertFalse(dnaAnalyzerSpy.isValidMatrix(dna));
	}
	
	@Test
	public void isSquareMatrixTrueTest() {
		assertTrue(dnaAnalizer.isSquareMatrix(mutantDNA, mutantDNA.length));
	}
	
	@Test
	public void isSquareMatrixFalseTest() {
		String[] notSquareMutantDNA = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA"};
		assertFalse(dnaAnalizer.isSquareMatrix(notSquareMutantDNA, notSquareMutantDNA.length));
	}
	
	@Test
	public void isMutantValidMatrixNotMutantTest() {
		assertFalse(dnaAnalizer.isMutant(notMutantDNA));
	}
	
	@Test
	public void isMutantValidMatrixMutantTest() {
		assertTrue(dnaAnalizer.isMutant(mutantDNA));
	}
	
	@Test
	public void isMutantNotValidMatrixNotMutantTest() {
		DNAAnalyzerImpl dnaAnalyzerSpy = Mockito.spy(dnaAnalizer);
		doReturn(false).when(dnaAnalyzerSpy).isValidMatrix(notMutantDNA);
		assertFalse(dnaAnalizer.isMutant(notMutantDNA));
	}
	
	@Test
	public void isMutantNotValidMatrixMutantTest() {
		DNAAnalyzerImpl dnaAnalyzerSpy = Mockito.spy(dnaAnalizer);
		doReturn(false).when(dnaAnalyzerSpy).isValidMatrix(notMutantDNA);
		assertFalse(dnaAnalizer.isMutant(notMutantDNA));
	}
}
