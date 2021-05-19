//package com.leimingtech.modules.service;
//
//import com.leimingtech.modules.dto.custom.GoodsClassCustomDTO;
//import com.leimingtech.modules.dto.custom.GoodsClassCustomSaveDTO;
//import com.leimingtech.modules.service.custom.GoodsClassCustomService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cglib.beans.BeanCopier;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(SpringJUnit4ClassRunner.class)
//public class GoodsCustomClassTest {
//
//    @Autowired
//    private GoodsClassCustomService classCustomService;
//
//    //保存一级展示分类
//    @Test
//    public void saveOneClass() {
//        List<GoodsClassCustomDTO> goodsClassCustomDTOS = classCustomService.selectListByParentId(0L, 1, 0);
//        for (GoodsClassCustomDTO goodsClassCustomDTO : goodsClassCustomDTOS) {
//            GoodsClassCustomSaveDTO goodsClassCustomSaveDTO = new GoodsClassCustomSaveDTO();
//            BeanCopier.create(GoodsClassCustomDTO.class, GoodsClassCustomSaveDTO.class, false)
//                    .copy(goodsClassCustomDTO, goodsClassCustomSaveDTO, null);
//            goodsClassCustomSaveDTO.setShowType(1);
//            classCustomService.save(goodsClassCustomSaveDTO);
//        }
//    }
//
//    //保存二级级展示分类
//    @Test
//    public void saveTwoClass() {
//        List<GoodsClassCustomDTO> h5ClassList = classCustomService.selectListByParentId(0L, 1, 0);
//        List<GoodsClassCustomDTO> pcClassList = classCustomService.selectListByParentId(0L, 1, 1);
//        for (GoodsClassCustomDTO goodsClassCustomDTO : h5ClassList) {
//            for (GoodsClassCustomDTO classCustomDTO : pcClassList) {
//                if (goodsClassCustomDTO.getGcName().equals(classCustomDTO.getGcName())) {
//                    List<GoodsClassCustomDTO> h5TwoClassList = classCustomService.selectListByParentId(goodsClassCustomDTO.getId(), 1, 0);
//                    for (GoodsClassCustomDTO customDTO : h5TwoClassList) {
//                        GoodsClassCustomSaveDTO goodsClassCustomSaveDTO = new GoodsClassCustomSaveDTO();
//                        BeanCopier.create(GoodsClassCustomDTO.class, GoodsClassCustomSaveDTO.class, false)
//                                .copy(customDTO, goodsClassCustomSaveDTO, null);
//                        goodsClassCustomSaveDTO.setShowType(1);
//                        goodsClassCustomSaveDTO.setGcParentId(classCustomDTO.getId());
//                        classCustomService.save(goodsClassCustomSaveDTO);
//                    }
//                }
//            }
//        }
//    }
//
//    //保存三级展示分类
//    @Test
//    public void saveThreeClass() {
//        List<GoodsClassCustomDTO> h5ClassList = classCustomService.selectListByParentId(0L, 1, 0);
//        List<GoodsClassCustomDTO> pcClassList = classCustomService.selectListByParentId(0L, 1, 1);
//        saveCustomClass(h5ClassList, pcClassList);
//    }
//
//    private void saveCustomClass(List<GoodsClassCustomDTO> h5ClassList, List<GoodsClassCustomDTO> pcClassList) {
//
//        for (GoodsClassCustomDTO goodsClassCustomDTO : h5ClassList) {
//            for (GoodsClassCustomDTO classCustomDTO : pcClassList) {
//                if (goodsClassCustomDTO.getGcName().equals(classCustomDTO.getGcName())) {
//                    if (goodsClassCustomDTO.getIdPath().split(",").length == 2 && classCustomDTO.getIdPath().split(",").length == 2) {
//                        List<GoodsClassCustomDTO> h5TwoClassList = classCustomService.selectListByParentId(goodsClassCustomDTO.getId(), 1, 0);
//                        for (GoodsClassCustomDTO customDTO : h5TwoClassList) {
//                            GoodsClassCustomSaveDTO goodsClassCustomSaveDTO = new GoodsClassCustomSaveDTO();
//                            BeanCopier.create(GoodsClassCustomDTO.class, GoodsClassCustomSaveDTO.class, false)
//                                    .copy(customDTO, goodsClassCustomSaveDTO, null);
//                            goodsClassCustomSaveDTO.setShowType(1);
//                            goodsClassCustomSaveDTO.setGcParentId(classCustomDTO.getId());
//                            classCustomService.save(goodsClassCustomSaveDTO);
//                        }
//                    } else {
//                        List<GoodsClassCustomDTO> h5TwoClassList = classCustomService.selectListByParentId(goodsClassCustomDTO.getId(), 1, 0);
//                        List<GoodsClassCustomDTO> pcTwoClassList = classCustomService.selectListByParentId(classCustomDTO.getId(), 1, 1);
//                        saveCustomClass(h5TwoClassList, pcTwoClassList);
//                    }
//                }
//            }
//        }
//    }
//}
