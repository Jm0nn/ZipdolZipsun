<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<%@ include file="/WEB-INF/views/included/head.jsp" %>
</head>
<body>
    <!-- header -->
   
    <%@ include file="/WEB-INF/views/included/nav.jsp"%>
    <!-- header -->
    <main>
      <div style="height: 70px"></div>
      <div class="img">
        <img src="${root}/assets/image/apt.jpg" alt="apt image" style="width: 100%" />
      </div>

      <div class="container">
        <div style="text-align: center">
          <a
            class="btn btn-outline-secondary m-3 find-home"
            href="${root}/house">
            아파트 검색
          </a>
        </div>
       </div>
      <div style="height: 300px"></div>
    </main>
    <!-- footer -->
 	<%@ include file="/WEB-INF/views/included/foot.jsp"%>
    <!-- footer -->


    
