package com.jstik.fancy.account.storage.entity.cassandra.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table("entity_by_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityByTag {

    @PrimaryKey
    private UserByTagPrimaryKey primaryKey;


    public EntityByTag(String tag, String entityId, String discriminator) {
        this.primaryKey = new UserByTagPrimaryKey(tag, entityId, discriminator);
    }

    @Getter
    @Setter
    @PrimaryKeyClass
    @NoArgsConstructor
    public static class UserByTagPrimaryKey{
        @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
        private String tag;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 0)
        private String discriminator;

        @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1)
        @Column(value = "entity_id")
        private String entityId;


        public UserByTagPrimaryKey(String tag, String entityId, String discriminator) {
            this.tag = tag;
            this.entityId = entityId;
            this.discriminator = discriminator;
        }
    }
}
