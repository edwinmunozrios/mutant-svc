package co.ml.mutant.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple POJO given in the incoming request for /mutant endpoint
 * @author edwinmunozrios
 * @since 2021/05/02
 */
@Getter
@Setter
public class MutantRequest {
	private String[] dna;
}
