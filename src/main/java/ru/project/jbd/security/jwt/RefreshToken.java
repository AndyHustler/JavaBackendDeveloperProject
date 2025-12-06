package ru.project.jbd.security.jwt;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.project.jbd.domain.model.User;
import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "refreshtoken")
public class RefreshToken {
    
    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
