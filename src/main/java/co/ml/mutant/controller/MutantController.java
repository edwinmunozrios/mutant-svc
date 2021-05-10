package co.ml.mutant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import co.ml.mutant.model.MutantRequest;
import co.ml.mutant.model.MutantStats;
import co.ml.mutant.service.DNAAnalyzer;
import co.ml.mutant.service.MutantPersistenceService;

/**
 * Application main controller for all the incoming requests supported by the application
 * @author edwinmunozrios
 * @since 2021/05/02
 */
@RestController
public class MutantController {
	
	@Autowired
	DNAAnalyzer dnaAnalyzer;
	
	@Autowired
	MutantPersistenceService mutantPersistenceService;
	
	/**
	 * This method handles the incoming POST requests hitting the /mutant endpoint and it verifies 
	 * whether the incoming DNA Matrix is mutant or not
	 * @param dna Array of strings representing the rows of the DNA matrix
	 * @return ResponseEntity 200 OK in case the given DNA matrix is mutant, otherwise, 403 Forbidden.
	 */
	@PostMapping(value = "/mutant")
	public ResponseEntity<Void> isMutant(@RequestBody MutantRequest mutantRequest) {
		ResponseEntity<Void> response;
		if(dnaAnalyzer.isMutant(mutantRequest.getDna())) {
			response = ResponseEntity.status(HttpStatus.OK).build();
		}else {
			response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		return response;
	}
	
	/**
	 * This method handles the incoming GET requests hitting the /stats endpoint calculating
	 * the mutants/humans stats and ratio inside a MutantStats object
	 * @return ResponseEntity 200 OK and the calculated MutantStats as the body, in case of error, 
	 * 500 Internal Server Error..
	 */
	@GetMapping(value = "/stats")
	public ResponseEntity<MutantStats> getMutantStats(){
		ResponseEntity<MutantStats> response;
		try {
			MutantStats mutantStats = mutantPersistenceService.getMutantStats();
			response = ResponseEntity.ok(mutantStats);
		}catch(Exception ex) {
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}

}
       