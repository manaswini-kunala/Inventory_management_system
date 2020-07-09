import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.event.*;

public class Inventory extends JFrame implements ActionListener,ListSelectionListener
{
JLabel l1,l2,l3,l4,l5,l6;
JTextField t1,t3,t4,t5;
JButton b1,b2,b3,b4,b5;
JPanel p1,p2,p3,p4;
JList<String> lt1;
JList<Object>lt2;
JTextArea ta;
Container c;
Connection cnn=null;
PreparedStatement ps;
String descr;
int item_code,qty_onhand,reord_lvl;float unit_price;
String cnstring,sqli,sqlu,sqld,sql;
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
Vector<Object> row=new Vector<Object>();
while(rs.next())
{
row.addElement(rs.getObject(1));
}
rs.close();
ps.close();
cnn.close();
lt2.setListData(row) ;
}
catch(Exception ex)
{
JOptionPane.showMessageDialog(null,"Error in loading values to the list"+ex.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
}
}

public static void main(String args[])
{
Inventory a1=new Inventory();
}
public Inventory()
{
setTitle("Data Entry Inventory Master");
c=getContentPane();
c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
p1=new JPanel();
p1.setLayout(new GridLayout(5,2,5,5));
l1=new JLabel("Item Code");
l2=new JLabel("Description");
l3=new JLabel("Quantity On Hand");
l4=new JLabel("Unit Price");
l5=new JLabel("Reorder Level");

t1=new JTextField(20);
t3=new JTextField(20);
t3.addActionListener(this);
t4=new JTextField(20);
t5=new JTextField(20);
t5.setEditable(false); // no value can be typed inside that text box
lt1=new JList<String>(new String[]{"Pen","Pencil","Eraser"});

p1.add(l1);
p1.add(t1);
p1.add(l2);
p1.add(new JScrollPane(lt1));
p1.add(l3);
p1.add(t3);
p1.add(l4);
p1.add(t4);
p1.add(l5);
p1.add(t5);
c.add(p1);

p2=new JPanel();
p2.setLayout(new GridLayout(1,3,5,5));
b1=new JButton("Insert");
b2=new JButton("Update");
b3=new JButton("Delete");
b1.addActionListener(this);
b2.addActionListener(this);
b3.addActionListener(this);
p2.add(b1);
p2.add(b2);
p2.add(b3);
c.add(p2);

p3=new JPanel();
p3.setLayout(new GridLayout(1,2,5,5));
b4=new JButton("Clear");
b5=new JButton("Close");
b4.addActionListener(this);
b5.addActionListener(this);
p3.add(b4);
p3.add(b5);
c.add(p3);


p4=new JPanel();
p4.setLayout(new BorderLayout());
l6=new JLabel("Selected item Code");
lt2=new JList<Object>();
listControl();
lt2.addListSelectionListener(this);
p4.add(l6,"North");
p4.add(new JScrollPane(lt2),"Center");
c.add(p4);

setSize(400,400);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setVisible(true);
}

public void valueChanged(ListSelectionEvent ex)
{
try{
connect();
int item=Integer.parseInt(lt2.getSelectedValue().toString());
sql="select* from invent_mast where item_code=?";
ps=cnn.prepareStatement(sql);
ps.setInt(1,item);
ResultSet rs=ps.executeQuery();
if(rs.next())
{
t1.setText(rs.getString(1));
lt1.setSelectedValue(rs.getObject(2),true);
t3.setText(rs.getString(3));
t4.setText(rs.getString(4));
t5.setText(rs.getString(5));
}
rs.close();
ps.close();
cnn.close();
}
catch(Exception e1)
{
JOptionPane.showMessageDialog(null,"Error in selecting values from the list"+e1.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
}
}

public void actionPerformed(ActionEvent e)
{
if(e.getSource()==b1)
insert();
if(e.getSource()==b2)
update();
if(e.getSource()==b3)
delete();
if(e.getSource()==b4)
clear();
if(e.getSource()==b5)
close();
if(e.getSource()==t3)
checkQty();
}
void checkQty()
{
int q=Integer.parseInt(t3.getText());
if(q<100)
{
JOptionPane.showMessageDialog(null,"Quantity on hand must be greater than 100","Message",JOptionPane.INFORMATION_MESSAGE);
t3.setText("");
t3.requestFocus(); //puts cursor inside the text box(t3)
}
else
{
int r=q/2;
t5.setText(String.valueOf(r));
t4.requestFocus();
}
}


void insert()
{
try
{ 
connect();
item_code=Integer.parseInt(t1.getText());
descr=lt1.getSelectedValue().toString();
qty_onhand=Integer.parseInt(t3.getText());
unit_price=Float.parseFloat(t4.getText());
reord_lvl=Integer.parseInt(t5.getText());
sqli="insert into invent_mast(item_code,descr,qty_onhand,unit_price,reord_lvl)Values(?,?,?,?,?)";
ps=cnn.prepareStatement(sqli);
ps.setInt(1,item_code); ps.setString(2,descr);ps.setInt(3,qty_onhand);ps.setFloat(4,unit_price);ps.setInt(5,reord_lvl);
int tot=ps.executeUpdate();
JOptionPane.showMessageDialog(null,"Row inserted"+tot);
cnn.close();
listControl();
}
catch(Exception e)
{
JOptionPane.showMessageDialog(null,"Error in connection"+e.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
}
}
void update()
{
try
{ 
connect();
item_code=Integer.parseInt(t1.getText());
descr=lt1.getSelectedValue().toString();
qty_onhand=Integer.parseInt(t3.getText());
unit_price=Float.parseFloat(t4.getText());
reord_lvl=Integer.parseInt(t5.getText());
sqlu="update invent_mast set descr=?,qty_onhand=?,unit_price=?,reord_lvl=? where item_code=?";
ps=cnn.prepareStatement(sqlu);
ps.setString(1,descr);ps.setInt(2,qty_onhand);ps.setFloat(3,unit_price);ps.setInt(4,reord_lvl);ps.setInt(5,item_code); 
int tot=ps.executeUpdate();
JOptionPane.showMessageDialog(null,"Row updated"+tot);
cnn.close();
}
catch(Exception e)
{
JOptionPane.showMessageDialog(null,"Error in connection"+e.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
}
}
void delete()
{
try
{ 
connect();
item_code=Integer.parseInt(t1.getText());
sqld="delete from invent_mast where item_code=?";
ps=cnn.prepareStatement(sqld);
ps.setInt(1,item_code);
int tot=ps.executeUpdate();
JOptionPane.showMessageDialog(null,"Row deleted"+tot);
cnn.close();
listControl();
}
catch(Exception e)
{
JOptionPane.showMessageDialog(null,"Error in connection"+e.toString(),"Message",JOptionPane.INFORMATION_MESSAGE);
}
}
void clear()
{
t1.setText("");
t3.setText("");
t4.setText("");
t5.setText("");
lt1.clearSelection();
}

void close()
{
setVisible(false);
}
}