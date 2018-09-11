package com.cb.producer.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
/**
 * @author jdl
 */
@Entity
@Table(name = "url_map",indexes = {@Index(columnList = "file_name"),
                                    @Index(columnList = "upload_time")})
@EntityListeners(AuditingEntityListener.class)
public class UrlMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name",unique = true,nullable = false)
    private String fileName;

    @Column(name = "group_name",nullable = false)
    private String groupName;

    @Column(name = "path",nullable = false)
    private String path;

    @CreatedDate
    @Column(name = "upload_time", updatable = false)
    private Date uploadTime;

    public UrlMap() {
    }

    public UrlMap(String fileName, String groupName, String path) {
        this.fileName = fileName;
        this.groupName = groupName;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
