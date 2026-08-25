package com.mnemora.backend.exception;

// A custom exception specifically for "entry not found" cases.
// Extending RuntimeException means we don't need to declare "throws" everywhere.
public class EntryNotFoundException extends RuntimeException {

    // The constructor just passes the message up to RuntimeException,
    // so we can do: throw new EntryNotFoundException("Entry not found with id: 5")
    public EntryNotFoundException(String message) {
        super(message);
    }
}