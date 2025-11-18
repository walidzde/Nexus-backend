package com.ecommerce_nexus.backend.cms;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cms_testimonials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Testimonial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;
    @Column(length = 2000)
    private String content;
    @Builder.Default
    private boolean active = true;
    @Builder.Default
    private int sortOrder = 0;
}
