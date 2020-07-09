import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
public class InventoryTransaction extends JFrame implements ActionListener
{
JLabel l1,l2,l3,l4,l5,l6;
JTextField t1,t3,t2;
JButton b1,b2,b3;
JPanel p1,p2,p3;
JList<String> lt1;
JComboBox<Object> c1; 
Container c;
ButtonGroup bg;
JRadioButton rb1,rb2;
Connection cnn=null;
PreparedStatement ps;
int item_code;
String cnstring,sqli,sqlu,sql;
int qoh; String type,dept;


void connect()        
{
try
{ 
Class.forName("com.mysql.jdbc.Driver");
cnstring="jdbc:mysql://localhost/invent";
cnn=DriverManager.getConnection(cnstring,"jav160","mice");
}
catch(Exception e)
{
JOptionPane.showMessageDialog(null,"Error in connection"+e.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
}
}

void listControl()
{
try
{
connect();
sql="select item_code from invent_mast";
ps=cnn.prepareStatement(sql);
ResultSet rs=ps.executeQuery();
//c1.removeAllItems();
while(rs.next())
{
c1.addItem(rs.getObject(1));
}
rs.close();
ps.close();
cnn.close();
}
catch(Exception e)
{
JOptionPane.showMessageDialog(null,"Error in loading values to the combo box"+e.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
}
}


public static void main(String args[])
{
InventoryTransaction a1=new InventoryTransaction();
}
public InventoryTransaction()
{
setTitle("Data Entry Inventory Transaction");
c=getContentPane();
c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
p1=new JPanel();
p1.setLayout(new GridLayout(6,2,5,5));
l1=new JLabel("Item Code");
l2=new JLabel("Description");
l3=new JLabel("Transaction Type");
l4=new JLabel("Department Code ");
l5=new JLabel("Transaction Quantity");
l6=new JLabel("Transaction Date");



c1=new JComboBox<Object>();
listControl();
c1.addActionListener(this);

t1=new JTextField(20);
t1.setEditable(false);

p2=new JPanel();
p2.setLayout(new GridLayout(2,1,5,5));
bg=new ButtonGroup();
rb1=new JRadioButton("Issue");
rb2=new JRadioButton("Receipt");
bg.add(rb1); bg.add(rb2);
rb1.addActionListener(this);
rb2.addActionListener(this);
p2.add(rb1);
p2.add(rb2);

lt1=new JList<String>(new String[]{"X1","X2","Y1","Y2","Z1","Z2"});

t2=new JTextField(20);
t2.addActionListener(this);
t3=new JTextField(20);

p1.add(l1);
p1.add(c1);
p1.add(l2);
p1.add(t1);
p1.add(l3);
p1.add(p2);
p1.add(l4);
p1.add(new JScrollPane(lt1));
p1.add(l5);
p1.add(t2);
p1.add(l6);
p1.add(t3);

c.add(p1);



p3=new JPanel();
p3.setLayout(new GridLayout(1,3,5,5));
b1=new JButton("Insert");
b2=new JButton("Clear");
b3=new JButton("Close");
b1.addActionListener(this);
b2.addActionListener(this);
b3.addActionListener(this);
p3.add(b1);
p3.add(b2);
p3.add(b3);
c.add(p3);

setSize(400,400);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setVisible(true);
}

public void actionPerformed(ActionEvent e)
{
if(e.getSource()==b1)
insert();
if(e.getSource()==b2)
clear();
if(e.getSource()==b3)
close();
if(e.getSource()==c1)
checkItem();
if(e.getSource()==rb1)
checkIssue();
if(e.getSource()==rb2)
checkReceipt();
if(e.getSource()==t2)
checkQty();
}

void checkQty()
{
int q=Integer.parseInt(t2.getText());
if(q<=0)
{
JOptionPane.showMessageDialog(null,"Transaction quantity must be greater than zero","Message",JOptionPane.INFORMATION_MESSAGE);
t2.setText("");
t2.requestFocus();
}
else if(type.equals("I") && q>qoh)
{
JOptionPane.showMessageDialog(null,"Transaction quantity must not be greater than "+qoh,"Message",JOptionPane.INFORMATION_MESSAGE);
t2.setText("");
t2.requestFocus();
}
else
t3.requestFocus();
}

void checkIssue()
{
type="I";
lt1.setEnabled(true);
}

void checkReceipt()
{
type="R";
lt1.setEnabled(false);
}

void checkItem()
{
try
{
int item=Integer.parseInt(c1.getSelectedItem().toString());
connect();
sql="select descr,qty_onhand from invent_mast where item_code=?";
ps=cnn.prepareStatement(sql);
ps.setInt(1,item);
ResultSet rs=ps.executeQuery();
if(rs.next())
{
t1.setText(rs.getString(1));
qoh=rs.getInt(2);
}
rs.close();
ps.close();
cnn.close();
}
catch(Exception e)
{
JOptionPane.showMessageDialog(null,"Error in selecting values from the combo box"+e.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
}

}

void insert()
{
try
{
connect();
int item=Integer.parseInt(c1.getSelectedItem().toString());
int qty=Integer.parseInt(t2.getText());
String dt=t3.getText();
if(type.equals("I"))
{
dept=lt1.getSelectedValue().toString();
qoh=qoh-qty;
}
else
{
dept="";
qoh=qoh+qty;
}
sqli="insert into invent_transact (ITEM_CODE,TRAN_TYPE,DEPT_CODE,TRAN_QTY,TRAN_DATE)Values(?,?,?,?,?)";
ps=cnn.prepareStatement(sqli);
ps.setInt(1,item);
ps.setString(2,type);
ps.setString(3,dept);
ps.setInt(4,qty);
ps.setString(5,dt);
int tot=ps.executeUpdate();
JOptionPane.showMessageDialog(null,"Row inserted to Transaction table","Message",JOptionPane.INFORMATION_MESSAGE);

sqlu="update invent_mast set qty_onhand=? where item_code=?";
ps=cnn.prepareStatement(sqlu);
ps.setInt(1,qoh);
ps.setInt(2,item);
tot=ps.executeUpdate();
JOptionPane.showMessageDialog(null,"Row updated to Master table","Message",JOptionPane.INFORMATION_MESSAGE);
ps.close();
cnn.close();
}
catch(Exception e)
{
JOptionPane.showMessageDialog(null,"Error in inserting or updating to the Transaction table "+e.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
e.printStackTrace();
}
}
void clear()
{
t1.setText("");
lt1.clearSelection();
bg.clearSelection();
t2.setText("");
t3.setText("");
}

void close()
{
setVisible(false);
}
}
