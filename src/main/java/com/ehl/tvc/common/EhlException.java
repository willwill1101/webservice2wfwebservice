package com.ehl.tvc.common;

public class EhlException extends Throwable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String message ; 
	
	
	
	public EhlException(String message) {
			super();
			this.message = message;
		}



	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return this.message;
	}
	 
}
