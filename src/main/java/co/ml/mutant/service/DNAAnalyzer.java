package co.ml.mutant.service;

/**
 * This interface defines the method to check if a DNA Matrix is mutant
 * @author edwinmunozrios
 * @since 2021/05/02
 */
public interface DNAAnalyzer {

	public boolean isMutant(String[] dna);
}
