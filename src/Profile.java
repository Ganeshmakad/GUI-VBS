import javax.swing.*;
import java.awt.*;
import java.sql.*;
//inside lambda expresiion i.e (a->{}) a local variable cannot be modified so for the time
// when username changes we must create astring globally and update that to cureent username in class
// every where use this new string for all mysql quesries as if username not changed it remmained same if changed the query
//will then also reponds
//inside class-> global variable
//inside constructor-> local variable
class Profile extends JFrame {
String currUserName;
    Profile(String username) {
      currUserName = username;
        Font f = new Font("Futura", Font.BOLD, 35);
        Font f2 = new Font("Calibri", Font.PLAIN, 20);

        JLabel title = new JLabel("Profile Settings", JLabel.CENTER);
        title.setFont(f);

        JLabel l1 = new JLabel("Select Field to Update:");
        JComboBox<String> box = new JComboBox<>(new String[]{"Username", "Password", "Phone", "Email"});

        JLabel l2 = new JLabel("Enter New Value:");
        JTextField t1 = new JTextField(15);

        JButton b1 = new JButton("Update");
        JButton b2 = new JButton("Back");

        l1.setFont(f2);
        box.setFont(f2);
        l2.setFont(f2);
        t1.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);

        title.setBounds(250, 20, 300, 40);
        l1.setBounds(200, 100, 200, 30);
        box.setBounds(400, 100, 200, 30);
        l2.setBounds(200, 160, 200, 30);
        t1.setBounds(400, 160, 200, 30);
        b1.setBounds(250, 220, 120, 40);
        b2.setBounds(400, 220, 120, 40);

        c.add(title);
        c.add(l1);
        c.add(box);
        c.add(l2);
        c.add(t1);
        c.add(b1);
        c.add(b2);

        b1.addActionListener(
                a->{
                    String s1 = box.getSelectedItem().toString().toLowerCase();
                    String s2 = t1.getText();
                    String test = "";
                    if(s2.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null,"CANNOT BE BLANK");
                        return;
                    }
                    String url = "jdbc:mysql://localhost:3306/batch2";
                    try (Connection con = DriverManager.getConnection(url,"root","Ganesh@216");)
                    {
                        String check = "select "+s1+" from users where  username=?";
                        try(PreparedStatement pst = con.prepareStatement(check))
                        {
                            pst.setString(1,currUserName);
                            ResultSet rs = pst.executeQuery();
                            if (rs.next())
                            {
                               test=rs.getString(s1);
                            }

                        }
                        if(s2.equalsIgnoreCase(test)) //test can be null and comparing null can give null pointer exception
                                                      //but s2 is textfilled it cant be null max it could be "" still sringexist
                                                      //so this wont create a error if written s2.eqaltest
                        {
                            JOptionPane.showMessageDialog(null,"Cannot Be Same");
                            t1.setText("");
                        }
                        else
                        {
                            String sql = "update users set " + s1 + " =? where username=?";
                            try (PreparedStatement pst = con.prepareStatement(sql)) {
                                pst.setString(1, s2);
                                pst.setString(2, currUserName);
                                pst.executeUpdate();
//here flow for username update is when user decide to change name then curr username has the old name from the db the sql
//query passed is set username = new value (name) where username = currusername(old one) once this is updated then we change
//the variable cur user name so that return or next page is called through name new name can be passed
                                if(s1.equalsIgnoreCase("username")) {
                                    //and will change the name in the transaction table also so that passbook is correct
                                    //then actually update the variable
                                    String sq = "update transactions set username = ? where username=?";
                                    try(PreparedStatement ps = con.prepareStatement(sq)){
                                        ps.setString(1,s2);
                                        ps.setString(2,currUserName);
                                        ps.executeUpdate();
                                    }
                                    currUserName = s2;
                                }
                                JOptionPane.showMessageDialog(null, "UPDATE SUCCESSFUL");
                                t1.setText("");
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(null,e.getMessage());
                    }
                }
        );

        //yaha par agar username change kiya to username me to abhi old vala hai usko update karna padega so we used var
        b2.addActionListener(
                a->{
                    new Home(currUserName);
                    dispose();
                }
        );



        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Profile Settings");
    }

    public static void main(String[] args) {
        new Profile("gangu");
    }
}
