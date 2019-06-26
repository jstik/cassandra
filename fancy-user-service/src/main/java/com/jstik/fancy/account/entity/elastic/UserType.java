package com.jstik.fancy.account.entity.elastic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Getter
@Setter
@Document(indexName = "account-service", type = "user")
public class UserType {

    @Id
    @MultiField(
            mainField = @Field(type = Text, fielddata = true),
            otherFields = {
                    @InnerField(suffix = "verbatim", type = Keyword)
            }
    )
    private String login;

    @MultiField(
            mainField = @Field(type = Text, fielddata = true),
            otherFields = {
                    @InnerField(suffix = "verbatim", type = Keyword)
            }
    )
    private String firstName;

    @MultiField(
            mainField = @Field(type = Text, fielddata = true),
            otherFields = {
                    @InnerField(suffix = "verbatim", type = Keyword)
            }
    )
    private String lastName;

    private String email;

    @Field(type = FieldType.Date)
    private LocalDateTime created;

    @Field(type = FieldType.Date)
    private LocalDateTime updated;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<String> tags;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<String> groups;
}
