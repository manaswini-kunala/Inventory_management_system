import java.awt.*; 
import javax.swing.*; 
import java.awt.event.*; 
public class MenuPgm extends JFrame  implements ActionListener
{ 
JFrame f;
JMenuBar mb;
JMenu m1,m2;
JMenuItem n1,n2,n3,n4;
public MenuPgm()
{
mb=new JMenuBar();
m1=new JMenu("Data Entry");
m2=new JMenu("Report");
n1=new JMenuItem("Inventory Master");
n2=new JMenuItem("Inventory Transaction");
n3=new JMenuItem("Exit");
n4=new JMenuItem("Inventory Report");
m1.add(n1); n1.addActionListener(this);
m1.add(n2); n2.addActionListener(this);
m1.addSeparator();
m1.add(n3); n3.addActionListener(this);
m2.add(n4); n4.addActionListener(this);
mb.add(m1); mb.add(m2);
this.setJMenuBar(mb);
setSize(500,500); 
setVisible(true); 
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
public void actionPerformed(ActionEvent e)
{
if(e.getSource()==n1)
{
new Inventory();
}
if(e.getSource()==n2)
{
new InventoryTransaction();
}
if(e.getSource()==n3)
{
System.exit(0);
}
if(e.getSource()==n4)
{
new InventoryReport();
}
}
public static void main(String args[])
{
MenuPgm a1=new MenuPgm();
}


}
