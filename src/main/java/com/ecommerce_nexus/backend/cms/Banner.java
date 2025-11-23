package com.ecommerce_nexus.backend.cms;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cms_banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 2048)
    private String imageUrl;
    private String linkUrl;
    @Builder.Default
    private boolean active = true;
    @Builder.Default
    private int sortOrder = 0;
}
