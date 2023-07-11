package org.ensembl.importer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "current_sign_in_at")
    private Timestamp currentSignInAt;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "last_sign_in_at")
    private Timestamp lastSignInAt;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_changed", nullable = false)
    private Boolean passwordChanged;

    @Column(name = "remember_created_at")
    private Timestamp rememberCreatedAt;

    @Column(name = "reset_password_sent_at")
    private Timestamp resetPasswordSentAt;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "sign_in_count", nullable = false)
    private Integer signInCount;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}