package com.cb.platform.yq.api.controller;

import com.cb.platform.yq.api.vo.ApiUserVo;
import com.cb.platform.yq.base.Result;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 吴海华
 * @serial config 通信接口
 */
@Api(value="swagger2测试用例",hidden = true)
@Controller
public class IndexController {
    @ApiOperation(value = "首页",notes = "获取首页链接")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("title","测试");
        model.addAttribute("content","哈哈哈");
        return "index1";
    }


    @ApiOperation(value = "单个参数案例",notes = "单个参数案例")
    @ApiImplicitParam(paramType="query", name = "id", value = "用户id", required = true, dataType = "String")
    @RequestMapping(value = "/one",method = RequestMethod.GET)
    public String one(@RequestParam String id, Model model){
        return "";
    }

    @ApiOperation(value = "多个参数案例",notes = "多个参数案例")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "password", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "newPassword", value = "新密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public String all(@RequestParam String userId,@RequestParam String password,@RequestParam String newPassword,Model model){
        return "test";
    }


    @ApiOperation(value = "对象参数",notes = "对象参数")
    @RequestMapping(value = "/objectParam",method = RequestMethod.GET)
    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "成功", response = Result.class, responseContainer = "Object" )
    })
    //@ApiResponse(message = "result对象",response = ApiUserVo.class)
    public Result objectParam(@ApiParam(value = "云签系统用户对象", required = true) @RequestBody ApiUserVo apiUserVo, Model model){
        Result result=new Result();
        result.setData(new ApiUserVo());
        return result;
    }


    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(Model model){
        return "login";
    }




}
