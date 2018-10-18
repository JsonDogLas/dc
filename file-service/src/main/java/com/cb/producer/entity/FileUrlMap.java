package com.cb.producer.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
/**
 * @author jdl
 */
@Entity
@Table(name = "file_url_map",indexes = {@Index(columnList = "upload_time")})
@EntityListeners(AuditingEntityListener.class)
@Data
public class FileUrlMap {
    @Id
    @GeneratedValue(generator = "myGen")
    @GenericGenerator(name = "myGen", strategy = "assigned")
    private String id;

    @Column(name = "file_name",nullable = false)
    private String fileName;

    @Column(name = "group_name",nullable = false)
    private String groupName;

    @Column(name = "path",nullable = false)
    private String path;

    @CreatedDate
    @Column(name = "upload_time", updatable = false)
    private Date uploadTime;

    public FileUrlMap() {
    }

    public FileUrlMap(String id, String fileName, String groupName, String path) {
        this.id = id;
        this.fileName = fileName;
        this.groupName = groupName;
        this.path = path;
    }

    public String getGroupPath() {
        return groupName + "/" + path;
    }
}
