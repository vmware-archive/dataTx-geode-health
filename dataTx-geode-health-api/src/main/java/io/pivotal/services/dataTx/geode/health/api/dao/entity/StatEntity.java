package io.pivotal.services.dataTx.geode.health.api.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Statistics entity
 * @author Gregory Green
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "StatEntity")
public class StatEntity
{
    //Ex LinuxSystemStats
    private String filterTypeName;
    //ex: iowait

    @Column(nullable = false)
    private String statName;
    private String machine;

    @Column(nullable = false)
    private double statValue;

    @Id
    @GeneratedValue
    private long id;

    private String label;

    @Column(nullable = false)
    private LocalDateTime statTimestamp;

}
