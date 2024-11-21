package com.onetry.spring.position.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "position_info")
@NoArgsConstructor
@Getter
public class Position {

    @Id
    private String id;
    private String category;
    private List<String> position;

}
