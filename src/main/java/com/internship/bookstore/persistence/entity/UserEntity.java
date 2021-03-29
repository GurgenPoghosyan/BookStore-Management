package com.internship.bookstore.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "pass_hash", nullable = false, length = 10485760)
    private String password;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_communities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private List<CommunityEntity> userCommunities = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<CollectionEntity> bookCollections = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "details_id", referencedColumnName = "id")
    private UserDetailsEntity details;
}
