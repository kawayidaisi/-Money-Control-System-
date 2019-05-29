package 个人财政管理系统;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


//收支编辑界面
class BalEditFrame extends JFrame implements ActionListener{
	private JLabel l_id,l_date,l_bal,l_type,l_item;
	private JTextField t_id,t_date,t_bal;
	private JComboBox c_type,c_item;
	private JButton b_update,b_delete,b_select,b_new,b_clear;
	private JPanel p1,p2,p3;
	private JScrollPane scrollpane;
	private JTable table;
	private int id,date;
	private String type,item;
	private double bal;
	private String[] cloum = { "编号", "日期", "类型","内容", "金额"};
	private String username;
	public BalEditFrame(String username){
		super("收支编辑" );
		this.username=username;
		l_id=new JLabel("请输入编号:");
		l_date=new JLabel("请输入日期：");
		l_bal=new JLabel("请输入金额：");
		l_type=new JLabel("请输入类型：");
		l_item=new JLabel("请输入内容：");
		t_id=new JTextField(30);
		t_date=new JTextField(30);
		t_bal=new JTextField(30);
		t_id.setEditable(false);
		String s1[]={"收入","支出"};
		String s2[]={"购物","餐饮","居家","交通","娱乐","人情","工资","奖金","其他"};
		c_type=new JComboBox(s1);
		c_item=new JComboBox(s2);
		
		b_select=new JButton("查询");
		b_update=new JButton("修改");
		b_delete=new JButton("删除");
		b_new=new JButton("录入");
		b_clear=new JButton("清空");
		
		Date date = new Date();//取得当前时间
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String datestr  = format.format(date);
		t_date.setText(datestr);
		
		Container c=this.getContentPane();
		c.setLayout(new BorderLayout());
		
		
		p1=new JPanel();
      p1.setLayout(new GridLayout(5,2,10,10));//5 2 10 10
      p1.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createTitledBorder("编辑收支信息"), 
      BorderFactory.createEmptyBorder(5,5,5,5)));
      DateChooser dateChooser1 = DateChooser.getInstance("yyyyMMdd");
      dateChooser1.register(t_date);
		p1.add(l_id);
		p1.add(t_id);
		p1.add(l_date);
		p1.add(t_date);
		p1.add(l_type);
		p1.add(c_type);
		p1.add(l_item);
		p1.add(c_item);
		p1.add(l_bal);
		p1.add(t_bal);
		c.add(p1, BorderLayout.WEST);
		
		p2=new JPanel();
		p2.setLayout(new GridLayout(5,1,10,10));
		p2.add(b_new);
		p2.add(b_update);
		p2.add(b_delete);
		p2.add(b_select);
		p2.add(b_clear);
	    
		c.add(p2,BorderLayout.CENTER);	
		
		p3=new JPanel();
		p3.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createTitledBorder("显示收支信息"), 
		BorderFactory.createEmptyBorder(5,5,5,5)));	
				
		Object[][] row = new Object[50][5];
		table = new JTable(row, cloum){
			   public boolean isCellEditable(int row, int column){
			       return false;
			   }
			};	
		scrollpane = new JScrollPane(table);
		scrollpane.setViewportView(table);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		p3.add(scrollpane);
		c.add(p3,BorderLayout.EAST);		

		b_update.addActionListener(this);
		b_delete.addActionListener(this);
		b_select.addActionListener(this);
		b_new.addActionListener(this);
		b_clear.addActionListener(this);
		table.addMouseListener(new MouseAdapter()
		{
			  public void mouseClicked(MouseEvent e) throws NullPointerException
			  {
					int count=table.getSelectedRow();
					String getid= table.getValueAt(count, 0).toString();
					String getdate= table.getValueAt(count, 1).toString();
					String getbal= table.getValueAt(count, 4).toString();
					String gettype= table.getValueAt(count, 2).toString();
					String getitem= table.getValueAt(count, 3).toString();
					t_id.setText(getid);
					t_date.setText(getdate);							
					t_bal.setText(getbal);
					c_type.setSelectedItem(gettype);
					c_item.setSelectedItem(getitem);
					t_id.setEditable(false);
			  }
			});//为table添加鼠标点击事件监听addMouseListener
		table.getTableHeader().setReorderingAllowed(false); 
	    this.setResizable(false);
		this.setSize(1000,480);//800 300
		Dimension screen = this.getToolkit().getScreenSize();
	    this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
		this.show();
	}

	public void actionPerformed(ActionEvent e) {
		if(b_select==e.getSource()){  //查询收支信息
				if(research()) JOptionPane.showMessageDialog(null, "查询成功");
    			else JOptionPane.showMessageDialog(null, "没有数据记录");
		}else if(b_update==e.getSource()){// 修改某条收支信息
			int isModify = JOptionPane.showConfirmDialog(null, "确定修改","提示", JOptionPane.YES_NO_OPTION);//修改确认
			if(isModify == JOptionPane.YES_OPTION) {
			if(judge()) {
				id=Integer.valueOf(t_id.getText());
				date=Integer.valueOf(t_date.getText());
				type=(String) c_type.getSelectedItem();
				item=(String) c_item.getSelectedItem();
				bal=Double.valueOf(t_bal.getText());
				if(modify()) {
					JOptionPane.showMessageDialog(null, "修改成功");
					research();
				}else JOptionPane.showMessageDialog(null, "修改失败");
				t_id.setText("");
				t_date.setText("");							
				t_bal.setText("");
				}
			}
		}else if(b_delete==e.getSource()){
			int isDelete = JOptionPane.showConfirmDialog(null, "确定删除","提示", JOptionPane.YES_NO_OPTION);//删除确认
			if(isDelete == JOptionPane.YES_OPTION) {//删除某条收支信息
			if(judge()) {
					id=Integer.valueOf(t_id.getText());
					date=Integer.valueOf(t_date.getText());
					type=(String) c_type.getSelectedItem();
					item=(String) c_item.getSelectedItem();
					bal=Double.valueOf(t_bal.getText());
					if(delete()) {
						JOptionPane.showMessageDialog(null, "删除成功");
						research();
					}else JOptionPane.showMessageDialog(null, "删除失败");
					t_id.setText("");
					t_date.setText("");							
					t_bal.setText("");
				}
			}
		}else if(b_new==e.getSource()){   //新增某条收支信息 	
			if(judge_2()) {
				date=Integer.valueOf(t_date.getText());
				type=(String) c_type.getSelectedItem();
				item=(String) c_item.getSelectedItem();
				bal=Double.valueOf(t_bal.getText());
				if(t_id.getText().matches("\\s*")) {
					t_id.setText("");input_();research();JOptionPane.showMessageDialog(null, "添加成功,系统已自动校准id");
					t_id.setText("");							
					t_bal.setText("");
				}else {
						id=Integer.valueOf(t_id.getText());
						if(!check()) {//check函数判断是否id已存在
							if(input()) {
								JOptionPane.showMessageDialog(null, "添加成功");
								research();
								t_id.setText("");							
								t_bal.setText("");
							}else {
								JOptionPane.showMessageDialog(null, "添加失败");
								t_id.setText("");						
								t_bal.setText("");
							}
						}else {
							t_id.setText("");input_();research();JOptionPane.showMessageDialog(null, "添加成功");
							}
						}
					}
		}else if(b_clear==e.getSource()){//清空输入框
			t_id.setText("");
			t_date.setText("");							
			t_bal.setText("");
		}
	}
	String form="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|" + 
 			"((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])" + 
 			"((0[48]|[2468][048]|[3579][26])00))0229)"; //日期格式正则判断
	public boolean judge() {
		boolean result=true;
		if(!t_id.getText().matches("\\d+")) { //过滤输入
			t_id.setText("");JOptionPane.showMessageDialog(null, "编号输入格式错误");
			result=false;
		}
		if(!t_date.getText().matches(form)){
			t_date.setText("");JOptionPane.showMessageDialog(null, "日期输入格式错误");
			result=false;
		}
		if(!t_bal.getText().matches("^\\d{0,7}(\\.\\d{0,2})?$")||t_bal.getText().matches("\\s*")){//这个表达式认为“123.”或“123.123.123”不是有效的浮点数，认为“.123”或“123.123”或“123”是正确的浮点数。不超过7位数 
			t_bal.setText("");JOptionPane.showMessageDialog(null, "金额输入格式错误，请勿超过7位数和两位小数");
			result=false;
		}return result;
	}
	
	public boolean judge_2() {
		boolean result=true;
		if(!t_id.getText().matches("\\d+")&&!t_id.getText().matches("\\s*")) { //过滤输入
			t_id.setText("");JOptionPane.showMessageDialog(null, "编号输入格式错误");
			result=false;
		}
		if(!t_date.getText().matches(form)){
			t_date.setText("");JOptionPane.showMessageDialog(null, "日期输入格式错误");
			result=false;
		}
		if(!t_bal.getText().matches("^\\d{0,7}(\\.\\d{0,2})?$")||t_bal.getText().matches("\\s*")){//这个表达式认为“123.”或“123.123.123”不是有效的浮点数，认为“.123”或“123.123”或“123”是正确的浮点数。不超过7位数 
			t_bal.setText("");JOptionPane.showMessageDialog(null, "金额输入格式错误，请勿超过7位数和两位小数");
			result=false;
		}return result;
	}
	
	public boolean judge_1() {
		boolean result=false;
		if(t_id.getText().equals("")&&t_date.getText().equals("")&&t_bal.getText().equals("")) { //过滤输入
			result=true;
		}
		return result;
	}
	
	public boolean research() {//查找全部
		int flag=0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result=false;
		String sql = "select * from pay_gain order by date";//调用数据库查询功能
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
			rs = ps.executeQuery();
			while(rs.next()) {//循环输出结果
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
	
	
	public boolean delete() {
		int count=0;
		PreparedStatement ps = null;
		boolean result=false;
		String sql = "delete from pay_gain where (id=? and date=? and kind=? and content=? and value=?)";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1,id);
			ps.setInt(2,date);
			ps.setString(3, type);
			ps.setString(4, item);
			ps.setDouble(5,bal);
			int i=ps.executeUpdate();
			if(i>0) {
				result=true;
			}
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
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
	
	
	public boolean modify() {
		int count=0;
		PreparedStatement ps = null;
		boolean result=false;
		String sql = "update pay_gain set date=? , kind=? , content=? , value=? where id=?";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(5,id);
			ps.setInt(1,date);
			ps.setString(2, type);
			ps.setString(3, item);
			ps.setDouble(4,bal);
			int i=ps.executeUpdate();
			if(i>0) {
				result=true;
			}
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
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
	
	public boolean input() {
		int count=0;
		PreparedStatement ps = null;
		boolean result=false;
		String sql = "INSERT INTO pay_gain set date=? , kind=? , content=? , value=? , id=?";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(5,id);
			ps.setInt(1,date);
			ps.setString(2, type);
			ps.setString(3, item);
			ps.setDouble(4,bal);
			int i=ps.executeUpdate();
			if(i>0) {
				result=true;
			}
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
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
	
	public boolean input_() {
		int count=0;
		PreparedStatement ps = null;
		boolean result=false;
		String sql = "INSERT INTO pay_gain set date=? , kind=? , content=? , value=? ";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1,date);
			ps.setString(2, type);
			ps.setString(3, item);
			ps.setDouble(4,bal);
			int i=ps.executeUpdate();
			if(i>0) {
				result=true;
			}
		}catch (SQLException e) {e.printStackTrace();}
		 finally {
			try {
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
	
	
	public boolean check() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result=false;
		String sql = "select * from pay_gain where id=? ";//调用数据库查询功能
		Connection con=new MysqlConnection().getConnection();
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
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
	
	public  void makeFace(JTable table) { //设置表格颜色
        try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,
                        Object value, boolean isSelected, boolean hasFocus,
                        int row, int column) {
                	if (Double.parseDouble(table.getValueAt(row, 4).toString()) <= 1000) {
                        setBackground(new Color(206, 231, 255)); //设置行底色
                    }
                    if (Double.parseDouble(table.getValueAt(row, 4).toString()) > 1000) {
                        setBackground(Color.pink);
                    }
             
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
