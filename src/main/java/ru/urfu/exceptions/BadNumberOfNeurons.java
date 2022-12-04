package ru.urfu.exceptions;

public class BadNumberOfNeurons extends RuntimeException{
    public BadNumberOfNeurons(String message) {
        super(message);
    }
}
