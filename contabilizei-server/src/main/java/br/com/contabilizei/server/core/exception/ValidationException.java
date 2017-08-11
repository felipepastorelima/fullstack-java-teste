package br.com.contabilizei.server.core.exception;

public class ValidationException extends BadRequestException {
	private static final long serialVersionUID = -2334493331614773643L;
	
    public ValidationException(String message) {
        super(message);
    }
    
}
