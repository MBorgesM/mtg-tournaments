package br.com.mborgesm.mtgtournaments.exceptions;

public class UsernameInUseException extends RuntimeException {
    public UsernameInUseException(String message) {
        super(message);
    }
}
