
package com.cb.platform.yq.api.base.swagger;

import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SwaggerOauth2Config implements ApiListingScannerPlugin {
    @Autowired
    private SystemProperties systemProperties;

    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        return new ArrayList<ApiDescription>(
                Arrays.asList(
                        new ApiDescription(
                                systemProperties.getOauthUrl(),  //url
                                "授权接口", //描述
                                Arrays.asList(
                                        new OperationBuilder(
                                                new CachingOperationNameGenerator())
                                                .method(HttpMethod.POST)//http请求类型
                                                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                                                .summary("获取token")
                                                .notes("获取token")//方法描述
                                                .tags(Sets.newHashSet("1.第三方授权"))//归类标签
                                                .parameters(
                                                        Arrays.asList(
                                                                new ParameterBuilder()
                                                                        .description("oauth2授权方式，如client_credentials")//参数描述
                                                                        .type(new TypeResolver().resolve(String.class))//参数数据类型
                                                                        .name("grant_type")//参数名称
                                                                        .defaultValue("client_credentials")//参数默认值
                                                                        .parameterType("query")//参数类型
                                                                        .parameterAccess("access")
                                                                        .required(true)//是否必填
                                                                        .modelRef(new ModelRef("string")) //参数数据类型
                                                                        .build(),
                                                                new ParameterBuilder()
                                                                        .description("客户端ID")
                                                                        .type(new TypeResolver().resolve(String.class))
                                                                        .name("client_id")
                                                                        .parameterType("query")
                                                                        .parameterAccess("access")
                                                                        .required(true)
                                                                        .modelRef(new ModelRef("string")) //<5>
                                                                        .build(),
                                                                new ParameterBuilder()
                                                                        .description("客户端密码")
                                                                        .type(new TypeResolver().resolve(String.class))
                                                                        .name("client_secret")
                                                                        .parameterType("query")
                                                                        .parameterAccess("access")
                                                                        .required(true)
                                                                        .modelRef(new ModelRef("string")) //<5>
                                                                        .build()
                                                        ))
                                                .build()),
                                false)));
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
