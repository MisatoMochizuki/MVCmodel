package controller;

/**
 * 社員情報管理コントローラー
 *
 * @author s.nanaumi
 * @since 2019/12/02
 *
 */

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.EmployeeBean;
import service.EmployeeService;
//webサーバー上で動かすため
//「doPost」メソッドはクライアントからデータが送られてくる場合に呼び出されます。
public class EmployeeController extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String id = request.getParameter("id");
            String password = request.getParameter("password");


            EmployeeService employeeService = new EmployeeService();


            //EmployeeServiceでserchメソッドの戻り値がemployeeDataになっていたので、それをemployeeBeanに格納
            EmployeeBean employeeBean = employeeService.search(id, password);


            //EmployeeBeanをkeyとしてjspに呼び出すことにより、値employeeBeaが画面に出力

            request.setAttribute("EmployeeBean", employeeBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ServletContext context = this.getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
    }
}
