package com.jstik.fancy.account.search.entity.elastic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Getter
@Setter
@NoArgsConstructor
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


    public UserType(String login, String firstName, String lastName, String email) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
