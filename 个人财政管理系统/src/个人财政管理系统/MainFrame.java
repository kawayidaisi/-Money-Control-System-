package ���˲�������ϵͳ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

//������
public class MainFrame extends JFrame implements ActionListener{
	private JMenuBar mb=new JMenuBar();
	private JMenu m_system=new JMenu("ϵͳ����");
	private JMenu m_fm=new JMenu("��֧����");
	private JMenuItem mI[]={new JMenuItem("��������"),new JMenuItem("�˳�ϵͳ")};
	private JMenuItem m_FMEdit=new JMenuItem("��֧�༭");
	private JLabel l_type,l_fromdate,l_todate,l_bal,l_ps,l_pc;  
	private JTextField t_fromdate,t_todate; 
	private JButton b_select;
	private JComboBox c_type;
	private JPanel p_condition,p_detail;
	private String s1[]={"����","֧��","Ĭ��"};
	private double bal1;	
	private JTable table;
	private String username,value;
	private String[] cloum = {"���", "����", "����","����","���",};
	private int todate,fromdate;
	public MainFrame(String username){	
		super(username+",��ӭʹ�ø�������˱�!");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�رմ��ڳ���ֱ�ӽ���
		this.username=username;
		Container c=this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(mb,"North");
		mb.add(m_system);
		mb.add(m_fm);
		m_system.add(mI[0]);
		m_system.add(mI[1]);
		m_fm.add(m_FMEdit);
		m_FMEdit.addActionListener(this);
	        mI[0].addActionListener(this);
	    mI[1].addActionListener(this);
	    
	    l_type=new JLabel("��֧���ͣ�");	
	    c_type=new JComboBox(s1);
		l_fromdate=new JLabel("��ʼʱ��");
    t_fromdate=new JTextField(8);
		l_todate=new JLabel("��ֹʱ��");
    t_todate=new JTextField(8);
		b_select=new JButton("��ѯ");
		l_ps = new JLabel("ע�⣺ʱ���ʽΪYYYYMMDD�����磺20150901��ʱ���Ϊ��,��ʱ��˳��������ʾ");
		p_condition=new JPanel();
		p_condition.setLayout(new GridLayout(2,1));
		p_condition.setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createTitledBorder("�����ѯ����"), 
	    BorderFactory.createEmptyBorder(5,5,5,5)));
			
		 DateChooser dateChooser1 = DateChooser.getInstance("yyyyMMdd"); //�������
	     DateChooser dateChooser2 = DateChooser.getInstance("yyyyMMdd");
	     dateChooser1.register(t_fromdate);
	     dateChooser2.register(t_todate);
	    JPanel p1 = new JPanel();
	    JPanel p2 = new JPanel();
	    
	    p1.add(l_type);
	    p1.add(c_type);
	    p1.add(l_fromdate);
		p1.add(t_fromdate);
		p1.add(l_todate);
		p1.add(t_todate);
		p1.add(b_select);
		p2.add(l_ps);

		
	    p_condition.add(p1);
	    p_condition.add(p2);
    c.add(p_condition,"Center");
    
    b_select.addActionListener(this);
  
    p_detail=new JPanel();
    p_detail.setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createTitledBorder("��֧��ϸ��Ϣ"), 
	    BorderFactory.createEmptyBorder(5,5,5,5)));
    l_bal=new JLabel();
    	Object[][] row = new Object[50][5];
		table = new JTable(row, cloum){
			   public boolean isCellEditable(int row, int column){
			       return false;
			   }
			};

		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(530,300));//580 350 530 280
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setViewportView(table);
		p_detail.add(l_bal);
		p_detail.add(scrollpane);
		c.add(p_detail,"South");
		table.getTableHeader().setReorderingAllowed(false); 
	    if(bal1<0) {
		    l_bal.setText("��������֧���Ϊ"+bal1+"Ԫ�����ѳ�֧�����ʶ����ѣ�");
	    	l_bal.setForeground(Color.red);
	    }
		else { 		
			l_bal.setText("��������֧���Ϊ"+bal1+"Ԫ��");
			l_bal.setForeground(Color.black);
		}
    this.setResizable(false);
		this.setSize(600,580);
		Dimension screen = this.getToolkit().getScreenSize();
	    this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}
	
public void actionPerformed(ActionEvent e) {
	     
	     if(e.getSource()==mI[0]){
	    	if(username.equals("")) JOptionPane.showMessageDialog(null, "��û��Ȩ��");
	    	else new ModifyPwdFrame(username);
	     }else if(e.getSource()==mI[1]){
	    	 int isCancel = JOptionPane.showConfirmDialog(null, "ȷ���˳�","��ʾ", JOptionPane.YES_NO_OPTION);//�˳�ȷ��
			 if(isCancel == JOptionPane.YES_OPTION)System.exit(0);
	     }else if(e.getSource()==m_FMEdit){
	    	 if(username.equals("")) JOptionPane.showMessageDialog(null, "��û��Ȩ��");
	    	 else new BalEditFrame(username);
	     }else if(e.getSource()==b_select){   //����ʱ�估�������ϲ�ѯ
	    	 	todate=0;fromdate=0;
	    	 	value = (String) c_type.getSelectedItem();//ȡ��combBoxֵ
	    	 	String form="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|" + 
	    	 			"((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])" + 
	    	 			"((0[48]|[2468][048]|[3579][26])00))0229)|\\s*"; //���ڸ�ʽ�����ж�
	    	 			
	    	 	if(!t_fromdate.getText().matches(form)||!t_todate.getText().matches(form)||t_todate.getText().matches("\\s+")||t_fromdate.getText().matches("\\s+")) {//����������ʽ��������
	    	 		JOptionPane.showMessageDialog(null, "���ڸ�ʽ����ȷ");
	    	 		if(!t_fromdate.getText().matches(form))t_fromdate.setText("");
	    	 		if(!t_todate.getText().matches(form))t_todate.setText("");
	    	 	}else if(t_fromdate.getText().equals("")&&t_todate.getText().equals("")) { //����ȫΪ��
	    	 			if(research()) JOptionPane.showMessageDialog(null, "��ѯ�ɹ�");
	    	 			else JOptionPane.showMessageDialog(null, "û�����ݼ�¼");
	    	 	}else if(t_fromdate.getText().equals("")&&!t_todate.getText().equals("")) { //��һ������Ϊ��
	    	 			todate=Integer.valueOf(t_todate.getText());
	    	 			if(research()) JOptionPane.showMessageDialog(null, "��ѯ�ɹ�");
	    	 			else JOptionPane.showMessageDialog(null, "û�����ݼ�¼");
	    	 	}else if(t_todate.getText().equals("")&&!t_fromdate.getText().equals("")) { //�ڶ�������Ϊ��
	    	 			fromdate=Integer.valueOf(t_fromdate.getText());
	    	 			if(research()) JOptionPane.showMessageDialog(null, "��ѯ�ɹ�");
	    	 			else JOptionPane.showMessageDialog(null, "û�����ݼ�¼");
	    	 	}else if(!t_todate.getText().equals("")&&!t_fromdate.getText().equals("")){ //����ȫ��Ϊ��
	    	 			todate=Integer.valueOf(t_todate.getText());
	    	 			fromdate=Integer.valueOf(t_fromdate.getText());
	    	 			if(research()) JOptionPane.showMessageDialog(null, "��ѯ�ɹ�");
	    	 			else JOptionPane.showMessageDialog(null, "û�����ݼ�¼");
	    	}//�����ʾ����
	    		bal1=count();
	    		  if(bal1<0) {
	    			    l_bal.setText("��������֧���Ϊ"+String.format("%.2f", bal1)+"Ԫ�����ѳ�֧�����ʶ����ѣ�");
	    		    	l_bal.setForeground(Color.red);
	    		    }
	    			else{ 		
	    				l_bal.setText("��������֧���Ϊ"+String.format("%.2f", bal1)+"Ԫ��");
	    				l_bal.setForeground(Color.black);
	    			}
	}
}

//��ѯ����
public boolean research() {
	int flag=0;
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean result=false;
	String sql = "select * from pay_gain where (kind=? or kind=?) and (date between ? and ?) order by date";//�������ݿ��ѯ����
	Connection con=new MysqlConnection().getConnection();
	DefaultTableModel dtm = new DefaultTableModel(cloum,0){
	      public Class getColumnClass(int column) {
	          Class returnValue;
	          if ((column >= 0) && (column < getColumnCount())) {
	            returnValue = getValueAt(0,column).getClass();
	          } else {
	            returnValue = Object.class;
	          }
	          return returnValue;
	        }
	      };
	try {
		ps = con.prepareStatement(sql);
		if(fromdate==0&&todate==0) {//���ݿ����ʹ���ж�
			if(value.equals("Ĭ��")) {ps.setString(1, "����");ps.setString(2, "֧��");ps.setInt(3, 0);ps.setInt(4, 99999999);}		
			else {ps.setString(1, value);ps.setString(2, value);ps.setInt(3, 0);ps.setInt(4, 99999999);}		
		}else if(todate==0){
			if(value.equals("Ĭ��")) {ps.setString(1, "����");ps.setString(2, "֧��");ps.setInt(3, fromdate);ps.setInt(4, 99999999);}		
			else {ps.setString(1, value);ps.setString(2, value);ps.setInt(3, fromdate);ps.setInt(4, 99999999);}	
		}else {
			if(value.equals("Ĭ��")) {ps.setString(1, "����");ps.setString(2, "֧��");ps.setInt(3, fromdate);ps.setInt(4, todate);}		
			else {ps.setString(1, value);ps.setString(2, value);ps.setInt(3, fromdate);ps.setInt(4, todate);}	
		}
		rs = ps.executeQuery();
		while(rs.next()) {//ѭ�����(д��ʱ��whileд����if��������2��Сʱ��bug�����ˣ�
			Vector hang=new Vector();
			hang.add(rs.getInt("id"));
			hang.add(rs.getInt("date"));
			hang.add(rs.getString("kind"));
			hang.add(rs.getString("content"));
			hang.add(rs.getDouble("value"));			
			dtm.addRow(hang);
			flag=1;
			result=true;
		}table.setModel(dtm);
		if(flag==1) table.setAutoCreateRowSorter(true);
		else table.setRowSorter(null);
		makeFace(table);
				
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

//ͳ������
public double count() {
	double count=0;
	double count_=0;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String sql = "select sum(value) as sumvalue from pay_gain where kind='����'";//�������ݿ��ѯ����
	String sql_1 = "select sum(value) as sumvalue_ from pay_gain where kind='֧��'";
	Connection con=new MysqlConnection().getConnection();
	try {
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		if(rs.next()) {
			count=rs.getDouble("sumvalue");
		}
		ps = con.prepareStatement(sql_1);
		rs = ps.executeQuery();
		if(rs.next()) {
			count_=rs.getDouble("sumvalue_");
		}
		count-=count_;
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
		return count;
	}

public  void makeFace(JTable table) { //���ñ����ɫ
        try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,
                        Object value, boolean isSelected, boolean hasFocus,
                        int row, int column) {
                	if (Double.parseDouble(table.getValueAt(row, 4).toString()) <= 1000) {
                        setBackground(new Color(206, 231, 255)); //����ż���е�ɫ
                    }
                    if (Double.parseDouble(table.getValueAt(row, 4).toString()) > 1000) {
                        setBackground(Color.pink);
                    }
                    //�����Ҫ����ĳһ��Cell��ɫ����Ҫ����column������������
                    return super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);
                }
            };
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}




