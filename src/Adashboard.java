import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Adashboard extends JFrame {
    Adashboard() {
        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel l1 = new JLabel("Admin Dashboard", JLabel.CENTER);
        JTextArea a1 = new JTextArea();
        JButton b2 = new JButton("Show All Users");
        JButton b3 = new JButton("Back");

        l1.setFont(f);
        b2.setFont(f2);
        b3.setFont(f2);
        a1.setFont(f2);
        a1.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(a1);
        scrollPane.setBounds(150, 220, 500, 200);

        Container c = getContentPane();
        c.setLayout(null);

        l1.setBounds(110, 30, 600, 50);
        b2.setBounds(250, 100, 300, 40);
        b3.setBounds(250, 160, 300, 40);

        c.add(l1);
        c.add(b2);
        c.add(b3);
        c.add(scrollPane);

        b2.addActionListener(
                a->{
                    String total = "";
                    String url = "jdbc:mysql://localhost:3306/batch2";
                    try(Connection con = DriverManager.getConnection(url,"root","Ganesh@216"))
                    {
                        String sql = "select username from users";
                        try(PreparedStatement pst = con.prepareStatement(sql))
                        {
                            ResultSet rs = pst.executeQuery();
                            while(rs.next()) //iska matlab while rs.next is false meaning till all the user are printed
                            {
                                total += rs.getString("username") + "\n";
                                //we have already made a text area to show user so will just paste the string in that text area i.e a1
                            }
                            a1.setText(total);
                            a1.setFont(f2);
                        }
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(null,e.getMessage());
                    }
                }
        );

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
    }

    public static void main(String[] args) {
        new Adashboard();
    }
}
