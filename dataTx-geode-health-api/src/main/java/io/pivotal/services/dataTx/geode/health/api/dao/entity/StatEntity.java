package io.pivotal.services.dataTx.geode.health.api.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
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
    private long id;

    private String label;

    @Column(nullable = false)
    private LocalDateTime statTimestamp;

}
