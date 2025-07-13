package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role")
public class Role extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    public Role(long id) {
        this.setId(id);
    }
}
