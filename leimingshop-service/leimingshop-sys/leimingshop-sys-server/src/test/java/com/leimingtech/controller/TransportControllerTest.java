//package com.leimingtech.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.leimingtech.dto.transport.SaveTransportExtendDTO;
//import com.leimingtech.dto.transport.TransportDTO;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author zhangtai
// * @date 2019/5/16 0016 10:40
// * @Description:
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
////配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
//@Transactional
//@Rollback
//public class TransportControllerTest {
//
//    protected MockMvc mockMvc;
//
//    @Autowired
//    protected WebApplicationContext wac;
//
//    @Before()  //这个方法在每个方法执行之前都会执行一遍
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
//    }
//
//    @org.junit.Test
//    public void getTest() throws Exception {
//        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/transport/1128491776152887297")//请求方法
//                .accept(MediaType.APPLICATION_JSON_UTF8))//设置返回类型
//                .andDo(MockMvcResultHandlers.print())////打印出请求和相应的内容
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))//验证结果中的code是否为0
//                .andReturn();//返回结果
//        int status=mvcResult.getResponse().getStatus();
//        String content =mvcResult.getResponse().getContentAsString();
//        System.out.println(content);
//    }
//    @org.junit.Test
//    public void pageTest() throws Exception{
//        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/transport/page?page=1&limit=10")
//                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))//验证结果中的code是否为0
//                .andReturn();
//        int status=mvcResult.getResponse().getStatus();
//        String content =mvcResult.getResponse().getContentAsString();
//        System.out.println(content);
//    }
//
//    @org.junit.Test
//    public void saveTest() throws Exception{
//        HashMap<String, Object> param = new HashMap<>();
//        param.put("title","mockMvcTest" + Math.random());
//        List<SaveTransportExtendDTO> transportExtendDTOs = new ArrayList<>();
//        SaveTransportExtendDTO transportExtendDTO = new SaveTransportExtendDTO();
//        transportExtendDTO.setType("py");
//        transportExtendDTO.setIsDefault(1);
//        transportExtendDTO.setSnum(1);
//        transportExtendDTO.setSprice(new BigDecimal(12.00));
//        transportExtendDTO.setXnum(1);
//        transportExtendDTO.setXprice(new BigDecimal(12.00));
//        transportExtendDTOs.add(transportExtendDTO);
//        param.put("transportExtendDTOs",transportExtendDTOs);
//        String s = JSON.toJSONString(param);
//        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.post("/transport")//post请求
//                .contentType(MediaType.APPLICATION_JSON_VALUE)//参数类型
//                .content(s)//在requestbody中添加数据
//                .accept(MediaType.APPLICATION_JSON_UTF8))//设置返回类型
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))//验证结果中的code是否为0
//                .andReturn();
//        int status=mvcResult.getResponse().getStatus();
//        String content =mvcResult.getResponse().getContentAsString();
//        System.out.println(content);
//    }
//
//    @org.junit.Test
//    public void updateTest() throws Exception{
//        TransportDTO transportDTO = new TransportDTO();
//        transportDTO.setId(1128905824052936706L);
//        transportDTO.setStoreId("");
//        transportDTO.setTitle("mockMvcTest" + Math.random());
//        List<SaveTransportExtendDTO> transportExtendDTOs = new ArrayList<>();
//        SaveTransportExtendDTO transportExtendDTO = new SaveTransportExtendDTO();
//        transportExtendDTO.setType("py");
//        transportExtendDTO.setIsDefault(1);
//        transportExtendDTO.setSnum(1);
//        transportExtendDTO.setSprice(new BigDecimal(12.00));
//        transportExtendDTO.setXnum(1);
//        transportExtendDTO.setXprice(new BigDecimal(12.00));
//        transportExtendDTOs.add(transportExtendDTO);
//        transportDTO.setTransportExtends(transportExtendDTOs);
//        String s = JSON.toJSONString(transportDTO);
//        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.put("/transport")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(s)
//                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))//验证结果中的code是否为0
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        int status=mvcResult.getResponse().getStatus();
//        String content =mvcResult.getResponse().getContentAsString();
//        System.out.println(content);
//    }
//
//    @org.junit.Test
//    public void deleteTest() throws Exception{
//        ArrayList<Long> strings = new ArrayList<>();
//        strings.add(1128905824052936706L);
//        String s = JSON.toJSONString(strings);
//        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.delete("/transport")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(s)
//                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))//验证结果中的code是否为0
//                .andReturn();
//        int status=mvcResult.getResponse().getStatus();
//        String content =mvcResult.getResponse().getContentAsString();
//        System.out.println(content);
//    }
//
//    @org.junit.Test
//    public void setDefaultTransportTest() throws Exception{
//        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.put("/transport/setDefaultTransport")
//                .param("id","1128905824052936706")
//                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))//验证结果中的code是否为0
//                .andReturn();
//        int status=mvcResult.getResponse().getStatus();
//        String content =mvcResult.getResponse().getContentAsString();
//        System.out.println(content);
//    }
//
//    @org.junit.Test
//    public void listTransportTypeTest() throws Exception{
//        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/transport/listTransportType")
//                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))//验证结果中的code是否为0
//                .andReturn();
//        int status=mvcResult.getResponse().getStatus();
//        String content =mvcResult.getResponse().getContentAsString();
//        System.out.println(content);
//    }
//}
