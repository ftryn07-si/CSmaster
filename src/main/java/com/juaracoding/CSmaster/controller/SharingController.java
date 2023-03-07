package com.juaracoding.CSmaster.controller;//package com.juaracoding._01JavaWeb.controller;
//
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/sharing")
//public class SharingController {
//
//    @GetMapping("/ifcondition")
//    public String conditionLogic( Model model,WebRequest request) {
//
//        request.setAttribute("MY_SESSION","PAUL",1);
//        List<String> list = new ArrayList<String>();
//        list.add("CONTENT-0");
//        list.add("CONTENT-1");
//        list.add("CONTENT-2");
//        model.addAttribute("paramOne",null);
//        model.addAttribute("paramTwo",null);
//        model.addAttribute("ifCondition","ada");
//        model.addAttribute("unlessCondition",null);
//        model.addAttribute("listContentz",list);
//        model.addAttribute("userName",null);
//        System.out.println(request.getAttribute("USR_ID",1));
//
//        List<TestThree> listTestThree = new ArrayList<TestThree>();
//        List<TestTwo> listTestTwo = new ArrayList<TestTwo>();
//
//        TestThree testThree = new TestThree();
//
//        testThree.setIdSubMenu("1");
//        testThree.setNamaSubMenu("SALESONE");
//        testThree.setStrStringTestThree("TEST SALES ONE");
//        testThree.setLinkSubMenu("/api/menu/salesone");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("2");
//        testThree.setNamaSubMenu("SALESTWO");
//        testThree.setStrStringTestThree("TEST SALES TWO");
//        testThree.setLinkSubMenu("/api/menu/salestwo");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("3");
//        testThree.setNamaSubMenu("SALES THREE");
//        testThree.setStrStringTestThree("TEST SALES THREE");
//        testThree.setLinkSubMenu("/api/menu/salesthree");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("4");
//        testThree.setNamaSubMenu("SALES FOUR");
//        testThree.setStrStringTestThree("TEST SALES FOUR");
//        testThree.setLinkSubMenu("/api/menu/salesfour");
//        listTestThree.add(testThree);
//
//        TestTwo testTwo = new TestTwo();
//        testTwo.setStrStringTestwo("SALES");
//        testTwo.setListMenu(listTestThree);
//
//        listTestTwo.add(testTwo);
//
//        listTestThree = new ArrayList<TestThree>();
//
//        testThree.setIdSubMenu("5");
//        testThree.setNamaSubMenu("HRONE");
//        testThree.setStrStringTestThree("TEST HR ONE");
//        testThree.setLinkSubMenu("/api/menu/hrone");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("6");
//        testThree.setNamaSubMenu("HRTWO");
//        testThree.setStrStringTestThree("TEST HR TWO");
//        testThree.setLinkSubMenu("/api/menu/hrtwo");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("7");
//        testThree.setNamaSubMenu("HR THREE");
//        testThree.setStrStringTestThree("TEST HR THREE");
//        testThree.setLinkSubMenu("/api/menu/hrthree");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("8");
//        testThree.setNamaSubMenu("HR FOUR");
//        testThree.setStrStringTestThree("TEST HR FOUR");
//        testThree.setLinkSubMenu("/api/menu/hrfour");
//        listTestThree.add(testThree);
//
//        testTwo = new TestTwo();
//        testTwo.setStrStringTestwo("HRD");
//        testTwo.setListMenu(listTestThree);
//        listTestTwo.add(testTwo);
//
//        listTestThree = new ArrayList<TestThree>();
//
//        testThree.setIdSubMenu("9");
//        testThree.setNamaSubMenu("FINANCEONE");
//        testThree.setStrStringTestThree("TEST FINANCE ONE");
//        testThree.setLinkSubMenu("/api/menu/financeone");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("10");
//        testThree.setNamaSubMenu("FINANCETWO");
//        testThree.setStrStringTestThree("TEST FINANCE TWO");
//        testThree.setLinkSubMenu("/api/menu/financetwo");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("11");
//        testThree.setNamaSubMenu("FINANCE THREE");
//        testThree.setStrStringTestThree("TEST FINANCE THREE");
//        testThree.setLinkSubMenu("/api/menu/financethree");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("12");
//        testThree.setNamaSubMenu("FINANCE FOUR");
//        testThree.setStrStringTestThree("TEST FINANCE FOUR");
//        testThree.setLinkSubMenu("/api/menu/financefour");
//        listTestThree.add(testThree);
//
//        testTwo = new TestTwo();
//        testTwo.setStrStringTestwo("FINANCE");
//        testTwo.setListMenu(listTestThree);
//        listTestTwo.add(testTwo);
//
//        listTestThree = new ArrayList<TestThree>();
//
//        testThree.setIdSubMenu("13");
//        testThree.setNamaSubMenu("USRMGMNT ONE");
//        testThree.setStrStringTestThree("TEST USERMANAGEMENT ONE");
//        testThree.setLinkSubMenu("/api/menu/usrone");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("14");
//        testThree.setNamaSubMenu("USRMGMNT TWO");
//        testThree.setStrStringTestThree("TEST USERMANAGEMENT TWO");
//        testThree.setLinkSubMenu("/api/menu/usrtwo");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("15");
//        testThree.setNamaSubMenu("USRMGMNTTHREE");
//        testThree.setStrStringTestThree("TEST USERMANAGEMENT THREE");
//        testThree.setLinkSubMenu("/api/menu/usrthree");
//        listTestThree.add(testThree);
//
//        testThree = new TestThree();
//        testThree.setIdSubMenu("16");
//        testThree.setNamaSubMenu("USRMGMNT FOUR");
//        testThree.setStrStringTestThree("TEST USERMANAGEMENT FOUR");
//        testThree.setLinkSubMenu("/api/menu/usrfour");
//        listTestThree.add(testThree);
//
//        testTwo = new TestTwo();
//        testTwo.setStrStringTestwo("USER MANAGEMENT");
//        testTwo.setListMenu(listTestThree);
//        listTestTwo.add(testTwo);
//
//        TestOne testOne = new TestOne();
//        testOne.setStrTestOne("ROLE ADMIN");
//        testOne.setListTestTwo(listTestTwo);
//
//        String x="<li>\n" +
//                "    <a href=\"#\">SALES</a>\n" +
//                "<ul class=\"menu-dropdown\">\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/salesone</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/salestwo</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/salesthree</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/hrone</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "</ul>\n" +
//                "</li>\n" +
//                "<li >\n" +
//                "<a href=\"#\">HRD</a>\n" +
//                "<ul class=\"menu-dropdown\">\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/hrone</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/hrtwo</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/hrthree</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/financeone</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "</ul>\n" +
//                "</li>\n" +
//                "<li >\n" +
//                "    <a href=\"#\">FINANCE</a>\n" +
//                "<ul class=\"menu-dropdown\">\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/financeone</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/financetwo</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/financethree</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "        <li>\n" +
//                "            <div>\n" +
//                "                <span>/api/menu/financefour</span>\n" +
//                "            </div>\n" +
//                "        </li>\n" +
//                "</ul>\n" +
//                "</li>";
//
//        model.addAttribute("AKZEZ",listTestTwo);
////        model.addAttribute("tagHTMLz",x);
//        request.setAttribute("LINK_MENU",x,1);
//
//        return "test_aja";
//    }
//}