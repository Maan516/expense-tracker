package org.example.expensetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "user_id"})
        }
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name is required")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Category name must contain only letters"
    )
    @Column(nullable = false)
    private String name;

    // MANY categories → ONE user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // =====================
    // GETTERS & SETTERS
    // =====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    // Normalize input
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name =
                    name.trim().substring(0, 1).toUpperCase()
                            + name.trim().substring(1).toLowerCase();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
