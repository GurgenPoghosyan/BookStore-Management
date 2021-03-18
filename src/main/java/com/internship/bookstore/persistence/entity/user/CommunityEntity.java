//package com.internship.bookstore.persistence.entity.user;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.List;
//
///**
// * @author Gurgen Poghosyan
// */
//@Data
//@Entity
//@Table(name = "community")
//@NoArgsConstructor
//public class CommunityEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @OneToMany
//    private List<CommunityEntity> listOfUserCommunities;
//}
