import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
class InventoryReport
{
public static void main(String args[])
{
InventoryReport a1=new InventoryReport();
}
public InventoryReport()
{
try
{
Class.forName("com.mysql.jdbc.Driver");
String str="jdbc:mysql://localhost/invent";
Connection cnn=DriverManager.getConnection(str,"jav160","mice");
Statement st=cnn.createStatement();
ResultSet rs=st.executeQuery("select invent_mast.item_code,descr,tran_qty,tran_qty*unit_price as sale_value from invent_mast,invent_transact where invent_mast.item_code=invent_transact.item_code and tran_type='I'");
ResultSetMetaData rm=rs.getMetaData();
int col=rm.getColumnCount();
Vector<String>colNames=new Vector<String>();
Vector<Object>data=new Vector<Object>();
for(int i=1;i<=col;i++)
{
colNames.addElement(rm.getColumnName(i));
}
float tot1=0.0F;
float tot2=0.0F;
while(rs.next())
{
Vector<Object>row=new Vector<Object>();
for(int i=1;i<=col;i++)
{
if(i==3)
{
float qty=rs.getFloat(i);
tot1=tot1+qty;
row.addElement(new Float(qty));
}
else if(i==4)
{
float sale=rs.getFloat(i);
tot2=tot2+sale;
row.addElement(new Float(sale));
}
else{
row.addElement(rs.getObject(i));
}
}
data.addElement(row);
}

Vector<Object>row=new Vector<Object>();
row.addElement("");
row.addElement("Total ");
row.addElement(new Float(tot1));
row.addElement(new Float(tot2));
data.addElement(row);
DefaultTableModel tm=new DefaultTableModel(data,colNames);
JFrame frame=new JFrame("View Data");
JTable table=new JTable(tm);
table.getTableHeader().setBackground(Color.yellow);
JScrollPane scrollPane=new JScrollPane(table);
frame.getContentPane().add(scrollPane);
frame.setSize(350,250);
frame.setVisible(true);
rs.close();
st.close();
cnn.close();
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
catch(Exception e)
{
System.out.println("Error..."+e.toString());
System.exit(1);

}}}