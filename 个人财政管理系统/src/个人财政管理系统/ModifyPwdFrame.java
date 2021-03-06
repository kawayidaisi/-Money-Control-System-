package 个人财政管理系统;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

//修改密码界面
class ModifyPwdFrame extends JFrame implements ActionListener{
	private JLabel l_oldPWD,l_newPWD,l_newPWDAgain;
	private JPasswordField t_oldPWD,t_newPWD,t_newPWDAgain;
	private JButton b_ok,b_cancel;
	private String username;
	private String password_again;
	private String password;
	private String password_old;
	public ModifyPwdFrame(String username){
		super("修改密码");
		this.username=username;
		l_oldPWD=new JLabel("输入旧密码：");
		l_newPWD=new JLabel("输入新密码：");
		l_newPWDAgain=new JLabel("确认新密码：");
		t_oldPWD=new JPasswordField(15);
		t_newPWD=new JPasswordField(15);
		t_newPWDAgain=new JPasswordField(15);
		b_ok=new JButton("确定");
		b_cancel=new JButton("取消");
		Container c=this.getContentPane();
		c.setLayout(new FlowLayout());
		c.add(l_oldPWD);
		c.add(t_oldPWD);
		c.add(l_newPWD);
		c.add(t_newPWD);
		c.add(l_newPWDAgain);
		c.add(t_newPWDAgain);
		c.add(b_ok);
		c.add(b_cancel);
		
		//背景添加
		ImageIcon img = new ImageIcon("src/1.jpg");
		JLabel jl_bg = new JLabel(img);
		jl_bg.setBounds(1, 1, 220, 160); 
		this.getLayeredPane().add(jl_bg, new Integer(Integer.MIN_VALUE));
		((JPanel)this.getContentPane()).setOpaque(false); 
		
		b_ok.addActionListener(this);
		b_cancel.addActionListener(this);
		this.setResizable(false);
		this.setSize(220,160);//280 160
		Dimension screen = this.getToolkit().getScreenSize();
	    this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(b_cancel==e.getSource()){
			dispose();
		}else if(b_ok==e.getSource()){  //修改密码
			password_old=new String(t_oldPWD.getPassword());
			password=new String(t_newPWD.getPassword());
			password_again=new String(t_newPWDAgain.getPassword());
			if(!login()) {
				JOptionPane.showMessageDialog(null, "旧密码错误");
				t_oldPWD.setText("");
				t_newPWDAgain.setText("");
				t_newPWD.setText("");
			}else if(!password.equals(password_again)) {
				JOptionPane.showMessageDialog(null, "密码不一致");
				t_newPWDAgain.setText("");
				t_newPWD.setText("");
			}else if(Modify()) {
				JOptionPane.showMessageDialog(null, "修改密码成功");
				dispose();
			}
		}
	}
	
	public boolean Modify() {//调用sql语句修改密码；
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result=false;
		String sql_1 ="update login set PSW=? where User=?";
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql_1);
			ps.setString(1,password);
			ps.setString(2,username);
			ps.execute();
			result=true;
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
	
	public boolean login() {//验证旧密码是否正确
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result=false;
		String sql = "select * from login where User=? and PSW=?";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password_old);
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
}
