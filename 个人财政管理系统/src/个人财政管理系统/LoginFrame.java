package 个人财政管理系统;

import java.awt.Container;
import java.sql.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//登录界面
public class LoginFrame extends JFrame implements ActionListener{
	private JLabel l_user,l_pwd; //用户名标签，密码标签
	private JTextField t_user;//用户名文本框
	private JPasswordField t_pwd; //密码文本框
	private JButton b_ok,b_cancel,b_forget,b_travel; //登录按钮，退出按钮,注册按钮,游客登陆
	private String name;
	private String password;
	private int Flag,Time;
	String datestr;
	public LoginFrame(){
		super("欢迎使用个人理财账本!");
		l_user=new JLabel("用户名：",JLabel.RIGHT);
		l_pwd=new JLabel("  密码：",JLabel.RIGHT);
		t_user=new JTextField(31);
		t_pwd=new JPasswordField(31);
		b_ok=new JButton("登录");
		b_cancel=new JButton("退出");
		b_forget=new JButton("忘记密码");
		b_travel=new JButton("游客");
		//背景添加
		ImageIcon img = new ImageIcon("src/2.jpg");
		JLabel jl_bg = new JLabel(img);
		jl_bg.setBounds(1, 1, 300, 170); 
		this.getLayeredPane().add(jl_bg, new Integer(Integer.MIN_VALUE));
		((JPanel)this.getContentPane()).setOpaque(false); 
		
		//布局方式FlowLayout，一行排满排下一行
		Container c=this.getContentPane();
		c.setLayout(new FlowLayout()); 
		c.add(l_user);
		c.add(t_user);
		c.add(l_pwd);
		c.add(t_pwd);
		c.add(b_ok);
		c.add(b_cancel);
		c.add(b_forget);
		c.add(b_travel);
		//为按钮添加监听事件
		getRootPane().setDefaultButton(b_ok); //添加回车确认功能
		b_ok.addActionListener(this);
		b_cancel.addActionListener(this);
		b_forget.addActionListener(this);
		b_travel.addActionListener(this);
		
		//界面大小不可调整 
		this.setResizable(false);
		this.setSize(300,170);
		//界面显示居中
		Dimension screen = this.getToolkit().getScreenSize();
	        this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	public void actionPerformed(ActionEvent e) {
		if(b_cancel==e.getSource()){
			int isCancel = JOptionPane.showConfirmDialog(null, "确定退出","提示", JOptionPane.YES_NO_OPTION);//退出确认
			if(isCancel == JOptionPane.YES_OPTION)System.exit(0);
			
		}else if(b_ok==e.getSource()){//锁定账号功能
			Date date = new Date();//取得当前时间
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			datestr  = format.format(date);
			name=t_user.getText();//取得输入的账号密码
			password=new String(t_pwd.getPassword());
			Date s_date=getDate();
			int time=getTimes();
			int flag=getFlag();
			Long timeSlot=date.getTime()-s_date.getTime();
			if(name.matches("\\s*")) {//用户名空白的处理；
				JOptionPane.showMessageDialog(null, "用户名不能为空");
				t_user.setText("");
				t_pwd.setText("");
			}else if(login()==true) {//登陆验证
				if(flag!=1) {
					JOptionPane.showMessageDialog(null, "登陆成功");
					Flag=0;Time=0;
					setFlag();setTimes();
					new MainFrame(t_user.getText().trim());
					dispose();
				}else {
					if(timeSlot < 180000) {//180s
						JOptionPane.showMessageDialog(null, "用户已被锁定请"+ (180-Math.ceil((double)timeSlot/1000)) +"秒之后再次尝试");
						t_pwd.setText("");
					}else {
						JOptionPane.showMessageDialog(null, "登陆成功");
						Flag=0;Time=0;
						setFlag();setTimes();
						new MainFrame(t_user.getText().trim());
						dispose();
					}
				}
			}else if(login()==false){
				if(!name.equals("root")) {
					JOptionPane.showMessageDialog(null, "账号错误");
					t_user.setText("");
					t_pwd.setText("");
				}else {
					if(flag!=1) {
						if(time==4) {
							JOptionPane.showMessageDialog(null, "密码错误账号已锁定");
							Flag=1;Time=5;
							setFlag();setTimes();
							t_pwd.setText("");
						}else {
							Time=time+1;
							setTimes();
							setDate();
							JOptionPane.showMessageDialog(null, "密码错误,您还有" + (5-Time) +"次登陆机会");
							t_pwd.setText("");
						}
					}else {
						if(timeSlot < 180000) {//180s
							JOptionPane.showMessageDialog(null, "用户已被锁定请"+ (180-Math.ceil((double)timeSlot/1000)) +"秒之后再次尝试");
							t_pwd.setText("");
						}else {
							JOptionPane.showMessageDialog(null, "密码错误账号已锁定");
							Flag=1;Time=5;
							setFlag();setTimes();setDate();
							t_pwd.setText("");
						}
					}
				}
			}
		}else if(b_forget==e.getSource()) {
			new Forgetpw();
		}else if(b_travel==e.getSource()) {
			JOptionPane.showMessageDialog(null, "游客身份登陆成功");
			new MainFrame("");
			dispose();
		}
	}
	
	public boolean login() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result=false;
		String sql = "select * from login where User=? and PSW=?";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			rs = ps.executeQuery();	
			if (rs.next()) {//验证成功
				result=true;
			}
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
				 if (rs != null)
					 rs.close();
				 if (ps != null)
					 ps.close();
				 if (con != null)
					 con.close();
			 }  catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int getFlag() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result=0;
		String sql = "select * from login where User=?";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, "root");
			rs = ps.executeQuery();	
			if (rs.next()) {
				result=rs.getInt("lock_flag");
			}
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
				 if (rs != null)
					 rs.close();
				 if (ps != null)
					 ps.close();
				 if (con != null)
					 con.close();
			 }  catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public Date getDate() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Date result=null;
		String sql = "select * from login where User=?";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, "root");
			rs = ps.executeQuery();	
			if (rs.next()) {
				result=rs.getTimestamp("login_date");
			}
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
				 if (rs != null)
					 rs.close();
				 if (ps != null)
					 ps.close();
				 if (con != null)
					 con.close();
			 }  catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int getTimes() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result=0;
		String sql = "select * from login where User=?";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, "root");
			rs = ps.executeQuery();	
			if (rs.next()) {
				result=rs.getInt("times");
			}
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
				 if (rs != null)
					 rs.close();
				 if (ps != null)
					 ps.close();
				 if (con != null)
					 con.close();
			 }  catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void setFlag() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql_1 ="update login set lock_flag=? where User=?";
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql_1);
			ps.setInt(1,Flag);
			ps.setString(2,name);
			ps.execute();
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
				 if (rs != null)
					 rs.close();
				 if (ps != null)
					 ps.close();
				 if (con != null)
					 con.close();
			 }  catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setTimes() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql_1 ="update login set times=? where User=?";
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql_1);
			ps.setInt(1,Time);
			ps.setString(2,name);
			ps.execute();
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
				 if (rs != null)
					 rs.close();
				 if (ps != null)
					 ps.close();
				 if (con != null)
					 con.close();
			 }  catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setDate() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql_1 ="update login set login_date=? where User=?";
		Connection con=new MysqlConnection().getConnection();
		try {
			Timestamp ts = Timestamp.valueOf(datestr);
			ps = con.prepareStatement(sql_1);
			ps.setTimestamp(1,ts);
			ps.setString(2,name);
			ps.execute();
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
				 if (rs != null)
					 rs.close();
				 if (ps != null)
					 ps.close();
				 if (con != null)
					 con.close();
			 }  catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

