<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/included/head.jsp"%>
<body>
<c:if test="${cookie.saveid.value ne null}">
	<c:set var="idck" value=" checked"/>
	<c:set var="save" value="${cookie.saveid.value}"/>
</c:if>
	<!-- header -->
	<%@ include file="/WEB-INF/views/included/nav.jsp"%>
	<!-- header -->
	<main>
	<div class="container login-page">
		<div class="form">
			<form method="post" id="login-form" name="login-form" action="">
			<div class="d-flex justify-content-center">
			<div>
				<input type="checkbox" id="saveid" name="saveid" ${idck} />
				</div>
				<div>
				<label class="form-check-label ms-2" for="saveid">아이디저장</label>
				</div>
			</div>
				<input id="userId" name="userId" type="text" placeholder="id" value="${save}" />
				<input id="userPass" name="userPass" type="password"
					placeholder="password" />
				<button id="btn-login" type="button">로그인</button>
				<button id="btn-regist" onclick="location.href='${root}/member/regist'" type="button">회원가입</button>
				<button id="btn-find" onclick="location.href='${root}/member/find'" type="button">비밀번호 찾기</button>
			</form>
		</div>
	</div>
	</main>
	<script>
      document.querySelector("#btn-login").addEventListener("click", function () {
        if (!document.querySelector("#userId").value) {
          alert("아이디 입력!!");
          return;
        } else if (!document.querySelector("#userPass").value) {
          alert("비밀번호 입력!!");
          return;
        } else {
          let form = document.querySelector("#login-form");
          form.setAttribute("action", "${root}/member/login");
          form.submit();
        }
      });
    </script>
	<%@ include file="/WEB-INF/views/included/foot.jsp"%>