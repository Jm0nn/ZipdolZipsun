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
    <div style="margin-top: 70px"></div>
      <div class="row justify-content-center">
        <div class="col-lg-8 col-md-10 col-sm-12">
          <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          </h2>
        </div>
          <div class="card text-center bg-light">
            <h2 class="fw-bold text-danger pt-3">${pass}</h2>
            <div class="card-body">
              <p class="card-text">
              </p>
              <button type="button" id="btn-index" class="btn btn-outline-danger">
                메인 페이지 이동...
              </button>
            </div>
          </div>
        </div>
    <script>
      document.querySelector("#btn-index").addEventListener("click", function () {
        location.href = "/";
      });
    </script>
    <!-- footer -->
 	<%@ include file="/WEB-INF/views/included/foot.jsp"%>
    <!-- footer -->

