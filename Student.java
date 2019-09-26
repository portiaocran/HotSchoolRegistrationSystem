//Portia Ocran
//991545021

/*This class represents the students from the student list in the Hotschool Database,
once instantiated, the student's individual course list is created
*/
package HotSchool;

public class Student {
	
	private String firstName;
	private String lastName;
	private CourseList studentCL;
	private int studentID;
	private int studentAge;
	
	public Student(int studentID, String firstName, String lastName,int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentID = studentID;
		this.studentAge = age;
		studentCL = new CourseList();
	}


	public Student() {
		
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public CourseList getStudentCL() {
		return studentCL;
	}


	public void setStudentCL(CourseList studentCL) {
		this.studentCL = studentCL;
	}


	public int getStudentID() {
		return studentID;
	}


	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	
	@Override
	   public String toString ()
	   {
	      String student = "";
	    	 student += getStudentID() + " - ";
	         student += getFirstName() + " ";
	         student += getLastName();
	         
	      return student;
	   }

}
