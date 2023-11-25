<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
<%@ include file="/WEB-INF/views/included/head.jsp" %>
</head>
<body>
     <!-- header -->
    <%@ include file="/WEB-INF/views/included/nav.jsp"%>
    <!-- header -->
    <main>
      <div class="container login-page">
        <div class="form">
          <form method="post" id="regist-form" name="regist-form" action="">
            <input id="userId" name="userId" type="text" placeholder="id" />
            <div id="idcheck-result"></div>
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
            <input id="userName" name="userName" type="text" placeholder="name" />
            <button type="button" id="btn-regist" class="btn btn-outline-primary mb-3">
                회원가입
              </button>
          </form>
        </div>
      </div>
    </main>
     <script>
     let isUseId = false;
     document.querySelector("#userId").addEventListener("keyup", function () {
 		let userId = this.value;
 		console.log(userId);
   	 	let resultDiv = document.querySelector("#idcheck-result");
   	 	if(userId.length < 5 || userId.length > 16) {
   		 	resultDiv.setAttribute("class", "mb-3 text-dark");
   		 	resultDiv.textContent = "아이디는 5자 이상 16자 이하 입니다.";
   		 	isUseId = false;
   	 	} else {
   		 	fetch("${root}/member/check/" + userId)
 	   		.then(response => response.text())
 	   		.then(data => {
 	   			console.log(data);
 		 		if(data == 0) {
 		   			resultDiv.setAttribute("class", "mb-3 text-primary");
 	       			resultDiv.textContent = userId + "는 사용할 수 있습니다.";
 	       			isUseId = true;
 		 		} else {
 		   			resultDiv.setAttribute("class", "mb-3 text-danger");
  		       		resultDiv.textContent = userId + "는 사용할 수 없습니다.";
  		     		isUseId = false;
 		 		}
   		   });
   	 	}
     });
     
     
      document.querySelector("#btn-regist").addEventListener("click", function () {
        if (!document.querySelector("#userId").value) {
          alert("아이디 입력!!");
          return;
        } else if (!document.querySelector("#userPass").value) {
          alert("비밀번호 입력!!");
          return;
        } else if (!document.querySelector("#userName").value) {
        	alert("이름 입력!!");
        	return;
      	} else if (document.querySelector("#userPass").value != document.querySelector("#pwdcheck").value) {
            alert("비밀번호 확인!!");
            return;
        } else if (!isUseId) {
        	alert("아이디 확인!!");
        	return;
        } else {
          let form = document.querySelector("#regist-form");
          form.setAttribute("action", "${root}/member/regist");
          form.submit();
        }
      });
    </script>
	<%@ include file="/WEB-INF/views/included/foot.jsp"%>
