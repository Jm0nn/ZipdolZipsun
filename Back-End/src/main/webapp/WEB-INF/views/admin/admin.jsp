<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/included/head.jsp" %>
<body>
     <!-- header -->
    <%@ include file="/WEB-INF/views/included/nav.jsp"%>
    <!-- header -->
    <div style="margin-top: 70px">
    <div class="row justify-content-center">
        <div class="col-lg-8 col-md-10 col-sm-12">
          <h2 class="my-3 py-3 shadow-sm bg-light text-center">
            <mark class="sky">회원목록</mark>
          </h2>
        </div>
        <div class="col-lg-8 col-md-10 col-sm-12">
          <div class="row align-self-center mb-2">
            <div class="col-md-2 text-start">
            <button
                type="button"
                class="btn btn-outline-primary"
                data-bs-toggle="modal"
                data-bs-target="#registerModal"
              >
                회원 등록
              </button>
            </div>
            <div class="col-md-7 offset-3">
              
            </div>
          </div>
          <table class="table table-hover">
            <thead>
              <tr class="text-center">
                <th scope="col">아이디</th>
                <th scope="col">이름</th>
                <th scope="col">비밀번호</th>
                <th scope="col">가입일</th>
                <th scope="col">수정/삭제</th>
              </tr>
            </thead>
            <tbody id="userlist"></tbody>
          </table>
        </div>
        <div class="row">
          
        </div>
      </div>
      </div>
      <!-- 회원 등록 Modal -->
    <div
      class="modal fade"
      id="registerModal"
      data-bs-backdrop="static"
      data-bs-keyboard="false"
      tabindex="-1"
      aria-labelledby="staticBackdropLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title"><mark class="purple">회원 등록</mark></h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label for="username" class="form-label">이름 : </label>
              <input type="text" class="form-control" id="username" placeholder="이름..." />
            </div>
            <div class="mb-3">
              <label for="userid" class="form-label">아이디 : </label>
              <input type="text" class="form-control" id="userid" placeholder="아이디..." />
            </div>
            <div id="idcheck-result"></div>
            <div class="mb-3">
              <label for="userpwd" class="form-label">비밀번호 : </label>
              <input type="password" class="form-control" id="userpwd" placeholder="비밀번호..." />
            </div>
            <div class="mb-3">
              <label for="pwdcheck" class="form-label">비밀번호확인 : </label>
              <input
                type="password"
                class="form-control"
                id="pwdcheck"
                placeholder="비밀번호확인..."
              />
            </div>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              id="btn-join"
              class="btn btn-outline-primary"
              data-bs-dismiss="modal"
            >
              회원가입
            </button>
            <button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal">
              닫기
            </button>
          </div>
        </div>
      </div>
    </div>
    <!-- 회원 정보 모달 -->
    <div
      class="modal fade"
      id="userViewModal"
      data-bs-backdrop="static"
      data-bs-keyboard="false"
      tabindex="-1"
      aria-labelledby="staticBackdropLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title"><mark class="purple">회원 정보</mark></h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <table class="table table-bordered">
              <colgroup>
                <col width="120" />
                <col width="*" />
                <col width="120" />
                <col width="*" />
              </colgroup>
              <tbody>
                <tr>
                  <th class="text-center">ID</th>
                  <td class="text-left" id="vid"></td>
                  <th class="text-center">회원명</th>
                  <td class="text-left" id="vname"></td>
                </tr>
                <tr>
                  <th class="text-center">가입일</th>
                  <td class="text-left" id="vjoindate" colspan="3"></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
     <script>
 	 	// 회원 목록.
    	 fetch(`${root}/admin/user`)
     		 .then((response) => response.json())
      		 .then((data) => makeUserList(data));
 	 
 	 	// 아이디 체크
    	 let isUseId = false;
         document.querySelector("#userid").addEventListener("keyup", function () {
     		let userid = this.value;
     		console.log(userid);
       	 	let resultDiv = document.querySelector("#idcheck-result");
       	 	if(userid.length < 5 || userid.length > 16) {
       		 	resultDiv.setAttribute("class", "mb-3 text-dark");
       		 	resultDiv.textContent = "아이디는 5자 이상 16자 이하 입니다.";
       		 	isUseId = false;
       	 	} else {
       		 	fetch("${root}/member/check/" + userid)
     	   		.then(response => response.text())
     	   		.then(data => {
     	   			console.log(data);
     		 		if(data == 0) {
     		   			resultDiv.setAttribute("class", "mb-3 text-primary");
     	       			resultDiv.textContent = userid + "는 사용할 수 있습니다.";
     	       			isUseId = true;
     		 		} else {
     		   			resultDiv.setAttribute("class", "mb-3 text-danger");
      		       		resultDiv.textContent = userid + "는 사용할 수 없습니다.";
      		     		isUseId = false;
     		 		}
       		   });
       	 	}
         });
 	 
    	// 회원 등록.
         document.querySelector("#btn-join").addEventListener("click", function () {
        	let userId = document.querySelector("#userid").value;
        	let userName = document.querySelector("#username").value;
        	let userPass = document.querySelector("#userpwd").value;
        	let pwdCheck = document.querySelector("#pwdcheck").value;
        	if (!userId) {
        		alert("아이디 입력!!");
        		return;
        	} else if (!userPass) {
        		alert("비밀번호 입력!!");
                return;
        	} else if (!userName) {
        		alert("이름 입력!!");
            	return;
        	} else if (userPass != pwdCheck) {
        		alert("비밀번호 확인!!");
                return;
        	} else if (!isUseId) {
        		alert("아이디 확인!!");
            	return;
        	} else {
           let registerinfo = {
             userName: userName,
             userId: userId,
             userPass: userPass,
           };
           let config = {
             method: "POST",
             headers: {
               "Content-Type": "application/json",
             },
             body: JSON.stringify(registerinfo),
           };
           fetch(`${root}/admin/user`, config)
             .then((response) => response.json())
             .then((data) => makeUserList(data));
        	}
         });
 	 
       //회원 정보 보기.
         function userView(el) {
           var modal = new bootstrap.Modal(document.getElementById("userViewModal"), {
             backdrop: true,
             keyboard: false,
           });
           let id = el.getAttribute("data-id");
           fetch(`${root}/admin/user/${"${id}"}`)
             .then((response) => response.json())
             .then((data) => {
               document.querySelector("#vid").innerText = data.userId;
               document.querySelector("#vname").innerText = data.userName;
               document.querySelector("#vjoindate").innerText = data.joinDate;
               modal.show();
             });
         }
       
       
      // 회원 정보 수정 보기.
         function viewModify(el) {
           let id = el.parentNode.parentNode.getAttribute("data-id");
           console.log(id);
           document.querySelector(`#view_${"${id}"}`).setAttribute("style", "display: none;");
           document.querySelector(`#mview_${"${id}"}`).setAttribute("style", "display: ;");
         }
      
      // 회원 정보 수정 닫기.
         function closeModify(el) {
           let id = el.parentNode.parentNode.getAttribute("data-id");
           document.querySelector(`#view_${"${id}"}`).setAttribute("style", "display: ;");
           document.querySelector(`#mview_${"${id}"}`).setAttribute("style", "display: none;");
         }
 	 
      // 회원 정보 수정.
         function userModify(el) {
           let id = el.parentNode.parentNode.getAttribute("data-id");
           let pwd = document.querySelector(`#userpwd${"${id}"}`).value;
           let modifyinfo = {
             userId: id,
             userPass: pwd,
           };
           console.log(modifyinfo);
           let config = {
             method: "PUT",
             headers: {
               "Content-Type": "application/json",
             },
             body: JSON.stringify(modifyinfo),
           };
           fetch(`${root}/admin/user`, config)
             .then((response) => response.json())
             .then((data) => makeUserList(data));
         }
 	 
      // 회원 삭제.
         function userDelete(el) {
           if (confirm("정말 삭제하시겠습니까?")) {
             let id = el.parentNode.parentNode.getAttribute("data-id");
             let config = {
               method: "DELETE",
               headers: {
                 "Content-Type": "application/json",
               },
             };
             fetch(`${root}/admin/user/${"${id}"}`, config)
               .then((response) => response.json())
               .then((data) => makeUserList(data));
           }
         }
 	 
      // 회원 목록 tbody
    	 function makeUserList(users) {
     		 let tbody = ``;
      		 users.forEach(function (user) {
        	 tbody += `
						<tr id="view_${"${user.userId}"}" class="view text-center" data-id="${"${user.userId}"}" ondblclick="userView(this);">
							<td>${"${user.userId}"}</td>
							<td>${"${user.userName}"}</td>
							<td>******************</td>
							<td>${"${user.joinDate}"}</td>
							<td>
								<button type="button" class="modifyViewBtn btn btn-outline-primary btn-sm" onclick="viewModify(this);">수정</button>
						`;
			if (`${"${user.userId}"}` != "admin") {
				tbody += `<button type="button" class="deleteBtn btn btn-outline-danger btn-sm" onclick="userDelete(this);">삭제</button>`;
			};
			tbody += `</td>
						</tr>
						<tr id="mview_${"${user.userId}"}" data-id="${"${user.userId}"}" style="display: none;" class="text-center">
							<td>${"${user.userId}"}</td>
							<td>${"${user.userName}"}</td>
							<td><input type="password" name="userpwd" id="userpwd${"${user.userId}"}" value=""></td>
							<td>${"${user.joinDate}"}</td>
							<td>
								<button type="button" class="modifyBtn btn btn-primary btn-sm" onclick="userModify(this);">수정</button>
								<button type="button" class="cancelBtn btn btn-danger btn-sm" onclick="closeModify(this);">취소</button>
							</td>
						</tr>
					`;
      		 });
      		document.querySelector("#userlist").innerHTML = tbody;
     	}
 	 
 	 
    </script>
    
    <%@ include file="/WEB-INF/views/included/foot.jsp"%>