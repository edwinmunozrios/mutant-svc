package co.ml.mutant.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import co.ml.mutant.model.DNAMatrix;
import co.ml.mutant.service.DNAAnalyzer;
import co.ml.mutant.service.MutantPersistenceService;
import lombok.Getter;
import lombok.Setter;

/**
 * This class contains all methods/utilities needed for iterate and find mutand DNA strings
 * @author edwinmunozrios
 * @since 2021/05/02
 */
@Getter
@Setter
@Service
public class DNAAnalyzerImpl implements DNAAnalyzer{
	
	private DNAMatrix dnaMatrix;
	
	@Autowired
	private MutantPersistenceService mutantPersitanceService;
	
	@Value("${mutant.constants.sequence.limit}")
	private int MUTANT_SEQUENCE_LIMIT;
	
	@Value("${mutant.constants.nucleobase.expression}")
	public String MUTANT_NUCLEO_BASE_EXPR ;
	
	@Value("${mutant.constants.minimum.dnastring.length}")
	public int MINIMUM_DNA_STRING_LENGTH ;
	
	@Value("${mutant.constants.matrix.max.length}")
	public int MAX_DNA_MATRIX_LENGTH ;
	
	public DNAAnalyzerImpl() {
		this.dnaMatrix =  new DNAMatrix();
	}
	
	/**
	 * It gets the number of occurrences of String mutantString in the given dnaString.
	 * Since overlapping match is not allowed, a split function makes the work.
	 * @param dnaString a DNA string to be analyzed
	 * @return the number of occurrences of String mutantString in the given dnaString
	 */
	protected int getMutantSequence(String dnaString) {
		return dnaString.split(MUTANT_NUCLEO_BASE_EXPR, -1).length -1;
	}
	
	/**
	 * This method converts the DNA String array to a char matrix
	 * @param dna Array of Strings with DNA nucleobases
	 * @return a nxm char matrix for n element in dna array and m chars in the String
	 */
	protected char[][] getMatrixfromArray(String[] dna){
		char matrixFromArray[][] = new char[dna.length][dna.length];
		for(int i = 0; i<= dna.length -1; i++) {
			matrixFromArray[i] = dna[i].toCharArray();
		}
		return matrixFromArray;
	}
	
	/**
	 * This method extracts the columns from the DNA matrix as a list of strings and
	 * it assigns the result to the column attribute of the DNAMatrix
	 * @param matrix a char[][] representation of the source DNA Matrix
	 */
	protected void getColumnsAsStrings(char[][] matrix) {
		List<String> columnsAsString = this.dnaMatrix.getColumns();
		for(int i = 0; i < matrix.length; i++) {
			char[] columnAsChar = new char[matrix.length];
			for(int j = 0; j< matrix.length; j++) {
				columnAsChar[j]= matrix[j][i];
			}
			columnsAsString.add(String.valueOf(columnAsChar));
		}
		this.dnaMatrix.setColumns(columnsAsString);
	}
	
	/**
	 * This method extracts the diagonals from the DNA matrix as a list of strings and
	 * it assigns the results to the leftToRighDiagonals and rightToLeftDiagonals attributes of the DNAMatrix
	 * @param matrix a char[][] representation of the source DNA Matrix
	 */
	protected void getDiagonalsAsStrings(char matrix[][]){
		int rowLenght = matrix.length;
		int colLenght = matrix.length;
		List<String> leftToRightDiagAsString =  this.dnaMatrix.getLeftToRighDiagonals();
		List<String> rightToLeftDiagsAsString = this.dnaMatrix.getRightToLeftDiagonals();
		String diagAscAsString = "";
		String diagDescAsString = "";
		//Diagonals above main diagonal loops
	    for(int level = 0; level < colLenght; level++){
	        for(int rowIndx = 0, colIndx = level; rowIndx < rowLenght && colIndx >= 0 ; rowIndx++, colIndx--){
	        	diagAscAsString = diagAscAsString.concat(String.valueOf(matrix[rowIndx][rowLenght - 1 - colIndx]));
	            diagDescAsString = diagDescAsString.concat(String.valueOf(matrix[rowIndx][colIndx]));
	        }
	        leftToRightDiagAsString.add(diagAscAsString);
	        diagAscAsString = "";
	        rightToLeftDiagsAsString.add(diagDescAsString);
	        diagDescAsString = "";
	    }
	    
	    // Diagonals below main diagonal loops
	    for(int level = 1; level < rowLenght; level++){
	        for(int rowIndx = level, colIndx = colLenght-1; rowIndx < rowLenght && colIndx >= 0; rowIndx++, colIndx--){
	        	diagAscAsString = diagAscAsString.concat(String.valueOf(matrix[rowIndx][rowLenght - 1 - colIndx]));
	            diagDescAsString = diagDescAsString.concat(String.valueOf(matrix[rowIndx][colIndx]));
	        }
	        leftToRightDiagAsString.add(diagAscAsString);
	        diagAscAsString = "";
	        rightToLeftDiagsAsString.add(diagDescAsString);
	        diagDescAsString = "";
	    }
	    this.dnaMatrix.setLeftToRighDiagonals(leftToRightDiagAsString);
	    this.dnaMatrix.setRightToLeftDiagonals(rightToLeftDiagsAsString);
	}
	
	/**
	 * This method analyzes the given DNA Strings looking for more than <MUTANT_SEQUENCE_LIMIT>
	 * in the DNA Matrix rows, columns, and diagonals to determine whether the DNA is mutant or not.
	 * This method also persists the generated DNAMatrix in an async way so we don't need to wait until
	 * the object is persisted in the database
	 * @param dna Array of strings representing the rows of the DNA matrix
	 */
	protected void analyzeDna(String[] dna) {
		if(dna == null) {
			return;
		}
		this.dnaMatrix = new DNAMatrix(dna);
		mutantPersitanceService.saveDNAMatrix(dnaMatrix);
		char dnaMatrix[][] = getMatrixfromArray(dna);
		int mutantSequences = 0;
		mutantSequences += sumMutantSequences(this.getDnaMatrix().getRows());
		if(isMutantCompliant(mutantSequences)) {
			return;
		}
		getColumnsAsStrings(dnaMatrix);
		mutantSequences += sumMutantSequences(this.getDnaMatrix().getColumns());
		if(isMutantCompliant(mutantSequences)) {
			return;
		}
		getDiagonalsAsStrings(dnaMatrix);
		mutantSequences += sumMutantSequences(this.getDnaMatrix().getLeftToRighDiagonals());
		if(isMutantCompliant(mutantSequences)) {
			return;
		}
		mutantSequences += sumMutantSequences(this.getDnaMatrix().getRightToLeftDiagonals());
		if(isMutantCompliant(mutantSequences)) {
			return;
		}
	}
	
	/**
	 * This method returns the sum of occurrences of the pattern of the mutant sequence 
	 * <MUTANT_NUCLEO_BASE_EXPR> in a list of DNA Strings filtering by strings with length 
	 * greater or equals than <MINIMUM_DNA_STRING_LENGTH>
	 * @param list A list of Strings representing the DNA matrix rows, columns, or diagonals
	 * @return The sum of occurrences of the pattern of the mutant sequence 
	 * <MUTANT_NUCLEO_BASE_EXPR> in a list of DNA Strings filtering by strings with length 
	 * greater or equals than <MINIMUM_DNA_STRING_LENGTH>
	 */
	protected int sumMutantSequences(List<String> list) {
		return list == null || list.isEmpty() ? 0 :  list.stream()
				.filter(it -> it.length() >= MINIMUM_DNA_STRING_LENGTH)
				.mapToInt(it -> getMutantSequence(it))
				.sum();
	}
	
	/**
	 * It checks the compliance of the number of mutant sequences returning true when the given 
	 * mutantSequences is greater or equals than <MUTANT_SEQUENCE_LIMIT>, otherwise, it returns false. 
	 * This method also set the mutant attribute according the previous evaluation
	 * @param mutantSequences The number of mutant sequences found in the DNA matrix analysis
	 * @return true when the given mutantSequences is greater or equals than <MUTANT_SEQUENCE_LIMIT>,
	 * otherwise, it returns false.
	 */
	protected boolean isMutantCompliant(int mutantSequences) {
		if(mutantSequences >= MUTANT_SEQUENCE_LIMIT) {
			this.dnaMatrix.setMutant(true);
			return true;
		}
		return false;
		
	}
	
	/**
	 * This method checks if the DNA String array is a square matrix and the matrix length
	 * is less than or equals to <MAX_DNA_MATRIX_LENGTH>. This constraint ensures the matrix can 
	 * be analyzed properly
	 * @param dna Array of strings representing the rows of the DNA matrix
	 * @return true if the DNA String array is a square matrix and the matrix length
	 * is less than or equals to Integer.MAX_VALUE.otherwise, it returns false
	 */
	protected boolean isValidMatrix(String[] dna) {
		if(dna != null) {
			int matrixLength = dna.length;
			return isSquareMatrix(dna, matrixLength) && matrixLength <= MAX_DNA_MATRIX_LENGTH;
		}
		return false;
	}
	
	/**
	 * This method checks if the DNA String array is a square matrix
	 * @param dna Array of strings representing the rows of the DNA matrix
	 * @param matrixLength The length of the DNA Matrix
	 * @return true if the DNA String array is a square matrix otherwise, it returns false
	 */
	protected boolean isSquareMatrix(String[] dna, int matrixLength) {
		return Arrays.stream(dna).filter(row -> row.length() == matrixLength).count() == matrixLength;
	}
	
	/**
	 * This method checks if the given DNA matrix passes the conditions for mutant DNA checking
	 * first if the given matrix is valid (square matrix and and the matrix length is less than 
	 * or equals to <MAX_DNA_MATRIX_LENGTH>) and performing the logic looking for more than 
	 * <MUTANT_SEQUENCE_LIMIT> in the DNA Matrix rows, columns, and diagonals to determine whether 
	 * the DNA is mutant or not
	 * @param dna Array of strings representing the rows of the DNA matrix
	 * @return It returns true if the given DNA matrix is valid and there are more than 
	 * <MUTANT_SEQUENCE_LIMIT> in the DNA Matrix rows, columns, and diagonals, otherwise, it returns false
	 */
	public boolean isMutant(String[] dna) {
		boolean isValidMatrix = isValidMatrix(dna);
		analyzeDna(dna);
		return isValidMatrix && this.dnaMatrix.isMutant();
	}
}