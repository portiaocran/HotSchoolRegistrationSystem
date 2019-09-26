//Portia Ocran
//991545021


//This class represents the courses from the course list in the Hotschool Database

package HotSchool;

public class Course {

	private int id;
	private String courseName;
	private String startTime;
	
	Course(int id, String courseName, String startTime){
		this.courseName = courseName;
		this.startTime= startTime;
		this.id = id;
	}
	
	public Course() {
		
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	 @Override
	   public String toString ()
	   {
	      String course = "";
	    	 course += getId() + " - ";
	         course += getCourseName();

	      return course;
	   }
	
}
