package ���˲�������ϵͳ;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sun.mail.util.MailSSLSocketFactory;

class Forgetpw extends JFrame implements ActionListener{
	private JLabel l_newUser,l_random;
	private JTextField t_newUser,t_random;
	private JButton b_ok,b_cancel,b_random;
	private String User;
	private String code=getRandomCharacterAndNumber(6);//������֤��
	public Forgetpw(){
		super("��������");
		l_newUser=new JLabel("�������䣺");
		t_newUser=new JTextField(15);
		l_random=new JLabel("����֤�룺");
		t_random=new JTextField(15);
		b_ok=new JButton("ȷ��");
		b_cancel=new JButton("ȡ��");
		b_random=new JButton("��֤��");
		Container c=this.getContentPane();
		c.setLayout(new FlowLayout());
		c.add(l_newUser);
		c.add(t_newUser);
		c.add(l_random);
		c.add(t_random);
		c.add(b_ok);
		c.add(b_random);
		c.add(b_cancel);
		
		//�������
		ImageIcon img = new ImageIcon("src/1.jpg");
		JLabel jl_bg = new JLabel(img);
		jl_bg.setBounds(1, 1, 220, 160); 
		this.getLayeredPane().add(jl_bg, new Integer(Integer.MIN_VALUE));
		((JPanel)this.getContentPane()).setOpaque(false); 
		
		b_ok.addActionListener(this);
		b_cancel.addActionListener(this);
		b_random.addActionListener(this);
		this.setResizable(false);
		this.setSize(220,150);//280 160
		Dimension screen = this.getToolkit().getScreenSize();
	    this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(b_cancel==e.getSource()){
			dispose();
		}else if(b_ok==e.getSource()){  
			User=t_newUser.getText();
			if(User.matches("\\s*")) {//�ʼ����հ׵Ĵ���
				JOptionPane.showMessageDialog(null, "���䲻��Ϊ��");
				t_newUser.setText("");
				t_random.setText("");
			}else if(!email()){
				JOptionPane.showMessageDialog(null, "�˺����󶨵������������");
				t_newUser.setText("");
				t_random.setText("");
			}else if(t_random.getText().equals(code)){
				JOptionPane.showMessageDialog(null, "��֤�ɹ�");
				new GetnewPwdFrame("root");
				dispose();
			}else {
				JOptionPane.showMessageDialog(null, "��֤�����");
				t_random.setText("");
			}
		}else if(b_random==e.getSource()) {
			User=t_newUser.getText();
			code=getRandomCharacterAndNumber(6);
			if(User.matches("\\s*")) {//�ʼ����հ׵Ĵ���
				JOptionPane.showMessageDialog(null, "���䲻��Ϊ��");
				t_newUser.setText("");
			}else if(!email()){
				JOptionPane.showMessageDialog(null, "�˺����󶨵������������");
				t_newUser.setText("");
			}else {
				try {
					Email();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "������֤��ɹ���δ�յ������·���");
			}
		}
	}
	
//ע�Ṧ��
	public boolean email() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result=false;
		String sql="select * from login where email=?";
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1,User);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
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
	
	public void Email() throws Exception {
		// �ռ��˵�������
        String from = "xxx@qq.com";

        // �����˵�������
        String to = "xxxx@qq.com";

        // ָ�������ʼ�������Ϊ smtp.qq.com
        String host = "smtp.qq.com";  //QQ �ʼ�������

        // ��ȡϵͳ����
        Properties properties = System.getProperties();

        // �����ʼ�������
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // ��ȡĬ��session����
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("xxxx", "xxxx"); //�������ʼ��û���������
            }
        });

        try{
            // ����Ĭ�ϵ� MimeMessage ����
            MimeMessage message = new MimeMessage(session);

            // Set From: ͷ��ͷ�ֶ�
            message.setFrom(new InternetAddress(from));

            // Set To: ͷ��ͷ�ֶ�
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: ͷ��ͷ�ֶ�
            message.setSubject("This is the identifying code!!");

            // ������Ϣ��
            message.setText("i think you need it:"+code);

            // ������Ϣ
            Transport.send(message);
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }   
	}
	
	public  String getRandomCharacterAndNumber(int length) {  
	       String val = "";  
	       Random random = new Random();  
	       for (int i = 0; i < length; i++) {  
	           String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // �����ĸ��������  
	  
	           if ("char".equalsIgnoreCase(charOrNum)) // �ַ���  
	           {  
	               int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // ȡ�ô�д��ĸ����Сд��ĸ  
	               val += (char) (choice + random.nextInt(26));  
	               // int choice = 97; // ָ���ַ���ΪСд��ĸ  
	               val += (char) (choice + random.nextInt(26));  
	           } else if ("num".equalsIgnoreCase(charOrNum)) // ����  
	           {  
	               val += String.valueOf(random.nextInt(10));  
	           }  
	       }  
	       return val;  
	   }  
}