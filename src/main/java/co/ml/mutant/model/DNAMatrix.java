package co.ml.mutant.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple POJO for a DNA matrix representation and MongoDB domain object definition
 * @author edwinmunozrios
 * @since 2021/05/02
 */
@Getter
@Setter
@Document(collection = "dna_matrix")
public class DNAMatrix {
	
	@Id
	private String id;
	
	@Indexed(unique=true)
	private String dnaString;
	
	@Indexed(name = "is_mutant")
	private boolean isMutant;
	
	private String[] dna;
	
	@Transient
	private List<String> rows;
	
	@Transient
	private List<String> columns;
	
	@Transient
	private List<String> leftToRighDiagonals;
	
	@Transient
	private List<String> rightToLeftDiagonals;
	
	public DNAMatrix() {
		this.isMutant = false;
		this.rows = new ArrayList<String>();
		this.columns = new ArrayList<String>();
		this.leftToRighDiagonals = new ArrayList<String>();
		this.rightToLeftDiagonals = new ArrayList<String>();
	}
	
	public DNAMatrix(String[] dna) {
		this.dna = dna;
		this.dnaString = Arrays.toString(dna);
		this.isMutant = false;
		this.rows = Arrays.asList(dna);
		this.columns = new ArrayList<String>();
		this.leftToRighDiagonals = new ArrayList<String>();
		this.rightToLeftDiagonals = new ArrayList<String>();
	}
}
