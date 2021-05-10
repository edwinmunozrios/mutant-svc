package co.ml.mutant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.ml.mutant.model.DNAMatrix;

/**
 * Repository definition for the DNAMatrix domain object
 * @author edwinmunozrios
 * @since 2021/05/02
 *
 */
@Repository
public interface DNAMatrixRepository extends MongoRepository<DNAMatrix, String>{
	
	public long countByIsMutant(boolean isMutant);

}
