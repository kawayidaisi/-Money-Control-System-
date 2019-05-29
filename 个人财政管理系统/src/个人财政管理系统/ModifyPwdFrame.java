package ���˲�������ϵͳ;

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

//�޸��������
class ModifyPwdFrame extends JFrame implements ActionListener{
	private JLabel l_oldPWD,l_newPWD,l_newPWDAgain;
	private JPasswordField t_oldPWD,t_newPWD,t_newPWDAgain;
	private JButton b_ok,b_cancel;
	private String username;
	private String password_again;
	private String password;
	private String password_old;
	public ModifyPwdFrame(String username){
		super("�޸�����");
		this.username=username;
		l_oldPWD=new JLabel("��������룺");
		l_newPWD=new JLabel("���������룺");
		l_newPWDAgain=new JLabel("ȷ�������룺");
		t_oldPWD=new JPasswordField(15);
		t_newPWD=new JPasswordField(15);
		t_newPWDAgain=new JPasswordField(15);
		b_ok=new JButton("ȷ��");
		b_cancel=new JButton("ȡ��");
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
		
		//�������
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
		}else if(b_ok==e.getSource()){  //�޸�����
			password_old=new String(t_oldPWD.getPassword());
			password=new String(t_newPWD.getPassword());
			password_again=new String(t_newPWDAgain.getPassword());
			if(!login()) {
				JOptionPane.showMessageDialog(null, "���������");
				t_oldPWD.setText("");
				t_newPWDAgain.setText("");
				t_newPWD.setText("");
			}else if(!password.equals(password_again)) {
				JOptionPane.showMessageDialog(null, "���벻һ��");
				t_newPWDAgain.setText("");
				t_newPWD.setText("");
			}else if(Modify()) {
				JOptionPane.showMessageDialog(null, "�޸�����ɹ�");
				dispose();
			}
		}
	}
	
	public boolean Modify() {//����sql����޸����룻
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
	
	public boolean login() {//��֤�������Ƿ���ȷ
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result=false;
		String sql = "select * from login where User=? and PSW=?";//�������ݿ��ѯ����
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password_old);
			rs = ps.executeQuery();
			
			if (rs.next()) {//��֤�ɹ�
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
