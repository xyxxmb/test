<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
      <script src="http://apps.bdimg.com/libs/jquery/1.11.1/jquery.js"></script>
      <!--引入jQuery库
      -->
  </head>
  
  <body>
  <div class="htmleaf-container">
      <div id="wrapper" class="login-page">
          <div id="login_form" class="form">
              <form class="login-form">
                  <p class="message">欢 迎 登 陆</p>
                  <input type="text" placeholder="用户名" id="user_name" name="user_name"/>
                  <input type="password" placeholder="密码" id="password"name="password"/>
                  <button type="submit" id="login" onclick="check_login()">登　录</button>
              </form>
          </div>
      </div>
  </div>


  <script type="text/javascript">
      function check_login(){
          var user=document.getElementsByName("user_name");
          var pass=document.getElementsByName("password");
          console.log(user[0].value.trim() + ", " + pass[0].value.trim());

          if(user[0].value.trim()==""||pass[0].value.trim()==""){
              alert("用户名或密码不能为空");
          }
          else{
              console.log("1233");
              $.ajax({
                  type: "POST",
                  url: "http://localhost:8080/SecondClass/user/web_login",
                  data:{
                      userId:parseInt($("user_name").val()),
                      password:$("password").val()
                  },
                  dataType: "json",
                  success: function(data) {
                      if (data.success == '1') {
                          window.location.href="http://www.baidu.com";
                      } else {
                          alert("用户名或密码错误");
                      }
                  },

              });
          }




      }





  </script>
  </body>
</html>
