<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
            <input id="userId" name="userId" type="text" value="${loginUser.userId}" readonly />
            <input
              id="userPass"
              name="userPass"
              type="password"
              placeholder="password" />
              <input
              id="pwdcheck"
              name="pwdcheck"
              type="password"
              placeholder="password 확인" />
            <input id="name" name="name" type="text" value="${loginUser.userName}" readonly />
            <button id="btn-regist" type="button">수정</button>
        
          <c:if test="${loginUser.userId != 'admin'}">    
          	<button id="btn-delete" type="button">회원탈퇴</button>
          </c:if>
		</form>
        </div>
      </div>
    </main>
    <script>
      document.querySelector("#btn-regist").addEventListener("click", function () {
        if (!document.querySelector("#userPass").value) {
          alert("비밀번호 입력!!");
          return;
        } else if (document.querySelector("#userPass").value != document.querySelector("#pwdcheck").value) {
            alert("비밀번호 확인!!");
            return;
        } else {
          let form = document.querySelector("#regist-form");
          form.setAttribute("action", "${root}/member/modify");
          form.submit();
        }
      });
      
      document.querySelector("#btn-delete").addEventListener("click", function () {

    	  if (confirm("정말로 삭제하겠습니까?")) {
            let form = document.querySelector("#regist-form");
            form.setAttribute("method", "get");
            form.setAttribute("action", "${root}/member/delete");
            form.submit();
    	  }
        });
    </script>
 	<%@ include file="/WEB-INF/views/included/foot.jsp"%>
 	
 	