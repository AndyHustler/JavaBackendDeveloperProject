package ru.project.jbd.domain.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<ERole> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TaskGroup> taskGroups = new HashSet<>();

    private User(UserBuilder builder) {
        this.username = builder.userName;
        this.password = builder.password;
        this.email = builder.email;
        this.roles = builder.roles;
        this.tasks = builder.tasks;
        this.taskGroups = builder.taskGroups;
    }

    public boolean hasRole(ERole role) {
        for(ERole r : this.roles) {
            if(role.equals(r)) return true;
        };
        return false;
    }

    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }

    public static class UserBuilder {
        private String userName;
        private String password;
        private String email;
        private Set<ERole> roles = new HashSet<>();
        private Set<Task> tasks = new HashSet<>();
        private Set<TaskGroup> taskGroups = new HashSet<>();

        public UserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder roles(Set<ERole> roles) {
            this.roles.addAll(roles);
            return this;
        }

        public UserBuilder tasks(Set<Task> tasks) {
            this.tasks.addAll(tasks);
            return this;
        }

        public UserBuilder taskGroups(Set<TaskGroup> taskGroups) {
            this.taskGroups.addAll(taskGroups);
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }
}
