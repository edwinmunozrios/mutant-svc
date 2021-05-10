package co.ml.mutant.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import co.ml.mutant.model.DNAMatrix;
import co.ml.mutant.model.MutantStats;
import co.ml.mutant.repository.DNAMatrixRepository;
import co.ml.mutant.service.MutantPersistenceService;

@Service
public class MutantPersistenceServiceImpl implements MutantPersistenceService {
	
	@Autowired
	private DNAMatrixRepository dnaMatrixREpository;
	
	private static final Logger log = LoggerFactory.getLogger(MutantPersistenceServiceImpl.class);

	/**
	 * This method triggers the DNAMatrixRepository methods in an async way.
	 * This method is skipping errors due to org.springframework.dao.DuplicateKeyException
	 */
	@Async
	public void saveDNAMatrix(DNAMatrix dnaMatrix) {
		try {
			dnaMatrixREpository.save(dnaMatrix);
		}catch(org.springframework.dao.DuplicateKeyException exception) {
			log.error("Error saving a DNAMatrix object: " + dnaMatrix.getDnaString() + ". Caused by: " + exception.getMessage());
		}
	}

	/**
	 * This method calculates the MutantStats object from the database human and mutant DNA values
	 * @return MutantStats object calculated from the database human and mutant DNA values
	 */
	public MutantStats getMutantStats() {
		long mutants = dnaMatrixREpository.countByIsMutant(true);
		long humans = dnaMatrixREpository.countByIsMutant(false);
		MutantStats mutantStats = MutantStats.builder()
				.count_mutant_dna(mutants)
				.count_human_dna(humans).build();
		mutantStats.setRatio(mutantStats.calculateRatio());
		return mutantStats;
	}

}
