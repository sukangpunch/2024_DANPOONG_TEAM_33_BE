package com.onetry.spring.city.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "city_info")
@Getter
@NoArgsConstructor
public class City {
    @Id
    private String id;
    private int loc_no;
    private String region;
    private List<String> subregion;

}
