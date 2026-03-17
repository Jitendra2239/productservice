package com.jitendra.productservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Id
    private String id;   // sequence name
    private long seq;    // current value
    // getters and setters
}