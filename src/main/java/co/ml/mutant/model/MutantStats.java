package co.ml.mutant.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Simple POJO for the /stats endpoint response
 * @author edwinmunozrios
 * @since 2021/05/02
 */
@Getter
@Setter
@Builder
public class MutantStats {
	
	private long count_mutant_dna;
	
    private long count_human_dna;
    
    private double ratio;
    
    /**
     * It calculates the mutant ratio by dividing count_mutant_dna / count_human_dna
     * and assign the value to the ratio property
     * @return the mutant ratio by dividing count_mutant_dna / count_human_dna
     */
    public double calculateRatio() {
    	double ratioCalc = new Double(0);
    	if(this.count_human_dna > 0) {
    		ratioCalc =  (double) this.count_mutant_dna / this.count_human_dna;
    	}
    	this.setRatio(ratioCalc);
    	return ratioCalc;
    }
}
