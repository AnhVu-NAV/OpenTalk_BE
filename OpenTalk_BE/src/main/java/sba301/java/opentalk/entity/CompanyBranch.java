package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "company_branch")
public class CompanyBranch extends BaseEntity {
    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @OneToMany(mappedBy = "companyBranch", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "companyBranch")
    private List<OpenTalkMeeting> meetings;

    public CompanyBranch(long id) {
        this.setId(id);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }
}