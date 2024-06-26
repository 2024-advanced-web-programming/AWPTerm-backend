package awpterm.backend.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
public class Admin extends User {
    private String name;

    public boolean isSame(Admin admin) {
        return getId().equals(admin.getId()) && getPassword().equals(admin.getPassword());
    }
}
