<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<%@ include file="/WEB-INF/views/included/head.jsp" %>
</head>
<body>
    <!-- header -->
   
    <%@ include file="/WEB-INF/views/included/nav.jsp"%>
    <main>
      <div class="container login-page">
        <div class="form">
          <form method="post" id="regist-form" name="regist-form" class="regist-form" action="">
            <input id="userId" name="userId" type="text" placeholder="ID" />
        
             <button id="btn-regist" type="button">비밀번호 찾기</button>
		</form>
        </div>
      </div>
    </main>
    <script>
      document.querySelector("#btn-regist").addEventListener("click", function () {
        if (!document.querySelector("#userId").value) {
          alert("아이디 입력!!");
          return;
        } else {
          let form = document.querySelector("#regist-form");
          form.setAttribute("action", "${root}/member/find");
          form.submit();
        }
      });
    </script>
 	<%@ include file="/WEB-INF/views/included/foot.jsp"%>