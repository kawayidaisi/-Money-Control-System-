package 个人财政管理系统;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.cj.xdevapi.Statement;
public class MysqlConnection {//建立sql连接
	public static final String DBDRIVER="com.mysql.cj.jdbc.Driver";
	public static final String DBURL="jdbc:mysql:"+"//127.0.0.1:3306/moneysystem?useSSL=false&serverTimezone=Asia/Shanghai";
	public static final String DBUSER="root";
	public static final String DBPASS="123456";
    public  Connection getConnection(){
    	Connection con=null;
        try{
            Class.forName(DBDRIVER);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        try{
            con=DriverManager.getConnection(DBURL,DBUSER,DBPASS);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return con;
    }
}
