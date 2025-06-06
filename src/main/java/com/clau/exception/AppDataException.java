package com.clau.exception;

public class AppDataException extends RuntimeException{

  public AppDataException(String message) {
    super(message);
  }

  public AppDataException(String message, Throwable cause) {
    super(message, cause);
  }

  public AppDataException(Throwable cause) {
    super(cause);
  }
}
