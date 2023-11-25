<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
<%@ include file="/WEB-INF/views/included/head.jsp" %>
</head>
<body>
     <!-- header -->
    <%@ include file="/WEB-INF/views/included/nav.jsp"%>
    <!-- header -->
    <div style="margin-top: 70px">


<div class="row justify-content-center">
        <div class="col-lg-8 col-md-10 col-sm-12">
          <h2 class="my-3 py-3 shadow-sm bg-light text-center">
            <mark class="sky">글수정</mark>
          </h2>
        </div>
        <div class="col-lg-8 col-md-10 col-sm-12">
          <form id="form-modify" method="POST" action="">
          	<input type="hidden" name="cmd" value="modify">
          	<input type="hidden" name="articleNo" value="${article.articleNo}">
            <div class="mb-3">
              <label for="subject" class="form-label">제목 : </label>
              <input type="text" class="form-control" id="subject" name="subject" value="${article.subject}" />
            </div>
            <div class="mb-3">
              <label for="content" class="form-label">내용 : </label>
              <textarea class="form-control" id="content" name="content" rows="7">${article.content}</textarea>
            </div>
            <div class="col-auto text-center">
              <button type="button" id="btn-modify" class="btn btn-outline-primary mb-3">
                글수정
              </button>
              <button type="button" id="btn-list" class="btn btn-outline-danger mb-3">
                목록으로이동...
              </button>
            </div>
            <input type="hidden" id="pgno" name="pgno" value="${pgno}">
    		<input type="hidden" id="key" name="key" value="${key}">
    		<input type="hidden" id="word" name="word" value="${word}">
          </form>
        </div>
      </div>
    </div>
    <form id="form-param" method="get" action="">
    	<input type="hidden" id="pgno2" name="pgno" value="${pgno}">
    	<input type="hidden" id="key2" name="key" value="${key}">
    	<input type="hidden" id="word2" name="word" value="${word}">
    </form>
    <script>
      document.querySelector("#btn-modify").addEventListener("click", function () {
        if (!document.querySelector("#subject").value) {
        	alert("제목 입력!!");
          return;
        } else if (!document.querySelector("#content").value) {
        	alert("내용 입력!!");
          return;
        } else {
          let form = document.querySelector("#form-modify");
          form.setAttribute("action", "${root}/announce/modify");
          form.submit();
        }
      });
      document.querySelector("#btn-list").addEventListener("click", function () {
        let form = document.querySelector("#form-param");
        form.setAttribute("action", "${root}/announce/list");
        form.submit();
      });
    </script>



    <%@ include file="/WEB-INF/views/included/foot.jsp"%>