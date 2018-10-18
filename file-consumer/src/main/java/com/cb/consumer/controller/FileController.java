package com.cb.consumer.controller;

import com.cb.consumer.service.FileManager;
import com.cb.consumer.service.FileManager2;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    private FileManager fileManager;

    @Autowired
    private FileManager2 fileManager2;


    @ApiOperation(value = "获取文件地址",notes = "nginx代理文件路径uri")
    @GetMapping("/uri/{signId}")
    @ResponseBody
    public String getUri(@PathVariable("signId") String signId) {
        return fileManager2.getFileUri(signId);
    }

    @ApiOperation(value = "获取服务实例地址",notes = "真实上传文件的地址")
    @GetMapping("/serviceUri/{serviceId}")
    @ResponseBody
    public String getService(@PathVariable("serviceId") String serviceId) {
        return fileManager2.getServiceUri(serviceId);
    }

    @ApiOperation(value = "上传文件",notes = "用表单提交方式上传文件")
    @PostMapping(value = "/upload",consumes = "multipart/*")
    @ResponseBody
    public String uploadByForm(@ApiParam("file data") @RequestParam("file") MultipartFile file, @RequestParam(name = "signId",required = false) String signId) {
        return fileManager.uploadFile(file, signId);
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
    @ApiIgnore
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
    @ApiImplicitParam(name = "id", value = "文档id", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteFile(@PathVariable("id") String id) {
        fileManager.deleteFile(id);
        return SUCCESS;
    }
}
