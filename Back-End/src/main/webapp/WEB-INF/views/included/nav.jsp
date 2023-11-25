<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<header>
	<nav class="navbar bg-body-secondary fixed-top" style="height: 70px">
		<a class="navbar-brand nav-item ms-5" href="/">
			🏠 Happy House
		</a>
		<a class="nav-item text-secondary text-decoration-none ms-3"
			href="${root}/announce/list?pgno=1&key=&word=">
			공지사항
		</a>
		<a class="nav-item text-secondary text-decoration-none ms-3" href="#">
			게시판
		</a>
		<a class="nav-item text-secondary text-decoration-none ms-3"
			href="${root}/announce/house">
			아파트검색
		</a>
		<c:choose>
			<c:when test="${empty loginUser}">
				<a class="nav-item btn btn-secondary ms-auto"
					href="${root}/member/regist"> Sign up </a>
				<a class="nav-item btn btn-outline-secondary ms-3 me-5"
					href="${root}/member/login"> Login </a>
			</c:when>
			<c:otherwise>
				<div class="nav-item ms-auto">
					<a href="${root}/member/modify" style="text-decoration: none">${loginUser.userId}</a>님 반갑습니다.
				</div>
				<c:if test="${loginUser.userId == 'admin'}">
					<a class="nav-item btn btn-outline-secondary ms-3"
					href="${root}/member/admin"> 관리자 </a>
				</c:if>
				<a class="nav-item btn btn-outline-danger ms-3 me-5" href="${root}/member/logout"> 로그아웃 </a>
			</c:otherwise>
		</c:choose>
	</nav>
</header>