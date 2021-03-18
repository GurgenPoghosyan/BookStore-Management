//package com.internship.bookstore.persistence.entity.user;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.List;
//
///**
// * @author Gurgen Poghosyan
// */
//@Entity
//@Table(name = "users")
//@Getter
//@Setter
//@NoArgsConstructor
//public class UserEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "pass_hash", nullable = false, length = 10485760)
//    private String passHash;
//
//    @Column(name = "first_name", nullable = false)
//    private String firstName;
//
//    @Column(name = "last_name", nullable = false)
//    private String lastName;
//
//    @Column(name = "status", nullable = false)
//    private String status;
//
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<CommunityEntity> listOfUserCommunities;
//}
