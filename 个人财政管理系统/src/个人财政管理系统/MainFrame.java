package 个人财政管理系统;

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

//主界面
public class MainFrame extends JFrame implements ActionListener{
	private JMenuBar mb=new JMenuBar();
	private JMenu m_system=new JMenu("系统管理");
	private JMenu m_fm=new JMenu("收支管理");
	private JMenuItem mI[]={new JMenuItem("密码重置"),new JMenuItem("退出系统")};
	private JMenuItem m_FMEdit=new JMenuItem("收支编辑");
	private JLabel l_type,l_fromdate,l_todate,l_bal,l_ps,l_pc;  
	private JTextField t_fromdate,t_todate; 
	private JButton b_select;
	private JComboBox c_type;
	private JPanel p_condition,p_detail;
	private String s1[]={"收入","支出","默认"};
	private double bal1;	
	private JTable table;
	private String username,value;
	private String[] cloum = {"编号", "日期", "类型","内容","金额",};
	private int todate,fromdate;
	public MainFrame(String username){	
		super(username+",欢迎使用个人理财账本!");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口程序直接结束
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
	    
	    l_type=new JLabel("收支类型：");	
	    c_type=new JComboBox(s1);
		l_fromdate=new JLabel("起始时间");
    t_fromdate=new JTextField(8);
		l_todate=new JLabel("终止时间");
    t_todate=new JTextField(8);
		b_select=new JButton("查询");
		l_ps = new JLabel("注意：时间格式为YYYYMMDD，例如：20150901，时间可为空,按时间顺序排序显示");
		p_condition=new JPanel();
		p_condition.setLayout(new GridLayout(2,1));
		p_condition.setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createTitledBorder("输入查询条件"), 
	    BorderFactory.createEmptyBorder(5,5,5,5)));
			
		 DateChooser dateChooser1 = DateChooser.getInstance("yyyyMMdd"); //添加日历
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
	    BorderFactory.createTitledBorder("收支明细信息"), 
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
		    l_bal.setText("个人总收支余额为"+bal1+"元。您已超支，请适度消费！");
	    	l_bal.setForeground(Color.red);
	    }
		else { 		
			l_bal.setText("个人总收支余额为"+bal1+"元。");
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
	    	if(username.equals("")) JOptionPane.showMessageDialog(null, "您没有权限");
	    	else new ModifyPwdFrame(username);
	     }else if(e.getSource()==mI[1]){
	    	 int isCancel = JOptionPane.showConfirmDialog(null, "确定退出","提示", JOptionPane.YES_NO_OPTION);//退出确认
			 if(isCancel == JOptionPane.YES_OPTION)System.exit(0);
	     }else if(e.getSource()==m_FMEdit){
	    	 if(username.equals("")) JOptionPane.showMessageDialog(null, "您没有权限");
	    	 else new BalEditFrame(username);
	     }else if(e.getSource()==b_select){   //根据时间及类型联合查询
	    	 	todate=0;fromdate=0;
	    	 	value = (String) c_type.getSelectedItem();//取得combBox值
	    	 	String form="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|" + 
	    	 			"((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])" + 
	    	 			"((0[48]|[2468][048]|[3579][26])00))0229)|\\s*"; //日期格式正则判断
	    	 			
	    	 	if(!t_fromdate.getText().matches(form)||!t_todate.getText().matches(form)||t_todate.getText().matches("\\s+")||t_fromdate.getText().matches("\\s+")) {//利用正则表达式过滤输入
	    	 		JOptionPane.showMessageDialog(null, "日期格式不正确");
	    	 		if(!t_fromdate.getText().matches(form))t_fromdate.setText("");
	    	 		if(!t_todate.getText().matches(form))t_todate.setText("");
	    	 	}else if(t_fromdate.getText().equals("")&&t_todate.getText().equals("")) { //日期全为空
	    	 			if(research()) JOptionPane.showMessageDialog(null, "查询成功");
	    	 			else JOptionPane.showMessageDialog(null, "没有数据记录");
	    	 	}else if(t_fromdate.getText().equals("")&&!t_todate.getText().equals("")) { //第一个日期为空
	    	 			todate=Integer.valueOf(t_todate.getText());
	    	 			if(research()) JOptionPane.showMessageDialog(null, "查询成功");
	    	 			else JOptionPane.showMessageDialog(null, "没有数据记录");
	    	 	}else if(t_todate.getText().equals("")&&!t_fromdate.getText().equals("")) { //第二个日期为空
	    	 			fromdate=Integer.valueOf(t_fromdate.getText());
	    	 			if(research()) JOptionPane.showMessageDialog(null, "查询成功");
	    	 			else JOptionPane.showMessageDialog(null, "没有数据记录");
	    	 	}else if(!t_todate.getText().equals("")&&!t_fromdate.getText().equals("")){ //日期全不为空
	    	 			todate=Integer.valueOf(t_todate.getText());
	    	 			fromdate=Integer.valueOf(t_fromdate.getText());
	    	 			if(research()) JOptionPane.showMessageDialog(null, "查询成功");
	    	 			else JOptionPane.showMessageDialog(null, "没有数据记录");
	    	}//余额显示更新
	    		bal1=count();
	    		  if(bal1<0) {
	    			    l_bal.setText("个人总收支余额为"+String.format("%.2f", bal1)+"元。您已超支，请适度消费！");
	    		    	l_bal.setForeground(Color.red);
	    		    }
	    			else{ 		
	    				l_bal.setText("个人总收支余额为"+String.format("%.2f", bal1)+"元。");
	    				l_bal.setForeground(Color.black);
	    			}
	}
}

//查询功能
public boolean research() {
	int flag=0;
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean result=false;
	String sql = "select * from pay_gain where (kind=? or kind=?) and (date between ? and ?) order by date";//调用数据库查询功能
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
		if(fromdate==0&&todate==0) {//数据库语句使用判断
			if(value.equals("默认")) {ps.setString(1, "收入");ps.setString(2, "支出");ps.setInt(3, 0);ps.setInt(4, 99999999);}		
			else {ps.setString(1, value);ps.setString(2, value);ps.setInt(3, 0);ps.setInt(4, 99999999);}		
		}else if(todate==0){
			if(value.equals("默认")) {ps.setString(1, "收入");ps.setString(2, "支出");ps.setInt(3, fromdate);ps.setInt(4, 99999999);}		
			else {ps.setString(1, value);ps.setString(2, value);ps.setInt(3, fromdate);ps.setInt(4, 99999999);}	
		}else {
			if(value.equals("默认")) {ps.setString(1, "收入");ps.setString(2, "支出");ps.setInt(3, fromdate);ps.setInt(4, todate);}		
			else {ps.setString(1, value);ps.setString(2, value);ps.setInt(3, fromdate);ps.setInt(4, todate);}	
		}
		rs = ps.executeQuery();
		while(rs.next()) {//循环输出(写的时候while写成了if，我找了2个小时的bug，哭了）
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

//统计余额功能
public double count() {
	double count=0;
	double count_=0;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String sql = "select sum(value) as sumvalue from pay_gain where kind='收入'";//调用数据库查询功能
	String sql_1 = "select sum(value) as sumvalue_ from pay_gain where kind='支出'";
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

public  void makeFace(JTable table) { //设置表格颜色
        try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,
                        Object value, boolean isSelected, boolean hasFocus,
                        int row, int column) {
                	if (Double.parseDouble(table.getValueAt(row, 4).toString()) <= 1000) {
                        setBackground(new Color(206, 231, 255)); //设置偶数行底色
                    }
                    if (Double.parseDouble(table.getValueAt(row, 4).toString()) > 1000) {
                        setBackground(Color.pink);
                    }
                    //如果需要设置某一个Cell颜色，需要加上column过滤条件即可
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




