package br.uece.ctmc.bdp;

public class InvalidBirthRateException extends InvalidBirthDeathMatrix {
	
	private static final long serialVersionUID = 240525924223157549L;

	public InvalidBirthRateException(String message) {
		super(message);
	}

}
