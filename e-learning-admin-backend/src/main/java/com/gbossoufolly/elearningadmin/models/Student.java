package com.gbossoufolly.elearningadmin.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Basic
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Basic
    @Column(name = "level", nullable = false, length = 45)
    private String level;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    public Student(String firstName, String lastName, String level, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(studentId, student.studentId) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(level, student.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, firstName, lastName, level);
    }
}
