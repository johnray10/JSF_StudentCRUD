/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author JayJomJohn
 */
@ManagedBean(name = "studentBean")
@RequestScoped
public class StudentBean {

    private int id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String address;

    private ArrayList studentList;

    private final Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    public StudentBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    Statement stmt;

    // Used to establish connection
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "");
            System.out.println("You Are Connected To Database");
        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }

    // Used to fetch all records
    public ArrayList studentList() {
        try {
            studentList = new ArrayList();
            conn = getConnection();
            pst=conn.prepareStatement("SELECT * FROM students");
            rs=pst.executeQuery();
            while (rs.next()) {
                StudentBean sb = new StudentBean();
                sb.setId(rs.getInt("id"));
                sb.setName(rs.getString("name"));
                sb.setEmail(rs.getString("email"));
                sb.setGender(rs.getString("gender"));
                sb.setPassword(rs.getString("password"));
                sb.setAddress(rs.getString("address"));
                studentList.add(sb);
            }
            conn.close();
        } catch (Exception e) {
             System.out.println(e);
        }
        return studentList;
    }
    
    // Used to save user record
    public String save() {
        int result = 0;
        try {
            conn = getConnection();
            pst = conn.prepareStatement("insert into students (name,email,password,gender,address) values(?,?,?,?,?)");
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setString(4, gender);
            pst.setString(5, address);
            result = pst.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (result != 0) {
            return "index.xhtml?faces-redirect=true";
        } else {
            return "new.xhtml?faces-redirect=true";
        }
    }

    // Used to fetch record to update
    public String edit(int id) {

        System.out.println(id);
        try {
            conn = getConnection();
            stmt = getConnection().createStatement();
            rs = stmt.executeQuery("select * from students where id = " + (id));
            rs.next();
            StudentBean s = new StudentBean();
            s.setId(rs.getInt("id"));
            s.setName(rs.getString("name"));
            s.setEmail(rs.getString("email"));
            s.setGender(rs.getString("gender"));
            s.setAddress(rs.getString("address"));
            s.setPassword(rs.getString("password"));
            System.out.println(rs.getString("password"));
            sessionMap.put("editStudent", s);
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/edit.xhtml?faces-redirect=true";
    }

    // Used to update user record
    public String update(StudentBean sb) {
        //int result = 0;
        try {
            conn = getConnection();
            pst = conn.prepareStatement("update students set name=?, email=?, password=?, gender=?, address=? where id=?");
            pst.setString(1, sb.getName());
            pst.setString(2, sb.getEmail());
            pst.setString(3, sb.getPassword());
            pst.setString(4, sb.getGender());
            pst.setString(5, sb.getAddress());
            pst.setInt(6, sb.getId());
            pst.executeUpdate();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "/index.xhtml?faces-redirect=true";
    }

    // Used to delete user record
    public void delete(int id) {
        try {
            conn = getConnection();
            pst = conn.prepareStatement("delete from students where id = " + id);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Used to set user gender
    public String getGenderName(char gender) {
        if (gender == 'M') {
            return "Male";
        } else {
            return "Female";
        }
    }
}
