package com.cb.consumer.controller;

import com.cb.consumer.service.FileManager;
import com.cb.consumer.service.FileManager2;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author jdl
 */
@Controller
public class FileController {

    private static final String SUCCESS = "success";

    @Autowired
    FileManager fileManager;

    @Autowired
    FileManager2 fileManager2;

    @ApiOperation(value = "上传文件",notes = "用表单提交方式上传文件")
    @ApiImplicitParam(name = "file", value = "表单文件", required = true)
    @PostMapping(value = "/upload",consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ResponseBody
    public String uploadByForm(@RequestParam("file") MultipartFile file) {
        return fileManager.uploadFile(file);
    }

    @ApiOperation(value = "上传文件",notes = "文件字符串上传")
    @PostMapping(value = "/upload/string")
    @ResponseBody
    public String uploadByString(@RequestBody Map<String,Object> map) {
        return fileManager2.uploadFileByString(map);
    }

    @ApiIgnore
    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @ApiIgnore
    @GetMapping("/upload2")
    public String upload2() {
        return "upload2";
    }

    @ApiOperation(value = "文件列表")
    @GetMapping("/files")
    @ResponseBody
    public String getFileList() {
        return fileManager.getFiles();
    }

    @ApiOperation(value = "删除所有文件")
    @DeleteMapping("/delete/all")
    @ResponseBody
    public String deleteAll() {
        fileManager.deleteAll();
        return SUCCESS;
    }

    @ApiIgnore
    @GetMapping("/delete/all")
    @ResponseBody
    public String deleteAll2() {
        fileManager.deleteAll();
        return SUCCESS;
    }

    @ApiOperation(value = "删除文件",notes = "根据id删除文件")
    @ApiImplicitParam(name = "id", value = "文档id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteFile(@PathVariable("id") Long id) {
        fileManager.deleteFile(id);
        return SUCCESS;
    }
}
