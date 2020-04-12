package com.shu.mpadmin;//package com.example.demo;
//
//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.coyote.http11.Http11NioProtocol;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class HttpsConfig {
//    //在项目中装配了ssl证书后,对其进行配置,让HTTP请求重定向到 HTTPS 请求
//    //当用户使用HTTP来进行访问的时候自动转为HTTPS的方式
//
//    @Value("${server.port}")
//    private int serverPortHttps;
//
//    @Value("${server.port-http}")
//    private int serverPortHttp;
//
//    /**
//     * http重定向到https
//     */
//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat;
//        tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//
//        return tomcat;
//    }
//
//    @Bean
//    public Connector httpConnector() {
//
//        //当为http请求并且为serverPortHttp端口时，自动重定向到https的serverPortHttps端口
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//
//        connector.setScheme("http");
//        //Connector监听的http的端口号
//        connector.setPort(serverPortHttp);
//        connector.setSecure(false);
//        //监听到http的端口号后转向到的https的端口号
//        connector.setRedirectPort(serverPortHttps);
//        return connector;
//    }
//
//}
