package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import bean.EmployeeBean;


public class EmployeeService {


    /** ドライバーのクラス名 */
    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";

    private static final String JDBC_CONNECTION = "jdbc:postgresql://localhost:5432/lesson_db";
    /** ・ユーザー名 */
    private static final String USER = "postgres";
    /** ・パスワード */
    private static final String PASS = "postgres";
    /** ・タイムフォーマット */
    private static final String TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";


    /** ・SQL UPDATE文 */
    private static final String SQL_UPDATE = "UPDATE employee SET login_time = ? WHERE id = ?";


    /** ・SQL SELECT文 */
    private static final String SQL_SELECT = "SELECT * FROM employee WHERE id = ? AND password = ?";

    //合致するものがあればemployeeDataにテーブルを格納し、なければ、 employeeData を null で返す。
    EmployeeBean employeeData = null;

    // 送信されたIDとPassWordを元に社員情報を検索するためのメソッド
    public EmployeeBean search(String id, String password) {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            // データベースに接続
        	// まずJDBCドライバを読み込みます。
        	//forNameはクラス名から Class オブジェクトを取得するためのメソッド
            Class.forName(POSTGRES_DRIVER);
            // コネクションを取得します。
            connection = DriverManager.getConnection(JDBC_CONNECTION, USER, PASS);
            statement = connection.createStatement();

            // 処理が流れた時間をフォーマットに合わせて生成
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdFormat = new SimpleDateFormat(TIME_FORMAT);

            // PreparedStatementで使用するため、String型に変換
            String login_time = sdFormat.format(cal.getTime());

            /*
             *  任意のユーザーのログインタイムを更新できるように、プリペアドステートメントを記述。
             */

            // preparedStatementに実行したいSQLを格納
            //preparedStatementはSQL文を受け取って解析し、値があればいつでも実行できる
            preparedStatement = connection.prepareStatement(SQL_UPDATE);

            preparedStatement.setString(1,login_time);
            preparedStatement.setString(2,id);


            preparedStatement.executeUpdate();

            /*
             *  UPDATEが成功したものを即座に表示
             *  任意のユーザーを検索できるように、プリペアドステートメントを記述。
             */
            preparedStatement = connection.prepareStatement(SQL_SELECT);

            //　.setInt(パラメータのインデックス, パラメータ値);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,password);

            // SQLを実行。実行した結果をresultSetに格納。
           //SELECT文のSQLの実行には executeQuery() メソッドを使用しましたが、INSERT文、UPDATE文、DELETE文の場合は、 executeUpdate() メソッドを使用します。
            resultSet = preparedStatement.executeQuery();

            //SQLの実行結果を格納したResultSetインタフェース型のオブジェクトを返します。ResultSetはSQLインターフェースは 実行結果を格納 し、その 情報も取得できる メソッドも備えているということです。
            //ResultSetインタフェースのresultSetオブジェクトは初期状態では、最初の行の1つ前に位置していますので、必ず next()メソッド （次の行に移動する）で最初の行に移動してやる必要があります。
            while (resultSet.next()) {

                String tmpName = resultSet.getString("name");
                String tmpComment = resultSet.getString("comment");
                String tmpLoginTime = resultSet.getString("login_time");


                employeeData = new EmployeeBean();
                employeeData.setName(tmpName);
                employeeData.setComment(tmpComment);
                employeeData.setLogin_Time(tmpLoginTime);
            }

            // forName()で例外発生 クラス が見つからなかった場合
            // JDBCドライバが存在しなかった場合に投げられます
            //スタックトレースとはエラーが発生したときに表示される内容で、そのエラーが発生するまでの過程（どんな処理がどの順番で呼び出されたかの流れ）を、ざっくりと表示したもの
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            // getConnection()、createStatement()、executeQuery()で例外発生
            //データベースアクセス中に発生したエラー

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
//何らかの理由でDBにつながらない／テーブルにアクセスできない／データが取れない事態が発生しても、finallyは必ず実行される。
            	//この場合、ResultSet、PreparedStatement、ConnectionはNULLの可能性があり、NULL判定を入れないとNullPointerExceptionで落ちる。（
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
              //データベースアクセス中に発生したエラー
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return employeeData;
    }
}
