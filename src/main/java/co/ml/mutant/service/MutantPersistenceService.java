package co.ml.mutant.service;

import co.ml.mutant.model.DNAMatrix;
import co.ml.mutant.model.MutantStats;

public interface MutantPersistenceService {
	
	public void saveDNAMatrix(DNAMatrix dnaMatrix);
	
	public MutantStats getMutantStats();
	
}
