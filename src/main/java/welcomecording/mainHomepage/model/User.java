package welcomecording.mainHomepage.model;

import java.sql.Timestamp;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

// ORM - Object Relation Mapping

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String username;

        private String email;

        private String picture;

        private String password;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role; // USER, ADMIN

        @CreationTimestamp
        private Timestamp createDate;

        @Builder
        public User(String username, String email, String picture, String password) {
                this.username = username;
                this.email = email;
                this.picture = picture;
                this.password = password;
    }

        public User update(String username, String email, String picture) {
                this.username = username;
                this.email = email;
                this.picture = picture;
                return this;
        }
}
