import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    String name;
    int marks;

    Student(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }
}

public class StudentGradeTrackerGUI extends JFrame implements ActionListener {

    JTextField nameField, marksField;
    JTextArea displayArea;
    ArrayList<Student> students = new ArrayList<>();

    JButton addButton, reportButton;

    StudentGradeTrackerGUI() {
        setTitle("Student Grade Tracker");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Marks:"));
        marksField = new JTextField();
        panel.add(marksField);

        addButton = new JButton("Add Student");
        reportButton = new JButton("Show Report");

        panel.add(addButton);
        panel.add(reportButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        addButton.addActionListener(this);
        reportButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String name = nameField.getText();
            int marks = Integer.parseInt(marksField.getText());
            students.add(new Student(name, marks));
            nameField.setText("");
            marksField.setText("");
        }

        if (e.getSource() == reportButton) {
            int total = 0;
            int highest = students.get(0).marks;
            int lowest = students.get(0).marks;

            displayArea.setText("Name\tMarks\n----------------------\n");

            for (Student s : students) {
                displayArea.append(s.name + "\t" + s.marks + "\n");
                total += s.marks;
                if (s.marks > highest) highest = s.marks;
                if (s.marks < lowest) lowest = s.marks;
            }

            double average = (double) total / students.size();

            displayArea.append("----------------------\n");
            displayArea.append("Average: " + average + "\n");
            displayArea.append("Highest: " + highest + "\n");
            displayArea.append("Lowest: " + lowest + "\n");
        }
    }

    public static void main(String[] args) {
        new StudentGradeTrackerGUI();
    }
}
