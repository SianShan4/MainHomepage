package welcomecording.mainHomepage.model;

import java.sql.Timestamp;
import javax.persistence.*;

import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

// ORM - Object Relation Mapping

@Data
@Entity
public class User {
//    @Id // primary key
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private Long id;
//
//    @Column(nullable = false)
//    private String username;
//
//    @Column(nullable = true)
//    private String email;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
//
//    @CreationTimestamp
//    private Timestamp createDate;
//
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "user_profile_id")
//    private UserProfile userProfile;
//
//    @Builder
//    public User(String username, UserProfile userProfile, String email, Role role) {
//        this.username = username;
//        this.userProfile = userProfile;
//        this.email = email;
//        this.role = role;
//    }
//
//    // 이름변경시 변경할 이름 받고, 변경된 유저 객체 리턴함.
//    public User update(String username) {
//        this.username = username;
//        return this;
//    }
//
//    // 유저객체 생성시에 role에서 final필드로 만들어진 key를 반환함.
//    public String getRoleKey() {
//        return this.role.getKey();
//    }
        @Id // primary key
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String username;
        private String password;
        private String email;
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role; // USER, ADMIN
        @CreationTimestamp
        private Timestamp createDate;

        @Builder
        public User(String username, String password, String email, Role role) {
                this.username = username;
                this.password = password;
                this.email = email;
                this.role = role;
    }
}
