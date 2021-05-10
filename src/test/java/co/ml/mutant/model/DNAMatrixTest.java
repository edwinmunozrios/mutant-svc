package co.ml.mutant.model;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class DNAMatrixTest {
	
	DNAMatrix dnaMatrix;
	
	@Test
	public void DNAMatrixNoArgsTest() {
		dnaMatrix = new DNAMatrix();
		assertFalse(dnaMatrix.isMutant());
		assertEquals(new ArrayList<String>(), dnaMatrix.getColumns());
		assertEquals(new ArrayList<String>(), dnaMatrix.getRows());
		assertEquals(new ArrayList<String>(), dnaMatrix.getLeftToRighDiagonals());
		assertEquals(new ArrayList<String>(), dnaMatrix.getRightToLeftDiagonals());
	}
	
	@Test
	public void DNAMatrixStringDnaArgsTest() {
		String[] dna = {"ATT", "CAC", "TCA"};
		dnaMatrix = new DNAMatrix(dna);
		assertFalse(dnaMatrix.isMutant());
		assertEquals(dna, dnaMatrix.getDna());
		assertEquals(Arrays.toString(dna), dnaMatrix.getDnaString());
		assertEquals(Arrays.asList(dna), dnaMatrix.getRows());
		assertEquals(new ArrayList<String>(), dnaMatrix.getColumns());
		assertEquals(new ArrayList<String>(), dnaMatrix.getLeftToRighDiagonals());
		assertEquals(new ArrayList<String>(), dnaMatrix.getRightToLeftDiagonals());
	}


}
