//Portia Ocran
//991545021

//This class creates an observable array list that will hold all students

package HotSchool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudentList {
	
	private ObservableList<Student> studentList;
	 
	public StudentList() {
		
		this.studentList = FXCollections.observableArrayList();
		
	}
	
	public void add (Student student) {
		
		this.studentList.add(student);
	}
	
	public void remove (Student student) {
		
		this.studentList.remove(student);
	}
	
	
	 public ObservableList<Student> getStudentList() {
		 
		return studentList;
	}

	@Override
	   public String toString ()
	   {
	      String student = "";
	      for (Student a : this.studentList) {
	    	 student += a.getStudentID() + " : ";
	         student += a.getFirstName() + " - ";
	         student += a.getLastName();

	      }
	      return student;
	   }

}
