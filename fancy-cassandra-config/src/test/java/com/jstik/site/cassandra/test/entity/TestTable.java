package com.jstik.site.cassandra.test.entity;


import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class TestTable {

    @PrimaryKey
    @NonNull
    private String primaryKey;

    private String testData;

}
