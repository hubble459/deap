package nl.hubble.deap.exeptions;

public class ArrayIsFullException extends RuntimeException {
    public ArrayIsFullException(String message) {
        super(message);
    }
}
