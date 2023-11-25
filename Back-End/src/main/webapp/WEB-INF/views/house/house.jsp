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
      <div style="height: 70px"></div>
      <div class="row mx-5 my-2">
        <div class="form-group col-2">
          <select class="form-select selectBox" id="sido">
            <option value="">시도선택</option>
          </select>
        </div>
        <div class="form-group col-2">
          <select class="form-select selectBox" id="gugun">
            <option value="">구군선택</option>
          </select>
        </div>
        <div class="form-group col-2">
          <select class="form-select selectBox" id="dong">
            <option value="">동선택</option>
          </select>
        </div>
        <div class="form-group col-2">
          <select class="form-select selectBox" id="year">
            <option value="">매매년선택</option>
          </select>
        </div>
        <div class="form-group col-2">
          <select class="form-select selectBox" id="month">
            <option value="">매매월선택</option>
          </select>
        </div>
        <div class="form-group col-2 d-block text-center">
          <button id="list-btn" class="btn btn-warning">매매정보검색</button>
        </div>
      </div>
      <div class="container-fluid col">
        <div class="row">
          <div
            class="mapBox col-lg-2 col-md-3 col-xl-2 px-sm-auto px-0 min-vh-100"
            id="map"></div>
          <div class="col-md" style="height: 930px; overflow-y: auto">
            <table class="table table-hover text-center">
              <thead id="apttitle">
                <!-- <tr>
                  <th>아파트이름</th>
                  <th>층</th>
                  <th>면적</th>
                  <th>법정동</th>
                  <th>거래금액</th>
                </tr> -->
              </thead>
              <tbody id="aptlist"></tbody>
            </table>
          </div>
        </div>
      </div>
      <div style="height: 50px"></div>
    </main>
    <!-- footer -->
    
   
 	<%@ include file="/WEB-INF/views/included/foot.jsp"%>
    <!-- footer -->

